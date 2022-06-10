package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;

import javax.annotation.Nullable;

public abstract class BlockAbstractSixWayConnections extends BlockAbstractFourConnections{

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    private static double UP2 = 16;
    private static double DOWN1 = 0;

    public BlockAbstractSixWayConnections(Properties properties, float nodeWidth, float nodeHeight, float collisionY) {
        super(properties, nodeWidth, nodeHeight, collisionY);
    }
    public BlockAbstractSixWayConnections(Properties properties, float nodeWidth, float nodeHeight)
    {
        super(properties, nodeWidth, nodeHeight, 16);
    }

    @Override
    protected BlockState getInitDefaultState() {
        BlockState defaultState = super.getInitDefaultState();
        for (Direction dir : Direction.values())
        {
            defaultState = defaultState.setValue(getPropertyBasedOnDirection(dir), false);
        }
        return defaultState;
    }

    @Override
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
            SOUTHZ2 = (nodeWidth / 2) + 8;
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
            EASTX2 = (nodeWidth / 2) + 8;
        }
        if (isConnected(state, UP))
        {
            UP2 = 16;
        } else
        {
            UP2 = 8 + (nodeHeight / 2);
        }
        if (isConnected(state, DOWN))
        {
            DOWN1 = 0;
        } else
        {
            DOWN1 = 8 - (nodeHeight / 2);
        }
        return Block.box(WESTX1, DOWN1, NORTHZ1, EASTX2, (collision && fenceCollision()) ? 24 : UP2, SOUTHZ2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState state = getInitDefaultState();
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(context.getLevel(), context.getClickedPos(), face));
        }
        return state;
    }

    @Override
    public BooleanProperty getPropertyBasedOnDirection(Direction direction)
    {
        switch (direction)
        {
            default:
            case DOWN:
                return DOWN;
            case UP:
                return UP;
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

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UP, DOWN);
    }
}
