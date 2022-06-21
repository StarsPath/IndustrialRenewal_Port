package com.cassiokf.IndustrialRenewal.model.carts;

import com.cassiokf.IndustrialRenewal.entity.EntityCargoContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelCargoContainer <T extends EntityCargoContainer> extends EntityModel<T> {

    public ModelRenderer Axis1;
    public ModelRenderer Axis2;
    public ModelRenderer Wheel1;
    public ModelRenderer Wheel2;
    public ModelRenderer Wheel3;
    public ModelRenderer Wheel4;
    public ModelRenderer TruckBase1;
    public ModelRenderer Axis3;
    public ModelRenderer Axis4;
    public ModelRenderer Wheel5;
    public ModelRenderer Wheel6;
    public ModelRenderer Wheel7;
    public ModelRenderer Wheel8;
    public ModelRenderer TruckBase2;
    public ModelRenderer Container1;
    public ModelRenderer Base1;
    public ModelRenderer shape22;

    public ModelCargoContainer() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.Axis1 = new ModelRenderer(this, 0, 0);
        this.Axis1.addBox(-9.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis1, 1.5707963267948966F, 0.0F, 0.0F);
        this.TruckBase1 = new ModelRenderer(this, 58, 4);
        this.TruckBase1.addBox(-11.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.Wheel8 = new ModelRenderer(this, 8, 0);
        this.Wheel8.setPos(7.0F, 5.0F, 5.0F);
        this.Wheel8.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Axis3 = new ModelRenderer(this, 58, 0);
        this.Axis3.addBox(4.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis3, 1.5707963267948966F, 0.0F, 0.0F);
        this.Wheel5 = new ModelRenderer(this, 8, 0);
        this.Wheel5.setPos(3.0F, 5.0F, -6.0F);
        this.Wheel5.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Axis4 = new ModelRenderer(this, 62, 0);
        this.Axis4.addBox(8.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis4, 1.5707963267948966F, 0.0F, 0.0F);
        this.Container1 = new ModelRenderer(this, 0, 15);
        this.Container1.addBox(-8.0F, -16.0F, -12.0F, 16, 16, 24, 0.0F);
        this.setRotateAngle(Container1, 0.005235987755982988F, 4.71238898038469F, 0.0F);
        this.shape22 = new ModelRenderer(this, 56, 19);
        this.shape22.addBox(-14.0F, 2.0F, -1.0F, 28, 1, 2, 0.0F);
        this.Wheel3 = new ModelRenderer(this, 8, 0);
        this.Wheel3.addBox(-6.0F, 2.0F, -6.0F, 3, 3, 1, 0.0F);
        this.Wheel4 = new ModelRenderer(this, 8, 0);
        this.Wheel4.setPos(-6.0F, 5.0F, 5.0F);
        this.Wheel4.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Base1 = new ModelRenderer(this, 0, 55);
        this.Base1.addBox(-13.0F, 0.0F, -9.0F, 26, 1, 18, 0.0F);
        this.TruckBase2 = new ModelRenderer(this, 26, 0);
        this.TruckBase2.addBox(2.0F, 1.0F, -7.0F, 9, 1, 14, 0.0F);
        this.Wheel6 = new ModelRenderer(this, 8, 0);
        this.Wheel6.setPos(3.0F, 5.0F, 5.0F);
        this.Wheel6.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Axis2 = new ModelRenderer(this, 4, 0);
        this.Axis2.addBox(-5.0F, -5.0F, -4.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(Axis2, 1.5707963267948966F, 0.0F, 0.0F);
        this.Wheel1 = new ModelRenderer(this, 8, 0);
        this.Wheel1.setPos(-10.0F, 2.0F, -6.0F);
        this.Wheel1.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel7 = new ModelRenderer(this, 8, 0);
        this.Wheel7.setPos(7.0F, 5.0F, -6.0F);
        this.Wheel7.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
        this.Wheel2 = new ModelRenderer(this, 8, 0);
        this.Wheel2.setPos(-10.0F, 5.0F, 5.0F);
        this.Wheel2.addBox(0.0F, -3.0F, 0.0F, 3, 3, 1, 0.0F);
    }

    @Override
    public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder builder, int i3, int i4, float f5, float f6, float f7, float f8) {
        this.Axis1.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.TruckBase1.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel8.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Axis3.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel5.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Axis4.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Container1.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.shape22.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel3.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel4.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Base1.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.TruckBase2.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel6.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Axis2.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel1.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel7.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
        this.Wheel2.render(matrixStack, builder, i3, i4, f5, f6, f7, f8);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
