package com.cassiokf.industrialrenewal.events;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.tesr.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = IndustrialRenewal.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){

    }

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(ModBlockEntity.BATTERY_BANK.get(), TESRBatteryBank::new);
        event.registerBlockEntityRenderer(ModBlockEntity.WIND_TURBINE_TILE.get(), TESRWindTurbineHead::new);
        event.registerBlockEntityRenderer(ModBlockEntity.TURBINE_PILLAR_TILE.get(), TESRWindTurbinePillar::new);
        event.registerBlockEntityRenderer(ModBlockEntity.PORTABLE_GENERATOR_TILE.get(), TESRPortableGenerator::new);
        event.registerBlockEntityRenderer(ModBlockEntity.CONVEYOR_TILE.get(), TESRConveyor::new);
    }
}
