package com.cassiokf.industrialrenewal.entity.render;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityCargoContainer;
import com.cassiokf.industrialrenewal.entity.model.ModelCargoContainer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderCargoContainer extends RenderBase<EntityCargoContainer> {

    public static final ResourceLocation TEXTURES = new ResourceLocation(IndustrialRenewal.MODID + ":textures/entities/cargocontainer.png");

    public RenderCargoContainer(EntityRendererProvider.Context context) {
        super(context, ModelCargoContainer.LAYER_LOCATION);
    }

    //protected final EntityModel<EntityCargoContainer> model2 = new ModelCargoContainer<>();

//    public RenderCargoContainer(EntityRendererManager renderManagerIn) {
//        super(renderManagerIn);
//        model = new ModelCargoContainer<>();
//    }

    @Override
    public ResourceLocation getTextureLocation(EntityCargoContainer p_110775_1_) {
        return TEXTURES;
    }
}
