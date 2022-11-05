package com.cassiokf.industrialrenewal.blocks.transport;

import com.cassiokf.industrialrenewal.blockentity.transport.BlockEntityHighPressureFluidPipe;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockPipeBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.jetbrains.annotations.Nullable;

public class BlockHighPressureFluidPipe extends BlockPipeBase<BlockEntityHighPressureFluidPipe> implements EntityBlock {
    public BlockHighPressureFluidPipe()  {
        super(BlockBehaviour.Properties.of(Material.METAL), 8, 8);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos(), super.getStateForPlacement(context));
    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
//        if(!worldIn.isClientSide){
//            for(Direction direction : Direction.values()){
//                if(canConnectTo(worldIn, pos, direction)){
//                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), true), Constants.BlockFlags.DEFAULT);
//                }
//                else{
//                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), false), Constants.BlockFlags.DEFAULT);
//                }
//            }
//        }
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
//    }

    public BlockState getState(Level world, BlockPos pos, BlockState oldState){
        return oldState
                .setValue(UP, canConnectTo(world, pos, Direction.UP))
                .setValue(DOWN, canConnectTo(world, pos, Direction.DOWN))
                .setValue(NORTH, canConnectTo(world, pos, Direction.NORTH))
                .setValue(SOUTH, canConnectTo(world, pos, Direction.SOUTH))
                .setValue(EAST, canConnectTo(world, pos, Direction.EAST))
                .setValue(WEST, canConnectTo(world, pos, Direction.WEST));
    }

    @Override
    public boolean canConnectTo(BlockGetter world, BlockPos pos, Direction facing) {
        BlockEntity te = world.getBlockEntity(pos.relative(facing));
        return (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).isPresent());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityHighPressureFluidPipe(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityHighPressureFluidPipe)blockEntity).tick();
    }
}
