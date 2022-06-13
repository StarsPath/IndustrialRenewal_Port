package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractFourConnections;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBrace extends BlockAbstractFourConnections {

    public static final EnumProperty<EnumOrientation> FACING = EnumProperty.create("facing", BlockBrace.EnumOrientation.class);

    public BlockBrace()
    {
        super(Block.Properties.of(Material.METAL));
        registerDefaultState(defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    protected boolean isValidConnection(final BlockState neighborState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection){
        Block nb = neighborState.getBlock();
        return nb instanceof BlockBrace;
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        return isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState p_230335_1_, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
        return NULL_SHAPE;
        //return super.getBlockSupportShape(p_230335_1_, p_230335_2_, p_230335_3_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        //BlockState state = super.getStateForPlacement(context);
        BlockState state = this.defaultBlockState();
        for(Direction face : Direction.Plane.HORIZONTAL){
            if(BlockBrace.EnumOrientation.forFacings(context.getClickedFace(), context.getHorizontalDirection()).getName().contains("down"))
                state = state.setValue(getPropertyBasedOnDirection(face.getOpposite()), canConnectTo(context.getLevel(), context.getClickedPos(), face));
            else
                state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(context.getLevel(), context.getClickedPos(), face));
        }
        return state.setValue(FACING, BlockBrace.EnumOrientation.forFacings(context.getClickedFace(), context.getHorizontalDirection()));
    }

    @Override
    public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        for(Direction face : Direction.Plane.HORIZONTAL){
            if(state.getValue(FACING).getName().contains("down"))
                state = state.setValue(getPropertyBasedOnDirection(face.getOpposite()), canConnectTo(level, pos, face));
            else
                state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(level, pos, face));
        }
        level.setBlock(pos, state, 2);
    }

    //=================================================================
    public enum EnumOrientation implements IStringSerializable
    {
        DOWN_EAST(0, "down_east", Direction.DOWN),
        EAST(1, "east", Direction.EAST),
        WEST(2, "west", Direction.WEST),
        SOUTH(3, "south", Direction.SOUTH),
        NORTH(4, "north", Direction.NORTH),
        DOWN_WEST(5, "down_west", Direction.DOWN),
        DOWN_SOUTH(6, "down_south", Direction.DOWN),
        DOWN_NORTH(7, "down_north", Direction.DOWN);

        private static final BlockBrace.EnumOrientation[] META_LOOKUP = new BlockBrace.EnumOrientation[values().length];

        static
        {
            for (BlockBrace.EnumOrientation blockbrace$enumorientation : values())
            {
                META_LOOKUP[blockbrace$enumorientation.getMetadata()] = blockbrace$enumorientation;
            }
        }

        private final int meta;
        private final String name;
        private final Direction facing;

        EnumOrientation(int meta, String name, Direction facing)
        {
            this.meta = meta;
            this.name = name;
            this.facing = facing;
        }

        public static BlockBrace.EnumOrientation byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public static BlockBrace.EnumOrientation forFacings(Direction clickedSide, Direction entityFacing)
        {
            switch (clickedSide)
            {
                case DOWN:
                case UP:
                    switch (entityFacing)
                    {
                        case EAST:
                            return DOWN_EAST;
                        case NORTH:
                            return DOWN_NORTH;
                        case SOUTH:
                            return DOWN_SOUTH;
                        case WEST:
                            return DOWN_WEST;

                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }

                case NORTH:
                    return SOUTH;
                case SOUTH:
                    return NORTH;
                case WEST:
                    return EAST;
                case EAST:
                    return WEST;
                default:
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
            }
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public Direction getFacing()
        {
            return this.facing;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
