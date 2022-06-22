package com.cassiokf.IndustrialRenewal.entity.render;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.entity.EntityPassengerCar;
import com.cassiokf.IndustrialRenewal.model.carts.ModelPassengerCar;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderPassengerCar<T extends EntityPassengerCar> extends RenderRotatableBase<EntityPassengerCar>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/passenger_car.png");

    public RenderPassengerCar(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        model = new ModelPassengerCar<>();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPassengerCar p_110775_1_) {
        return TEXTURES;
    }
}
