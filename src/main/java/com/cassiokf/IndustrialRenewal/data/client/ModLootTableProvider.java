package com.cassiokf.IndustrialRenewal.data.client;

import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK)
        );
        //return super.getTables();
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) -> LootTableManager.validate(validationtracker, p_218436_2_, p_218436_3_));
        //super.validate(map, validationtracker);
    }

    public static class ModBlockLootTables extends BlockLootTables{
        @Override
        protected void addTables() {
            dropSelf(ModBlocks.AISLEHAZARD.getBlock());
            dropSelf(ModBlocks.BLOCKHAZARD.getBlock());
            dropSelf(ModBlocks.CAUTIONHAZARD.getBlock());
            dropSelf(ModBlocks.DEFECTIVEHAZARD.getBlock());
            dropSelf(ModBlocks.SAFETYHAZARD.getBlock());
            dropSelf(ModBlocks.FIREHAZARD.getBlock());
            dropSelf(ModBlocks.RADIATIONHAZARD.getBlock());
            dropSelf(ModBlocks.CONCRETE.getBlock());
            dropSelf(ModBlocks.CONCRETEWALL.get());
            dropSelf(ModBlocks.STEELBLOCK.getBlock());
            dropSelf(ModBlocks.LOCKER.get());
            dropSelf(ModBlocks.SPANEL.get());
            dropSelf(ModBlocks.ENERGYCABLE_HV.get());
            dropSelf(ModBlocks.ENERGYCABLE_MV.get());
            dropSelf(ModBlocks.ENERGYCABLE_LV.get());
            dropSelf(ModBlocks.TRASH.get());
            dropSelf(ModBlocks.TURBINE_PILLAR.get());
            dropSelf(ModBlocks.WIND_TURBINE.get());
            dropSelf(ModBlocks.PORTABLE_GENERATOR.get());
            dropSelf(ModBlocks.FLUID_PIPE.get());
            //super.addTables();
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            ArrayList<Block> list = new ArrayList<>();
            list.add(ModBlocks.AISLEHAZARD.getBlock());
            list.add(ModBlocks.BLOCKHAZARD.getBlock());
            list.add(ModBlocks.CAUTIONHAZARD.getBlock());
            list.add(ModBlocks.DEFECTIVEHAZARD.getBlock());
            list.add(ModBlocks.SAFETYHAZARD.getBlock());
            list.add(ModBlocks.FIREHAZARD.getBlock());
            list.add(ModBlocks.RADIATIONHAZARD.getBlock());
            list.add(ModBlocks.CONCRETE.getBlock());
            list.add(ModBlocks.CONCRETEWALL.get());
            list.add(ModBlocks.STEELBLOCK.getBlock());
            list.add(ModBlocks.LOCKER.get());
            list.add(ModBlocks.SPANEL.get());
            list.add(ModBlocks.ENERGYCABLE_HV.get());
            list.add(ModBlocks.ENERGYCABLE_MV.get());
            list.add(ModBlocks.ENERGYCABLE_LV.get());
            list.add(ModBlocks.TRASH.get());
            list.add(ModBlocks.TURBINE_PILLAR.get());
            list.add(ModBlocks.WIND_TURBINE.get());
            list.add(ModBlocks.PORTABLE_GENERATOR.get());
            list.add(ModBlocks.FLUID_PIPE.get());

            return list;
//            return ModBlocks.BLOCKS.getEntries().stream()
//                    .map(RegistryObject::get)
//                    .collect(Collectors.toList());
            //return super.getKnownBlocks();
        }
    }
}
