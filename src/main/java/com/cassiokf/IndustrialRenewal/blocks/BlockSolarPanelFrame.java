package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockConnectedMultiblocks;
import com.cassiokf.IndustrialRenewal.item.ItemPowerScrewDrive;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntitySolarPanelFrame;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class BlockSolarPanelFrame extends BlockConnectedMultiblocks<TileEntitySolarPanelFrame> {

    public BlockSolarPanelFrame(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        TileEntitySolarPanelFrame tile = (TileEntitySolarPanelFrame) worldIn.getBlockEntity(pos);
        IItemHandler itemHandler = tile.getPanelHandler();
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!heldItem.isEmpty() && (Block.byItem(heldItem.getItem()) instanceof BlockSolarPanel || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (Block.byItem(heldItem.getItem()) instanceof BlockSolarPanel && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isClientSide)
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!worldIn.isClientSide)
                {
                    ItemStack panel = itemHandler.extractItem(0, 1, false);
                    if (!player.isCreative()) player.addItem(panel);
                }
                return ActionResultType.SUCCESS;
            }
        }

        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }

    @Override
    public TileEntitySolarPanelFrame createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntitySolarPanelFrame();
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return false;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0f;
        //return super.getShadeBrightness(p_220080_1_, p_220080_2_, p_220080_3_);
    }
}
