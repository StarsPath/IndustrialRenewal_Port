package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blockentity.*;
import com.cassiokf.industrialrenewal.blockentity.transport.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntity {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, IndustrialRenewal.MODID);

    public static RegistryObject<BlockEntityType<BlockEntitySolarPanel>> SOLAR_PANEL =
            BLOCK_ENTITIES.register("solar_panel_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntitySolarPanel::new, ModBlocks.SOLAR_PANEL.get()
            ).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityBatteryBank>> BATTERY_BANK =
            BLOCK_ENTITIES.register("battery_bank_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityBatteryBank::new, ModBlocks.BATTERY_BANK.get()
            ).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableLV>> ENERGYCABLE_LV_TILE =
            BLOCK_ENTITIES.register("energycable_lv_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityEnergyCableLV::new, ModBlocks.ENERGYCABLE_LV.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableMV>> ENERGYCABLE_MV_TILE =
            BLOCK_ENTITIES.register("energycable_mv_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityEnergyCableMV::new, ModBlocks.ENERGYCABLE_MV.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityEnergyCableHV>> ENERGYCABLE_HV_TILE =
            BLOCK_ENTITIES.register("energycable_hv_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityEnergyCableHV::new, ModBlocks.ENERGYCABLE_HV.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityFluidPipe>> FLUIDPIPE_TILE =
            BLOCK_ENTITIES.register("fluidpipe_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityFluidPipe::new, ModBlocks.FLUID_PIPE.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityHighPressureFluidPipe>> HIGH_PRESSURE_PIPE =
            BLOCK_ENTITIES.register("high_pressure_pipe", ()-> BlockEntityType.Builder.of(
                    BlockEntityHighPressureFluidPipe::new, ModBlocks.HIGH_PRESSURE_PIPE.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityBarrel>> BARREL_TILE =
            BLOCK_ENTITIES.register("barrel_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityBarrel::new, ModBlocks.BARREL.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityEnergySwitch>> ENERGY_SWITCH_TILE =
            BLOCK_ENTITIES.register("energy_switch_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityEnergySwitch::new, ModBlocks.ENERGY_SWITCH.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityFluidValve>> FLUID_VALVE_TILE =
            BLOCK_ENTITIES.register("fluid_valve_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityFluidValve::new, ModBlocks.FLUID_VALVE.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityWindTurbinePillar>> TURBINE_PILLAR_TILE =
            BLOCK_ENTITIES.register("turbine_pillar_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityWindTurbinePillar::new, ModBlocks.TURBINE_PILLAR.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityWindTurbineHead>> WIND_TURBINE_TILE =
            BLOCK_ENTITIES.register("wind_turbine_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityWindTurbineHead::new, ModBlocks.WIND_TURBINE.get()).build(null));

    public static RegistryObject<BlockEntityType<BlockEntityPortableGenerator>> PORTABLE_GENERATOR_TILE =
            BLOCK_ENTITIES.register("portable_generator_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityPortableGenerator::new, ModBlocks.PORTABLE_GENERATOR.get()).build(null));


    public static RegistryObject<BlockEntityType<BlockEntityConveyor>> CONVEYOR_TILE =
            BLOCK_ENTITIES.register("conveyor_tile", ()-> BlockEntityType.Builder.of(
                    BlockEntityConveyor::new, ModBlocks.CONVEYOR_BASIC.get(), ModBlocks.CONVEYOR_FAST.get(), ModBlocks.CONVEYOR_EXPRESS.get()).build(null));

//    public static RegistryObject<TileEntityType<TileEntityConveyorHopper>> CONVEYOR_HOPPER_TILE =
//            TILE_ENTITIES.register("conveyor_hopper_tile", ()-> TileEntityType.Builder.of(
//                    TileEntityConveyorHopper::new, ModBlocks.CONVEYOR_HOPPER.get()).build(null));
//
//    public static RegistryObject<TileEntityType<TileEntityConveyorInserter>> CONVEYOR_INSERTER_TILE =
//            TILE_ENTITIES.register("conveyor_inserter_tile", ()-> TileEntityType.Builder.of(
//                    TileEntityConveyorInserter::new, ModBlocks.CONVEYOR_INSERTER.get()).build(null));

    public static void registerInit(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }

}
