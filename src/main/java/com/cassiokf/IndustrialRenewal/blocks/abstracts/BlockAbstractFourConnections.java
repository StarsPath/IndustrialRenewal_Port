package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockAbstractFourConnections extends BlockAbstractNotFullCube{

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public static double NORTHZ1 = 4;
    public static double SOUTHZ2 = 12;
    public static double WESTX1 = 4;
    public static double EASTX2 = 12;

    public float nodeWidth;
    public float nodeHeight;
    public float collisionY;

    public BlockAbstractFourConnections(Properties properties, float nodeWidth, float nodeHeight, float collisionY)
    {
        super(properties.noOcclusion());
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.collisionY = collisionY;
        //setDefaultState(getDefaultBlockState());
    }

    public BlockAbstractFourConnections(Properties props) {
        super(props.noOcclusion());
        this.nodeWidth = 16;
        this.nodeHeight = 16;
        this.collisionY = 16;
    }

    @Override
    protected BlockState getInitDefaultState() {
        return super.getInitDefaultState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, false);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, true);
    }

    public VoxelShape getVoxelShape(BlockState state, boolean collision)
    {
        if (isConnected(state, NORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, SOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = nodeWidth / 2 + 8;
        }
        if (isConnected(state, WEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, EAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = nodeWidth / 2 + 8;
        }
        return Block.box(WESTX1, 8 - (nodeHeight / 2), NORTHZ1, EASTX2, (collision && fenceCollision()) ? 24 : (8 + (nodeHeight / 2)), SOUTHZ2);
    }

    public boolean fenceCollision()
    {
        return false;
    }

    public final boolean isConnected(final BlockState state, final BooleanProperty property)
    {
        return state.getValue(property);
    }

    public BooleanProperty getPropertyBasedOnDirection(Direction direction)
    {
        switch (direction)
        {
            default:
            case DOWN:
            case UP:
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
    }

    public abstract boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection);

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = getInitDefaultState();
        for (Direction face : Direction.Plane.HORIZONTAL)
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(context.getLevel(), context.getClickedPos(), face));
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, EAST, WEST);
    }
}
