package com.cassiokf.industrialrenewal.entity.model;
// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityFluidContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ModelCartFluidTank<T extends EntityFluidContainer> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(IndustrialRenewal.MODID, "fluid_cart"), "main");
	private final ModelPart Weels;
	private final ModelPart Shafts;
	private final ModelPart Truck;
	private final ModelPart Tank;
	private final ModelPart Gauges;
	private final ModelPart Gauges2;

	public ModelCartFluidTank(ModelPart root) {
		this.Weels = root.getChild("Weels");
		this.Shafts = root.getChild("Shafts");
		this.Truck = root.getChild("Truck");
		this.Tank = root.getChild("Tank");
		this.Gauges = root.getChild("Gauges");
		this.Gauges2 = root.getChild("Gauges2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Weels = partdefinition.addOrReplaceChild("Weels", CubeListBuilder.create().texOffs(8, 0).addBox(7.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(3.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-6.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-10.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(66, 0).addBox(7.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(74, 0).addBox(3.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(82, 0).addBox(-6.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 0).addBox(-10.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Shafts = partdefinition.addOrReplaceChild("Shafts", CubeListBuilder.create().texOffs(32, 27).addBox(8.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(32, 27).addBox(4.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(32, 27).addBox(-5.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(32, 27).addBox(-9.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(-14.0F, -22.0F, -1.0F, 28.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Truck = partdefinition.addOrReplaceChild("Truck", CubeListBuilder.create().texOffs(26, 0).addBox(2.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(58, 4).addBox(-11.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-13.0F, -24.0F, -9.0F, 26.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(-10.5F, -28.0F, -9.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(0.5F, -28.0F, -9.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -28.0F, -9.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-11.5F, -28.0F, -9.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(10.5F, -28.0F, -9.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(5.0F, -42.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-6.0F, -42.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -42.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(2.0F, -42.0F, -5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(3.0F, -42.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(-5.0F, -42.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(-5.0F, -42.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(3.0F, -42.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-13.0F, -31.0F, -7.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-13.0F, -31.0F, 6.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(92, 23).addBox(-13.0F, -31.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(92, 23).addBox(12.0F, -31.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(12.0F, -31.0F, -7.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(12.0F, -31.0F, 6.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(92, 23).addBox(12.0F, -28.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(92, 23).addBox(-13.0F, -28.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(10.5F, -28.0F, 8.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(0.5F, -28.0F, 8.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -28.0F, 8.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(-10.5F, -28.0F, 8.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-11.5F, -28.0F, 8.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Tank = partdefinition.addOrReplaceChild("Tank", CubeListBuilder.create().texOffs(0, 40).addBox(-12.0F, -8.0F, -4.0F, 24.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 65).addBox(-12.0F, -4.0F, -8.0F, 24.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(76, 39).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(8, 4).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition TankCorner = Tank.addOrReplaceChild("TankCorner", CubeListBuilder.create().texOffs(72, 40).addBox(-12.0F, -3.15F, -8.47F, 24.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(68, 49).addBox(-12.0F, -8.47F, -3.16F, 24.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(72, 58).addBox(-12.0F, -2.9F, 5.5F, 24.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(72, 68).addBox(-12.0F, 5.5F, -2.9F, 24.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition Gauges = partdefinition.addOrReplaceChild("Gauges", CubeListBuilder.create().texOffs(0, 51).addBox(-2.0F, -41.0F, -8.9F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 42).addBox(-3.0F, -40.0F, -8.9F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-2.44F, -42.02F, -9.2F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-2.42F, -37.19F, -9.2F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(1.0F, -40.6F, -9.2F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-3.9F, -40.6F, -9.2F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 7).addBox(-6.0F, -36.2F, -9.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(118, 6).addBox(-2.1F, -41.3F, -9.1F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 11).mirror().addBox(0.26F, -39.9F, -9.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(117, 0).mirror().addBox(-3.14F, -39.9F, -9.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 30.0F, 0.5F));

		PartDefinition GaugeCorners = Gauges.addOrReplaceChild("GaugeCorners", CubeListBuilder.create().texOffs(14, 42).addBox(-2.65F, -1.05F, -0.4F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 42).addBox(-1.05F, -2.65F, -0.4F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(2.28F, -1.02F, -0.7F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-3.05F, -1.05F, -0.7F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 16).addBox(-1.0F, -2.43F, -0.6F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -39.0F, -8.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone = Gauges.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(24, 21).addBox(-3.155F, -1.1F, -0.7F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(2.125F, -1.2F, -0.7F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(105, 19).mirror().addBox(-1.275F, -2.5F, -0.6F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.025F, -39.0F, -8.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition Gauges2 = partdefinition.addOrReplaceChild("Gauges2", CubeListBuilder.create().texOffs(0, 51).addBox(-2.0F, -41.0F, 7.9F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 42).addBox(-3.0F, -40.0F, 7.9F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-2.44F, -42.02F, 8.2F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-2.42F, -37.19F, 8.2F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(1.0F, -40.6F, 8.2F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-3.9F, -40.6F, 8.2F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 12).addBox(-6.0F, -36.2F, 8.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(118, 6).addBox(-2.1F, -41.3F, 8.1F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(124, 11).mirror().addBox(-3.14F, -39.9F, 8.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(117, 0).mirror().addBox(0.26F, -39.9F, 8.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 30.0F, -0.5F));

		PartDefinition GaugeCorners2 = Gauges2.addOrReplaceChild("GaugeCorners2", CubeListBuilder.create().texOffs(14, 42).addBox(-2.65F, -1.05F, -0.6F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 42).addBox(-1.05F, -2.65F, -0.6F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(2.28F, -1.02F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(-3.05F, -1.05F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 16).addBox(-0.8F, -2.43F, -0.4F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -39.0F, 8.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bone2 = Gauges2.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(24, 21).addBox(-3.155F, -1.1F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(2.125F, -1.2F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(105, 19).mirror().addBox(-0.875F, -2.5F, -0.4F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.025F, -39.0F, 8.5F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityFluidContainer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Weels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Shafts.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Truck.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Tank.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Gauges.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Gauges2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}