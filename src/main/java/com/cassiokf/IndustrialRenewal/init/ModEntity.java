package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.entity.EntityCargoContainer;
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

    public static final RegistryObject<EntityType<EntityCargoContainer>> CARGO_CONTAINER =
            ENTITY_TYPES.register("cargo_container",
                    ()->EntityType.Builder.<EntityCargoContainer>of(EntityCargoContainer::new,
                            EntityClassification.MISC)
                    .build(new ResourceLocation(References.MODID, "cargo_container").toString()));

    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
