package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.recipes.LatheRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, References.MODID);

    public static final RegistryObject<LatheRecipe.Serializer> LATHE_SERIALIZER =
            RECIPE_SERIALIZER.register("lathe", LatheRecipe.Serializer::new);

    public static IRecipeType<LatheRecipe> LATHE_RECIPE =
            new LatheRecipe.LatheRecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, LatheRecipe.TYPE_ID, LATHE_RECIPE);
    }
}
