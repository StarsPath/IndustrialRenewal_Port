package com.cassiokf.IndustrialRenewal.blocks;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.util.function.BiFunction;

// Todo: Obsolete, pending delete
public class IRBaseBlockWall extends IRBaseBlock{

    public static final BooleanProperty CORE = BooleanProperty.create("core");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    private static float NORTHZ1 = 0.25f;
    private static float SOUTHZ2 = 0.75f;
    private static float WESTX1 = 0.25f;
    private static float EASTX2 = 0.75f;
    private static final float DOWNY1 = 0.0f;
    private static final float UPY2 = 1.0f;

    public IRBaseBlockWall(String name, BiFunction<Block, Item.Properties, Item> createItemBlock) {
        super(name, Properties.of(Material.STONE).sound(SoundType.STONE).strength(2f).noOcclusion() ,IRBlockItem::new);
    }

    public final boolean isConnected(final BlockState blockState, final BooleanProperty props){
        return blockState.getValue(props);
    }

    private boolean shouldRenderCenter(IBlockReader world, BlockPos ownPos)
    {
        return !((canCenterConnectTo(world, ownPos, Direction.NORTH) && canCenterConnectTo(world, ownPos, Direction.SOUTH) && !canCenterConnectTo(world, ownPos, Direction.EAST) && !canCenterConnectTo(world, ownPos, Direction.WEST))
                || (!canCenterConnectTo(world, ownPos, Direction.NORTH) && !canCenterConnectTo(world, ownPos, Direction.SOUTH) && canCenterConnectTo(world, ownPos, Direction.EAST) && canCenterConnectTo(world, ownPos, Direction.WEST)));
    }

    private boolean canCenterConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection.getNormal());
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        return nb instanceof IRBaseBlockWall || neighbourState.isFaceSturdy(worldIn, neighbourPos, neighbourDirection); //|| nb instanceof BlockWindow;
    }

    private boolean canConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighbourDirection)
    {
        final BlockPos neighbourPos = ownPos.offset(neighbourDirection.getNormal());
        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
        Block nb = neighbourState.getBlock();
        return nb instanceof IRBaseBlockWall || neighbourState.isFaceSturdy(worldIn, neighbourPos, neighbourDirection);
//                || nb instanceof BlockElectricGate
//                || nb instanceof BlockLight
//                || nb instanceof BlockSignalIndicator
//                || nb instanceof BlockWindow;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CORE, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return this.getCollisionShape(state, world, pos, context);
        //return super.getShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (isConnected(state, NORTH))
        {
            NORTHZ1 = 0.0f;
        }
        else
        {
            NORTHZ1 = 0.25f;
        }
        if (isConnected(state, SOUTH))
        {
            SOUTHZ2 = 1.0f;
        }
        else
        {
            SOUTHZ2 = 0.75f;
        }
        if (isConnected(state, WEST))
        {
            WESTX1 = 0.0f;
        }
        else
        {
            WESTX1 = 0.25f;
        }
        if (isConnected(state, EAST))
        {
            EASTX2 = 1.0f;
        }
        else
        {
            EASTX2 = 0.75f;
        }
        return Block.box(WESTX1, DOWNY1, NORTHZ1, EASTX2, UPY2, SOUTHZ2);
        //return super.getCollisionShape(state, world, pos, context);
    }
}
