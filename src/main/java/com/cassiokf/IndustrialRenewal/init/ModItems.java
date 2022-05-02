package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.item.*;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);


    // TODO: add more Items

    public static final IRBaseItem ingotSteel;
    public static final IRBaseItem stickIron;
    public static final IRBaseItem stickSteel;
    public static final IRBaseItem spongeIron;
    public static final IRBaseItem smallSlab;
    public static final IRBaseItem sMotor;
    public static final IRItemCartridge cartridge_plus;
    public static final IRItemCartridge cartridge_minus;
    public static final IRItemCartridge cartridge_half;
    public static final IRItemCartridge cartridge_double;
    public static final IRItemCartridge cartridge_inverter;
    //    public static final ItemIronPlow locomotivePlowIron;
//    public static final ItemCartLinkable cartLinkable;
//    public static final ItemPowerScrewDrive screwDrive;
//    public static final ItemSteelSaw steelSaw;
//    public static final ItemMineCartCargoContainer cargoContainer;
//    public static final ItemHopperCart hopperCart;
//    public static final ItemMineCartFluidContainer fluidContainer;
//    public static final ItemSteamLocomotive steamLocomotive;
//    public static final ItemLogCart logCart;
//    public static final ItemMineCartPassengerCar passengerCar;
//    public static final ItemMineCartFlat mineCartFlat;
//    public static final ItemCartTender tender;
//    public static final ItemMedKit medkit;
//    public static final ItemFireExtinguisher fireExtinguisher;
//    public static final ItemSafetyHelmet safetyHelmet;
//    public static final ItemSafetyBelt safetyBelt;
//    public static final ItemDiscBase disc1;
//    public static final ItemBookManual manual;
    public static final IRBaseItem pointer;
    public static final IRBaseItem limiter;
    public static final IRBaseItem pointerLong;
    public static final IRBaseItem fire;
    public static final IRBaseItem barLevel;
    public static final IRBaseItem fluidLoaderArm;
    public static final IRBaseItem tambor;
    public static final IRBaseItem cutter;
    public static final IRBaseItem indicator_on;
    public static final IRBaseItem indicator_off;
    public static final IRBaseItem switch_on;
    public static final IRBaseItem switch_off;
    public static final IRBaseItem push_button;
    public static final IRBaseItem label_5;
    public static final IRBaseItem discR;
    public static final IRItemBattery battery;
    public static final IRItemBattery battery_lithium;
    //    public static final ItemCoilHV coilHV;
//    public static final ItemBarrel barrel;
//    public static final ItemFireBox fireBoxSolid;
//    public static final ItemFireBox fireBoxFluid;
    public static final ItemWindBlade windBlade;
    public static final IRItemDrill drillSteel;
    public static final IRItemDrill drillDiamond;
    public static final IRItemDrill drillDeep;
//    public static final ItemProspectingPan prospectingPan;
//    public static final ItemOre hematiteChunk;
//    public static final ItemRegenerationWand regenerationWand;




    //public static final IRItemBarrel barrel;

    static {
        ingotSteel = new IRBaseItem("ingot_steel", industrialrenewal.IR_TAB);
        stickIron = new IRBaseItem("stick_iron", industrialrenewal.IR_TAB);
        stickSteel = new IRBaseItem("stick_steel", industrialrenewal.IR_TAB);
        spongeIron = new IRBaseItem("sponge_iron", industrialrenewal.IR_TAB);
        smallSlab = new IRBaseItem("small_slab", industrialrenewal.IR_TAB);
        sMotor = new IRBaseItem("motor", industrialrenewal.IR_TAB);

        cartridge_plus = new IRItemCartridge("cartridge_plus", industrialrenewal.IR_TAB);
        cartridge_minus = new IRItemCartridge("cartridge_minus", industrialrenewal.IR_TAB);
        cartridge_half = new IRItemCartridge("cartridge_half", industrialrenewal.IR_TAB);
        cartridge_double = new IRItemCartridge("cartridge_double", industrialrenewal.IR_TAB);
        cartridge_inverter = new IRItemCartridge("cartridge_inverter", industrialrenewal.IR_TAB);

        pointer = new IRBaseItem("pointer");
        limiter = new IRBaseItem("limiter");
        pointerLong = new IRBaseItem("pointer_long");
        fire = new IRBaseItem("fire");
        barLevel = new IRBaseItem("bar_level");
        fluidLoaderArm = new IRBaseItem("fluid_loader_arm");
        tambor = new IRBaseItem("rotary_drum");
        cutter = new IRBaseItem("lathecutter");
        indicator_on = new IRBaseItem("indicator_on");
        indicator_off = new IRBaseItem("indicator_off");
        switch_on = new IRBaseItem("switch_on");
        switch_off = new IRBaseItem("switch_off");
        push_button = new IRBaseItem("push_button");
        label_5 = new IRBaseItem("label_5");
        discR = new IRBaseItem("disc_r");

        battery = new IRItemBattery("battery", industrialrenewal.IR_TAB);
        battery_lithium = new IRItemBattery("battery_lithium", industrialrenewal.IR_TAB);

        windBlade = new ItemWindBlade("small_wind_blade");
        drillSteel = new IRItemDrill("drill_steel", industrialrenewal.IR_TAB, 600);
        drillDiamond = new IRItemDrill("drill_diamond", industrialrenewal.IR_TAB, 1200);
        drillDeep = new IRItemDrill("drill_deep", industrialrenewal.IR_TAB, 2000);

        //barrel =  new IRItemBarrel("barrel_item", industrialrenewal.IR_TAB);

    }

    public static void register(final IForgeRegistry<Item> registry) {
        for (Item item : industrialrenewal.registeredIRItems) {
            registry.register(item);
        }
    }

    public static void registerInit(IEventBus bus){
        ITEMS.register(bus);
    }

}