package com.cassiokf.industrialrenewal.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public abstract class BlockBase extends Block
{
    public static final VoxelShape NULL_SHAPE = Block.box(0, 0, 0, 0, 0, 0);
    protected static final VoxelShape FULL_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public BlockBase(Properties properties)
    {
        super(properties.strength(2f, 10f));
    }


    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }


    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }
}