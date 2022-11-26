package com.cassiokf.industrialrenewal.events;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.tesr.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = IndustrialRenewal.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOLAR_PANEL.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BATTERY_BANK.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STEAM_BOILER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STEAM_TURBINE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.INDUSTRIAL_BATTERY_BANK.get(), RenderType.translucent());

        });
    }

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(ModBlockEntity.BATTERY_BANK.get(), TESRBatteryBank::new);
        event.registerBlockEntityRenderer(ModBlockEntity.WIND_TURBINE_TILE.get(), TESRWindTurbineHead::new);
        event.registerBlockEntityRenderer(ModBlockEntity.TURBINE_PILLAR_TILE.get(), TESRWindTurbinePillar::new);
        event.registerBlockEntityRenderer(ModBlockEntity.PORTABLE_GENERATOR_TILE.get(), TESRPortableGenerator::new);
        event.registerBlockEntityRenderer(ModBlockEntity.CONVEYOR_TILE.get(), TESRConveyor::new);
        event.registerBlockEntityRenderer(ModBlockEntity.STEAM_BOILER_TILE.get(), TESRSteamBoiler::new);
        event.registerBlockEntityRenderer(ModBlockEntity.STEAM_TURBINE_TILE.get(), TESRSteamTurbine::new);
        event.registerBlockEntityRenderer(ModBlockEntity.MINER_TILE.get(), TESRMining::new);
        event.registerBlockEntityRenderer(ModBlockEntity.INDUSTRIAL_BATTERY_TILE.get(), TESRIndustrialBatteryBank::new);

    }
}
