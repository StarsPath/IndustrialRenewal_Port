package com.cassiokf.industrialrenewal.entity.render;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityFluidContainer;
import com.cassiokf.industrialrenewal.entity.model.ModelCartFluidTank;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderFluidContainer extends RenderBase<EntityFluidContainer>{

    public static final ResourceLocation TEXTURES = new ResourceLocation(IndustrialRenewal.MODID + ":textures/entities/fluid_container.png");

    public RenderFluidContainer(EntityRendererProvider.Context context) {
        super(context, ModelCartFluidTank.LAYER_LOCATION);
    }

//    public RenderFluidContainer(EntityRendererManager renderManagerIn) {
//        super(renderManagerIn);
//        model = new ModelFluidContainer<>();
//    }

    @Override
    public ResourceLocation getTextureLocation(EntityFluidContainer p_110775_1_) {
        return TEXTURES;
    }
}
