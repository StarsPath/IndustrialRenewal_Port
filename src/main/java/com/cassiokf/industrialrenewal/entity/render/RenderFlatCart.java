package com.cassiokf.industrialrenewal.entity.render;


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityFlatCart;
import com.cassiokf.industrialrenewal.entity.model.ModelCartFlat;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderFlatCart extends RenderBase<EntityFlatCart>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(IndustrialRenewal.MODID + ":textures/entities/base_cart.png");

    public RenderFlatCart(EntityRendererProvider.Context context) {
        super(context, ModelCartFlat.LAYER_LOCATION);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFlatCart p_114482_) {
        return TEXTURES;
    }
}
