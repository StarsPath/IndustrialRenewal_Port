package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHandRail extends BlockAbstractHorizontalFacing {

    protected static final VoxelShape RNORTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape RSOUTH_AABB = Block.box(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.box(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.box(15, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 24, 1);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 1, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15, 0, 0, 16, 24, 16);

    public BlockHandRail(Properties properties) {
        super(properties);
    }
    public BlockHandRail()
    {
        super(Block.Properties.of(Material.METAL));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
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
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide){
            if (handIn == Hand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.screwDrive)) {
                    state = state.cycle(FACING);
                    worldIn.setBlock(pos, state, 2);
                    return ActionResultType.SUCCESS;
                }
            }
            else
                return ActionResultType.PASS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, true);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, false);
    }

    private VoxelShape getVoxelShape(BlockState state, boolean isForRender)
    {
        Direction face = state.getValue(FACING);
        if (face == Direction.NORTH)
        {
            return isForRender ? RNORTH_AABB : NORTH_AABB;
        }
        if (face == Direction.SOUTH)
        {
            return isForRender ? RSOUTH_AABB : SOUTH_AABB;
        }
        if (face == Direction.WEST)
        {
            return isForRender ? RWEST_AABB : WEST_AABB;
        } else
        {
            return isForRender ? REAST_AABB : EAST_AABB;
        }
    }
}
