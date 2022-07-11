package com.cassiokf.IndustrialRenewal.blocks.locomotion.rails;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;

public abstract class BlockRailFacing extends AbstractRailBlock
{
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BlockRailFacing(Properties properties)
    {
        super(true, properties.noCollission().strength(0.7F).sound(SoundType.METAL));
    }

    public static void propelMinecart(BlockState state, AbstractMinecartEntity minecart)
    {
        RailShape dir = state.getValue(BlockRailFacing.SHAPE);
        Direction facing = state.getValue(FACING);
        double speed = 0.4d;
        if (dir == RailShape.EAST_WEST || dir == RailShape.ASCENDING_EAST || dir == RailShape.ASCENDING_WEST)
        {
            if (facing == Direction.EAST)
            {
                minecart.setDeltaMovement(speed, 0, 0);
                //minecart.motionX = 0.2d;
            } else
            {
                minecart.setDeltaMovement(-speed, 0, 0);
                //minecart.motionX = -0.2d;
            }
        } else if (dir == RailShape.NORTH_SOUTH || dir == RailShape.ASCENDING_NORTH || dir == RailShape.ASCENDING_SOUTH)
        {
            if (facing == Direction.SOUTH)
            {
                minecart.setDeltaMovement(0, 0, speed);
                //minecart.motionZ = 0.2d;
            } else
            {
                minecart.setDeltaMovement(0, 0, -speed);
                //minecart.motionZ = -0.2d;
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction facing = context.getHorizontalDirection();
        RailShape shape = facing == Direction.NORTH || facing == Direction.SOUTH ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST;
        return super.getStateForPlacement(context).setValue(FACING, facing).setValue(SHAPE, shape);
    }

    @Nonnull
    @Override
    public Property<RailShape> getShapeProperty()
    {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SHAPE, FACING);
    }
}
