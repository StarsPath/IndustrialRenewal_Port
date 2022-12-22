package com.cassiokf.industrialrenewal.entity.model;
// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityPassengerCartMk2;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelPassengerCartMk2<T extends EntityPassengerCartMk2> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(IndustrialRenewal.MODID, "passenger_cart_mk2"), "main");
	private final ModelPart Wheel7;
	private final ModelPart Wheel8;
	private final ModelPart Wheel5;
	private final ModelPart Wheel6;
	private final ModelPart Wheel3;
	private final ModelPart Wheel4;
	private final ModelPart Wheel1;
	private final ModelPart Wheel2;
	private final ModelPart Shaft;
	private final ModelPart Axis1;
	private final ModelPart Axis2;
	private final ModelPart Axis3;
	private final ModelPart Axis4;
	private final ModelPart TruckBase2;
	private final ModelPart TruckBase1;
	private final ModelPart Base1;
	private final ModelPart shape34;
	private final ModelPart Case1;
	private final ModelPart bb_main;

	public ModelPassengerCartMk2(ModelPart root) {
		this.Wheel7 = root.getChild("Wheel7");
		this.Wheel8 = root.getChild("Wheel8");
		this.Wheel5 = root.getChild("Wheel5");
		this.Wheel6 = root.getChild("Wheel6");
		this.Wheel3 = root.getChild("Wheel3");
		this.Wheel4 = root.getChild("Wheel4");
		this.Wheel1 = root.getChild("Wheel1");
		this.Wheel2 = root.getChild("Wheel2");
		this.Shaft = root.getChild("Shaft");
		this.Axis1 = root.getChild("Axis1");
		this.Axis2 = root.getChild("Axis2");
		this.Axis3 = root.getChild("Axis3");
		this.Axis4 = root.getChild("Axis4");
		this.TruckBase2 = root.getChild("TruckBase2");
		this.TruckBase1 = root.getChild("TruckBase1");
		this.Base1 = root.getChild("Base1");
		this.shape34 = root.getChild("shape34");
		this.Case1 = root.getChild("Case1");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Wheel7 = partdefinition.addOrReplaceChild("Wheel7", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 5.0F, -6.0F));

		PartDefinition Wheel8 = partdefinition.addOrReplaceChild("Wheel8", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 5.0F, 5.0F));

		PartDefinition Wheel5 = partdefinition.addOrReplaceChild("Wheel5", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, -6.0F));

		PartDefinition Wheel6 = partdefinition.addOrReplaceChild("Wheel6", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, 5.0F));

		PartDefinition Wheel3 = partdefinition.addOrReplaceChild("Wheel3", CubeListBuilder.create().texOffs(0, 11).addBox(-6.0F, 2.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Wheel4 = partdefinition.addOrReplaceChild("Wheel4", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 5.0F, 5.0F));

		PartDefinition Wheel1 = partdefinition.addOrReplaceChild("Wheel1", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, 2.0F, -6.0F));

		PartDefinition Wheel2 = partdefinition.addOrReplaceChild("Wheel2", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, 5.0F, 5.0F));

		PartDefinition Shaft = partdefinition.addOrReplaceChild("Shaft", CubeListBuilder.create().texOffs(52, 61).addBox(-14.0F, 2.0F, -1.0F, 28.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Axis1 = partdefinition.addOrReplaceChild("Axis1", CubeListBuilder.create().texOffs(12, 0).addBox(-9.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Axis2 = partdefinition.addOrReplaceChild("Axis2", CubeListBuilder.create().texOffs(8, 0).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Axis3 = partdefinition.addOrReplaceChild("Axis3", CubeListBuilder.create().texOffs(4, 0).addBox(4.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Axis4 = partdefinition.addOrReplaceChild("Axis4", CubeListBuilder.create().texOffs(0, 0).addBox(8.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition TruckBase2 = partdefinition.addOrReplaceChild("TruckBase2", CubeListBuilder.create().texOffs(70, 19).addBox(2.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition TruckBase1 = partdefinition.addOrReplaceChild("TruckBase1", CubeListBuilder.create().texOffs(70, 0).addBox(-11.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Base1 = partdefinition.addOrReplaceChild("Base1", CubeListBuilder.create().texOffs(0, 19).addBox(-13.0F, 0.0F, -9.0F, 26.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape34 = partdefinition.addOrReplaceChild("shape34", CubeListBuilder.create().texOffs(68, 64).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -2.0F, -6.0F));

		PartDefinition Case1 = partdefinition.addOrReplaceChild("Case1", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(54, 38).addBox(-13.0F, -46.0F, -9.0F, 26.0F, 22.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(-13.0F, -46.0F, 8.0F, 26.0F, 22.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 61).addBox(12.0F, -46.0F, -8.0F, 1.0F, 22.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 61).addBox(-13.0F, -46.0F, -8.0F, 1.0F, 22.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-13.0F, -47.0F, -9.0F, 26.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityPassengerCartMk2 entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Wheel7.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel8.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Shaft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Base1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape34.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Case1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}