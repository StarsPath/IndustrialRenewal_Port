package com.cassiokf.IndustrialRenewal.entity.render;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.entity.EntityFlatCart;
import com.cassiokf.IndustrialRenewal.model.carts.ModelFlatCart;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderFlatCart extends RenderBase<EntityFlatCart>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/base_cart.png");

    public RenderFlatCart(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        model = new ModelFlatCart<>();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFlatCart p_110775_1_) {
        return TEXTURES;
    }
}
