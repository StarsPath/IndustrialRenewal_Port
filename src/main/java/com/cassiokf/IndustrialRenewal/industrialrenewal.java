package com.cassiokf.IndustrialRenewal;

import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
import com.cassiokf.IndustrialRenewal.containers.screen.StorageChestScreen;
import com.cassiokf.IndustrialRenewal.init.*;
//import com.cassiokf.IndustrialRenewal.proxy.ClientProxy;
//import com.cassiokf.IndustrialRenewal.proxy.CommonProxy;
//import com.cassiokf.IndustrialRenewal.model.ModelLoaderCustom;
import com.cassiokf.IndustrialRenewal.tesr.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
//import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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


    //public static CommonProxy proxy;
//    public static CommonProxy proxy = DistExecutor.safeRunForDist(
//            bootstrapErrorToXCPInDev(() -> ClientProxy::new), bootstrapErrorToXCPInDev(() -> CommonProxy::new)
//    );


    public industrialrenewal() {
        // Register the setup method for modloading
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModTileEntities.register(modEventBus);
        ModItems.registerInit(modEventBus);
        ModBlocks.registerInit(modEventBus);
        ModFluids.init(modEventBus);
        ModContainers.register(modEventBus);

        modEventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        modEventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::doClientStuff);
        //modEventBus.addListener(this::registerModels);

        //ModItems.ITEMS.register(modEventBus);

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
        //ModelLoaderRegistry.registerLoader(new ResourceLocation(References.MODID, "smartmodel"), new ModelLoaderCustom());
        //industrialrenewal.proxy.preInit();
        //industrialrenewal.proxy.registerRenderers(); // doesnt do shit
        //proxy.preInit();
        //proxy.registerRenderers();
        LOGGER.info("Done");
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

            RenderTypeLookup.setRenderLayer(ModFluids.STEAM.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.STEAM_FLOWING.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(ModFluids.STEAM_BLOCK.get(), RenderType.translucent());

            ScreenManager.register(ModContainers.STORAGE_CHEST_CONTAINER.get(), StorageChestScreen::new);
        });

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BATTERY_BANK_TILE.get(), TESRBatteryBank::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.PORTABLE_GENERATOR_TILE.get(), TESRPortableGenerator::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.TURBINE_PILLAR_TILE.get(), TESRWindTurbinePillar::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.WIND_TURBINE_TILE.get(), TESRWindTurbineHead::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.STEAM_BOILER_TILE.get(), TESRSteamBoiler::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.STEAM_TURBINE_TILE.get(), TESRSteamTurbine::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.MINER_TILE.get(), TESRMining::new);
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

//    @SubscribeEvent
//    public void registerModels(ModelRegistryEvent event){
//        LOGGER.info("Registering models...");
//        ModelLoaderRegistry.registerLoader(new ResourceLocation(References.MODID, "model/smartmodel"), new ModelLoaderCustom());
//    }

//    @Mod.EventBusSubscriber
//    public static class RegistrationHandler{
//        @SubscribeEvent
//        public static void registerBlocks(RegistryEvent.Register<Block> event)
//        {
//            //ModBlocks.register(event.getRegistry());
//        }
//
//        @SubscribeEvent
//        public static void registerItems(RegistryEvent.Register<Item> event)
//        {
//            ModItems.register(event.getRegistry());
//            //ModBlocks.registerItemBlocks(event.getRegistry());
//            //ModItems.registerOreDict();
//            //ModBlocks.registerOreDict();
//        }
//
//        @SubscribeEvent
//        public static void registerItems(ModelRegistryEvent event) {
//            //ModItems.registerModels();
//            //ModBlocks.registerItemModels();
//        }
//
////        @SubscribeEvent
////        public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
////        {
////            if (event.getModID().equals(MODID))
////            {
////                ConfigManager.sync(MODID, Config.Type.INSTANCE);
////            }
////        }
//    }

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
