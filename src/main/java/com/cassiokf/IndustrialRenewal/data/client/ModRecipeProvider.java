package com.cassiokf.IndustrialRenewal.data.client;

import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModBlocks.BLOCKHAZARD, 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.YELLOW_DYE)
                .define('B', Items.BLACK_DYE)
                .pattern("###")
                .pattern("Y B")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.AISLEHAZARD, 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.BLACK_DYE)
                .define('B', Items.WHITE_DYE)
                .pattern("###")
                .pattern("Y B")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.CAUTIONHAZARD, 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.ORANGE_DYE)
                .define('B', Items.WHITE_DYE)
                .pattern("###")
                .pattern("Y B")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.DEFECTIVEHAZARD, 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.BLUE_DYE)
                .define('B', Items.WHITE_DYE)
                .pattern("###")
                .pattern("Y B")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.FIREHAZARD, 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.RED_DYE)
                .define('B', Items.WHITE_DYE)
                .pattern("###")
                .pattern("Y B")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.RADIATIONHAZARD, 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.MAGENTA_DYE)
                .define('B', Items.YELLOW_DYE)
                .pattern("###")
                .pattern("Y B")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.SAFETYHAZARD, 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.LIME_DYE)
                .define('B', Items.WHITE_DYE)
                .pattern("###")
                .pattern("Y B")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.sMotor, 2)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Tags.Items.DUSTS_REDSTONE)
                .define('B', Items.GOLD_INGOT)
                .define('X', Items.PISTON)
                .pattern("###")
                .pattern("YXB")
                .pattern("###")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.battery, 1)
                .define('#', Tags.Items.DUSTS_REDSTONE)
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', Items.GOLD_NUGGET)
                .define('C', Items.LAPIS_LAZULI)
                .define('D', Tags.Items.INGOTS_IRON)
                .define('E', Items.GOLD_INGOT)
                .pattern("A#B")
                .pattern("A#B")
                .pattern("DCE")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.battery_lithium, 1)
                .define('#', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('A', Tags.Items.NUGGETS_IRON)
                .define('B', Items.GOLD_NUGGET)
                .define('C', Items.LAPIS_BLOCK)
                .define('D', Tags.Items.INGOTS_IRON)
                .define('E', Items.GOLD_INGOT)
                .pattern("A#B")
                .pattern("D#E")
                .pattern("DCE")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.screwDrive, 1)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', ModItems.sMotor)
                .define('A', ModItems.battery)
                .define('B', Items.STONE_BUTTON)
                .pattern("##Y")
                .pattern(" BA")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.SPANEL.get(), 1)
                .define('#', Items.GLASS_PANE)
                .define('A', Items.LAPIS_LAZULI)
                .define('B', Tags.Items.INGOTS_IRON)
                .pattern("###")
                .pattern("AAA")
                .pattern("B B")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ENERGYCABLE_LV.get(), 6)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('A', Tags.Items.DUSTS_REDSTONE)
                .define('X', Items.GLASS_PANE)
                .pattern("#X#")
                .pattern("AAA")
                .pattern("#X#")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ENERGYCABLE_MV.get(), 6)
                .define('#', Items.GOLD_INGOT)
                .define('A', Tags.Items.DUSTS_REDSTONE)
                .define('X', Items.GLASS_PANE)
                .pattern("#X#")
                .pattern("AAA")
                .pattern("#X#")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

//        ShapedRecipeBuilder.shaped(ModBlocks.ENERGYCABLE_MV.get(), 6)
//                .define('#', ModBlocks.ENERGYCABLE_LV.get())
//                .define('A', Items.GOLD_INGOT)
//                .pattern("###")
//                .pattern("AAA")
//                .pattern("###")
//                .unlockedBy("has_item", has(Items.IRON_INGOT))
//                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ENERGYCABLE_HV.get(), 6)
                .define('#', Items.GLASS_PANE)
                .define('A', Tags.Items.DUSTS_REDSTONE)
                .define('X', Items.DIAMOND)
                .pattern("#X#")
                .pattern("AAA")
                .pattern("#X#")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

