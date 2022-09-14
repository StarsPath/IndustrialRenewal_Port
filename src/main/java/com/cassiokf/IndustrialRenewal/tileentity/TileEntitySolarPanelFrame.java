package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.BlockSolarPanel;
import com.cassiokf.IndustrialRenewal.blocks.BlockSolarPanelFrame;
import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.CustomItemStackHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileEntitySolarPanelFrame extends TileEntityMultiBlocksTube<TileEntitySolarPanelFrame> {

    public LazyOptional<IItemHandler> panelInv = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private Set<BlockPos> panelReady = new HashSet<>();
    private int tick;
    private Direction blockFacing;

    private int MAX_CAPACITY = Config.SOLAR_FRAME_CAPACITY.get();
    private int MAX_TRANSFER_RATE = Config.SOLAR_FRAME_TRANSFER_RATE.get();
    private float MULTIPLIER = Config.SOLAR_FRAME_MULTIPLIER.get();

    private boolean DECORATIVE = Config.SOLAR_FRAME_DECORATIVE.get();

    public TileEntitySolarPanelFrame() {
        super(ModTileEntities.SOLAR_PANEL_FRAME.get());
    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return Block.byItem(stack.getItem()) instanceof BlockSolarPanel;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntitySolarPanelFrame.this.sync();
            }
        };
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(MAX_CAPACITY, 0, MAX_TRANSFER_RATE)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySolarPanelFrame.this.sync();
            }
        };
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntitySolarPanelFrame;
    }

    @Override
    public void tick()
    {
        if(DECORATIVE)
            return;
        super.tick();
        if (this.hasLevel() && !level.isClientSide)
        {
            if (isMaster())
            {
                //Utils.debug("PosSet", getPosSet().keySet());
                IEnergyStorage thisEnergy = energyStorage.orElse(null);
                int size = panelReady.size();
                energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(MAX_CAPACITY * size, thisEnergy.getEnergyStored())));
                if (size > 0) getEnergyFromSun();
                for (BlockPos posT : getPosSet().keySet())
                {
                    final TileEntity tileEntity = level.getBlockEntity(posT);
                    if (tileEntity != null && !tileEntity.isRemoved())
                    {
                        Direction facing = getPosSet().get(posT);
                        final IEnergyStorage consumer = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
                        if (consumer != null)
                        {
                            thisEnergy.extractEnergy(consumer.receiveEnergy(thisEnergy.extractEnergy(MAX_TRANSFER_RATE, true), false), false);
                        }
                    }
                }
            }

            if (tick >= 20)
            {
                tick = 0;
                checkIfIsReady();
            }
            tick++;
        }
    }

    public void checkIfIsReady()
    {
        //Utils.debug("CHECK", hasPanel(), level.canSeeSky(worldPosition.relative(Direction.UP)), level.getBrightness(LightType.SKY, worldPosition) > 0);
        if (hasPanel() && level.canSeeSky(worldPosition.relative(Direction.UP))
                && level.getBrightness(LightType.SKY, worldPosition) > 0)
        {
            getMaster().getPanelReadySet().add(worldPosition);
        } else getMaster().getPanelReadySet().remove(worldPosition);
    }

    public void getEnergyFromSun()
    {
        IEnergyStorage thisEnergy = getMaster().energyStorage.orElse(null);
        //Utils.debug("GET ENERGY", level.canSeeSky(worldPosition.relative(Direction.UP)), level.getBrightness(LightType.SKY, worldPosition) > 0, (thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored()), thisEnergy.getMaxEnergyStored(), thisEnergy.getEnergyStored());
//        if (level.canSeeSky(worldPosition.relative(Direction.UP))
//                && level.getBrightness(LightType.SKY, worldPosition) > 0 && (thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored()))
        if ((thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored()))
        {
            int result = thisEnergy.getEnergyStored() + Math.round((TileEntitySolarPanelBase.getGeneration(level, worldPosition) * panelReady.size()) * getMultiplier());
            if (result > thisEnergy.getMaxEnergyStored())
            {
                result = thisEnergy.getMaxEnergyStored();
            }
            //Utils.debug("reusult", result);
            final int energy = result;
            energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(energy));
        }
    }

    @Override
    public void checkForOutPuts(BlockPos pos) {
        if (level.isClientSide) return;
        for (Direction face : Direction.values())
        {
            Direction facing = getBlockFacing();
//            Utils.debug("FACE", face, facing, face == facing, face == facing.getClockWise(), face == facing.getCounterClockWise());
            boolean canConnect = face == facing || face == facing.getClockWise() || face == facing.getCounterClockWise();
            if (!canConnect) continue;

            BlockPos currentPos = pos.relative(face);

            BlockState state = level.getBlockState(currentPos);
            TileEntity te = level.getBlockEntity(currentPos);
            boolean hasMachine = !(state.getBlock() instanceof BlockSolarPanelFrame) && te != null
                    && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();
            if (hasMachine)
            {
                if (!isMasterInvalid()) {
                    getMaster().addMachine(currentPos, face);
                }
            }
            else{
                if (!isMasterInvalid()) {
                    getMaster().removeMachine(pos, currentPos);
                }
            }
        }
    }

    public Direction getBlockFacing()
    {
        if (blockFacing == null)
        {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockSolarPanelFrame)
                blockFacing = state.getValue(BlockSolarPanelFrame.FACING);
            else blockFacing = Direction.NORTH;
        }
        return blockFacing;
    }

    @Override
    public void removeMachine(BlockPos ownPos, BlockPos machinePos)
    {
        getPanelReadySet().remove(ownPos);
        super.removeMachine(ownPos, machinePos);
    }

    private float getMultiplier()
    {
        return MULTIPLIER;
        //return IRConfig.Main.panelFrameMultiplier.get();
    }

    public Set<BlockPos> getPanelReadySet()
    {
        return panelReady;
    }

    public boolean hasPanel()
    {
        return !panelInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    public ItemStack getPanel()
    {
        return panelInv.orElse(null).getStackInSlot(0);
    }

    public IItemHandler getPanelHandler()
    {
        return panelInv.orElse(null);
    }

    @Override
    public void setRemoved() {
        getMaster().getPanelReadySet().remove(worldPosition);
        Utils.dropInventoryItems(level, worldPosition, panelInv.orElse(null));
        super.setRemoved();
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        panelInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("energy")));
        panelInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("inv")));
        super.load(state, compound);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        Direction face = getBlockFacing();

        if (facing == null)
            return super.getCapability(capability, facing);

        boolean canConnect = facing == face || facing == face.getClockWise() || facing == face.getCounterClockWise();
        if (capability == CapabilityEnergy.ENERGY && canConnect)
            return getMaster().energyStorage.cast();
        return super.getCapability(capability, facing);
    }
}
