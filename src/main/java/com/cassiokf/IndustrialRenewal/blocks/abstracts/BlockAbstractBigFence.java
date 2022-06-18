package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class BlockAbstractBigFence extends BlockBasicElectricFence{

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 2);

    public BlockAbstractBigFence(Properties property) {
        super(property, 8);
    }

    public BlockAbstractBigFence(Properties property, int nodeWidth) {
        super(property, nodeWidth);
    }

    public BlockAbstractBigFence(Properties property, int nodeWidth, int nodeHeight) {
        super(property, nodeWidth, nodeHeight);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, INDEX);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(!world.getBlockState(pos.above()).getMaterial().isReplaceable() ||
                !world.getBlockState(pos.above(2)).getMaterial().isReplaceable())
            return null;
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(INDEX, 0);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        return false;
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack) {
        if(!world.getBlockState(pos.above()).getMaterial().isReplaceable() ||
                !world.getBlockState(pos.above(2)).getMaterial().isReplaceable())
            return;
        if (state.getValue(INDEX) == 0)
        {
            world.setBlock(pos.above(), state.setValue(INDEX, 1), 3);
            world.setBlock(pos.above(2), state.setValue(INDEX, 2), 3);
        }
        super.setPlacedBy(world, pos, state, entity, itemStack);
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        if (state.getValue(INDEX) == 0)
        {
            world.removeBlock(pos.above(), true);
            world.removeBlock(pos.above(2), true);
        }
        else if(state.getValue(INDEX) == 1)
        {
            world.removeBlock(pos.below(), true);
            world.removeBlock(pos.above(), true);
        }
        else if(state.getValue(INDEX) == 2)
        {
            world.removeBlock(pos.below(), true);
            world.removeBlock(pos.below(2), true);
        }
        super.destroy(world, pos, state);
    }

    public abstract boolean isBigFence(World world, BlockPos pos);
}
