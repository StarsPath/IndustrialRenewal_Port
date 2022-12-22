package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityFlatCart;
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


    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
