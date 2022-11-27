package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.recipes.LatheRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, IndustrialRenewal.MODID);

    public static final RegistryObject<LatheRecipe.Serializer> LATHE_SERIALIZER =
            RECIPE_SERIALIZER.register("lathe", LatheRecipe.Serializer::new);

//    public static RecipeType<LatheRecipe> LATHE_RECIPE =
//            new LatheRecipe.LatheRecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

//        Registry.register(Registry.RECIPE_TYPE, LatheRecipe.TYPE_ID, LATHE_RECIPE);
    }

}
