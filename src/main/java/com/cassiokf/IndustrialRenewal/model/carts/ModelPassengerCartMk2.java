package com.cassiokf.IndustrialRenewal.model.carts;// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.IndustrialRenewal.entity.EntityPassengerCartMk2;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelPassengerCartMk2 <T extends EntityPassengerCartMk2> extends EntityModel<T> {
	private final ModelRenderer Wheel7;
	private final ModelRenderer Wheel8;
	private final ModelRenderer Wheel5;
	private final ModelRenderer Wheel6;
	private final ModelRenderer Wheel3;
	private final ModelRenderer Wheel4;
	private final ModelRenderer Wheel1;
	private final ModelRenderer Wheel2;
	private final ModelRenderer Shaft;
	private final ModelRenderer Axis1;
	private final ModelRenderer Axis2;
	private final ModelRenderer Axis3;
	private final ModelRenderer Axis4;
	private final ModelRenderer TruckBase2;
	private final ModelRenderer TruckBase1;
	private final ModelRenderer Base1;
	private final ModelRenderer shape34;
	private final ModelRenderer Case1;
	private final ModelRenderer bb_main;

	public ModelPassengerCartMk2() {
		texWidth = 128;
		texHeight = 128;

		Wheel7 = new ModelRenderer(this);
		Wheel7.setPos(7.0F, 5.0F, -6.0F);
		Wheel7.texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel8 = new ModelRenderer(this);
		Wheel8.setPos(7.0F, 5.0F, 5.0F);
		Wheel8.texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel5 = new ModelRenderer(this);
		Wheel5.setPos(3.0F, 5.0F, -6.0F);
		Wheel5.texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel6 = new ModelRenderer(this);
		Wheel6.setPos(3.0F, 5.0F, 5.0F);
		Wheel6.texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel3 = new ModelRenderer(this);
		Wheel3.setPos(0.0F, 0.0F, 0.0F);
		Wheel3.texOffs(0, 11).addBox(-6.0F, 2.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel4 = new ModelRenderer(this);
		Wheel4.setPos(-6.0F, 5.0F, 5.0F);
		Wheel4.texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel1 = new ModelRenderer(this);
		Wheel1.setPos(-10.0F, 2.0F, -6.0F);
		Wheel1.texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel2 = new ModelRenderer(this);
		Wheel2.setPos(-10.0F, 5.0F, 5.0F);
		Wheel2.texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Shaft = new ModelRenderer(this);
		Shaft.setPos(0.0F, 0.0F, 0.0F);
		Shaft.texOffs(52, 61).addBox(-14.0F, 2.0F, -1.0F, 28.0F, 1.0F, 2.0F, 0.0F, false);

		Axis1 = new ModelRenderer(this);
		Axis1.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis1, 1.5708F, 0.0F, 0.0F);
		Axis1.texOffs(12, 0).addBox(-9.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		Axis2 = new ModelRenderer(this);
		Axis2.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis2, 1.5708F, 0.0F, 0.0F);
		Axis2.texOffs(8, 0).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		Axis3 = new ModelRenderer(this);
		Axis3.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis3, 1.5708F, 0.0F, 0.0F);
		Axis3.texOffs(4, 0).addBox(4.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		Axis4 = new ModelRenderer(this);
		Axis4.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis4, 1.5708F, 0.0F, 0.0F);
		Axis4.texOffs(0, 0).addBox(8.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		TruckBase2 = new ModelRenderer(this);
		TruckBase2.setPos(0.0F, 0.0F, 0.0F);
		TruckBase2.texOffs(70, 19).addBox(2.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);

		TruckBase1 = new ModelRenderer(this);
		TruckBase1.setPos(0.0F, 0.0F, 0.0F);
		TruckBase1.texOffs(70, 0).addBox(-11.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);

		Base1 = new ModelRenderer(this);
		Base1.setPos(0.0F, 0.0F, 0.0F);
		Base1.texOffs(0, 19).addBox(-13.0F, 0.0F, -9.0F, 26.0F, 1.0F, 18.0F, 0.0F, false);

		shape34 = new ModelRenderer(this);
		shape34.setPos(-6.0F, -2.0F, -6.0F);
		shape34.texOffs(68, 64).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, 0.0F, false);

		Case1 = new ModelRenderer(this);
		Case1.setPos(0.0F, 24.0F, 0.0F);
		

		bb_main = new ModelRenderer(this);
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(54, 38).addBox(-13.0F, -46.0F, -9.0F, 26.0F, 22.0F, 1.0F, 0.0F, false);
		bb_main.texOffs(0, 38).addBox(-13.0F, -46.0F, 8.0F, 26.0F, 22.0F, 1.0F, 0.0F, false);
		bb_main.texOffs(34, 61).addBox(12.0F, -46.0F, -8.0F, 1.0F, 22.0F, 16.0F, 0.0F, false);
		bb_main.texOffs(0, 61).addBox(-13.0F, -46.0F, -8.0F, 1.0F, 22.0F, 16.0F, 0.0F, false);
		bb_main.texOffs(0, 0).addBox(-13.0F, -47.0F, -9.0F, 26.0F, 1.0F, 18.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(EntityPassengerCartMk2 entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Wheel7.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel8.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel5.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel6.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel4.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Shaft.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis4.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Base1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape34.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Case1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}