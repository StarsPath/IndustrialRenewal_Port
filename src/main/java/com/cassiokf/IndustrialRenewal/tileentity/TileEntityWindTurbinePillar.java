package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.BlockWindTurbinePillar;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
//import com.cassiokf.IndustrialRenewal.util.MultiBlockHelper;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

public class TileEntityWindTurbinePillar extends TileEntityMultiBlocksTube<TileEntityWindTurbinePillar> implements ICapabilityProvider {

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);

    private float amount;//For Lerp

    private int tick;

    private Direction[] faces = new Direction[]{Direction.UP, Direction.DOWN};
    private BlockPos turbinePos;
    private boolean isBase;

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(1024, 1024, 1024)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityWindTurbinePillar.this.setChanged();
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

    public TileEntityWindTurbinePillar()
    {
        super(ModTileEntities.TURBINE_PILLAR_TILE.get());
    }

    @Override
    public void doTick()
    {
        if (isMaster())
        {
            if (!level.isClientSide)
            {
                IEnergyStorage thisEnergy = energyStorage.orElse(null);
                energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(1024 * getPosSet().size(), thisEnergy.getEnergyStored())));
                int energyReceived = 0;
                for (BlockPos currentPos : getPosSet().keySet())
                {
                    TileEntity te = level.getBlockEntity(currentPos);
                    Direction face = getPosSet().get(currentPos);
                    if (te != null)
                    {
                        IEnergyStorage eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
                        if (eStorage != null && eStorage.canReceive())
                        {
                            energyReceived += eStorage.receiveEnergy(thisEnergy.extractEnergy(1024, true), false);
                            thisEnergy.extractEnergy(energyReceived, false);
                        }
                    }
                }

                outPut = energyReceived;

                if (oldOutPut != outPut)
                {
                    oldOutPut = outPut;
                    this.sync();
                }
            } else if (getTurbinePos() != null && isBase)
            {
                if (tick % 10 == 0)
                {
                    tick = 0;
                    this.sync();
                    if (!(level.getBlockEntity(turbinePos) instanceof TileEntityWindTurbineHead))
                    {
                        forceNewTurbinePos();
                    }
                }
                tick++;
            }
        }
    }

    @Override
    public Direction[] getFacesToCheck()
    {
        return faces;
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityWindTurbinePillar;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        isBase = getIsBase();
        if (isBase) forceNewTurbinePos();
        if (level.isClientSide) return;
        for (Direction face : Direction.Plane.HORIZONTAL)
        {
            BlockPos currentPos = worldPosition.relative(face);
            if (isBase)
            {
                BlockState state = level.getBlockState(currentPos);
                TileEntity te = level.getBlockEntity(currentPos);
                boolean hasMachine = !(state.getBlock() instanceof BlockWindTurbinePillar)
                        && te != null && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();

                if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                    if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                    else if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
            } else
            {
                if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
            }
        }
        this.sync();
    }

    private BlockPos getTurbinePos()
    {
        if (turbinePos != null) return turbinePos;
        return forceNewTurbinePos();
    }


    private BlockPos forceNewTurbinePos()
    {
        int n = 1;
        while (level.getBlockEntity(worldPosition.above(n)) instanceof TileEntityWindTurbinePillar)
        {
            n++;
        }
        if (level.getBlockEntity(worldPosition.above(n)) instanceof TileEntityWindTurbineHead) turbinePos = worldPosition.above(n);
        else turbinePos = null;
        return turbinePos;
    }

    public Direction getBlockFacing()
    {
        return getBlockState().getValue(BlockWindTurbinePillar.FACING);
    }

    public float getGenerationforGauge()
    {
        float currentAmount = getEnergyGenerated();
        float totalCapacity = TileEntityWindTurbineHead.getMaxGeneration();
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1) * 90f;
    }

    public int getEnergyGenerated()
    {
        return getMaster().outPut;
    }

    public String getText()
    {
        if (getMaster() == null || getMaster().getTurbinePos() == null) return "No Turbine";
        return getEnergyGenerated() + " FE/t";
    }

    public boolean isBase()
    {
        return isBase;
    }

    public boolean getIsBase()
    {
        BlockState state = level.getBlockState(worldPosition.below());
        return !(state.getBlock() instanceof BlockWindTurbinePillar);
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && (facing == Direction.UP))
            return getMaster().energyStorage.cast();
        if (capability == CapabilityEnergy.ENERGY && (isBase()))
            return dummyEnergy.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        //industrialrenewal.LOGGER.info("load compound");
        energyStorage.ifPresent(h -> {
            ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("energy"));
            //industrialrenewal.LOGGER.info("energyStorage present, load success");
        });
        this.isBase = compound.getBoolean("base");
        TileEntityWindTurbinePillar te = null;
        if (compound.contains("masterPos") && hasLevel())
            te = (TileEntityWindTurbinePillar) level.getBlockEntity(BlockPos.of(compound.getLong("masterPos")));
        if (te != null) this.setMaster(te);
        super.load(state, compound);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
        //industrialrenewal.LOGGER.info("save compound");
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
            //industrialrenewal.LOGGER.info("energyStorage present, save success");
        });
        compound.putBoolean("base", this.isBase);
        if (getMaster() != null) compound.putLong("masterPos", getMaster().getBlockPos().asLong());
        return super.save(compound);
    }

