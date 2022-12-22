package com.cassiokf.industrialrenewal.entity.render;


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityPassengerCart;
import com.cassiokf.industrialrenewal.entity.model.ModelCartFlat;
import com.cassiokf.industrialrenewal.entity.model.ModelPassengerCart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderPassengerCart<T extends EntityPassengerCart> extends RenderRotatableBase<EntityPassengerCart>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(IndustrialRenewal.MODID + ":textures/entities/passenger_cart.png");

    public RenderPassengerCart(EntityRendererProvider.Context context) {
        super(context, ModelPassengerCart.LAYER_LOCATION);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPassengerCart p_110775_1_) {
        return TEXTURES;
    }
}
