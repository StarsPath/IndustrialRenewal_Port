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
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

            dropSelf(ModBlocks.BRACE.get());
            dropSelf(ModBlocks.BRACE_STEEL.get());
            dropSelf(ModBlocks.RAZOR_WIRE.get());
            dropSelf(ModBlocks.BIG_FENCE_WIRE.get());
            dropSelf(ModBlocks.BIG_FENCE_COLUMN.get());
            dropSelf(ModBlocks.ELECTRIC_GATE.get());
            dropSelf(ModBlocks.ELECTRIC_FENCE.get());
            dropSelf(ModBlocks.CATWALK_GATE.get());
            dropSelf(ModBlocks.CATWALK.get());
            dropSelf(ModBlocks.CATWALK_STEEL.get());
            dropSelf(ModBlocks.CATWALK_LADDER.get());
            dropSelf(ModBlocks.CATWALK_LADDER_STEEL.get());
            dropSelf(ModBlocks.CATWALK_STAIR.get());
            dropSelf(ModBlocks.CATWALK_STAIR_STEEL.get());
            dropSelf(ModBlocks.CATWALK_HATCH.get());
            dropSelf(ModBlocks.SCAFFOLD.get());
            dropSelf(ModBlocks.PLATFORM.get());
            dropSelf(ModBlocks.FRAME.get());
            dropSelf(ModBlocks.HANDRAIL.get());
            dropSelf(ModBlocks.HANDRAIL_STEEL.get());
            dropSelf(ModBlocks.COLUMN.get());
            dropSelf(ModBlocks.COLUMN_STEEL.get());
            dropSelf(ModBlocks.PILLAR.get());
            dropSelf(ModBlocks.PILLAR_STEEL.get());

            dropSelf(ModBlocks.LIGHT.get());
            dropSelf(ModBlocks.FLUORESCENT.get());
            dropSelf(ModBlocks.DAM_OUTLET.get());
            dropSelf(ModBlocks.DAM_INTAKE.get());
            dropSelf(ModBlocks.HIGH_PRESSURE_PIPE.get());
            dropSelf(ModBlocks.ROTATIONAL_SHAFT.get());
            dropSelf(ModBlocks.CONVEYOR.get());
//            dropSelf(ModBlocks.CONVEYOR_INSERTER.get());
//            dropSelf(ModBlocks.CONVEYOR_HOPPER.get());
            dropSelf(ModBlocks.BOOSTER_RAIL.get());
            dropSelf(ModBlocks.INDUSTRIAL_FLOOR.get());

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

            list.add(ModBlocks.BRACE.get());
            list.add(ModBlocks.BRACE_STEEL.get());
            list.add(ModBlocks.RAZOR_WIRE.get());
            list.add(ModBlocks.BIG_FENCE_WIRE.get());
            list.add(ModBlocks.BIG_FENCE_COLUMN.get());
            list.add(ModBlocks.ELECTRIC_GATE.get());
            list.add(ModBlocks.ELECTRIC_FENCE.get());
            list.add(ModBlocks.CATWALK_GATE.get());
            list.add(ModBlocks.CATWALK.get());
            list.add(ModBlocks.CATWALK_STEEL.get());
            list.add(ModBlocks.CATWALK_LADDER.get());
            list.add(ModBlocks.CATWALK_LADDER_STEEL.get());
            list.add(ModBlocks.CATWALK_STAIR.get());
            list.add(ModBlocks.CATWALK_STAIR_STEEL.get());
            list.add(ModBlocks.CATWALK_HATCH.get());
            list.add(ModBlocks.SCAFFOLD.get());
            list.add(ModBlocks.PLATFORM.get());
            list.add(ModBlocks.FRAME.get());
            list.add(ModBlocks.HANDRAIL.get());
            list.add(ModBlocks.HANDRAIL_STEEL.get());
            list.add(ModBlocks.COLUMN.get());
            list.add(ModBlocks.COLUMN_STEEL.get());
            list.add(ModBlocks.PILLAR.get());
            list.add(ModBlocks.PILLAR_STEEL.get());

            list.add(ModBlocks.LIGHT.get());
            list.add(ModBlocks.FLUORESCENT.get());
            list.add(ModBlocks.DAM_OUTLET.get());
            list.add(ModBlocks.DAM_INTAKE.get());
            list.add(ModBlocks.HIGH_PRESSURE_PIPE.get());
            list.add(ModBlocks.ROTATIONAL_SHAFT.get());
            list.add(ModBlocks.CONVEYOR.get());
//            list.add(ModBlocks.CONVEYOR_INSERTER.get());
//            list.add(ModBlocks.CONVEYOR_HOPPER.get());
            list.add(ModBlocks.BOOSTER_RAIL.get());
            list.add(ModBlocks.INDUSTRIAL_FLOOR.get());

            return list;
        }
    }
}
