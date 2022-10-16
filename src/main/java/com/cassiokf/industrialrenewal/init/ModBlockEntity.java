package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityBatteryBank;
import com.cassiokf.industrialrenewal.blockentity.BlockEntitySolarPanel;
import com.cassiokf.industrialrenewal.blockentity.tubes.BlockEntityEnergyCableHV;
import com.cassiokf.industrialrenewal.blockentity.tubes.BlockEntityEnergyCableLV;
import com.cassiokf.industrialrenewal.blockentity.tubes.BlockEntityEnergyCableMV;
import com.cassiokf.industrialrenewal.blocks.BlockSolarPanel;
import net.minecraft.world.level.block.entity.BlockEntity;
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


    public static void registerInit(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }

}
