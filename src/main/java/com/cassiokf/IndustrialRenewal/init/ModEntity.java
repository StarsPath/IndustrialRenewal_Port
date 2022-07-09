package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.entity.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntity {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, References.MODID);

//    public static final RegistryObject<EntityType<CouplerEntity>> COUPLER_ENTITY =
//            ENTITY_TYPES.register("coupler_entity",
//                    ()->EntityType.Builder.<CouplerEntity>of(CouplerEntity::new,
//                            EntityClassification.MISC).sized(0.2f, 0.2f)
//                            .build(new ResourceLocation(References.MODID, "coupler_entity").toString()));

    public static final RegistryObject<EntityType<EntityCargoContainer>> CARGO_CONTAINER =
            ENTITY_TYPES.register("cargo_container",
                    ()->EntityType.Builder.<EntityCargoContainer>of(EntityCargoContainer::new,
                            EntityClassification.MISC).sized(1f, 1f)
                    .build(new ResourceLocation(References.MODID, "cargo_container").toString()));

    public static final RegistryObject<EntityType<EntityFlatCart>> FLAT_CART =
            ENTITY_TYPES.register("flat_cart",
                    ()->EntityType.Builder.<EntityFlatCart>of(EntityFlatCart::new,
                            EntityClassification.MISC).sized(1f, 0.4f)
                            .build(new ResourceLocation(References.MODID, "flat_cart").toString()));

    public static final RegistryObject<EntityType<EntityPassengerCar>> PASSENGER_CAR =
            ENTITY_TYPES.register("passenger_car",
                    ()->EntityType.Builder.<EntityPassengerCar>of(EntityPassengerCar::new,
                            EntityClassification.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(References.MODID, "passenger_car").toString()));

    public static final RegistryObject<EntityType<EntityFluidContainer>> FLUID_CONTAINER =
            ENTITY_TYPES.register("fluid_container",
                    ()->EntityType.Builder.<EntityFluidContainer>of(EntityFluidContainer::new,
                            EntityClassification.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(References.MODID, "fluid_container").toString()));

    public static final RegistryObject<EntityType<EntityPassengerCartMk2>> PASSENGER_CART_MK2 =
            ENTITY_TYPES.register("passenger_cart_mk2",
                    ()->EntityType.Builder.<EntityPassengerCartMk2>of(EntityPassengerCartMk2::new,
                            EntityClassification.MISC).sized(1f, 1f)
                            .build(new ResourceLocation(References.MODID, "passenger_cart_mk2").toString()));



    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
