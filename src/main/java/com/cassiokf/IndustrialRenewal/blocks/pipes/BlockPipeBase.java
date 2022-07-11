package com.cassiokf.IndustrialRenewal.blocks.pipes;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockConnectedMultiblocks;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockPipeBase<TE extends TileEntityMultiBlocksTube> extends BlockConnectedMultiblocks<TE> {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public static final BooleanProperty FLOOR = BooleanProperty.create("floor");

    private static float NORTHZ1 = 0.250f;
    private static float SOUTHZ2 = 0.750f;
    private static float WESTX1 = 0.250f;
    private static float EASTX2 = 0.750f;
    private static double UP2 = 16;
    private static double DOWN1 = 0;

    public float nodeWidth;
    public float nodeHeight;

    public BlockPipeBase(Block.Properties property, float nodeWidth, float nodeHeight)
    {
        super(property);
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        registerDefaultState(this.defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(FLOOR, false));
    }

    public boolean isMaster(IBlockReader world, BlockPos pos)
    {
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) world.getBlockEntity(pos);
        return te != null && te.isMaster();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        //builder.add(MASTER, SOUTH, NORTH, EAST, WEST, UP, DOWN, CSOUTH, CNORTH, CEAST, CWEST, CUP, CDOWN);
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, FLOOR);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return defaultBlockState();
        //return super.getStateForPlacement(p_196258_1_);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        ItemStack playerStack = player.getItemInHand(handIn);
        if(playerStack.getItem().equals(ModBlocks.INDUSTRIAL_FLOOR.get().asItem()) && !state.getValue(FLOOR)){
            worldIn.setBlock(pos, state.setValue(FLOOR, true), 3);
            if (!player.isCreative())
                playerStack.shrink(1);
            return ActionResultType.SUCCESS;
        }

        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
        if(state.getValue(FLOOR))
            popResource((World)world, pos, new ItemStack(ModBlocks.INDUSTRIAL_FLOOR.get()));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if(state.getValue(FLOOR)){
            return FULL_SHAPE;
        }
        if (isConnected(worldIn, pos, Direction.NORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.SOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = 8 + (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.WEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.EAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = 8 + (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.DOWN))
        {
            DOWN1 = 0;
        } else
        {
            DOWN1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(worldIn, pos, Direction.UP))
        {
            UP2 = 16;
        } else
        {
            UP2 = 8 + (nodeWidth / 2);
        }
        return Block.box(WESTX1, DOWN1, NORTHZ1, EASTX2, UP2, SOUTHZ2);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    public final boolean isConnected(IBlockReader worldIn, BlockPos pos, Direction facing)
    {
        if(worldIn.getBlockState(pos).getBlock() instanceof BlockPipeBase)
            return worldIn.getBlockState(pos).getValue(directionToBooleanProp(facing));
        return false;
    }

    public BooleanProperty directionToBooleanProp(Direction d){
        switch (d){
            case UP: return UP;
            case DOWN: return DOWN;
            default:
            case NORTH: return NORTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case SOUTH: return SOUTH;
        }
    }

    public abstract boolean canConnectTo(IBlockReader world, BlockPos pos, Direction facing);
}
