package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacingWithActivating;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class BlockCatwalkHatch extends BlockAbstractHorizontalFacingWithActivating {

    protected static final VoxelShape RDOWN_AABB = Block.box(0, 0, 0, 16, 3, 16);

    protected static final VoxelShape OPEN_NORTH_AABB = Block.box(0, 0, 0, 16, 16, 2);
    protected static final VoxelShape OPEN_SOUTH_AABB = Block.box(0, 0, 14, 16, 16, 16);
    protected static final VoxelShape OPEN_WEST_AABB = Block.box(0, 0, 0, 2, 16, 16);
    protected static final VoxelShape OPEN_EAST_AABB = Block.box(14, 0, 0, 16, 16, 16);

    public BlockCatwalkHatch(Properties properties) {
        super(properties);
    }

    public BlockCatwalkHatch()
    {
        super(Block.Properties.of(Material.METAL));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return state.getValue(ACTIVE);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return world.getBlockState(pos).getValue(ACTIVE);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return RDOWN_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (state.getValue(ACTIVE))
        {
            Direction direction = state.getValue(FACING);
            switch (direction)
            {
                default:
                case NORTH:
                    return OPEN_NORTH_AABB;
                case SOUTH:
                    return OPEN_SOUTH_AABB;
                case WEST:
                    return OPEN_WEST_AABB;
                case EAST:
                    return OPEN_EAST_AABB;
            }
        } else
        {
            return RDOWN_AABB;
        }
    }
}
