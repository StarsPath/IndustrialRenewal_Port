package com.cassiokf.industrialrenewal.events;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.model.*;
import com.cassiokf.industrialrenewal.entity.render.*;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.init.ModEntity;
import com.cassiokf.industrialrenewal.init.ModMenus;
import com.cassiokf.industrialrenewal.menus.screens.CargoLoaderScreen;
import com.cassiokf.industrialrenewal.menus.screens.LatheScreen;
import com.cassiokf.industrialrenewal.menus.screens.SteamLocomotiveScreen;
import com.cassiokf.industrialrenewal.menus.screens.StorageChestScreen;
import com.cassiokf.industrialrenewal.tesr.*;
import net.minecraft.client.gui.screens.MenuScreens;
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
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_TANK.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STORAGE_CHEST.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LATHE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPANEL_FRAME.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DAM_GENERATOR.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DAM_TURBINE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TRANSFORMER.get(), RenderType.translucent());

        });

        MenuScreens.register(ModMenus.STORAGE_CHEST_CONTAINER.get(), StorageChestScreen::new);
        MenuScreens.register(ModMenus.LATHE_CONTAINER.get(), LatheScreen::new);
        MenuScreens.register(ModMenus.CARGO_LOADER_CONTAINER.get(), CargoLoaderScreen::new);

        MenuScreens.register(ModMenus.STEAM_LOCOMOTIVE_MENU.get(), SteamLocomotiveScreen::new);
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
        event.registerBlockEntityRenderer(ModBlockEntity.FLUID_TANK_TILE.get(), TESRFluidTank::new);
        event.registerBlockEntityRenderer(ModBlockEntity.LATHE_TILE.get(), TESRLathe::new);
        event.registerBlockEntityRenderer(ModBlockEntity.SOLAR_PANEL_FRAME.get(), TESRSolarPanelFrame::new);
        event.registerBlockEntityRenderer(ModBlockEntity.DAM_TURBINE_TILE.get(), TESRDamTurbine::new);
        event.registerBlockEntityRenderer(ModBlockEntity.DAM_GENERATOR.get(), TESRDamGenerator::new);
        event.registerBlockEntityRenderer(ModBlockEntity.TRANSFORMER_TILE.get(), TESRTransformerHV::new);
        event.registerBlockEntityRenderer(ModBlockEntity.ISOLATOR_TILE.get(), TESRWire::new);

    }


    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntity.FLAT_CART.get(), RenderFlatCart::new);
        event.registerEntityRenderer(ModEntity.PASSENGER_CART.get(), RenderPassengerCart::new);
        event.registerEntityRenderer(ModEntity.PASSENGER_CART_MK2.get(), RenderPassengerCartMk2::new);
        event.registerEntityRenderer(ModEntity.CARGO_CONTAINER.get(), RenderCargoContainer::new);
        event.registerEntityRenderer(ModEntity.FLUID_CONTAINER.get(), RenderFluidContainer::new);
        event.registerEntityRenderer(ModEntity.STEAM_LOCOMOTIVE.get(), RenderSteamLocomotive::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelCartFlat.LAYER_LOCATION, ModelCartFlat::createBodyLayer);
        event.registerLayerDefinition(ModelPassengerCart.LAYER_LOCATION, ModelPassengerCart::createBodyLayer);
        event.registerLayerDefinition(ModelPassengerCartMk2.LAYER_LOCATION, ModelPassengerCartMk2::createBodyLayer);
        event.registerLayerDefinition(ModelCargoContainer.LAYER_LOCATION, ModelCargoContainer::createBodyLayer);
        event.registerLayerDefinition(ModelCartFluidTank.LAYER_LOCATION, ModelCartFluidTank::createBodyLayer);
        event.registerLayerDefinition(ModelSteamLocomotive.LAYER_LOCATION, ModelSteamLocomotive::createBodyLayer);
    }
}
