package com.cassiokf.IndustrialRenewal.item.locomotion;

import com.cassiokf.IndustrialRenewal.item.IRBaseItem;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class IRCartItemBase extends IRBaseItem {
    public IRCartItemBase(String name) {
        super(name, new Properties().stacksTo(16));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);

        if(!state.is(BlockTags.RAILS) || !(state.getBlock() instanceof AbstractRailBlock))
            return ActionResultType.FAIL;

        ItemStack stack = context.getItemInHand();

        if(!world.isClientSide){
            RailShape railShape = ((AbstractRailBlock) state.getBlock()).getRailDirection(state, world, pos, null);
            double d0 = 0.0D;

            if(railShape.isAscending())
                d0 = 0.5D;

            AbstractMinecartEntity minecartEntity = getEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);

            if (stack.hasCustomHoverName()) {
                minecartEntity.setCustomName(stack.getHoverName());
            }
            //Utils.debug("SPAWNING CART", , minecartEntity);
            world.addFreshEntity(minecartEntity);
            if(!context.getPlayer().isCreative())
                stack.shrink(1);

            return ActionResultType.SUCCESS;
        }

        return super.useOn(context);
    }

    public abstract AbstractMinecartEntity getEntity(World world, double x, double y, double z);
}
