package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blocks.*;
import com.cassiokf.industrialrenewal.blocks.abstracts.IRBaseBlock;
import com.cassiokf.industrialrenewal.blocks.transport.*;
import com.cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, IndustrialRenewal.MODID);

    public static final RegistryObject<Block> BLOCKHAZARD = registerBlock("block_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> CAUTIONHAZARD = registerBlock("caution_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> DEFECTIVEHAZARD = registerBlock("defective_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> SAFETYHAZARD = registerBlock("safety_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> RADIATIONHAZARD = registerBlock("radiation_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> AISLEHAZARD = registerBlock("aisle_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> FIREHAZARD = registerBlock("fire_hazard", IRBaseBlock::new);
    public static final RegistryObject<Block> STEELBLOCK = registerBlock("block_steel", IRBaseBlock::new);
    public static final RegistryObject<Block> CONCRETE = registerBlock("concrete", IRBaseBlock::new);

    public static final RegistryObject<Block> SOLAR_PANEL = registerBlock("solar_panel", BlockSolarPanel::new);

    public static final RegistryObject<Block> BATTERY_BANK = registerBlock("battery_bank", BlockBatteryBank::new);


    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_LV = registerBlock("energy_cable_lv",
            () -> new BlockEnergyCable(EnumEnergyCableType.LV));

    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_MV = registerBlock("energy_cable_mv",
            () -> new BlockEnergyCable(EnumEnergyCableType.MV));

    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_HV = registerBlock("energy_cable_hv",
            () -> new BlockEnergyCable(EnumEnergyCableType.HV));


    public static final RegistryObject<BlockFluidPipe> FLUID_PIPE = registerBlock("fluid_pipe", BlockFluidPipe::new);

    public static final RegistryObject<BlockHighPressureFluidPipe> HIGH_PRESSURE_PIPE = registerBlock("high_pressure_pipe", BlockHighPressureFluidPipe::new);

    public static final RegistryObject<BlockBarrel> BARREL = registerBlock("barrel", BlockBarrel::new);

    public static final RegistryObject<BlockEnergySwitch> ENERGY_SWITCH = registerBlock("energy_switch",
            ()-> new BlockEnergySwitch(BlockBehaviour.Properties.of(Material.METAL).strength(0.8f)
                    .sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<BlockFluidValve> FLUID_VALVE = registerBlock("valve_pipe_large",
            ()-> new BlockFluidValve(BlockBehaviour.Properties.of(Material.METAL).strength(0.8f)
                    .sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockWindTurbinePillar> TURBINE_PILLAR = registerBlock("small_wind_turbine_pillar",
            () -> new BlockWindTurbinePillar(BlockBehaviour.Properties.of(Material.METAL).strength(0.8f)
                    .sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockWindTurbineHead> WIND_TURBINE = registerBlock("small_wind_turbine",
            () -> new BlockWindTurbineHead(BlockBehaviour.Properties.of(Material.METAL).strength(0.8f)
                    .sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockPortableGenerator> PORTABLE_GENERATOR = registerBlock("portable_generator",
            () -> new BlockPortableGenerator(BlockBehaviour.Properties.of(Material.METAL).strength(2f)
                    .sound(SoundType.METAL).noOcclusion()));






    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, true);
    }

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean createItem) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        if(createItem)
            registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(IndustrialRenewal.IR_TAB)));
    }

    public static void register(IForgeRegistry<Block> registry){
        for(Block block : IndustrialRenewal.registeredIRBlocks)
            registry.register(block);
    }

    public  static void registerInit(IEventBus bus){
        BLOCKS.register(bus);
    }

}
