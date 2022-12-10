package com.cassiokf.industrialrenewal.integration.jei;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.recipes.LatheRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class LatheRecipeCategory implements IRecipeCategory<LatheRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(IndustrialRenewal.MODID, "lathe");
    public final static ResourceLocation TEXTURE = new ResourceLocation(IndustrialRenewal.MODID, "textures/gui/container/lathe.png");

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
    public TextComponent getTitle() {
        return new TextComponent(ModBlocks.LATHE.get().getName().getString());
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
    public void setRecipe(IRecipeLayoutBuilder builder, LatheRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 30).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 30).addItemStack(recipe.getResultItem());
        IRecipeCategory.super.setRecipe(builder, recipe, focuses);
    }

//    @Override
//    public void setRecipe(IRecipeLayout recipeLayout, LatheRecipe recipe, IIngredients ingredients) {
//        recipeLayout.getItemStacks().init(0, true, 43, 29);
//        recipeLayout.getItemStacks().init(1, false, 133, 29);
//        recipeLayout.getItemStacks().set(ingredients);
//    }


    @Override
    public void draw(LatheRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
        this.energy.draw(stack, 8, 9);
    }
}
