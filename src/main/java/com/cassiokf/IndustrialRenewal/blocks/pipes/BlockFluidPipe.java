package com.cassiokf.IndustrialRenewal.blocks.pipes;

import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityFluidPipe;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityFluidPipeBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class BlockFluidPipe extends BlockPipeBase<TileEntityFluidPipe>{

    public BlockFluidPipe(Properties property) {
        super(property, 4, 4);
        registerDefaultState(this.defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
    }

//    public BlockFluidPipe(Properties property, float nodeWidth, float nodeHeight) {
//        super(property, nodeWidth, nodeHeight);
//    }

    @Override
    public TileEntityFluidPipe createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityFluidPipe();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getState(context.getLevel(), context.getClickedPos(), super.getStateForPlacement(context));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                if(canConnectTo(worldIn, pos, direction)){
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), true), Constants.BlockFlags.DEFAULT);
                }
                else{
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), false), Constants.BlockFlags.DEFAULT);
                }
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

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
//        Utils.debug("MY TE: ", te, pos, facing);
//        if(te!=null && te instanceof TileEntityFluidPipe){
//            //Utils.debug("detect");
//            return ((TileEntityFluidPipe) te).canConnectToPipe(facing) ||
//                    ((TileEntityFluidPipe) te).canConnectToCapability(facing);
//        }
//        return false;
        return (te != null && te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).isPresent());
    }
}
