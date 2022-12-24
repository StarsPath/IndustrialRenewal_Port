package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntity {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, IndustrialRenewal.MODID);

    public static final RegistryObject<EntityType<EntityFlatCart>> FLAT_CART =
            ENTITY_TYPES.register("flat_cart",
                    ()->EntityType.Builder.<EntityFlatCart>of(EntityFlatCart::new,
                                    MobCategory.MISC).sized(1f, 0.4f)
                            .build(new ResourceLocation(IndustrialRenewal.MODID, "flat_cart").toString()));

    public static final RegistryObject<EntityType<EntityCargoContainer>> CARGO_CONTAINER =
            ENTITY_TYPES.register("cargo_container",
                    ()->EntityType.Builder.<EntityCargoContainer>of(EntityCargoContainer::new,
                                    MobCategory.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(IndustrialRenewal.MODID, "cargo_container").toString()));

    public static final RegistryObject<EntityType<EntityFluidContainer>> FLUID_CONTAINER =
            ENTITY_TYPES.register("fluid_container",
                    ()->EntityType.Builder.<EntityFluidContainer>of(EntityFluidContainer::new,
                                    MobCategory.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(IndustrialRenewal.MODID, "fluid_container").toString()));

    public static final RegistryObject<EntityType<EntityPassengerCart>> PASSENGER_CART =
            ENTITY_TYPES.register("passenger_cart",
                    ()->EntityType.Builder.<EntityPassengerCart>of(EntityPassengerCart::new,
                                    MobCategory.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(IndustrialRenewal.MODID, "passenger_cart").toString()));

    public static final RegistryObject<EntityType<EntityPassengerCartMk2>> PASSENGER_CART_MK2 =
            ENTITY_TYPES.register("passenger_cart_mk2",
                    ()->EntityType.Builder.<EntityPassengerCartMk2>of(EntityPassengerCartMk2::new,
                                    MobCategory.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(IndustrialRenewal.MODID, "passenger_cart_mk2").toString()));

    public static final RegistryObject<EntityType<EntitySteamLocomotive>> STEAM_LOCOMOTIVE =
            ENTITY_TYPES.register("steam_locomotive",
                    ()->EntityType.Builder.<EntitySteamLocomotive>of(EntitySteamLocomotive::new,
                                    MobCategory.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(IndustrialRenewal.MODID, "steam_locomotive").toString()));







    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
