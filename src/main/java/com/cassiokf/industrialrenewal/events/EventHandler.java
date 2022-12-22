package com.cassiokf.industrialrenewal.events;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModFluids;
import com.cassiokf.industrialrenewal.items.ItemCartLinker;
import com.cassiokf.industrialrenewal.util.CouplingHandler;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = IndustrialRenewal.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void entityInFluid(LivingEvent.LivingUpdateEvent event){
        Entity entity = event.getEntity();
        entity.updateFluidHeightAndDoFluidPushing(ModFluids.STEAM_TAG, 0);

        if(entity.getFluidHeight(ModFluids.STEAM_TAG) > 0.0D){
            entity.hurt(new DamageSource("steamBurn"), 2f);
        }
    }
    @SubscribeEvent
    public static void onPlayerInteractWithMineCarts(PlayerInteractEvent.EntityInteract event){
        Entity entity = event.getTarget();
        Level world = event.getWorld();
        Player playerEntity = event.getPlayer();
        if(!world.isClientSide){
            if(entity instanceof AbstractMinecart && event.getItemStack().getItem() instanceof ItemCartLinker){
                event.setCanceled(true);
                ItemCartLinker.onPlayerUseLinkableItemOnCart(playerEntity, (AbstractMinecart) event.getTarget());
            }
        }
    }

    @SubscribeEvent
    public static void onServerTickEvent(TickEvent.WorldTickEvent event){
        ServerLevel level = (ServerLevel) event.world;
        Iterable<Entity> entityIterable = level.getAllEntities();

        entityIterable.forEach((x)->{
            if(x instanceof AbstractMinecart){
                CouplingHandler.onMinecartTick((AbstractMinecart) x);
            }
        });

    }

//    @SubscribeEvent
//    public static void onMinecartEvent(EntityJoinWorldEvent event){
//        Entity entity = event.getEntity();
//        if(entity instanceof AbstractMinecart){
//            Utils.debug("MINECART");
//            CouplingHandler.onMinecartTick((AbstractMinecart) entity);
//        }
//    }

//    @SubscribeEvent
//    public static void onMinecartEvent(LivingEvent event){
//        Entity entity = event.getEntity();
//        if(entity instanceof AbstractMinecart){
//            Utils.debug("MINECART");
//            CouplingHandler.onMinecartTick((AbstractMinecart) entity);
//        }
//    }
//
//
//    @SubscribeEvent
//    public static void onMinecartEvent(net.minecraftforge.event.entity.EntityEvent event){
//        Entity entity = event.getEntity();
//        if(entity instanceof AbstractMinecart){
//            Utils.debug("MINECART");
//            CouplingHandler.onMinecartTick((AbstractMinecart) entity);
//        }
//    }
}
