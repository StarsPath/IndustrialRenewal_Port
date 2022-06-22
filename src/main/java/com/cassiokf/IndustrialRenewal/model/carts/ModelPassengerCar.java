// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.cassiokf.IndustrialRenewal.model.carts;

import com.cassiokf.IndustrialRenewal.entity.EntityPassengerCar;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelPassengerCar <T extends EntityPassengerCar> extends EntityModel<T> {
	private final ModelRenderer Wheel7;
	private final ModelRenderer shape18_2;
	private final ModelRenderer Wheel8;
	private final ModelRenderer shape18_1;
	private final ModelRenderer Wheel5;
	private final ModelRenderer Wheel6;
	private final ModelRenderer shape18_3;
	private final ModelRenderer Wheel3;
	private final ModelRenderer Wheel4;
	private final ModelRenderer Wheel1;
	private final ModelRenderer Wheel2;
	private final ModelRenderer shape25;
	private final ModelRenderer Shaft;
	private final ModelRenderer Axis1;
	private final ModelRenderer Axis2;
	private final ModelRenderer Axis3;
	private final ModelRenderer Axis4;
	private final ModelRenderer shape18;
	private final ModelRenderer TruckBase2;
	private final ModelRenderer shape22_1;
	private final ModelRenderer TruckBase1;
	private final ModelRenderer shape25_1;
	private final ModelRenderer shape22_2;
	private final ModelRenderer Base1;
	private final ModelRenderer shape22_3;
	private final ModelRenderer shape26;
	private final ModelRenderer shape26_1;
	private final ModelRenderer shape26_2;
	private final ModelRenderer shape30;
	private final ModelRenderer shape31;
	private final ModelRenderer shape31_1;
	private final ModelRenderer shape31_2;
	private final ModelRenderer shape34;
	private final ModelRenderer shape35;
	private final ModelRenderer shape35_1;
	private final ModelRenderer shape35_2;

	public ModelPassengerCar() {
		texWidth = 128;
		texHeight = 128;

		Wheel7 = new ModelRenderer(this);
		Wheel7.setPos(7.0F, 5.0F, -6.0F);
		Wheel7.texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		shape18_2 = new ModelRenderer(this);
		shape18_2.setPos(12.0F, -20.0F, 6.0F);
		shape18_2.texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 3.0F, 0.0F, false);

		Wheel8 = new ModelRenderer(this);
		Wheel8.setPos(7.0F, 5.0F, 5.0F);
		Wheel8.texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		shape18_1 = new ModelRenderer(this);
		shape18_1.setPos(-13.0F, -20.0F, 6.0F);
		shape18_1.texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 11.0F, 3.0F, 0.0F, false);

		Wheel5 = new ModelRenderer(this);
		Wheel5.setPos(3.0F, 5.0F, -6.0F);
		Wheel5.texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel6 = new ModelRenderer(this);
		Wheel6.setPos(3.0F, 5.0F, 5.0F);
		Wheel6.texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		shape18_3 = new ModelRenderer(this);
		shape18_3.setPos(12.0F, -20.0F, -9.0F);
		shape18_3.texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 3.0F, 0.0F, false);

		Wheel3 = new ModelRenderer(this);
		Wheel3.setPos(0.0F, 0.0F, 0.0F);
		Wheel3.texOffs(8, 0).addBox(-6.0F, 2.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel4 = new ModelRenderer(this);
		Wheel4.setPos(-6.0F, 5.0F, 5.0F);
		Wheel4.texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel1 = new ModelRenderer(this);
		Wheel1.setPos(-10.0F, 2.0F, -6.0F);
		Wheel1.texOffs(8, 0).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Wheel2 = new ModelRenderer(this);
		Wheel2.setPos(-10.0F, 5.0F, 5.0F);
		Wheel2.texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		shape25 = new ModelRenderer(this);
		shape25.setPos(-13.0F, -9.0F, -9.0F);
		shape25.texOffs(0, 102).addBox(0.0F, 0.0F, 0.0F, 21.0F, 9.0F, 1.0F, 0.0F, false);

		Shaft = new ModelRenderer(this);
		Shaft.setPos(0.0F, 0.0F, 0.0F);
		Shaft.texOffs(56, 19).addBox(-14.0F, 2.0F, -1.0F, 28.0F, 1.0F, 2.0F, 0.0F, false);

		Axis1 = new ModelRenderer(this);
		Axis1.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis1, 1.5708F, 0.0F, 0.0F);
		Axis1.texOffs(0, 0).addBox(-9.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		Axis2 = new ModelRenderer(this);
		Axis2.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis2, 1.5708F, 0.0F, 0.0F);
		Axis2.texOffs(4, 0).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		Axis3 = new ModelRenderer(this);
		Axis3.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis3, 1.5708F, 0.0F, 0.0F);
		Axis3.texOffs(58, 0).addBox(4.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		Axis4 = new ModelRenderer(this);
		Axis4.setPos(0.0F, 0.0F, 0.0F);
		setRotationAngle(Axis4, 1.5708F, 0.0F, 0.0F);
		Axis4.texOffs(62, 0).addBox(8.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, 0.0F, false);

		shape18 = new ModelRenderer(this);
		shape18.setPos(-13.0F, -20.0F, -9.0F);
		shape18.texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 11.0F, 3.0F, 0.0F, false);

		TruckBase2 = new ModelRenderer(this);
		TruckBase2.setPos(0.0F, 0.0F, 0.0F);
		TruckBase2.texOffs(26, 0).addBox(2.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);

		shape22_1 = new ModelRenderer(this);
		shape22_1.setPos(7.0F, -20.0F, -8.9F);
		shape22_1.texOffs(9, 75).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 6.0F, 0.0F, false);

		TruckBase1 = new ModelRenderer(this);
		TruckBase1.setPos(0.0F, 0.0F, 0.0F);
		TruckBase1.texOffs(58, 4).addBox(-11.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);

		shape25_1 = new ModelRenderer(this);
		shape25_1.setPos(-13.0F, -9.0F, 8.0F);
		shape25_1.texOffs(48, 102).addBox(0.0F, 0.0F, 0.0F, 21.0F, 9.0F, 1.0F, 0.0F, false);

		shape22_2 = new ModelRenderer(this);
		shape22_2.setPos(-13.0F, -9.0F, -8.0F);
		shape22_2.texOffs(29, 98).addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 16.0F, 0.0F, false);

		Base1 = new ModelRenderer(this);
		Base1.setPos(0.0F, 0.0F, 0.0F);
		Base1.texOffs(0, 23).addBox(-13.0F, 0.0F, -9.0F, 26.0F, 1.0F, 18.0F, 0.0F, false);

		shape22_3 = new ModelRenderer(this);
		shape22_3.setPos(7.0F, -20.0F, 2.9F);
		shape22_3.texOffs(9, 75).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 6.0F, 0.0F, false);

		shape26 = new ModelRenderer(this);
		shape26.setPos(-15.0F, -22.0F, -2.0F);
		shape26.texOffs(0, 43).addBox(0.0F, 0.0F, 0.0F, 30.0F, 2.0F, 4.0F, 0.0F, false);

		shape26_1 = new ModelRenderer(this);
		shape26_1.setPos(-15.0F, -21.6F, 2.0F);
		setRotationAngle(shape26_1, -0.2094F, 0.0F, 0.0F);
		shape26_1.texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 30.0F, 1.0F, 9.0F, 0.0F, false);

		shape26_2 = new ModelRenderer(this);
		shape26_2.setPos(-15.0F, -21.6F, -2.0F);
		setRotationAngle(shape26_2, 0.2094F, 0.0F, 0.0F);
		shape26_2.texOffs(0, 61).addBox(0.0F, 0.0F, -9.0F, 30.0F, 1.0F, 9.0F, 0.0F, false);

		shape30 = new ModelRenderer(this);
		shape30.setPos(12.0F, -7.0F, -6.0F);
		shape30.texOffs(24, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);

		shape31 = new ModelRenderer(this);
		shape31.setPos(12.0F, -6.0F, -4.0F);
		shape31.texOffs(26, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);

		shape31_1 = new ModelRenderer(this);
		shape31_1.setPos(12.0F, -6.0F, -0.5F);
		shape31_1.texOffs(26, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);

		shape31_2 = new ModelRenderer(this);
		shape31_2.setPos(12.0F, -6.0F, 3.0F);
		shape31_2.texOffs(26, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);

		shape34 = new ModelRenderer(this);
		shape34.setPos(-6.0F, -2.0F, -6.0F);
		shape34.texOffs(79, 44).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, 0.0F, false);

		shape35 = new ModelRenderer(this);
		shape35.setPos(4.0F, -27.0F, -3.5F);
		shape35.texOffs(51, 72).addBox(0.0F, 0.0F, 0.0F, 8.0F, 6.0F, 7.0F, 0.0F, false);

		shape35_1 = new ModelRenderer(this);
		shape35_1.setPos(3.0F, -28.0F, 0.0F);
		setRotationAngle(shape35_1, -0.2443F, 0.0F, 0.0F);
		shape35_1.texOffs(70, 62).addBox(0.0F, 0.0F, 0.0F, 10.0F, 1.0F, 5.0F, 0.0F, false);

		shape35_2 = new ModelRenderer(this);
		shape35_2.setPos(3.0F, -28.0F, 0.0F);
		setRotationAngle(shape35_2, 0.2443F, 0.0F, 0.0F);
		shape35_2.texOffs(97, 64).addBox(0.0F, 0.0F, -5.0F, 10.0F, 1.0F, 5.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(EntityPassengerCar entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Wheel7.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18_2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel8.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18_1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel5.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel6.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18_3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel4.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape25.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Shaft.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis4.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape22_1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape25_1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape22_2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Base1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape22_3.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape26.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape26_1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape26_2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape30.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape31.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape31_1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape31_2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape34.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape35.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape35_1.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		shape35_2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}