//    @Nonnull
//    @Override
//    public IModelData getModelData()
//    {
//        BlockState eState = getBlockState();
//        Direction facing = eState.getValue(BlockWindTurbinePillar.FACING);
//        ModelDataMap.Builder builder = new ModelDataMap.Builder();
//        boolean down = canConnectTo(Direction.DOWN);
//        builder.withInitial(DOWN, down).withInitial(UP, false);
//        if (down)
//            return builder
//                    .withInitial(SOUTH, canConnectTo(facing.getOpposite()))
//                    .withInitial(NORTH, canConnectTo(facing))
//                    .withInitial(EAST, canConnectTo(facing.getClockWise()))
//                    .withInitial(WEST, canConnectTo(facing.getCounterClockWise()))
//                    .build();
//        else
//            return builder
//                    .withInitial(SOUTH, false)
//                    .withInitial(NORTH, false)
//                    .withInitial(EAST, false)
//                    .withInitial(WEST, false)
//                    .build();
//    }
    private boolean canConnectTo(final Direction neighborDirection)
    {
        final BlockPos neighborPos = worldPosition.relative(neighborDirection);
        final BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborDirection == Direction.DOWN)
        {
            return !(neighborState.getBlock() instanceof BlockWindTurbinePillar);
        }
        TileEntity te = level.getBlockEntity(neighborPos);
        return te != null
                && te.getCapability(CapabilityEnergy.ENERGY, neighborDirection.getOpposite()).isPresent();
    }

