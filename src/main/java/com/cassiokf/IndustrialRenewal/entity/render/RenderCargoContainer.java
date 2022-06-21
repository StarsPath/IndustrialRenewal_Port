package com.cassiokf.IndustrialRenewal.entity.render;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.entity.EntityCargoContainer;
import com.cassiokf.IndustrialRenewal.model.carts.ModelCargoContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.MinecartModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderCargoContainer extends RenderBase<EntityCargoContainer> {

    public static final ResourceLocation TEXTURES = new ResourceLocation(References.MODID + ":textures/entities/cargocontainer.png");

    //protected final EntityModel<EntityCargoContainer> model2 = new ModelCargoContainer<>();

    public RenderCargoContainer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        model = new ModelCargoContainer<>();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCargoContainer p_110775_1_) {
        return TEXTURES;
    }
}
