package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacingWithActivating;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockCatwalkGate extends BlockAbstractHorizontalFacingWithActivating {

    protected static final VoxelShape RNORTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape RSOUTH_AABB = Block.box(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.box(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.box(15, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 24, 1);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 15, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15, 0, 0, 16, 24, 16);

    public BlockCatwalkGate(Properties properties) {
        super(properties);
    }

    public BlockCatwalkGate()
    {
        super(Block.Properties.of(Material.METAL));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Vector3d hit = context.getClickLocation();
        Vector3d hitQuad = hit.subtract(Vector3d.atCenterOf(context.getClickedPos()));

        return defaultBlockState().setValue(FACING, quadToDir(hitQuad));
    }

    public Direction quadToDir(Vector3d vector3d){
        if(vector3d.z > vector3d.x && vector3d.z > -vector3d.x)
            return Direction.SOUTH;
        if(vector3d.z < vector3d.x && vector3d.z < -vector3d.x)
            return Direction.NORTH;
        if(vector3d.z > vector3d.x && vector3d.z < -vector3d.x)
            return Direction.WEST;
        if(vector3d.z < vector3d.x && vector3d.z > -vector3d.x)
            return Direction.EAST;
        return Direction.NORTH;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        Direction face = state.getValue(FACING);
        if (face == Direction.NORTH)
        {
            return RNORTH_AABB;
        }
        if (face == Direction.SOUTH)
        {
            return RSOUTH_AABB;
        }
        if (face == Direction.WEST)
        {
            return RWEST_AABB;
        }
        if (face == Direction.EAST)
        {
            return REAST_AABB;
        }
        return RNORTH_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        Boolean open = state.getValue(ACTIVE);
        VoxelShape FINAL_SHAPE = NULL_SHAPE;
        Direction face = state.getValue(FACING);
        if (!open)
        {
            if (face == Direction.NORTH)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
            } else if (face == Direction.SOUTH)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
            } else if (face == Direction.WEST)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            } else
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
            }
        }
        return FINAL_SHAPE;
    }
}
