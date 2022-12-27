package com.cassiokf.industrialrenewal.blocks.locomotion.rails;

import com.cassiokf.industrialrenewal.entity.LocomotiveBase;
import com.cassiokf.industrialrenewal.util.TrainUtil;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public abstract class BlockRailFacing extends BaseRailBlock
{
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BlockRailFacing(Properties properties)
    {
        super(true, properties.noCollission().strength(0.7F).sound(SoundType.METAL));
    }

    public static void propelMinecart(BlockState state, AbstractMinecart minecart)
    {
        RailShape dir = state.getValue(BlockRailFacing.SHAPE);
        Direction facing = state.getValue(FACING);
        double speed = 0.4d;
        if (dir == RailShape.EAST_WEST || dir == RailShape.ASCENDING_EAST || dir == RailShape.ASCENDING_WEST)
        {
            if (facing == Direction.EAST)
                propelInDir(minecart, new Vec3(speed, 0, 0));
             else
                propelInDir(minecart, new Vec3(-speed, 0, 0));
        }
        else if (dir == RailShape.NORTH_SOUTH || dir == RailShape.ASCENDING_NORTH || dir == RailShape.ASCENDING_SOUTH)
        {
            if (facing == Direction.SOUTH)
                propelInDir(minecart, new Vec3(0, 0, speed));
            else
                propelInDir(minecart, new Vec3(0, 0, -speed));
        }
    }

    public static void propelInDir(AbstractMinecart minecart, Vec3 vec3){
        if(minecart instanceof LocomotiveBase locomotiveBase){
            locomotiveBase.directionOverride = TrainUtil.getTravelDirection(vec3);
//            locomotiveBase.setDeltaMovement(vec3);

//            Utils.debug("SET LOCOMOTIVE TO DIR", TrainUtil.getTravelDirection(vec3));
        }
        else{
            minecart.setDeltaMovement(vec3);
        }
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SHAPE, FACING, WATERLOGGED);

    }
}
