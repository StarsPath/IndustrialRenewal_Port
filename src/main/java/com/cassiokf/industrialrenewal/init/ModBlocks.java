package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blocks.abstracts.IRBaseBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
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

    public  static void registerInit(IEventBus bus){
        BLOCKS.register(bus);
    }

}
