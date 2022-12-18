package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockPipeSwitchBase;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityFluidValve extends BlockEntitySyncable {
    public BlockEntityFluidValve(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUID_VALVE_TILE.get(), pos, state);
    }

//    public int CAPACITY = Config.HIGH_PRESSURE_PIPE_TRANSFER_RATE.get();
    public int CAPACITY = 1000 * 20;//Config.HIGH_PRESSURE_PIPE_TRANSFER_RATE.get();

    public CustomFluidTank dummyTank = new CustomFluidTank(0);

    public LazyOptional<CustomFluidTank> dummyTankHandler = LazyOptional.of(()->dummyTank);

    public void tick() {
        if(level == null) return;
        if(!level.isClientSide){
            if(isOpen()){
                transferFluid();
            }
        }
    }

    public void transferFluid(){
        Direction facing = getFacing();
        BlockEntity outputTile = level.getBlockEntity(worldPosition.relative(facing));
        BlockEntity inputTile = level.getBlockEntity(worldPosition.relative(facing.getOpposite()));

        if(inputTile != null && outputTile != null){
            IFluidHandler inputTileHandler = inputTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing).orElse(null);
            IFluidHandler outputTileHandler = outputTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);

            if(inputTileHandler!=null && outputTileHandler!=null){
                int amount = outputTileHandler.fill(inputTileHandler.drain(CAPACITY, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE);
                outputTileHandler.fill(inputTileHandler.drain(amount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public boolean isOpen(){
        return getBlockState().is(ModBlocks.FLUID_VALVE.get())? getBlockState().getValue(BlockPipeSwitchBase.ON_OFF) : false;
    }

    public Direction getFacing(){
        return getBlockState().is(ModBlocks.FLUID_VALVE.get())? getBlockState().getValue(BlockPipeSwitchBase.FACING) : Direction.NORTH;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction facing = getFacing();

        if (side == null)
            return super.getCapability(cap, side);

        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (side == facing.getOpposite() || side == facing))
            return dummyTankHandler.cast();
        return super.getCapability(cap, side);
    }
}
