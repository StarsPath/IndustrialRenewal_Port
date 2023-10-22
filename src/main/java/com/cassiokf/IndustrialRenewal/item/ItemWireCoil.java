package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWireIsolator;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class ItemWireCoil extends IRBaseItem{
    TileEntityWireIsolator firstClickedOn = null;

    public ItemWireCoil(String name) {
        super(name, industrialrenewal.IR_TAB);
    }

    private final int TRANSFORMER_MAX_DISTANCE = Config.TRANSFORMER_MAX_DISTANCE.get();

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World level = context.getLevel();
        PlayerEntity player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();

        if(!level.isClientSide && context.getHand() == Hand.MAIN_HAND && context.getItemInHand().getItem().equals(this)){
            TileEntity te = level.getBlockEntity(blockPos);
            if(te != null && te instanceof TileEntityWireIsolator && firstClickedOn == null){
                firstClickedOn = (TileEntityWireIsolator)te;
                Utils.sendChatMessage(player, "Starting Link " + blockPos);
                return ActionResultType.PASS;
            }
            else if(te != null && te instanceof TileEntityWireIsolator){
                TileEntityWireIsolator tempClick = (TileEntityWireIsolator)te;
                if(firstClickedOn == tempClick){
                    Utils.sendChatMessage(player, "Link Cancelled");
                }
                else if(Utils.distance(firstClickedOn.getBlockPos(), blockPos) > TRANSFORMER_MAX_DISTANCE){
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
                return ActionResultType.PASS;
            }
        }
        return super.useOn(context);
    }
}
