package com.cassiokf.industrialrenewal.blocks.abstracts;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityMultiBlocksTube;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockPipeBase<TE extends BlockEntityMultiBlocksTube> extends BlockConnectedMultiblocks<TE> {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public static final BooleanProperty FLOOR = BooleanProperty.create("floor");
    public static final BooleanProperty UPFlOOR = BooleanProperty.create("upfloor");

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
                .setValue(FLOOR, false)
                .setValue(UPFlOOR, false));
    }

    public boolean isMaster(Level world, BlockPos pos)
    {
        BlockEntityMultiBlocksTube te = (BlockEntityMultiBlocksTube) world.getBlockEntity(pos);
        return te != null && te.isMaster();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, FLOOR, UPFlOOR);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        ItemStack playerStack = player.getItemInHand(handIn);
//        if(playerStack.getItem().equals(ModBlocks.INDUSTRIAL_FLOOR.get().asItem()) && !state.getValue(FLOOR)){
//            state = state.setValue(FLOOR, true);
//            BlockState stateAbove = worldIn.getBlockState(pos.above());
//            if(stateAbove.getBlock() instanceof BlockPipeBase) {
//                worldIn.setBlock(pos, state.setValue(UPFlOOR, stateAbove.getValue(FLOOR)), 3);
//            }
//            else{
//                worldIn.setBlock(pos, state.setValue(UPFlOOR, false), 3);
//            }
//            if (!player.isCreative())
//                playerStack.shrink(1);
//            return InteractionResult.SUCCESS;
//        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }


//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
//        return defaultBlockState();
//        //return super.getStateForPlacement(p_196258_1_);
//    }

//    @Override
//    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
//        ItemStack playerStack = player.getItemInHand(handIn);
//        if(playerStack.getItem().equals(ModBlocks.INDUSTRIAL_FLOOR.get().asItem()) && !state.getValue(FLOOR)){
//            state = state.setValue(FLOOR, true);
//            BlockState stateAbove = worldIn.getBlockState(pos.above());
//            if(stateAbove.getBlock() instanceof BlockPipeBase) {
//                worldIn.setBlock(pos, state.setValue(UPFlOOR, stateAbove.getValue(FLOOR)), 3);
//            }
//            else{
//                worldIn.setBlock(pos, state.setValue(UPFlOOR, false), 3);
//            }
//            if (!player.isCreative())
//                playerStack.shrink(1);
//            return ActionResultType.SUCCESS;
//        }
//
//        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
//    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                if(canConnectTo(worldIn, pos, direction)){
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), true), 3);
                }
                else{
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), false), 3);
                }
            }

            BlockState stateAbove = worldIn.getBlockState(pos.above());
            if(stateAbove.getBlock() instanceof BlockPipeBase){
                worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(UPFlOOR, stateAbove.getValue(FLOOR)), 3);
            }
            else{
                worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(UPFlOOR, false), 3);
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }


//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
//        if(!worldIn.isClientSide){
//            for(Direction direction : Direction.values()){
//                if(canConnectTo(worldIn, pos, direction)){
//                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), true), Constants.BlockFlags.DEFAULT);
//                }
//                else{
//                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), false), Constants.BlockFlags.DEFAULT);
//                }
//            }
//
//            BlockState stateAbove = worldIn.getBlockState(pos.above());
//            if(stateAbove.getBlock() instanceof BlockPipeBase){
//                worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(UPFlOOR, stateAbove.getValue(FLOOR)), 3);
//            }
//            else{
//                worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(UPFlOOR, false), 3);
//            }
//        }
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
//    }
//
//    @Override
//    public void destroy(IWorld world, BlockPos pos, BlockState state) {
//        super.destroy(world, pos, state);
//        if(state.getValue(FLOOR))
//            popResource((World)world, pos, new ItemStack(ModBlocks.INDUSTRIAL_FLOOR.get()));
//    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return true;
    }

    public final boolean isConnected(BlockGetter worldIn, BlockPos pos, Direction facing)
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

    public abstract boolean canConnectTo(BlockGetter world, BlockPos pos, Direction facing);
}
