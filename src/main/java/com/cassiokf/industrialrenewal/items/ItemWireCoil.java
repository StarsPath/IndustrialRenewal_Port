package com.cassiokf.industrialrenewal.items;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityHVIsolator;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemWireCoil extends IRBaseItem{
    BlockEntityHVIsolator firstClickedOn = null;

    private static final int maxDistance = Config.TRANSFORMER_MAX_DISTANCE.get();

    public ItemWireCoil(Properties props) {
        super(props);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();

        if(!level.isClientSide && context.getHand() == InteractionHand.MAIN_HAND && context.getItemInHand().getItem().equals(this)){
            BlockEntity te = level.getBlockEntity(blockPos);
            if(te != null && te instanceof BlockEntityHVIsolator && firstClickedOn == null){
                firstClickedOn = (BlockEntityHVIsolator)te;
                Utils.sendChatMessage(player, "Starting Link " + blockPos);
                return InteractionResult.PASS;
            }
            else if(te != null && te instanceof BlockEntityHVIsolator){
                BlockEntityHVIsolator tempClick = (BlockEntityHVIsolator)te;
                if(firstClickedOn == tempClick){
                    Utils.sendChatMessage(player, "Link Cancelled");
                }
                else if(Utils.distance(firstClickedOn.getBlockPos(), blockPos) > maxDistance){
                    Utils.sendChatMessage(player, "Link Cancelled, Too far");
                }
                else{
                    if(firstClickedOn.link(tempClick)){
                        Utils.sendChatMessage(player, "Link Complete " + blockPos);
                        context.getItemInHand().shrink(1);
                    }
                    else{
                        Utils.sendChatMessage(player, "Already Linked");
                    }
                }
                firstClickedOn = null;
                return InteractionResult.PASS;
            }
        }
        return super.useOn(context);
    }
}
