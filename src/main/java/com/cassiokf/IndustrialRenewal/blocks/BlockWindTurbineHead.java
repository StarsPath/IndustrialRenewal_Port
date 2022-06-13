package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.item.ItemPowerScrewDrive;
import com.cassiokf.IndustrialRenewal.item.ItemWindBlade;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWindTurbineHead;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockWindTurbineHead extends BlockAbstractHorizontalFacing {
    public BlockWindTurbineHead(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public BlockWindTurbineHead() {
        super(Properties.of(Material.METAL).strength(0.8f).noOcclusion());
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit) {

        TileEntityWindTurbineHead tile = (TileEntityWindTurbineHead) world.getBlockEntity(pos);
        if (tile == null) return ActionResultType.PASS;

        IItemHandler itemHandler = tile.getBladeHandler();
        ItemStack heldItem = playerEntity.getItemInHand(hand);
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemWindBlade || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemWindBlade && itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isClientSide())
                {
                    itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                    if (!playerEntity.isCreative()) heldItem.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && !itemHandler.getStackInSlot(0).isEmpty())
            {
                if (!world.isClientSide())
                {
                    playerEntity.addItem(itemHandler.extractItem(0, 64, false));
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;

        //return super.use(state, world, pos, playerEntity, hand, hit);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new StringTextComponent(String.format("Produces %d FR/t max", 128)));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0f;
        //return super.getShadeBrightness(p_220080_1_, p_220080_2_, p_220080_3_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getPlayer().isCrouching() ? context.getHorizontalDirection().getOpposite() : context.getHorizontalDirection());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityWindTurbineHead();
    }
}
