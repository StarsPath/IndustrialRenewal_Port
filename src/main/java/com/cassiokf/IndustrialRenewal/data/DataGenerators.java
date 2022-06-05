package com.cassiokf.IndustrialRenewal.data;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.data.client.ModBlockStateProvider;
import com.cassiokf.IndustrialRenewal.data.client.ModItemModelProvider;
import com.cassiokf.IndustrialRenewal.data.client.ModLootTableProvider;
import com.cassiokf.IndustrialRenewal.data.client.ModRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators(){

    }
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
        gen.addProvider(new ModLootTableProvider(gen));
        gen.addProvider(new ModRecipeProvider(gen));
    }
}
