package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.item.IRBaseItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiFunction;

public class IRBaseBlock extends Block{
    protected String name;

    public IRBaseBlock(String name, BiFunction<Block, Item.Properties, Item> createItemBlock){
        super(Block.Properties.of(Material.METAL, MaterialColor.METAL).strength(5.0f, 12.0f));
        this.name = name;
        this.registerDefaultState(getInitDefaultState());
        ResourceLocation registryName = createRegistryName();
        setRegistryName(registryName);
        industrialrenewal.registeredIRBlocks.add(this);

        Item item = createItemBlock.apply(this, new Item.Properties().tab(industrialrenewal.IR_TAB));
        if(item!=null)
        {
            item.setRegistryName(registryName);
            industrialrenewal.registeredIRItems.add(item);
        }
        else{
            LOGGER.info("item is null");
        }
    }

    public IRBaseBlock(String name, Properties props, BiFunction<Block, Item.Properties, Item> createItemBlock) {
        super(props);
        this.name = name;
        this.registerDefaultState(getInitDefaultState());
        ResourceLocation registryName = createRegistryName();
        setRegistryName(registryName);

        Item item = createItemBlock.apply(this, new Item.Properties().tab(industrialrenewal.IR_TAB));
        if(item!=null)
        {
            item.setRegistryName(registryName);
            industrialrenewal.registeredIRItems.add(item);
        }
        else{
            LOGGER.info("item is null");
        }
    }

    protected BlockState getInitDefaultState()
    {
        BlockState state = this.stateDefinition.any();
        if(state.hasProperty(BlockStateProperties.WATERLOGGED))
            state = state.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE);
        return state;
    }

    public ResourceLocation createRegistryName() {
        return new ResourceLocation(References.MODID, name);
    }
}
