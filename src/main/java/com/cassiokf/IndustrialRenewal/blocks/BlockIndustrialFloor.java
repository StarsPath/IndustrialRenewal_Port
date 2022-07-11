package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import com.cassiokf.IndustrialRenewal.blocks.industrialfloor.BlockFloorCable;
import com.cassiokf.IndustrialRenewal.blocks.industrialfloor.BlockFloorPipe;
import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockPipeBase;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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

    private static boolean isValidConnection(final BlockState neighborState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection)
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
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        return !isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                if(canConnectTo(worldIn, pos, direction))
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(getPropertyBasedOnDirection(direction), true), Constants.BlockFlags.DEFAULT);
                else
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(getPropertyBasedOnDirection(direction), false), Constants.BlockFlags.DEFAULT);
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
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack playerStack = player.getItemInHand(handIn);
        if(playerStack.getItem().equals(ModBlocks.FLUID_PIPE.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.FLUID_PIPE.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if(playerStack.getItem().equals(ModBlocks.HIGH_PRESSURE_PIPE.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.HIGH_PRESSURE_PIPE.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        else if(playerStack.getItem().equals(ModBlocks.ENERGYCABLE_LV.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.ENERGYCABLE_LV.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        else if(playerStack.getItem().equals(ModBlocks.ENERGYCABLE_MV.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.ENERGYCABLE_MV.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        else if(playerStack.getItem().equals(ModBlocks.ENERGYCABLE_HV.get().asItem())){
            worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
            worldIn.setBlock(pos, ModBlocks.ENERGYCABLE_HV.get().defaultBlockState().setValue(BlockPipeBase.FLOOR, true), 3);
            if (!player.isCreative())
            {
                playerStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }
}
