package com.cassiokf.IndustrialRenewal;

import com.cassiokf.IndustrialRenewal.containers.screen.CargoLoaderScreen;
import com.cassiokf.IndustrialRenewal.containers.screen.FluidLoaderScreen;
import com.cassiokf.IndustrialRenewal.containers.screen.LatheScreen;
import com.cassiokf.IndustrialRenewal.containers.screen.StorageChestScreen;
import com.cassiokf.IndustrialRenewal.entity.EntityCargoContainer;
import com.cassiokf.IndustrialRenewal.entity.render.*;
import com.cassiokf.IndustrialRenewal.handlers.EventHandler;
import com.cassiokf.IndustrialRenewal.init.*;
import com.cassiokf.IndustrialRenewal.tesr.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("industrialrenewal")
public class industrialrenewal
{
    // Directly reference a log4j logger.
    public static List<Block> registeredIRBlocks = new ArrayList<>();
    public static List<Item> registeredIRItems = new ArrayList<>();

    public static final Logger LOGGER = LogManager.getLogger();
    public static final industrialrenewal.IndustrialRenewalTab IR_TAB = new industrialrenewal.IndustrialRenewalTab("industrialrenewal");


    public industrialrenewal() {
        // Register the setup method for modloading
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModTileEntities.register(modEventBus);
        ModItems.registerInit(modEventBus);
        ModBlocks.registerInit(modEventBus);
        ModFluids.init(modEventBus);
        ModContainers.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModEntity.register(modEventBus);

        modEventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        modEventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::doClientStuff);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static class IndustrialRenewalTab extends ItemGroup{

        public IndustrialRenewalTab(String label) {
            super(label);
        }

        @Override
        public ItemStack makeIcon() {
            //return ModItems.SCREWDRIVE.get().getDefaultInstance();
            return Items.IRON_BLOCK.getItem().getDefaultInstance();
        }
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("Industrial Renewal is loading preInit!");

        LOGGER.info("Done");
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        event.enqueueWork(PacketHandler::init);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);

        event.enqueueWork(()->{
            RenderTypeLookup.setRenderLayer(ModBlocks.SPANEL.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.BATTERYBANK.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.PORTABLE_GENERATOR.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.TRASH.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.TURBINE_PILLAR.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.WIND_TURBINE.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.STEAM_BOILER.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.MINER.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.INDUSTRIAL_BATTERY_BANK.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_TANK.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.STORAGE_CHEST.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.LATHE.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.ROTATIONAL_SHAFT.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.BOOSTER_RAIL.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.LIGHT.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModBlocks.FLUORESCENT.get(), RenderType.translucent());

            RenderTypeLookup.setRenderLayer(ModFluids.STEAM.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.STEAM_FLOWING.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.STEAM_BLOCK.get(), RenderType.translucent());

            ScreenManager.register(ModContainers.STORAGE_CHEST_CONTAINER.get(), StorageChestScreen::new);
            ScreenManager.register(ModContainers.LATHE_CONTAINER.get(), LatheScreen::new);
            ScreenManager.register(ModContainers.CARGO_LOADER_CONTAINER.get(), CargoLoaderScreen::new);
            ScreenManager.register(ModContainers.FLUID_LOADER_CONTAINER.get(), FluidLoaderScreen::new);
        });

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BATTERY_BANK_TILE.get(), TESRBatteryBank::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.PORTABLE_GENERATOR_TILE.get(), TESRPortableGenerator::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TURBINE_PILLAR_TILE.get(), TESRWindTurbinePillar::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WIND_TURBINE_TILE.get(), TESRWindTurbineHead::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.STEAM_BOILER_TILE.get(), TESRSteamBoiler::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.STEAM_TURBINE_TILE.get(), TESRSteamTurbine::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.MINER_TILE.get(), TESRMining::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.INDUSTRIAL_BATTERY_TILE.get(), TESRIndustrialBatteryBank::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.FLUID_TANK_TILE.get(), TESRFluidTank::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.LATHE_TILE.get(), TESRLathe::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CARGO_LOADER.get(), TESRCargoLoader::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.FLUID_LOADER.get(), TESRFluidLoader::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.DAM_TURBINE_TILE.get(), TESRDamTurbine::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.DAM_GENERATOR.get(), TESRDamGenerator::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CONVEYOR_TILE.get(), TESRConveyor::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CONVEYOR_HOPPER_TILE.get(), TESRConveyor::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.CONVEYOR_INSERTER_TILE.get(), TESRConveyor::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntity.CARGO_CONTAINER.get(), RenderCargoContainer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.FLAT_CART.get(), RenderFlatCart::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.PASSENGER_CAR.get(), RenderPassengerCar::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.FLUID_CONTAINER.get(), RenderFluidContainer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntity.PASSENGER_CART_MK2.get(), RenderPassengerCartMk2::new);
//        RenderingRegistry.registerEntityRenderingHandler(ModEntity.COUPLER_ENTITY.get(), RenderCouplerEntity::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("industrialrenewal", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public static <T>
    Supplier<T> bootstrapErrorToXCPInDev(Supplier<T> in)
    {
        if(FMLLoader.isProduction())
            return in;
        return () -> {
            try
            {
                return in.get();
            } catch(BootstrapMethodError e)
            {
                throw new RuntimeException(e);
            }
        };
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
//        @SubscribeEvent
//        public void registerModels(ModelRegistryEvent event)
//        {
//            industrialrenewal.LOGGER.info("registering new model");
//
//        }
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            // register a new block here
            ModBlocks.register(event.getRegistry());
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            ModItems.register(event.getRegistry());
            LOGGER.info("HELLO from Register Item");
        }
    }
}
