package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractBigFence;
import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockBasicElectricFence;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class BlockElectricBigFenceWire extends BlockAbstractBigFence {
    //protected static final VoxelShape CBASE_AABB = Block.box(.1, 0, .1, 8, 16, 8);

    public BlockElectricBigFenceWire()
    {
        super(Block.Properties.of(Material.METAL), 8);
        registerDefaultState(defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
        );
    }

    @Override
    public boolean isBigFence(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricBigFenceWire;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        if(state == null)
            return state;

        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        for (Direction face : Direction.Plane.HORIZONTAL) {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        return state;
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        return nb instanceof BlockBasicElectricFence
                || nb instanceof IRBaseWall
//                    && ((worldIn.getBlockState(currentPos).getValue(FACING).getClockWise() == neighborDirection )
//                    ||(worldIn.getBlockState(currentPos).getValue(FACING).getCounterClockWise() == neighborDirection )))
                || neighborState.isFaceSturdy(worldIn, neighborPos, neighborDirection.getOpposite())
                ;
    }

    //    @Override
//    public VoxelShape getVoxelShape(BlockState state, boolean collision)
//    {
//        if (collision) return CBASE_AABB;
//        return FULL_SHAPE;
//    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        for (Direction face : Direction.Plane.HORIZONTAL) {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlock(pos, state, Constants.BlockFlags.DEFAULT);
        super.neighborChanged(state, world, pos, block, neighborPos, flag);
    }
}
