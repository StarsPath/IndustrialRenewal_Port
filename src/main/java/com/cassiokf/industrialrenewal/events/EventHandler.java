package com.cassiokf.industrialrenewal.events;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModFluids;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
//    @SubscribeEvent
//    public static void onPlayerInteractWithMineCarts(PlayerInteractEvent.EntityInteract event){
//        Entity entity = event.getTarget();
//        World world = event.getWorld();
//        PlayerEntity playerEntity = event.getPlayer();
//        if(!world.isClientSide){
//            if(entity instanceof AbstractMinecartEntity && event.getItemStack().getItem() instanceof ItemCartLinker){
//                event.setCanceled(true);
//                ItemCartLinker.onPlayerUseLinkableItemOnCart(playerEntity, (AbstractMinecartEntity) event.getTarget());
//            }
//        }
//    }
}
