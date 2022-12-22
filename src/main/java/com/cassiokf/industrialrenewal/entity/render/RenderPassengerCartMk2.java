package com.cassiokf.industrialrenewal.entity.render;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityPassengerCartMk2;
import com.cassiokf.industrialrenewal.entity.model.ModelPassengerCartMk2;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderPassengerCartMk2 extends RenderBase<EntityPassengerCartMk2>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(IndustrialRenewal.MODID + ":textures/entities/passenger_cart_mk2.png");

    public RenderPassengerCartMk2(EntityRendererProvider.Context context) {
        super(context, ModelPassengerCartMk2.LAYER_LOCATION);
    }

//    public RenderPassengerCartMk2(EntityRendererManager renderManagerIn) {
//        super(renderManagerIn);
//        model = new ModelPassengerCartMk2<>();
//    }

    @Override
    public ResourceLocation getTextureLocation(EntityPassengerCartMk2 p_110775_1_) {
        return TEXTURES;
    }
}
