package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockFluidPipe;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public abstract class TileEntityFluidPipeBase<T> extends TileEntityMultiBlocksTube<TileEntityFluidPipeBase> implements ICapabilityProvider {
    //TODO: add to config
    public int maxOutput = 1000;

    public TileEntityFluidPipeBase(TileEntityType<?> tileEntityTypeIn, int maxOutput) {
        super(tileEntityTypeIn);
        this.maxOutput = maxOutput;
    }
    public CustomFluidTank tank = new CustomFluidTank(FluidAttributes.BUCKET_VOLUME)
    {
        @Override
        protected void onContentsChanged()
        {
            TileEntityFluidPipeBase.this.setChanged();
        }
    };

    @Override
    public void doTick()
    {
        if (hasLevel() && !level.isClientSide && isMaster())
        {
            final Map<BlockPos, Direction> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            tank.setCapacity(Math.max(maxOutput * quantity, tank.getFluidAmount()));

            if (quantity > 0)
            {
                int canAccept = moveFluid(IFluidHandler.FluidAction.SIMULATE, quantity, mapPosSet);
                outPut = canAccept > 0 ? moveFluid(IFluidHandler.FluidAction.EXECUTE, canAccept, mapPosSet) : 0;
            } else outPut = 0;

            outPutCount = mapPosSet.size();
            if ((oldOutPut != outPut) || (oldOutPutCount != outPutCount))
            {
                oldOutPut = outPut;
                oldOutPutCount = outPutCount;
                this.sync();
            }
        }
    }

    public int moveFluid(IFluidHandler.FluidAction action, int validOutputs, Map<BlockPos, Direction> mapPosSet)
    {
        int canAccept = 0;
        int out = 0;
        int realMaxOutput = Math.min(tank.getFluidAmount() / validOutputs, maxOutput);

        //Utils.debug("valid outputs, real outputs", validOutputs, realMaxOutput);
        for (BlockPos posM : mapPosSet.keySet())
        {
            TileEntity te = level.getBlockEntity(posM);
            Direction face = mapPosSet.get(posM).getOpposite();
            if (te != null)
            {
                IFluidHandler tankStorage = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).orElse(null);
                if (tankStorage != null && tankStorage.isFluidValid(0, tank.getFluid()) && tank.drain(maxOutput, IFluidHandler.FluidAction.SIMULATE) != null)
                {
                    int fluid = tankStorage.fill(
                            tank.drain(realMaxOutput, IFluidHandler.FluidAction.SIMULATE),
                            action == IFluidHandler.FluidAction.SIMULATE ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE);
                    if (action == IFluidHandler.FluidAction.SIMULATE)
                    {
                        if (fluid > 0) canAccept++;
                    } else
                    {
                        out += fluid;
                        tank.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
        return action == IFluidHandler.FluidAction.SIMULATE ? canAccept : out;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (!level.isClientSide)
        {
            for (Direction face : Direction.values())
            {
                BlockPos currentPos = worldPosition.relative(face);
                TileEntity te = level.getBlockEntity(currentPos);
                boolean hasMachine = te != null
                        && !(te instanceof TileEntityFluidPipeBase)
                        && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).isPresent();
                if (hasMachine)
                {
                    if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                } else if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
            }
        }
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getMaster() != null)
            return LazyOptional.of(() -> getMaster().tank).cast();
        return super.getCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getMaster() != null)
            return LazyOptional.of(() -> getMaster().tank).cast();
        return super.getCapability(cap);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT tag = new CompoundNBT();
        tank.writeToNBT(tag);
        compound.put("fluid", tag);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        CompoundNBT tag = compound.getCompound("fluid");
        tank.readFromNBT(tag);
        super.load(state, compound);
    }

    @Override
    public boolean instanceOf(TileEntity te)
    {
        return te instanceof TileEntityFluidPipeBase;// || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }


    public boolean canConnectToPipe(Direction neighborDirection)
    {
        BlockPos neighborPos = worldPosition.relative(neighborDirection);
        TileEntity te = level.getBlockEntity(neighborPos);
        return instanceOf(te);
    }

    public boolean canConnectToCapability(Direction neighborDirection)
    {
        BlockPos offset = worldPosition.relative(neighborDirection);
        BlockState state = level.getBlockState(offset);
        TileEntity te = level.getBlockEntity(offset);
        //Utils.debug("te: ", te);
        return !(state.getBlock() instanceof BlockFluidPipe)
                && te != null
                && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighborDirection.getOpposite()).isPresent();
    }
}