//        ShapedRecipeBuilder.shaped(ModBlocks.ENERGYCABLE_HV.get(), 6)
//                .define('#', ModBlocks.ENERGYCABLE_MV.get())
//                .define('A', Items.REDSTONE)
//                .define('X', Items.DIAMOND)
//                .pattern("###")
//                .pattern("AXA")
//                .pattern("###")
//                .unlockedBy("has_item", has(Items.IRON_INGOT))
//                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.BARREL.get(), 1)
                .define('#', Items.IRON_TRAPDOOR)
                .define('A', Tags.Items.STORAGE_BLOCKS_IRON)
                .pattern("#")
                .pattern("A")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.PORTABLE_GENERATOR.get(), 1)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('Y', Items.FURNACE)
                .define('A', ModItems.sMotor)
                .define('B', ModBlocks.BARREL.get())
                .pattern("#B#")
                .pattern("A#Y")
                .pattern("# #")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.FLUID_PIPE.get(), 16)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('A', Tags.Items.NUGGETS_IRON)
                .pattern("#A#")
                .pattern("A A")
                .pattern("#A#")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.FLUID_TANK.get(), 1)
                .define('#', ModItems.stickSteel)
                .define('A', ModBlocks.BARREL.get())
                .define('B', ModBlocks.FLUID_PIPE.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("#B#")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.fireBoxSolid, 1)
                .define('A', Tags.Items.DUSTS_REDSTONE)
                .define('B', Items.HOPPER)
                .define('C', Items.DROPPER)
                .define('D', Items.FURNACE)
                .pattern(" A ")
                .pattern("BCD")
                .pattern(" A ")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.fireBoxFluid, 1)
                .define('A', Tags.Items.DUSTS_REDSTONE)
                .define('B', ModBlocks.FLUID_PIPE.get())
                .define('C', ModBlocks.BARREL.get())
                .define('D', Items.FURNACE)
                .pattern(" A ")
                .pattern("CBD")
                .pattern(" A ")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.INDUSTRIAL_BATTERY_BANK.get(), 1)
                .define('A', ModBlocks.ENERGYCABLE_HV.get())
                .define('B', ModItems.stickSteel)
                .define('C', ItemTags.bind("forge:storage_blocks/steel"))
                .pattern("ABA")
                .pattern("B B")
                .pattern("BCB")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.STEELBLOCK, 1)
                .define('A', ItemTags.bind("forge:ingots/steel"))
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy("has_item", has(ModItems.ingotSteel))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.ingotSteel, 9)
                .requires(ItemTags.bind("forge:storage_blocks/steel"))
                .unlockedBy("has_item", has(ModItems.ingotSteel))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.STEAM_BOILER.get(), 1)
                .define('A', ModBlocks.FLUID_PIPE.get())
                .define('B', ModBlocks.BARREL.get())
                .define('C', Tags.Items.DUSTS_REDSTONE)
                .define('D', ItemTags.bind("forge:ingots/steel"))
                .pattern("ABA")
                .pattern("CBC")
                .pattern("DDD")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.STEAM_TURBINE.get(), 1)
                .define('A', ModBlocks.FLUID_PIPE.get())
                .define('B', ModBlocks.BARREL.get())
                .define('C', Tags.Items.DUSTS_REDSTONE)
                .define('D', ItemTags.bind("forge:ingots/steel"))
                .define('E', ModItems.sMotor)
                .define('F', ModBlocks.BATTERYBANK.get())
                .pattern("CBA")
                .pattern("DED")
                .pattern("DFD")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.MINER.get(), 1)
                .define('A', ItemTags.bind("forge:ingots/steel"))
                .define('B', ModItems.sMotor)
                .define('C', Tags.Items.DUSTS_REDSTONE)
                .define('D', ModBlocks.ELECTRIC_PUMP.get())
                .define('E', Items.HOPPER)
                .define('F', ModBlocks.BARREL.get())
                .pattern("ABA")
                .pattern("CDE")
                .pattern("AFA")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.STORAGE_CHEST.get(), 1)
                .define('#', ModItems.stickIron)
                .define('A', Tags.Items.CHESTS)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("# #")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.TURBINE_PILLAR.get(), 6)
                .define('#', Tags.Items.INGOTS_IRON)
                .define('A', ModBlocks.ENERGYCABLE_LV.get())
                .pattern("#A#")
                .pattern("#A#")
                .pattern("#A#")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.WIND_TURBINE.get(), 1)
                .define('#', ItemTags.bind("forge:ingots/steel"))
                .define('A', ModItems.sMotor)
                .define('B', ModBlocks.ENERGYCABLE_LV.get())
                .define('C', ModItems.stickSteel)
                .pattern(" # ")
                .pattern("#AC")
                .pattern(" B ")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.windBlade, 1)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', ModItems.stickSteel)
                .define('C', ItemTags.bind("forge:ingots/steel"))
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.drillSteel, 1)
                .define('A', ModItems.stickSteel)
                .define('B', ItemTags.bind("forge:ingots/steel"))
                .pattern(" A ")
                .pattern(" A ")
                .pattern("BBB")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.drillDiamond, 1)
                .define('A', ModItems.stickSteel)
                .define('B', ItemTags.bind("forge:ingots/steel"))
                .define('C', Items.DIAMOND)
                .pattern(" A ")
                .pattern(" B ")
                .pattern("CBC")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.drillDeep, 1)
                .define('A', ItemTags.bind("forge:ingots/steel"))
                .define('B', ItemTags.bind("forge:storage_blocks/steel"))
                .define('C', Items.DIAMOND_BLOCK)
                .pattern(" B ")
                .pattern("ABA")
                .pattern("C C")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.BATTERYBANK.get(), 1)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', ModItems.battery)
                .define('C', ModItems.stickIron)
                .define('D', Tags.Items.DUSTS_REDSTONE)
                .pattern("ABA")
                .pattern("BBB")
                .pattern("CDC")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.TRASH.get(), 1)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', Items.HOPPER)
                .define('C', ModBlocks.FLUID_PIPE.get())
                .define('D', Items.ENDER_PEARL)
                .define('E', ModBlocks.ENERGYCABLE_LV.get())
                .pattern("ABA")
                .pattern("CDE")
                .pattern("AAA")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.ELECTRIC_PUMP.get(), 1)
                .define('A', Tags.Items.INGOTS_IRON)
                .define('B', ModBlocks.FLUID_PIPE.get())
                .define('C', ModItems.sMotor)
                .define('D', ModItems.stickIron)
                .pattern(" BA")
                .pattern("CDA")
                .pattern(" BA")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.LOCKER.get(), 1)
                .define('A', ModItems.stickIron)
                .define('B', Tags.Items.CHESTS)
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.CONCRETE, 8)
                .define('A', Tags.Items.GRAVEL)
                .define('B', Tags.Items.SAND)
                .pattern("BAB")
                .pattern("A A")
                .pattern("BAB")
                .unlockedBy("has_item", has(Tags.Items.SAND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.CONCRETEWALL.get(), 8)
                .define('A', ModBlocks.CONCRETE)
                .pattern("AAA")
                .pattern("AAA")
                .unlockedBy("has_item", has(ModBlocks.CONCRETE))
                .save(consumer);
        //super.buildShapelessRecipes(consumer);
    }
}
