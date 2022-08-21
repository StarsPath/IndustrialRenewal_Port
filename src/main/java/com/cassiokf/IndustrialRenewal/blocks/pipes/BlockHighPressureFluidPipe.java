package com.cassiokf.IndustrialRenewal.blocks.pipes;

import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityHighPressureFluidPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class BlockHighPressureFluidPipe extends BlockPipeBase<TileEntityHighPressureFluidPipe>{
    public BlockHighPressureFluidPipe(Properties property) {
        super(property, 8, 8);
//        registerDefaultState(this.defaultBlockState()
//                .setValue(NORTH, false)
//                .setValue(SOUTH, false)
//                .setValue(EAST, false)
//                .setValue(WEST, false)
//                .setValue(UP, false)
//                .setValue(DOWN, false));
    }

    @Override
    public TileEntityHighPressureFluidPipe createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityHighPressureFluidPipe();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
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

    public BlockState getState(World world, BlockPos pos, BlockState oldState){
        return oldState
                .setValue(UP, canConnectTo(world, pos, Direction.UP))
                .setValue(DOWN, canConnectTo(world, pos, Direction.DOWN))
                .setValue(NORTH, canConnectTo(world, pos, Direction.NORTH))
                .setValue(SOUTH, canConnectTo(world, pos, Direction.SOUTH))
                .setValue(EAST, canConnectTo(world, pos, Direction.EAST))
                .setValue(WEST, canConnectTo(world, pos, Direction.WEST));
    }

    @Override
    public boolean canConnectTo(IBlockReader world, BlockPos pos, Direction facing) {
        TileEntity te = world.getBlockEntity(pos.relative(facing));
        return (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).isPresent());
    }
}