//    public int averageEnergy;
//    public int potentialEnergy;
//    private int oldPotential = -1;
//    private int oldEnergy;
//    private int tick;
//
//    private float amount;//For Lerp
//
//    private Direction facing;
//
//    private static final Direction[] faces = new Direction[]{Direction.UP, Direction.DOWN};
//    private boolean isBase;
//
//    public TileEntityWindTurbinePillar(TileEntityType<?> tileEntityType) {
//        super(tileEntityType);
//    }
//
//    public TileEntityWindTurbinePillar() {
//        super(ModTileEntities.TURBINE_PILLAR_TILE.get());
//    }
//
//    @Override
//    public void beforeInitialize()
//    {
//        getIsBase();
//        sync();
//    }
//
//    public int onEnergyReceived(int maxReceive, boolean simulate)
//    {
//        if (level.isClientSide()) return 0;
//        if (!isMaster()) return getMaster().onEnergyReceived(maxReceive, simulate);
//
//        if (inUse) return 0; //to prevent stack overflow (IE)
//        inUse = true;
//        if (!simulate) potentialEnergy = maxReceive;
//        if (maxReceive <= 0)
//        {
//            inUse = false;
//            return 0;
//        }
//        List<Integer> out = MultiBlockHelper.outputEnergy(this, maxReceive, energyContainer.getMaxOutput(), simulate, level);
//        if (!simulate) outPut += out.get(0);
//        outPutCount = out.get(1);
//        inUse = false;
//        return out.get(0);
//    }
//
//    @Override
//    public void tick() {
//        //super.tick();
//        if(level != null)
//        if (!level.isClientSide() && isMaster())
//        {
//            if (tick >= 10)
//            {
//                tick = 0;
//                averageEnergy = outPut / 10;
//                outPut = 0;
//                if (averageEnergy != oldEnergy || potentialEnergy != oldPotential)
//                {
//                    oldPotential = potentialEnergy;
//                    oldEnergy = averageEnergy;
//                    sync();
//                }
//            }
//            tick++;
//        }
//    }
//
//    @Override
//    public Direction[] getFacesToCheck()
//    {
//        return faces;
//    }
//
//    @Override
//    public boolean instanceOf(TileEntity te)
//    {
//        return te instanceof TileEntityWindTurbinePillar;
//    }
//
//    @Override
//    public void checkForOutPuts()
//    {
//        outPut = 0;
//        afterInit();
//        //potentialEnergy = 0;
//        if(level != null)
//        if (!level.isClientSide && isBase)
//        {
//            for (Direction face : Direction.Plane.HORIZONTAL)
//            {
//                BlockPos currentPos = worldPosition.relative(face);
//
//                TileEntity te = level.getBlockEntity(currentPos);
////                boolean hasMachine = !(te instanceof TileEntityWindTurbinePillar) && te != null;
////                IEnergyStorage cap = null;
////                if (hasMachine) cap = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
////                if (hasMachine && cap != null && cap.canReceive()) getMaster().addMachine(te, face);
//                if(te instanceof TileEntitySmallWindTurbine)
//                    te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).ifPresent(cap->{
//                        if(cap.canReceive()){
//                            getMaster().addMachine(te, face);
//                        }
//                    });
//            }
//        }
//        this.sync();
//    }
//
//    @Override
//    public void afterInit()
//    {
//        if (getIsBase() && getMaster() != this) setMaster(this);
//    }
//
//    public Direction getBlockFacing()
//    {
//        if (facing != null) return facing;
//        BlockState state = level.getBlockState(worldPosition);
//        facing = state.getBlock() instanceof BlockWindTurbinePillar ? state.getValue(BlockWindTurbinePillar.FACING) : Direction.NORTH;
//        return facing;
//    }
//
//    public void setFacing(Direction facing)
//    {
//        this.facing = facing;
//    }
//
//    public float getGenerationForGauge()
//    {
//        float currentAmount = Utils.normalizeClamped(getMaster().averageEnergy, 0, 128);
//        amount = Utils.lerp(amount, currentAmount, 0.1f);
//        return Math.min(amount, 1) * 90f;
//    }
//
//    public float getPotentialValue()
//    {
//        float currentAmount = Utils.normalizeClamped(getMaster().potentialEnergy, 0, 128);
//        return currentAmount * 90f;
//    }
//
//    public String getText()
//    {
//        return Utils.formatEnergyString(getMaster().averageEnergy) + "/t";
//    }
//
//    public boolean isBase()
//    {
//        return isBase;
//    }
//
//    public boolean getIsBase()
//    {
//        BlockState state = level.getBlockState(worldPosition.below());
//        isBase = !(state.getBlock() instanceof BlockWindTurbinePillar);
//        return isBase;
//    }
//
//    @Override
//    public void setRemoved() {
//        super.setRemoved();
//        LazyOptional.of(()->getMaster().energyContainer).invalidate();
//        LazyOptional.of(()->dummyEnergyContainer).invalidate();
//        setChanged();
//    }
//
//    @Override
//    @Nullable
//    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
//    {
//        if (capability == CapabilityEnergy.ENERGY)
//        {
//            if (facing == Direction.UP)
//                return LazyOptional.of(()->getMaster().energyContainer).cast();
//                //return CapabilityEnergy.ENERGY.cast(getMaster().energyContainer);
//            else if (isBase)
//                return LazyOptional.of(()->dummyEnergyContainer).cast();
//                //return CapabilityEnergy.ENERGY.cast(dummyEnergyContainer);
//        }
//        return null;
//    }
//
//    @Override
//    public CompoundNBT save(CompoundNBT compoundNBT) {
//        compoundNBT.putBoolean("base", this.isBase);
//        compoundNBT.putInt("energy_average", averageEnergy);
//        compoundNBT.putInt("potential", potentialEnergy);
//        compoundNBT.putInt("outPut", outPut);
//        return super.save(compoundNBT);
//    }
//
//    @Override
//    public void load(BlockState state, CompoundNBT compoundNBT) {
//        this.isBase = compoundNBT.getBoolean("base");
//        averageEnergy = compoundNBT.getInt("energy_average");
//        if (hasLevel() && level.isClientSide()) this.potentialEnergy = compoundNBT.getInt("potential");
//        if (hasLevel() && level.isClientSide()) this.outPut = compoundNBT.getInt("outPut");
//        super.load(state, compoundNBT);
//    }
}

