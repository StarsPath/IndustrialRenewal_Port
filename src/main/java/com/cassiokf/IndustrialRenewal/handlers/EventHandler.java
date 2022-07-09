package com.cassiokf.IndustrialRenewal.handlers;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.item.ItemCartLinker;
//import com.cassiokf.IndustrialRenewal.util.CouplingHandler;
import com.cassiokf.IndustrialRenewal.util.CouplingHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = References.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void onPlayerInteractWithMineCarts(PlayerInteractEvent.EntityInteract event){
        Entity entity = event.getTarget();
        World world = event.getWorld();
        PlayerEntity playerEntity = event.getPlayer();
        if(!world.isClientSide){
            if(entity instanceof AbstractMinecartEntity && event.getItemStack().getItem() instanceof ItemCartLinker){
                event.setCanceled(true);
                ItemCartLinker.onPlayerUseLinkableItemOnCart(playerEntity, (AbstractMinecartEntity) event.getTarget());
            }
        }
    }
}
