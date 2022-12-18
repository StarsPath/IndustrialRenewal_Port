package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import com.cassiokf.industrialrenewal.blocks.BlockElectricPump;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BlockEntityElectricPump extends BlockEntitySyncable implements ICapabilityProvider {

    private int index = -1;
    //private int everyXtick = 10;
    private int tick;
    private final int energyPerTick = Config.PUMP_RF_PER_TICK.get();
    private final int energyCapacity = Config.PUMP_ENERGY_CAPACITY.get();
    private final int maxRadius = Config.PUMP_RADIUS.get();
    private final boolean config = Config.PUMP_INFINITE_WATER.get();
    private final boolean replaceCobbleConfig = Config.PUMP_REPLACE_COBBLE.get();

    private Direction facing;

    private final List<BlockPos> fluidSet = new ArrayList<>();

    //IEnergyStorage motorEnergy = null;

    private boolean isRunning = false;
    //private boolean oldIsRunning = false;
    private boolean starting = false;
    //private boolean oldStarting = false;
    private boolean firstLoad = false;

    public CustomFluidTank tank = new CustomFluidTank(1000)
    {
        @Override
        protected void onContentsChanged()
        {
            BlockEntityElectricPump.this.setChanged();
        }
    };

    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(energyCapacity, energyCapacity, energyCapacity) {
        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public void onEnergyChange() {
            BlockEntityElectricPump.this.sync();
        }
    };

    public LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);
    public LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(()->tank);


    public BlockEntityElectricPump(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ELECTRIC_PUMP_TILE.get(), pos, state);
    }



    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void setFirstLoad(){
        if(level == null) return;
        if(!level.isClientSide && getIdex() == 1){
            BlockEntityElectricPump energyInputTile = (BlockEntityElectricPump) level.getBlockEntity(worldPosition.relative(getBlockState().getValue(BlockElectricPump.FACING).getOpposite()));
            if(energyInputTile != null)
                energyStorageHandler = energyInputTile.energyStorageHandler;
        }
    }

    public void tick() {
        if(level == null) return;
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
        BlockState state = getBlockState();
        index = state.getBlock() instanceof BlockElectricPump ? state.getValue(BlockElectricPump.INDEX) : -1;
        return index;
    }

    private boolean consumeEnergy()
    {
        IEnergyStorage e = energyStorageHandler.orElse(null);
        if(e == null)
            return false;
        return e.getEnergyStored() > energyPerTick && e.extractEnergy(energyPerTick, false) > 0;
    }

    private void GetFluidDown()
    {
        if(level == null) return;
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
                            level.setBlock(fluidPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
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
        if(level == null) return;
        if (level.getFluidState(worldPosition.below()) != Fluids.EMPTY.defaultFluidState())
        {
            Stack<BlockPos> traversingFluids = new Stack<>();
            List<BlockPos> flowingPos = new ArrayList<>();
            traversingFluids.add(worldPosition.below());
            while (!traversingFluids.isEmpty())
            {
                BlockPos fluidPos = traversingFluids.pop();
                if (instanceOf(fluidPos, true)){
//                    Utils.debug("ADD TO SOURCE SET");
                    fluidSet.add(fluidPos);
                }
                else {
//                    Utils.debug("ADD TO FLOW SET");
                    flowingPos.add(fluidPos);
                }

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
        if(level == null) return false;
        if (pos == null) return false;
        FluidState state = level.getFluidState(pos);
        return state != Fluids.EMPTY.defaultFluidState()
                && (!checkLevel || state.isSource())
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
    public void invalidateCaps() {
        energyStorageHandler.invalidate();
        tankHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    private BlockEntityElectricPump getMotor(){
        if(level == null) return null;
        BlockEntity te = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(BlockElectricPump.FACING).getOpposite()));
        if(te != null && te instanceof BlockEntityElectricPump)
            if(((BlockEntityElectricPump) te).index == 0)
                return (BlockEntityElectricPump)te;
        return null;
    }

    private IFluidHandler GetTankUp()
    {
        if(level == null) return null;
        BlockEntity upTE = level.getBlockEntity(worldPosition.above());
        if (upTE != null)
        {
            return upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
        }
        return null;
    }

    private Direction getBlockFacing()
    {
        if (facing != null) return facing;
        facing = getBlockState().getValue(BlockElectricPump.FACING);
        return facing;
    }

    @NotNull
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
            return energyStorageHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        CompoundTag tag = new CompoundTag();
        tank.writeToNBT(tag);
        compoundTag.put("fluid", tag);
        compoundTag.putBoolean("isRunning", isRunning);
        compoundTag.putBoolean("starting", starting);
        compoundTag.putInt("energy", energyStorage.getEnergyStored());

        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        CompoundTag tag = compoundTag.getCompound("fluid");
        tank.readFromNBT(tag);
        isRunning = compoundTag.getBoolean("isRunning");
        starting = compoundTag.getBoolean("starting");
        energyStorage.setEnergy(compoundTag.getInt("energy"));

        super.load(compoundTag);
    }
}
