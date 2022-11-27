package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.items.*;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialRenewal.MODID);

    public static final RegistryObject<Item> STEAM_BUCKET = ITEMS.register("steam_bucket",
            () -> new BucketItem(() -> ModFluids.STEAM.get(),
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MATERIALS)));

//    public static final IRBaseItem pointer;
    public static final RegistryObject<Item> pointer = registerItem("pointer", IRBaseItem::new);
    public static final RegistryObject<Item> limiter = registerItem("limiter", IRBaseItem::new);
    public static final RegistryObject<Item> pointerLong = registerItem("pointer_long", IRBaseItem::new);
    public static final RegistryObject<Item> fire = registerItem("fire", IRBaseItem::new);
    public static final RegistryObject<Item> barLevel = registerItem("bar_level", IRBaseItem::new);
    public static final RegistryObject<Item> fluidLoaderArm = registerItem("fluid_loader_arm", IRBaseItem::new);
    public static final RegistryObject<Item> tambor = registerItem("rotary_drum", IRBaseItem::new);
    public static final RegistryObject<Item> cutter = registerItem("lathecutter", IRBaseItem::new);
    public static final RegistryObject<Item> indicator_on = registerItem("indicator_on", IRBaseItem::new);
    public static final RegistryObject<Item> indicator_off = registerItem("indicator_off", IRBaseItem::new);
    public static final RegistryObject<Item> switch_on = registerItem("switch_on", IRBaseItem::new);
    public static final RegistryObject<Item> switch_off = registerItem("switch_off", IRBaseItem::new);
    public static final RegistryObject<Item> push_button = registerItem("push_button", IRBaseItem::new);
    public static final RegistryObject<Item> label_5 = registerItem("label_5", IRBaseItem::new);


    public static final RegistryObject<Item> INGOT_STEEL = registerItem("ingot_steel", ()->
            new IRBaseItem(new Item.Properties().tab(IndustrialRenewal.IR_TAB)));
    public static final RegistryObject<Item> STICK_STEEL = registerItem("stick_steel", ()->
            new IRBaseItem(new Item.Properties().tab(IndustrialRenewal.IR_TAB)));
    public static final RegistryObject<Item> STICK_IRON = registerItem("stick_iron", ()->
            new IRBaseItem(new Item.Properties().tab(IndustrialRenewal.IR_TAB)));
    public static final RegistryObject<Item> STICK_COPPER = registerItem("stick_copper", ()->
            new IRBaseItem(new Item.Properties().tab(IndustrialRenewal.IR_TAB)));
    public static final RegistryObject<Item> STICK_GOLD = registerItem("stick_gold", ()->
            new IRBaseItem(new Item.Properties().tab(IndustrialRenewal.IR_TAB)));


//    public static final RegistryObject<Item> SCREW_DRIVE = registerItem("screwdrive", ItemPowerScrewDrive::new);
    public static final RegistryObject<Item> SCREW_DRIVE = registerItem("screwdrive", ()->
            new ItemPowerScrewDrive(new Item.Properties().stacksTo(1).tab(IndustrialRenewal.IR_TAB)));

    public static final RegistryObject<Item> WIND_BLADE = registerItem("small_wind_blade", ()->
            new ItemWindBlade(new Item.Properties().durability(48 * 60).tab(IndustrialRenewal.IR_TAB)));

    public static final RegistryObject<Item>FIREBOX_SOLID = registerItem("firebox_solid", ()->
            new ItemFireBox(new Item.Properties().tab(IndustrialRenewal.IR_TAB), 1));

    public static final RegistryObject<Item>FIREBOX_FLUID = registerItem("firebox_fluid", ()->
            new ItemFireBox(new Item.Properties().tab(IndustrialRenewal.IR_TAB), 2));

    public static final RegistryObject<Item>DRILL_STEEL = registerItem("drill_steel", ()->
            new ItemDrill(new Item.Properties().tab(IndustrialRenewal.IR_TAB).durability(600)));

    public static final RegistryObject<Item>DRILL_DIAMOND = registerItem("drill_diamond", ()->
            new ItemDrill(new Item.Properties().tab(IndustrialRenewal.IR_TAB).durability(1200)));

    public static final RegistryObject<Item>DRILL_DEEP = registerItem("drill_deep", ()->
            new ItemDrill(new Item.Properties().tab(IndustrialRenewal.IR_TAB).durability(2000)));

    public static final RegistryObject<Item>BATTERY = registerItem("battery", ()->
            new ItemBattery(new Item.Properties().tab(IndustrialRenewal.IR_TAB).stacksTo(1), 10000, 1000));

    public static final RegistryObject<Item>BATTERY_LITHIUM = registerItem("battery_lithium", ()->
            new ItemBattery(new Item.Properties().tab(IndustrialRenewal.IR_TAB).stacksTo(1), 100000, 10000));
















    public static void register(final IForgeRegistry<Item> registry) {
        for (Item item : IndustrialRenewal.registeredIRItems) {
            registry.register(item);
        }
    }

    private static <T extends Item>RegistryObject<T> registerItem(String name, Supplier<T> item) {
        RegistryObject<T> toReturn = ITEMS.register(name, item);
        return toReturn;
    }

    public static void registerInit(IEventBus bus){
        ITEMS.register(bus);
    }

}
