package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacingWithActivating;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalkLadder extends BlockAbstractHorizontalFacingWithActivating {

    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    protected static final VoxelShape DOWN_AABB = Block.box(0, 0, 0, 16, 0.5, 16);
    protected static final VoxelShape LADDER_EAST_AABB = Block.box(0, 0, 0, 3, 16, 16);
    protected static final VoxelShape LADDER_WEST_AABB = Block.box(13, 0, 0, 16, 16, 16);
    protected static final VoxelShape LADDER_SOUTH_AABB = Block.box(0, 0, 0, 16, 16, 3);
    protected static final VoxelShape LADDER_NORTH_AABB = Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15.5, 0, 0, 16, 16, 16);

    public BlockCatwalkLadder(Properties properties) {
        super(properties);
    }

    public BlockCatwalkLadder()
    {
        super(Block.Properties.of(Material.METAL));
        registerDefaultState(defaultBlockState().setValue(DOWN, false).setValue(ACTIVE, true));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DOWN);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    private boolean downConnection(IBlockReader world, BlockPos pos)
    {
        Block downB = world.getBlockState(pos.below()).getBlock();
        return !(downB instanceof LadderBlock
                || downB instanceof BlockCatwalkLadder
                || downB instanceof BlockCatwalkHatch
                || downB instanceof BlockCatwalkStair
                || downB instanceof StairsBlock
                || downB instanceof TrapDoorBlock);
    }

    protected boolean OpenCageIf(final IBlockReader worldIn, BlockPos ownPos)
    {
        final BlockPos downpos = ownPos.below();
        final BlockPos twoDownPos = downpos.below();
        final BlockState downState = worldIn.getBlockState(downpos);
        final BlockState twoDownState = worldIn.getBlockState(twoDownPos);
        return !downState.isFaceSturdy(worldIn, downpos, Direction.UP)
                && (!(downState.getBlock() instanceof BlockCatwalkLadder)
                || downState.getValue(ACTIVE)
                || !twoDownState.isFaceSturdy(worldIn, twoDownPos, Direction.UP));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(ACTIVE, OpenCageIf(context.getLevel(), context.getClickedPos()))
                .setValue(DOWN, downConnection(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        //Utils.debug("neighbor changed", state, world, pos, block, neighbor, flag);
        state = defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(ACTIVE, OpenCageIf(world, pos))
                .setValue(DOWN, downConnection(world, pos));
        world.setBlockAndUpdate(pos, state);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        Direction face = state.getValue(FACING);
        boolean active = state.getValue(ACTIVE);
        boolean down = state.getValue(DOWN);

        VoxelShape FINAL_SHAPE = NULL_SHAPE;

        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_SOUTH_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        else if (face == Direction.SOUTH)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_NORTH_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        else if (face == Direction.WEST)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_EAST_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
            }
        }
        else
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, LADDER_WEST_AABB);
            if (active)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            }
        }
        if (down)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, DOWN_AABB);
        }
        return FINAL_SHAPE;
    }


}
