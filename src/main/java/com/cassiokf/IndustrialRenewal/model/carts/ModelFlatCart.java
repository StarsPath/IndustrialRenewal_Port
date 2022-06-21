package com.cassiokf.IndustrialRenewal.model.carts;// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.IndustrialRenewal.entity.EntityCargoContainer;
import com.cassiokf.IndustrialRenewal.entity.EntityFlatCart;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFlatCart <T extends EntityFlatCart> extends EntityModel<T> {
	private final ModelRenderer wheels;
	private final ModelRenderer shafts;
	private final ModelRenderer truck;

	public ModelFlatCart() {
		texWidth = 128;
		texHeight = 128;

		wheels = new ModelRenderer(this);
		wheels.setPos(0.0F, 24.0F, 0.0F);
		wheels.texOffs(8, 0).addBox(7.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		wheels.texOffs(16, 0).addBox(3.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		wheels.texOffs(24, 0).addBox(-6.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		wheels.texOffs(32, 0).addBox(-10.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		wheels.texOffs(66, 0).addBox(7.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		wheels.texOffs(74, 0).addBox(3.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		wheels.texOffs(82, 0).addBox(-6.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		wheels.texOffs(90, 0).addBox(-10.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		shafts = new ModelRenderer(this);
		shafts.setPos(0.0F, 24.0F, 0.0F);
		shafts.texOffs(1, 6).addBox(8.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		shafts.texOffs(1, 6).addBox(4.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		shafts.texOffs(1, 6).addBox(-5.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		shafts.texOffs(1, 6).addBox(-9.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		shafts.texOffs(56, 19).addBox(-14.0F, -22.0F, -1.0F, 28.0F, 1.0F, 2.0F, 0.0F, false);

		truck = new ModelRenderer(this);
		truck.setPos(0.0F, 24.0F, 0.0F);
		truck.texOffs(26, 0).addBox(2.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);
		truck.texOffs(58, 4).addBox(-11.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);
		truck.texOffs(0, 22).addBox(-13.0F, -24.0F, -9.0F, 26.0F, 1.0F, 18.0F, 0.0F, false);
	}

    @Override
    public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

    }

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		wheels.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shafts.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		truck.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}