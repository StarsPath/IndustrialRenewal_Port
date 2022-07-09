package com.cassiokf.IndustrialRenewal.entity.render;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.entity.EntityPassengerCartMk2;
import com.cassiokf.IndustrialRenewal.model.carts.ModelPassengerCartMk2;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderPassengerCartMk2 extends RenderBase<EntityPassengerCartMk2>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/passenger_cart_mk2.png");

    public RenderPassengerCartMk2(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        model = new ModelPassengerCartMk2<>();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPassengerCartMk2 p_110775_1_) {
        return TEXTURES;
    }
}
