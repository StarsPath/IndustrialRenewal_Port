package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public abstract class BlockBase extends Block
{
    public static final VoxelShape NULL_SHAPE = Block.box(0, 0, 0, 0, 0, 0);
    protected static final VoxelShape FULL_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public BlockBase(Properties properties)
    {
        super(properties.strength(2f, 10f));
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        return ActionResultType.PASS;
    }
}