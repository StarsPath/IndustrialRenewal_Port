package com.cassiokf.IndustrialRenewal.integration.jei;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.recipes.LatheRecipe;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LatheRecipeCategory implements IRecipeCategory<LatheRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(References.MODID, "lathe");
    public final static ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/gui/container/lathe.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic energy;

    public LatheRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.LATHE.get()));
        this.energy = helper.createDrawable(TEXTURE, 176, 0, 16, 69);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends LatheRecipe> getRecipeClass() {
        return LatheRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.LATHE.get().getName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(LatheRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, LatheRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 43, 29);
        recipeLayout.getItemStacks().init(1, false, 133, 29);
        recipeLayout.getItemStacks().set(ingredients);
    }

    @Override
    public void draw(LatheRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        this.energy.draw(matrixStack, 8, 9);
    }
}
