package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import com.cassiokf.industrialrenewal.blocks.BlockWindTurbinePillar;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

//import com.cassiokf.IndustrialRenewal.util.MultiBlockHelper;

public class BlockEntityWindTurbinePillar extends BlockEntityMultiBlocksTube<BlockEntityWindTurbinePillar> implements ICapabilityProvider {

    private float amount;//For Lerp
    private int tick;
    private Direction[] faces = new Direction[]{Direction.UP, Direction.DOWN};
    private BlockPos turbinePos;
    private boolean isBase;

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(1024, 1024, 1024)
    {
        @Override
        public void onEnergyChange()
        {
            BlockEntityWindTurbinePillar.this.setChanged();
        }
    };

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

    private LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);
    private LazyOptional<IEnergyStorage> dummyEnergy = LazyOptional.of(this::createEnergyDummy);

    public BlockEntityWindTurbinePillar(BlockPos pos, BlockState state)
    {
        super(ModBlockEntity.TURBINE_PILLAR_TILE.get(), pos, state);
    }


    public void tick()
    {
        if (isMaster())
        {
            if (!level.isClientSide)
            {
                IEnergyStorage thisEnergy = energyStorageHandler.orElse(null);
                energyStorageHandler.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(1024 * getPosSet().size(), thisEnergy.getEnergyStored())));
                int energyReceived = 0;
                for (BlockPos currentPos : getPosSet().keySet())
                {
                    BlockEntity te = level.getBlockEntity(currentPos);
                    Direction face = getPosSet().get(currentPos);
                    if (thisEnergy != null && te != null)
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
                    if (!(level.getBlockEntity(turbinePos) instanceof BlockEntityWindTurbineHead))
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
    public boolean instanceOf(BlockEntity te)
    {
        return te instanceof BlockEntityWindTurbinePillar;
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
                BlockEntity te = level.getBlockEntity(currentPos);
                boolean hasMachine = !(state.getBlock() instanceof BlockWindTurbinePillar)
                        && te != null && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();

//                if(te == null)
//                    return;
//                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
//                if (hasMachine && energyStorage != null && energyStorage.canReceive())

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
        while (level.getBlockEntity(worldPosition.above(n)) instanceof BlockEntityWindTurbinePillar)
        {
            n++;
        }
        if (level.getBlockEntity(worldPosition.above(n)) instanceof BlockEntityWindTurbineHead) turbinePos = worldPosition.above(n);
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
        float totalCapacity = BlockEntityWindTurbineHead.energyGeneration;
        currentAmount = currentAmount / totalCapacity;
        amount = Utils.lerp(amount, currentAmount, 0.1f);
        return Math.min(amount, 1) * 90f;
    }

    public int getEnergyGenerated()
    {
        if(getMaster() == null || getMaster().getTurbinePos() == null)
            return 0;
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
        if (facing == null)
            return super.getCapability(capability, facing);

        if (capability == CapabilityEnergy.ENERGY && (facing == Direction.UP))
            return getMaster().energyStorageHandler.cast();
        if (capability == CapabilityEnergy.ENERGY && (isBase()))
            return dummyEnergy.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void load(CompoundTag compound) {
        energyStorage.setEnergy(compound.getInt("energy"));
        this.isBase = compound.getBoolean("base");
        BlockEntityWindTurbinePillar te = null;
        if (compound.contains("masterPos") && hasLevel())
            te = (BlockEntityWindTurbinePillar) level.getBlockEntity(BlockPos.of(compound.getLong("masterPos")));
        if (te != null) this.setMaster(te);
        super.load(compound);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.putInt("energy", energyStorage.getEnergyStored());
        compound.putBoolean("base", this.isBase);
        if (getMaster() != null) compound.putLong("masterPos", getMaster().getBlockPos().asLong());
        super.saveAdditional(compound);
    }

    private boolean canConnectTo(final Direction neighborDirection)
    {
        final BlockPos neighborPos = worldPosition.relative(neighborDirection);
        final BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborDirection == Direction.DOWN)
        {
            return !(neighborState.getBlock() instanceof BlockWindTurbinePillar);
        }
        BlockEntity te = level.getBlockEntity(neighborPos);
        return te != null
                && te.getCapability(CapabilityEnergy.ENERGY, neighborDirection.getOpposite()).isPresent();
    }
}

