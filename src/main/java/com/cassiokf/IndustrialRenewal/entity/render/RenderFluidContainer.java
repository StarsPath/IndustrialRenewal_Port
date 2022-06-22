package com.cassiokf.IndustrialRenewal.entity.render;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.entity.EntityFluidContainer;
import com.cassiokf.IndustrialRenewal.model.carts.ModelFluidContainer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderFluidContainer extends RenderBase<EntityFluidContainer>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/fluid_container.png");

    public RenderFluidContainer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        model = new ModelFluidContainer<>();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFluidContainer p_110775_1_) {
        return TEXTURES;
    }
}
