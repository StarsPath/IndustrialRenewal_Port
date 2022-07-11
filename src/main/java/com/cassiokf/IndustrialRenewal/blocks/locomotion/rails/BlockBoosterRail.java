package com.cassiokf.IndustrialRenewal.blocks.locomotion.rails;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBoosterRail extends BlockRailFacing
{
    public static BooleanProperty POWERED = BlockStateProperties.POWERED;
    public BlockBoosterRail()
    {
        super(Block.Properties.of(Material.METAL));
        registerDefaultState(defaultBlockState()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false));
    }

    @Override
    public void onMinecartPass(BlockState state, World world, BlockPos pos, AbstractMinecartEntity cart)
    {
        if(!world.isClientSide){
            if (state.getValue(POWERED)) {
                BlockRailFacing.propelMinecart(state, cart);
            } else {
                cart.setDeltaMovement(0, 0, 0);
                cart.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            }
        }
//        super.onMinecartPass(state, world, pos, cart);
    }

    @Override
    protected void updateState(BlockState state, World world, BlockPos pos, Block block) {
//        Utils.debug("void updateState called", state);
        boolean flag = state.getValue(POWERED);
        boolean flag1 = world.hasNeighborSignal(pos);
        if(flag!=flag1){
            state = state.setValue(POWERED, flag1);
            world.setBlock(pos, state, 3);
            world.updateNeighborsAt(pos.below(), this);
            if (state.getValue(SHAPE).isAscending()) {
                world.updateNeighborsAt(pos.above(), this);
            }
        }

        Direction facing = state.getValue(FACING);
        RailShape shape = state.getValue(SHAPE);
        switch (shape){
            case NORTH_SOUTH:
            case ASCENDING_NORTH:
            case ASCENDING_SOUTH:
                if(facing!=Direction.NORTH && facing!=Direction.SOUTH)
                    state = state.setValue(FACING, Direction.NORTH);
                break;
            case EAST_WEST:
            case ASCENDING_EAST:
            case ASCENDING_WEST:
                if(facing!=Direction.EAST && facing!=Direction.WEST)
                    state = state.setValue(FACING, Direction.EAST);
                break;
        }
        world.setBlock(pos, state, 3);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        Direction facing = p_185499_1_.getValue(FACING);
        switch(p_185499_2_) {
            case CLOCKWISE_180:
                p_185499_1_.setValue(FACING, facing.getOpposite());
                switch((RailShape)p_185499_1_.getValue(SHAPE)) {
                    case ASCENDING_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_SOUTH: //Forge fix: MC-196102
                    case EAST_WEST:
                        return p_185499_1_;
                }
            case COUNTERCLOCKWISE_90:
                p_185499_1_.setValue(FACING, facing.getCounterClockWise());
                switch((RailShape)p_185499_1_.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_WEST);
                }
            case CLOCKWISE_90:
                p_185499_1_.setValue(FACING, facing.getClockWise());
                switch((RailShape)p_185499_1_.getValue(SHAPE)) {
                    case NORTH_SOUTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_SOUTH);
                    case ASCENDING_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return p_185499_1_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return p_185499_1_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return p_185499_1_.setValue(SHAPE, RailShape.SOUTH_EAST);
                }
            default:
                return p_185499_1_;
        }
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        Direction facing = p_185471_1_.getValue(FACING);
        RailShape railshape = p_185471_1_.getValue(SHAPE);
        switch(p_185471_2_) {
            case LEFT_RIGHT:
                switch(railshape) {
                    case ASCENDING_NORTH:
                        return p_185471_1_.setValue(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return p_185471_1_.setValue(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return p_185471_1_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return p_185471_1_.setValue(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return p_185471_1_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return p_185471_1_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.mirror(p_185471_1_, p_185471_2_);
                }
            case FRONT_BACK:
                p_185471_1_.setValue(FACING, facing.getOpposite());
                switch(railshape) {
                    case ASCENDING_EAST:
                        return p_185471_1_.setValue(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return p_185471_1_.setValue(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return p_185471_1_.setValue(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return p_185471_1_.setValue(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return p_185471_1_.setValue(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return p_185471_1_.setValue(SHAPE, RailShape.NORTH_WEST);
                }
        }

        return super.mirror(p_185471_1_, p_185471_2_);
    }

//    @OnlyIn(Dist.CLIENT)
//    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
//    {
//        String str = I18n.get("tile.industrialrenewal.loader_rail.info");
//        tooltip.add(new StringTextComponent(str));
//    }

//    @Override
//    public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
//        return true;
//    }
//
//    @Override
//    public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
//        return false;
//    }
}