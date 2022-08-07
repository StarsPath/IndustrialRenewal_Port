package com.cassiokf.IndustrialRenewal.integration.jei;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.init.ModRecipes;
import com.cassiokf.IndustrialRenewal.recipes.LatheRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class IndustrialrenewalJEI implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(References.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LatheRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        registration.addRecipes(rm.getAllRecipesFor(ModRecipes.LATHE_RECIPE).stream().filter(r -> r instanceof LatheRecipe).collect(Collectors.toList()),
                LatheRecipeCategory.UID);
    }
}
