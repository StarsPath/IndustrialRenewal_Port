package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.IndustrialRenewal.item.IRItemDrill;
import com.cassiokf.IndustrialRenewal.item.ItemPowerScrewDrive;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityMiner;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockMiner extends Block3x3x3Base<TileEntityMiner> {
    public BlockMiner(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntityMiner te = (TileEntityMiner) world.getBlockEntity(pos);
        if (te != null) te.dropAllItems();
        super.onRemove(state, world, pos, oldState, isMoving);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        TileEntityMiner tile = (TileEntityMiner) worldIn.getBlockEntity(pos);
        IItemHandler itemHandler = tile.getDrillHandler();
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof IRItemDrill || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof IRItemDrill && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isClientSide)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty() && !tile.isRunning())
            {
                if (!worldIn.isClientSide)
                {
                    player.addItem(itemHandler.extractItem(0, 64, false));
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public TileEntityMiner createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityMiner();
    }
}
