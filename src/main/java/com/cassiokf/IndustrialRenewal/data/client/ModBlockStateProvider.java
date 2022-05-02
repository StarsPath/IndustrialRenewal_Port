package com.cassiokf.IndustrialRenewal.data.client;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, References.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.BLOCKHAZARD.getBlock());
        simpleBlock(ModBlocks.CAUTIONHAZARD.getBlock());
        simpleBlock(ModBlocks.DEFECTIVEHAZARD.getBlock());
        simpleBlock(ModBlocks.SAFETYHAZARD.getBlock());
        simpleBlock(ModBlocks.RADIATIONHAZARD.getBlock());
        simpleBlock(ModBlocks.FIREHAZARD.getBlock());
        simpleBlock(ModBlocks.AISLEHAZARD.getBlock());
        simpleBlock(ModBlocks.STEELBLOCK.getBlock());
        simpleBlock(ModBlocks.CONCRETE.getBlock());

        //wallBlock(ModBlocks.CONCRETEWALL, new ResourceLocation("block/concrete"));

        //simpleBlock(ModBlocks.BLOCKINDFLOOR.getBlock());

    }
}
