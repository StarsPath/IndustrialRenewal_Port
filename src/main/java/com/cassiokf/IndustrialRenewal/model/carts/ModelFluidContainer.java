package com.cassiokf.IndustrialRenewal.model.carts;// Made with Blockbench 4.2.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.IndustrialRenewal.entity.EntityFlatCart;
import com.cassiokf.IndustrialRenewal.entity.EntityFluidContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelFluidContainer <T extends EntityFluidContainer> extends EntityModel<T> {
	private final ModelRenderer Weels;
	private final ModelRenderer Shafts;
	private final ModelRenderer Truck;
	private final ModelRenderer Tank;
	private final ModelRenderer TankCorner;
	private final ModelRenderer Gauges;
	private final ModelRenderer GaugeCorners;
	private final ModelRenderer bone;
	private final ModelRenderer Gauges2;
	private final ModelRenderer GaugeCorners2;
	private final ModelRenderer bone2;

	public ModelFluidContainer() {
		texWidth = 128;
		texHeight = 128;

		Weels = new ModelRenderer(this);
		Weels.setPos(0.0F, 24.0F, 0.0F);
		Weels.texOffs(8, 0).addBox(7.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Weels.texOffs(16, 0).addBox(3.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Weels.texOffs(24, 0).addBox(-6.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Weels.texOffs(32, 0).addBox(-10.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Weels.texOffs(66, 0).addBox(7.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Weels.texOffs(74, 0).addBox(3.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Weels.texOffs(82, 0).addBox(-6.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Weels.texOffs(90, 0).addBox(-10.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		Shafts = new ModelRenderer(this);
		Shafts.setPos(0.0F, 24.0F, 0.0F);
		Shafts.texOffs(32, 27).addBox(8.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		Shafts.texOffs(32, 27).addBox(4.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		Shafts.texOffs(32, 27).addBox(-5.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		Shafts.texOffs(32, 27).addBox(-9.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
		Shafts.texOffs(0, 15).addBox(-14.0F, -22.0F, -1.0F, 28.0F, 1.0F, 2.0F, 0.0F, false);

		Truck = new ModelRenderer(this);
		Truck.setPos(0.0F, 24.0F, 0.0F);
		Truck.texOffs(26, 0).addBox(2.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);
		Truck.texOffs(58, 4).addBox(-11.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, 0.0F, false);
		Truck.texOffs(0, 19).addBox(-13.0F, -24.0F, -9.0F, 26.0F, 1.0F, 18.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(-10.5F, -28.0F, -9.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(0.5F, -28.0F, -9.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-0.5F, -28.0F, -9.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-11.5F, -28.0F, -9.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(10.5F, -28.0F, -9.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(5.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(5.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-6.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-6.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 19).addBox(5.0F, -42.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
		Truck.texOffs(0, 19).addBox(-6.0F, -42.0F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(2.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-3.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-3.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(2.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(3.0F, -42.0F, 4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(-5.0F, -42.0F, 4.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(-5.0F, -42.0F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(3.0F, -42.0F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-13.0F, -31.0F, -7.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-13.0F, -31.0F, 6.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		Truck.texOffs(92, 23).addBox(-13.0F, -31.0F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
		Truck.texOffs(92, 23).addBox(12.0F, -31.0F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(12.0F, -31.0F, -7.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(12.0F, -31.0F, 6.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		Truck.texOffs(92, 23).addBox(12.0F, -28.0F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
		Truck.texOffs(92, 23).addBox(-13.0F, -28.0F, -6.0F, 1.0F, 1.0F, 12.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(10.5F, -28.0F, 8.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(0.5F, -28.0F, 8.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-0.5F, -28.0F, 8.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		Truck.texOffs(16, 16).addBox(-10.5F, -28.0F, 8.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
		Truck.texOffs(0, 0).addBox(-11.5F, -28.0F, 8.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		Tank = new ModelRenderer(this);
		Tank.setPos(0.0F, -8.0F, 0.0F);
		Tank.texOffs(0, 40).addBox(-12.0F, -8.0F, -4.0F, 24.0F, 16.0F, 8.0F, 0.0F, false);
		Tank.texOffs(0, 65).addBox(-12.0F, -4.0F, -8.0F, 24.0F, 8.0F, 16.0F, 0.0F, false);
		Tank.texOffs(76, 39).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
		Tank.texOffs(8, 4).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 1.0F, 6.0F, 0.0F, false);

		TankCorner = new ModelRenderer(this);
		TankCorner.setPos(0.0F, 0.0F, 0.0F);
		Tank.addChild(TankCorner);
		setRotationAngle(TankCorner, -0.7854F, 0.0F, 0.0F);
		TankCorner.texOffs(72, 40).addBox(-12.0F, -3.15F, -8.47F, 24.0F, 6.0F, 3.0F, 0.0F, false);
		TankCorner.texOffs(68, 49).addBox(-12.0F, -8.47F, -3.16F, 24.0F, 3.0F, 6.0F, 0.0F, false);
		TankCorner.texOffs(72, 58).addBox(-12.0F, -2.9F, 5.5F, 24.0F, 6.0F, 3.0F, 0.0F, false);
		TankCorner.texOffs(72, 68).addBox(-12.0F, 5.5F, -2.9F, 24.0F, 3.0F, 6.0F, 0.0F, false);

		Gauges = new ModelRenderer(this);
		Gauges.setPos(1.0F, 30.0F, 0.5F);
		Gauges.texOffs(0, 51).addBox(-2.0F, -41.0F, -8.9F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(14, 42).addBox(-3.0F, -40.0F, -8.9F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(24, 21).addBox(-2.44F, -42.02F, -9.2F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(24, 21).addBox(-2.42F, -37.19F, -9.2F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(24, 21).addBox(1.0F, -40.6F, -9.2F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(24, 21).addBox(-3.9F, -40.6F, -9.2F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(90, 7).addBox(-6.0F, -36.2F, -9.0F, 10.0F, 2.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(118, 6).addBox(-2.1F, -41.3F, -9.1F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		Gauges.texOffs(124, 11).addBox(0.26F, -39.9F, -9.1F, 1.0F, 1.0F, 1.0F, 0.0F, true);
		Gauges.texOffs(117, 0).addBox(-3.14F, -39.9F, -9.1F, 1.0F, 1.0F, 1.0F, 0.0F, true);

		GaugeCorners = new ModelRenderer(this);
		GaugeCorners.setPos(-1.0F, -39.0F, -8.5F);
		Gauges.addChild(GaugeCorners);
		setRotationAngle(GaugeCorners, 0.0F, 0.0F, -0.7854F);
		GaugeCorners.texOffs(14, 42).addBox(-2.65F, -1.05F, -0.4F, 5.0F, 2.0F, 1.0F, 0.0F, false);
		GaugeCorners.texOffs(14, 42).addBox(-1.05F, -2.65F, -0.4F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		GaugeCorners.texOffs(24, 21).addBox(2.28F, -1.02F, -0.7F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		GaugeCorners.texOffs(24, 21).addBox(-3.05F, -1.05F, -0.7F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		GaugeCorners.texOffs(106, 16).addBox(-1.0F, -2.43F, -0.6F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setPos(-1.025F, -39.0F, -8.5F);
		Gauges.addChild(bone);
		setRotationAngle(bone, 0.0F, 0.0F, 0.7854F);
		bone.texOffs(24, 21).addBox(-3.155F, -1.1F, -0.7F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone.texOffs(24, 21).addBox(2.125F, -1.2F, -0.7F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone.texOffs(105, 19).addBox(-1.275F, -2.5F, -0.6F, 3.0F, 1.0F, 1.0F, 0.0F, true);

		Gauges2 = new ModelRenderer(this);
		Gauges2.setPos(1.0F, 30.0F, -0.5F);
		Gauges2.texOffs(0, 51).addBox(-2.0F, -41.0F, 7.9F, 2.0F, 4.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(14, 42).addBox(-3.0F, -40.0F, 7.9F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(24, 21).addBox(-2.44F, -42.02F, 8.2F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(24, 21).addBox(-2.42F, -37.19F, 8.2F, 3.0F, 1.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(24, 21).addBox(1.0F, -40.6F, 8.2F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(24, 21).addBox(-3.9F, -40.6F, 8.2F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(90, 12).addBox(-6.0F, -36.2F, 8.0F, 10.0F, 2.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(118, 6).addBox(-2.1F, -41.3F, 8.1F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		Gauges2.texOffs(124, 11).addBox(-3.14F, -39.9F, 8.1F, 1.0F, 1.0F, 1.0F, 0.0F, true);
		Gauges2.texOffs(117, 0).addBox(0.26F, -39.9F, 8.1F, 1.0F, 1.0F, 1.0F, 0.0F, true);

		GaugeCorners2 = new ModelRenderer(this);
		GaugeCorners2.setPos(-1.0F, -39.0F, 8.5F);
		Gauges2.addChild(GaugeCorners2);
		setRotationAngle(GaugeCorners2, 0.0F, 0.0F, -0.7854F);
		GaugeCorners2.texOffs(14, 42).addBox(-2.65F, -1.05F, -0.6F, 5.0F, 2.0F, 1.0F, 0.0F, false);
		GaugeCorners2.texOffs(14, 42).addBox(-1.05F, -2.65F, -0.6F, 2.0F, 5.0F, 1.0F, 0.0F, false);
		GaugeCorners2.texOffs(24, 21).addBox(2.28F, -1.02F, -0.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		GaugeCorners2.texOffs(24, 21).addBox(-3.05F, -1.05F, -0.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		GaugeCorners2.texOffs(106, 16).addBox(-0.8F, -2.43F, -0.4F, 2.0F, 1.0F, 1.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setPos(-1.025F, -39.0F, 8.5F);
		Gauges2.addChild(bone2);
		setRotationAngle(bone2, 0.0F, 0.0F, 0.7854F);
		bone2.texOffs(24, 21).addBox(-3.155F, -1.1F, -0.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone2.texOffs(24, 21).addBox(2.125F, -1.2F, -0.3F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone2.texOffs(105, 19).addBox(-0.875F, -2.5F, -0.4F, 2.0F, 1.0F, 1.0F, 0.0F, true);
	}

	@Override
	public void setupAnim(EntityFluidContainer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Weels.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Shafts.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Truck.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Tank.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Gauges.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Gauges2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}