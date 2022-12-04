package com.cassiokf.industrialrenewal.blocks.decor;

import com.cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockPipeBase;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockIndustrialFloor extends BlockAbstractSixWayConnections {

    protected static final VoxelShape NONE_AABB = Block.box(6, 6, 6, 10, 10, 10);

    protected static final VoxelShape C_UP_AABB = Block.box(0, 15, 0, 16, 16, 16);
    protected static final VoxelShape C_DOWN_AABB = Block.box(0, 0, 0, 16, 1, 16);
    protected static final VoxelShape C_NORTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
    protected static final VoxelShape C_SOUTH_AABB = Block.box(0, 0, 15, 16, 16, 16);
    protected static final VoxelShape C_WEST_AABB = Block.box(0, 0, 0, 1, 16, 16);
    protected static final VoxelShape C_EAST_AABB = Block.box(15, 0, 0, 16, 16, 16);

    public BlockIndustrialFloor() {
        super(Block.Properties.of(Material.METAL), 16, 16);
    }

    private static boolean isValidConnection(final BlockState neighborState, final BlockGetter world, final BlockPos ownPos, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();
        return nb instanceof BlockIndustrialFloor
                || (nb instanceof DoorBlock && neighborState.getValue(DoorBlock.FACING).equals(neighborDirection))
                || (neighborDirection.equals(Direction.DOWN) && nb instanceof BlockCatwalkLadder)
                || (neighborDirection.equals(Direction.UP) && nb instanceof BlockCatwalkHatch)
                //start check for horizontal Iladder
                || ((neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
                && nb instanceof BlockCatwalkLadder && !neighborState.getValue(BlockCatwalkLadder.ACTIVE))
                //end
                ;
    }

    @Override
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        return !isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                if(canConnectTo(worldIn, pos, direction))
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(getPropertyBasedOnDirection(direction), true), 3);
                else
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(getPropertyBasedOnDirection(direction), false), 3);
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

//    @Override
//    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        return getVoxelShape(state);
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        return VoxelShapes.or(getVoxelShape(state), NONE_AABB);
//    }
//
//    private VoxelShape getVoxelShape(BlockState state)
//    {
//        VoxelShape FINAL_SHAPE = NULL_SHAPE;
//        if (isConnected(state, UP))
//        {
//            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_UP_AABB);
//        }
//        if (isConnected(state, DOWN))
//        {
//            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_DOWN_AABB);
//        }
//        if (isConnected(state, NORTH))
//        {
//            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_NORTH_AABB);
//        }
//        if (isConnected(state, SOUTH))
//        {
//            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_SOUTH_AABB);
//        }
//        if (isConnected(state, WEST))
//        {
//            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_WEST_AABB);
//        }
//        if (isConnected(state, EAST))
//        {
//            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, C_EAST_AABB);
//        }
//        return FINAL_SHAPE;
//    }


    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack playerStack = player.getItemInHand(handIn);
        if(playerStack.getItem().equals(ModBlocks.FLUID_PIPE.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.FLUID_PIPE.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        if(playerStack.getItem().equals(ModBlocks.HIGH_PRESSURE_PIPE.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.HIGH_PRESSURE_PIPE.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        else if(playerStack.getItem().equals(ModBlocks.ENERGYCABLE_LV.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.ENERGYCABLE_LV.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        else if(playerStack.getItem().equals(ModBlocks.ENERGYCABLE_MV.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.ENERGYCABLE_MV.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        else if(playerStack.getItem().equals(ModBlocks.ENERGYCABLE_HV.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.ENERGYCABLE_HV.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }
}
