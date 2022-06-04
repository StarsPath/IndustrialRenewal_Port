package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TE6WayConnection;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.Utils;
//import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TileEntityBatteryBank extends TE6WayConnection implements ICapabilityProvider, ITickableTileEntity {

    private final Set<Direction> outPutFacings = new HashSet<>();
    private final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private final LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);
    private Direction blockFacing;
    //private int tick;


    public TileEntityBatteryBank(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public TileEntityBatteryBank(){
        super(ModTileEntities.BATTERY_BANK_TILE.get());
    }

    // TODO: Add to config
    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(1000000, 10000, 10000)
        {
            @Override
            public void onEnergyChange()
            {
                if (!level.isClientSide)
                {
                    TileEntityBatteryBank.this.sync();
                }
            }
        };
    }

    private IEnergyStorage createEnergyDummy()
    {
        return new CustomEnergyStorage(0, 0, 0)
        {
            @Override
            public boolean canReceive()
            {
                return false;
            }
        };
    }


    private boolean canFaceBeOutPut(Direction face)
    {
        return isFacingOutput(face) && face != getBlockFacing();
    }


    @Override
    public void tick() {

        if (this.hasLevel() && !level.isClientSide)
        {
            for (Direction face : outPutFacings)
            {
                TileEntity te = level.getBlockEntity(worldPosition.relative(face));
                if (te != null)
                {
                    IEnergyStorage thisStorage = energyStorage.orElse(null);
                    IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
                    if (eStorage != null && eStorage.canReceive())
                    {
                        thisStorage.extractEnergy(eStorage.receiveEnergy(thisStorage.extractEnergy(10000, true), false), false);
                    }
                }
            }
        }
    }
    public boolean toggleFacing(final Direction facing)
    {
        if (outPutFacings.contains(facing))
        {
            outPutFacings.remove(facing);
            this.sync();
            this.requestModelDataUpdate();
            return false;
        } else
        {
            outPutFacings.add(facing);
            this.sync();
            this.requestModelDataUpdate();
            return true;
        }
    }

    public boolean isFacingOutput(final @Nullable Direction facing) {
        return outPutFacings.contains(facing) || facing == null;
    }

    public String getText() {
        int energy = energyStorage.orElse(null).getEnergyStored();
        //industrialrenewal.LOGGER.info(energy);
        return Utils.formatEnergyString(energy);
    }

    public Direction getBlockFacing()
    {
        if (blockFacing != null) return blockFacing;
        return forceFaceCheck();
    }

    public Direction forceFaceCheck()
    {
        blockFacing = getBlockState().getValue(BlockAbstractHorizontalFacing.FACING);
        return blockFacing;
    }

    public float getTankFill() //0 ~ 180
    {
        float currentAmount = energyStorage.orElse(null).getEnergyStored() / 1000F;
        float totalCapacity = energyStorage.orElse(null).getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount;
    }

    public static float getBatteryFill(int energy, int min, int max)
    {
        return Utils.normalizeClamped(energy, min, max);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && isFacingOutput(facing))
            return dummyEnergy.cast();
        if (capability == CapabilityEnergy.ENERGY && facing != getBlockFacing().getOpposite())
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }

        @Override
    public void load(BlockState state, CompoundNBT compound) {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("energy")));
        outPutFacings.clear();

        final int[] enabledFacingIndices = compound.getIntArray("OutputFacings");
        for (int fd : enabledFacingIndices)
        {
            outPutFacings.add(Direction.from3DDataValue(fd));
        }
        blockFacing = Direction.from2DDataValue(compound.getInt("face"));
        super.load(state, compound);
    }
//
    @Override
    public CompoundNBT save(CompoundNBT compound) {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });

        final int[] enabledFacingIndices = outPutFacings.stream()
                .mapToInt(Direction::get2DDataValue)
                .toArray();

        compound.putIntArray("OutputFacings", enabledFacingIndices);
        compound.putInt("face", getBlockFacing().get2DDataValue());
        return super.save(compound);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.load(state, tag);
    }
}
