package com.cassiokf.industrialrenewal.data.client;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, IndustrialRenewal.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.BLOCKHAZARD.get());
        simpleBlock(ModBlocks.CAUTIONHAZARD.get());
        simpleBlock(ModBlocks.DEFECTIVEHAZARD.get());
        simpleBlock(ModBlocks.SAFETYHAZARD.get());
        simpleBlock(ModBlocks.RADIATIONHAZARD.get());
        simpleBlock(ModBlocks.FIREHAZARD.get());
        simpleBlock(ModBlocks.AISLEHAZARD.get());
        simpleBlock(ModBlocks.STEELBLOCK.get());
        simpleBlock(ModBlocks.CONCRETE.get());
    }
}
