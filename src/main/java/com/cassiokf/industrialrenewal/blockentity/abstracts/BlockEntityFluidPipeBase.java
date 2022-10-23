package com.cassiokf.industrialrenewal.blockentity.abstracts;

import com.cassiokf.industrialrenewal.blocks.transport.BlockFluidPipe;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;

public abstract class BlockEntityFluidPipeBase<T> extends BlockEntityMultiBlocksTube<BlockEntityFluidPipeBase> implements ICapabilityProvider {
    //TODO: add to config
    public int maxOutput = 1000;
    private int t = 0;

    public BlockEntityFluidPipeBase(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state, int maxOutput) {
        super(tileEntityTypeIn, pos, state);
        this.maxOutput = maxOutput;
    }
    public CustomFluidTank tank = new CustomFluidTank(FluidAttributes.BUCKET_VOLUME)
    {
        @Override
        protected void onContentsChanged()
        {
            BlockEntityFluidPipeBase.this.setChanged();
            BlockEntityFluidPipeBase.this.sync();
        }
    };
    public LazyOptional tankHandler = LazyOptional.of(()->tank);


    public void tick()
    {
        super.tick();
        if (hasLevel() && !level.isClientSide && isMaster())
        {
            final Map<BlockPos, Direction> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            tank.setCapacity(Math.max(maxOutput, tank.getFluidAmount()));

            if (quantity > 0)
            {
                moveFluid(null, quantity, mapPosSet, t);
                t = (t+1)%quantity;
            }
        }
    }

    public void moveFluid(IFluidHandler.FluidAction action, int validOutputs, Map<BlockPos, Direction> mapPosSet, int offset)
    {
        BlockEntityFluidPipeBase master = getMaster();
        int amountToExtract = Math.min(master.tank.getFluidAmount()/validOutputs, maxOutput);
        if(master.tank.getFluidAmount()/validOutputs < 1)
            amountToExtract = master.tank.getFluidAmount();

        for(int i = offset ; i < offset+mapPosSet.keySet().size(); i++){
            BlockPos[] poses = mapPosSet.keySet().toArray(new BlockPos[mapPosSet.keySet().size()]);
//            Utils.debug("poses", poses.length, i);
            BlockPos pos = poses[i%mapPosSet.keySet().size()];
//        for(BlockPos pos : mapPosSet.keySet()){
            BlockEntity te = level.getBlockEntity(pos);
            Direction face = mapPosSet.get(pos).getOpposite();
            if(te != null){
                IFluidHandler teFluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).orElse(null);
                if(teFluidHandler != null && teFluidHandler.isFluidValid(0, master.tank.getFluid())){
                    FluidStack fluidStack = master.tank.drain(amountToExtract, IFluidHandler.FluidAction.SIMULATE);
                    int amountInserted = teFluidHandler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);
//                    Utils.debug("amount", fluidStack.getAmount(), amountToExtract, amountInserted, master.tank.getFluidAmount());

                    int postFill = teFluidHandler.fill(master.tank.drain(amountInserted, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
//                    Utils.debug("After fill", master.tank.getFluidAmount(), postFill);
                }
            }
        }
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (!level.isClientSide)
        {
            for (Direction face : Direction.values())
            {
                BlockPos currentPos = worldPosition.relative(face);
                BlockEntity te = level.getBlockEntity(currentPos);
                boolean hasMachine = te != null
                        && !(te instanceof BlockEntityFluidPipeBase)
                        && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face.getOpposite()).isPresent();
                if (hasMachine)
                {
                    if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                } else if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
            }
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@Nullable Capability<T> capability, @Nullable Direction facing)
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
    protected void saveAdditional(CompoundTag compound) {
        CompoundTag tag = new CompoundTag();
        tank.writeToNBT(tag);
        compound.put("fluid", tag);
        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        CompoundTag tag = compound.getCompound("fluid");
        tank.readFromNBT(tag);
        super.load(compound);
    }

    @Override
    public boolean instanceOf(BlockEntity te)
    {
        return te instanceof BlockEntityFluidPipeBase;// || (te instanceof TileEntityCableTray && ((TileEntityCableTray) te).hasPipe());
    }


    public boolean canConnectToPipe(Direction neighborDirection)
    {
        BlockPos neighborPos = worldPosition.relative(neighborDirection);
        BlockEntity te = level.getBlockEntity(neighborPos);
        return instanceOf(te);
    }

    public boolean canConnectToCapability(Direction neighborDirection)
    {
        BlockPos offset = worldPosition.relative(neighborDirection);
        BlockState state = level.getBlockState(offset);
        BlockEntity te = level.getBlockEntity(offset);
        //Utils.debug("te: ", te);
        return !(state.getBlock() instanceof BlockFluidPipe)
                && te != null
                && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighborDirection.getOpposite()).isPresent();
    }
}
