package com.cassiokf.industrialrenewal.items.locomotion;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.items.IRBaseItem;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;


public abstract class IRCartItemBase extends IRBaseItem {
    public IRCartItemBase() {
        super(new Properties().stacksTo(16).tab(IndustrialRenewal.IR_TAB));
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);

        if(!state.is(BlockTags.RAILS) || !(state.getBlock() instanceof BaseRailBlock))
            return InteractionResult.FAIL;

        ItemStack stack = context.getItemInHand();

        if(!world.isClientSide){
            RailShape railShape = ((BaseRailBlock) state.getBlock()).getRailDirection(state, world, pos, null);
            double d0 = 0.0D;

            if(railShape.isAscending())
                d0 = 0.5D;

            AbstractMinecart minecartEntity = getEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);

            if (stack.hasCustomHoverName()) {
                minecartEntity.setCustomName(stack.getHoverName());
            }
            //Utils.debug("SPAWNING CART", , minecartEntity);
            world.addFreshEntity(minecartEntity);
            if(!context.getPlayer().isCreative())
                stack.shrink(1);

            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    public abstract AbstractMinecart getEntity(Level world, double x, double y, double z);
}
