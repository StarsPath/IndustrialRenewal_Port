package com.cassiokf.IndustrialRenewal.recipes;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModRecipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class LatheRecipe implements IRecipe<IInventory> {

    public static final ResourceLocation TYPE_ID = new ResourceLocation(References.MODID, "lathe");

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int processTime;

    public LatheRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int processTime) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.processTime = processTime;
    }


//    public static void populateLatheRecipes()
//    {
//        int recipesAmount = 0;
//        for (IRecipeSerializer recipe : ForgeRegistries.RECIPE_SERIALIZERS)
//        {
//            if (recipe instanceof LatheRecipe)
//            {
//                //List<Item> list = LatheRecipe.inputToItemList(((LatheRecipe) recipe).input);
//                LATHE_RECIPES.add((LatheRecipe) recipe);
//                recipesAmount++;
//                for (ItemStack item : ((LatheRecipe) recipe).input)
//                {
//                    CACHED_RECIPES.put(item.getItem(), (LatheRecipe) recipe);
//                }
//            }
//        }
//
//        //IndustrialRenewal.LOGGER.info(TextFormatting.GREEN + References.NAME + " Registered " + recipesAmount + " Recipes for Lathe Machine via json");
//    }

    public int getProcessTime()
    {
        return processTime;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public boolean matches(IInventory inv, World p_77569_2_) {
        return recipeItems.get(0).test(inv.getItem(0));
        //return this.input.equals(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return this.output;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public ItemStack getIcon(){
        return new ItemStack(ModBlocks.LATHE.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.LATHE_SERIALIZER.get();
        //return
    }

    @Override
    public IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
//        return null;
    }

    public static class LatheRecipeType implements IRecipeType<LatheRecipe>{
        @Override
        public String toString() {
            return LatheRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<LatheRecipe>{

        @Override
        public LatheRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            final int processTime = JSONUtils.getAsInt(json, "process_time", 100);
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));
            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            inputs.set(0, Ingredient.fromJson(ingredients.get(0)));

            return new LatheRecipe(resourceLocation, output, inputs, processTime);
        }

        @Nullable
        @Override
        public LatheRecipe fromNetwork(ResourceLocation resourceLocation, PacketBuffer buffer) {
            final int processTime = buffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            ItemStack output = buffer.readItem();
            inputs.set(0, Ingredient.fromNetwork(buffer));

            return new LatheRecipe(resourceLocation, output, inputs, processTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, LatheRecipe recipe) {
            buffer.writeInt(recipe.processTime);
            buffer.writeItemStack(recipe.getResultItem(), false);
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
        }
    }
}
