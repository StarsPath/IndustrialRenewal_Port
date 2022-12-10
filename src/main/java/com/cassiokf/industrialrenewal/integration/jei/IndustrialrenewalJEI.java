package com.cassiokf.industrialrenewal.integration.jei;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModRecipes;
import com.cassiokf.industrialrenewal.recipes.LatheRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class IndustrialrenewalJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(IndustrialRenewal.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LatheRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<LatheRecipe> recipeList = rm.getAllRecipesFor(LatheRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(LatheRecipeCategory.UID, LatheRecipe.class), recipeList);
    }
}
