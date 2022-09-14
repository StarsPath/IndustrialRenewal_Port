package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.BlockElectricPump;
import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TileEntityElectricPump extends TileEntitySyncable implements ICapabilityProvider, ITickableTileEntity {


    public CustomFluidTank tank = new CustomFluidTank(1000)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityElectricPump.this.setChanged();
        }
    };

    public LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    public LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(()->tank);

    private int index = -1;
    //private int everyXtick = 10;
    private int tick;
    private int energyPerTick = Config.PUMP_RF_PER_TICK.get();
    private int energyCapacity = Config.PUMP_ENERGY_CAPACITY.get();
    private int maxRadius = Config.PUMP_RADIUS.get();
    private boolean config = Config.PUMP_INFINITE_WATER.get();
    private boolean replaceCobbleConfig = Config.PUMP_REPLACE_COBBLE.get();

    private Direction facing;

    private List<BlockPos> fluidSet = new ArrayList<>();

    //IEnergyStorage motorEnergy = null;

    private boolean isRunning = false;
    //private boolean oldIsRunning = false;
    private boolean starting = false;
    //private boolean oldStarting = false;
    private boolean firstLoad = false;

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(energyCapacity, energyCapacity, energyCapacity) {
            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public void onEnergyChange() {
                TileEntityElectricPump.this.sync();
            }
        };
    }

    public TileEntityElectricPump() {
        super(ModTileEntities.ELECTRIC_PUMP_TILE.get());
    }

    public TileEntityElectricPump(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void setFirstLoad(){
        if(!level.isClientSide && getIdex() == 1){
            TileEntityElectricPump energyInputTile = (TileEntityElectricPump) level.getBlockEntity(worldPosition.relative(getBlockState().getValue(BlockElectricPump.FACING).getOpposite()));
            if(energyInputTile != null)
                energyStorage = energyInputTile.energyStorage;
        }
    }

    @Override
    public void tick() {
        if (!level.isClientSide && getIdex() == 1)
        {
            if(!firstLoad){
                firstLoad = true;
                setFirstLoad();
                //this.onLoad();
            }

            if (isRunning = consumeEnergy())
            {
                GetFluidDown();
                passFluidUp();
                this.sync();
            }
        }
//        else
//        {
//            if (getIdex() == 1)
//            {
//                handleSound();
//            }
//        }
    }

    private int getIdex()
    {
        if (index != -1) return index;
        BlockState state = level.getBlockState(worldPosition);
        index = state.getBlock() instanceof BlockElectricPump ? state.getValue(BlockElectricPump.INDEX) : -1;
        return index;
    }

    private boolean consumeEnergy()
    {
        IEnergyStorage e = energyStorage.orElse(null);
        if(e == null)
            return false;
        return e.getEnergyStored() > energyPerTick && e.extractEnergy(energyPerTick, false) > 0;
    }

    private void GetFluidDown()
    {
        if (tank.getFluidAmount() <= 0 && isRunning)
        {
            if (config
                    && level.getBlockState(worldPosition.below()).getBlock().equals(Blocks.WATER))
            {
                tank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
            }
            //Utils.debug("fluid set", getFluidSet() != null, !getFluidSet().isEmpty());
            if (getFluidSet() != null && !getFluidSet().isEmpty())
            {
                BlockPos fluidPos = getFluidSet().get(0);
                //Utils.debug("fluid pos", fluidPos);

                while (!instanceOf(fluidPos, true))
                {
                    getFluidSet().remove(fluidPos);
                    if (getFluidSet() == null || getFluidSet().isEmpty()) return;
                    fluidPos = getFluidSet().get(0);
                }

                FluidState state = level.getFluidState(fluidPos);

                boolean consumeFluid = !(state.getType().equals(Fluids.WATER) && config);
                        //&& IRConfig.Main.pumpInfinityWater.get());

                //Utils.debug("downFluid", downFluid.drain(1000, IFluidHandler.FluidAction.SIMULATE).getRawFluid());
                if(state.isSource()){
                    if(tank.fill(new FluidStack(state.getType(), 1000), IFluidHandler.FluidAction.EXECUTE) > 0){
                        if(state.getType().equals(Fluids.LAVA) && replaceCobbleConfig) {
                            level.setBlock(fluidPos, Blocks.COBBLESTONE.defaultBlockState(), Constants.BlockFlags.DEFAULT);
                            getFluidSet().remove(fluidPos);
                        }
                        else if(consumeFluid){
                            level.setBlockAndUpdate(fluidPos, Blocks.AIR.defaultBlockState());
                            getFluidSet().remove(fluidPos);
                        }
                    }
                }
            }
        }
    }

    private List<BlockPos> getFluidSet()
    {
        if (fluidSet.isEmpty()) getAllFluids();
        return fluidSet;
    }

    private void getAllFluids()
    {
        if (level.getBlockState(worldPosition.below()).getBlock() instanceof FlowingFluidBlock)
        {
            Stack<BlockPos> traversingFluids = new Stack<>();
            List<BlockPos> flowingPos = new ArrayList<>();
            traversingFluids.add(worldPosition.below());
            while (!traversingFluids.isEmpty())
            {
                BlockPos fluidPos = traversingFluids.pop();
                if (instanceOf(fluidPos, true)) fluidSet.add(fluidPos);
                else flowingPos.add(fluidPos);

                for (Direction d : Direction.values())
                {
                    BlockPos newPos = fluidPos.relative(d);
                    if (instanceOf(newPos, false) && !fluidSet.contains(newPos) && !flowingPos.contains(newPos))
                    {
                        traversingFluids.add(newPos);
                    }
                }
            }
        }
    }

    private boolean instanceOf(BlockPos pos, boolean checkLevel)
    {
        if (pos == null) return false;
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof FlowingFluidBlock
                && (!checkLevel || state.getValue(FlowingFluidBlock.LEVEL) == 0)
                && Utils.getDistanceSq(worldPosition, pos.getX(), pos.getY(), pos.getZ()) <= maxRadius * maxRadius;
    }

    private void passFluidUp()
    {
        IFluidHandler upTank = GetTankUp();
        if (upTank != null)
        {
            if (upTank.fill(tank.drain(tank.getCapacity(), IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE) > 0)
            {
                upTank.fill(tank.drain(tank.getCapacity(), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public void setRemoved() {
        //if (world.isRemote) IRSoundHandler.stopTileSound(pos);
        starting = false;
        energyStorage.invalidate();
        tankHandler.invalidate();
        super.setRemoved();
    }

    private TileEntityElectricPump getMotor(){
        TileEntity te = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(BlockElectricPump.FACING).getOpposite()));
        if(te != null && te instanceof TileEntityElectricPump)
            if(((TileEntityElectricPump) te).index == 0)
                return (TileEntityElectricPump)te;
        return null;
    }

    private IFluidHandler GetTankUp()
    {
        TileEntity upTE = level.getBlockEntity(worldPosition.above());
        if (upTE != null)
        {
            return upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
        }
        return null;
    }

    private Direction getBlockFacing()
    {
        if (facing != null) return facing;
        BlockState state = level.getBlockState(worldPosition);
        facing = state.getValue(BlockElectricPump.FACING);
        return facing;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        int index = getIdex();

        if (facing == null)
            return super.getCapability(capability, facing);
        //Utils.debug("index, capability, facing", index, capability, facing);
        if (index == 1 && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == Direction.UP)
            return LazyOptional.of(() -> tank).cast();
        Direction face = getBlockFacing();
        if (index == 0 && capability == CapabilityEnergy.ENERGY && facing == face.getOpposite())
            return energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT tag = new CompoundNBT();
        tank.writeToNBT(tag);
        compound.put("fluid", tag);
        compound.putBoolean("isRunning", isRunning);
        compound.putBoolean("starting", starting);
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag2 = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag2);
        });

        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        CompoundNBT tag = compound.getCompound("fluid");
        tank.readFromNBT(tag);
        isRunning = compound.getBoolean("isRunning");
        starting = compound.getBoolean("starting");
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("energy")));

        super.load(state, compound);
    }
}
