package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.item.ItemBlockCatwalk;
import com.cassiokf.IndustrialRenewal.item.ItemBlockCatwalkStair;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalkStair extends BlockAbstractHorizontalFacing {

    public static final BooleanProperty ACTIVE_LEFT = BooleanProperty.create("active_left");
    public static final BooleanProperty ACTIVE_RIGHT = BooleanProperty.create("active_right");

    protected static final VoxelShape BASE_AABB = Block.box(0, 0, 0, 16, 8, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 8, 0, 16, 16, 8);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 8, 8, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 8, 0, 8, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.box(8, 8, 0, 16, 16, 16);

    protected static final VoxelShape NC_AABB = Block.box(0, 0, 0, 16, 32, 0.5);
    protected static final VoxelShape SC_AABB = Block.box(0, 0, 15.5, 16, 32, 16);
    protected static final VoxelShape WC_AABB = Block.box(0, 0, 0, 0.5, 32, 16);
    protected static final VoxelShape EC_AABB = Block.box(15.5, 0, 0, 16, 32, 16);

    protected static final VoxelShape RNC_AABB = Block.box(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape RSC_AABB = Block.box(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape RWC_AABB = Block.box(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape REC_AABB = Block.box(15.5, 0, 0, 16, 16, 16);

    public BlockCatwalkStair(Properties properties) {
        super(properties);
    }

    public BlockCatwalkStair()
    {
        super(Block.Properties.of(Material.METAL));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockItemUseContext context) {
        //PlayerEntity player = context.getPlayer();
        if(!context.getPlayer().isCrouching())
            return context.getItemInHand().getItem() instanceof ItemBlockCatwalkStair || context.getItemInHand().getItem() instanceof ItemBlockCatwalk;
        // || Block.byItem(context.getItemInHand().getItem()) instanceof BlockCatwalk) && !player.isCrouching();
        return super.canBeReplaced(state, context);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide){
            if (handIn == Hand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.screwDrive)) {
                    state = state.cycle(FACING);
                    worldIn.setBlockAndUpdate(pos, state);
                    return ActionResultType.SUCCESS;
                }
                BlockPos posOffset = pos.relative(state.getValue(FACING)).above();
                BlockState stateOffset = worldIn.getBlockState(posOffset);

                BlockCatwalkStair catwalk_stair = playerItem.equals(ModBlocks.CATWALK_STAIR.get().asItem()) ? ModBlocks.CATWALK_STAIR.get() : (playerItem.equals(ModBlocks.CATWALK_STAIR_STEEL.get().asItem()) ? ModBlocks.CATWALK_STAIR_STEEL.get() : null);
                BlockCatwalk catwalk = playerItem.equals(ModBlocks.CATWALK.get().asItem()) ? ModBlocks.CATWALK.get() : (playerItem.equals(ModBlocks.CATWALK_STEEL.get().asItem()) ? ModBlocks.CATWALK_STEEL.get() : null);

//                if (catwalk_stair != null) {
//                    if (stateOffset.getMaterial().isReplaceable()) {
//                        worldIn.setBlockAndUpdate(posOffset, catwalk_stair.defaultBlockState().setValue(FACING, state.getValue(FACING)));
//                        worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
//                        if (!player.isCreative()) {
//                            //player.getHeldItemMainhand().shrink(1);
//                            player.getMainHandItem().shrink(1);
//                        }
//                        return ActionResultType.SUCCESS;
//                    }
//                }
//                else if(catwalk != null){
//                    if (stateOffset.getMaterial().isReplaceable()) {
//                        worldIn.setBlockAndUpdate(posOffset, catwalk.getStateForPlacement(worldIn, posOffset));
//                        worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
//                        if (!player.isCreative()) {
//                            //player.getHeldItemMainhand().shrink(1);
//                            player.getMainHandItem().shrink(1);
//                        }
//                        return ActionResultType.SUCCESS;
//                    }
//                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE_LEFT, ACTIVE_RIGHT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction facing = context.getHorizontalDirection();
        BlockState state = defaultBlockState().setValue(FACING, facing);
        return state.setValue(ACTIVE_LEFT, sideConnected(state, context.getLevel(), context.getClickedPos(), facing.getCounterClockWise()))
                .setValue(ACTIVE_RIGHT, sideConnected(state, context.getLevel(), context.getClickedPos(), facing.getClockWise()));
    }

    public BlockState getStateForPlacement(World level, BlockPos pos, Direction facing)
    {
        BlockState state = defaultBlockState().setValue(FACING, facing);
        return state.setValue(ACTIVE_LEFT, sideConnected(state, level, pos, facing.getCounterClockWise()))
                .setValue(ACTIVE_RIGHT, sideConnected(state, level, pos, facing.getClockWise()));
    }

    private Boolean sideConnected(BlockState state, IBlockReader world, BlockPos pos, Direction direction)
    {
        Direction face = state.getValue(FACING);
//        TileEntityCatWalkStair te = (TileEntityCatWalkStair) world.getTileEntity(pos);
//        if (te != null && te.isFacingBlackListed(direction)) return false;

        BlockPos posOffset = pos.relative(direction);
        BlockState stateOffset = world.getBlockState(posOffset);

        if (stateOffset.getBlock() instanceof BlockCatwalkStair)
        {
            Direction sideStairFace = stateOffset.getValue(FACING);
            return !(sideStairFace == face);
        }
        return true;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        VoxelShape FINAL_SHAPE = BASE_AABB;

        Direction face = state.getValue(FACING);
        Boolean left = state.getValue(ACTIVE_LEFT);
        Boolean right = state.getValue(ACTIVE_RIGHT);
        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
        }
        if (face == Direction.SOUTH) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
        }
        if (face == Direction.WEST) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
        }
        if (face == Direction.EAST) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
        }
        return FINAL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state, false);
    }

    private VoxelShape getVoxelShape(BlockState state, boolean isForRender)
    {
        VoxelShape FINAL_SHAPE = BASE_AABB;

        Direction face = state.getValue(FACING);
        Boolean left = state.getValue(ACTIVE_LEFT);
        Boolean right = state.getValue(ACTIVE_RIGHT);
        if (face == Direction.NORTH)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
            if (left)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RWC_AABB : WC_AABB);
            }
            if (right)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? REC_AABB : EC_AABB);
            }

        }
        if (face == Direction.SOUTH) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
            if (left) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? REC_AABB : EC_AABB);
            }
            if (right) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RWC_AABB : WC_AABB);
            }
        }
        if (face == Direction.WEST) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
            if (left) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RSC_AABB : SC_AABB);
            }
            if (right) {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RNC_AABB : NC_AABB);
            }
        }
        if (face == Direction.EAST) {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
            if (left)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RNC_AABB : NC_AABB);
            }
            if (right)
            {
                FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, isForRender ? RSC_AABB : SC_AABB);
            }
        }
        return FINAL_SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        //Utils.debug("neighbor changed", state, world, pos, block, neighbor, flag);
        state = state.setValue(ACTIVE_LEFT, sideConnected(state, world, pos, state.getValue(FACING).getCounterClockWise()))
                .setValue(ACTIVE_RIGHT, sideConnected(state, world, pos, state.getValue(FACING).getClockWise()));
        //Utils.debug("new state", state);
        world.setBlock(pos, state, 2);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }


}
