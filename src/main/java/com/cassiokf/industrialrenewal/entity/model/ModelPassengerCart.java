package com.cassiokf.industrialrenewal.entity.model;// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityPassengerCart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ModelPassengerCart<T extends EntityPassengerCart> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(IndustrialRenewal.MODID, "electric_locomotive"), "main");
	private final ModelPart Wheel7;
	private final ModelPart shape18_2;
	private final ModelPart Wheel8;
	private final ModelPart shape18_1;
	private final ModelPart Wheel5;
	private final ModelPart Wheel6;
	private final ModelPart shape18_3;
	private final ModelPart Wheel3;
	private final ModelPart Wheel4;
	private final ModelPart Wheel1;
	private final ModelPart Wheel2;
	private final ModelPart shape25;
	private final ModelPart Shaft;
	private final ModelPart Axis1;
	private final ModelPart Axis2;
	private final ModelPart Axis3;
	private final ModelPart Axis4;
	private final ModelPart shape18;
	private final ModelPart TruckBase2;
	private final ModelPart shape22_1;
	private final ModelPart TruckBase1;
	private final ModelPart shape25_1;
	private final ModelPart shape22_2;
	private final ModelPart Base1;
	private final ModelPart shape22_3;
	private final ModelPart shape26;
	private final ModelPart shape26_1;
	private final ModelPart shape26_2;
	private final ModelPart shape30;
	private final ModelPart shape31;
	private final ModelPart shape31_1;
	private final ModelPart shape31_2;
	private final ModelPart shape34;
	private final ModelPart shape35;
	private final ModelPart shape35_1;
	private final ModelPart shape35_2;

	public ModelPassengerCart(ModelPart root) {
		this.Wheel7 = root.getChild("Wheel7");
		this.shape18_2 = root.getChild("shape18_2");
		this.Wheel8 = root.getChild("Wheel8");
		this.shape18_1 = root.getChild("shape18_1");
		this.Wheel5 = root.getChild("Wheel5");
		this.Wheel6 = root.getChild("Wheel6");
		this.shape18_3 = root.getChild("shape18_3");
		this.Wheel3 = root.getChild("Wheel3");
		this.Wheel4 = root.getChild("Wheel4");
		this.Wheel1 = root.getChild("Wheel1");
		this.Wheel2 = root.getChild("Wheel2");
		this.shape25 = root.getChild("shape25");
		this.Shaft = root.getChild("Shaft");
		this.Axis1 = root.getChild("Axis1");
		this.Axis2 = root.getChild("Axis2");
		this.Axis3 = root.getChild("Axis3");
		this.Axis4 = root.getChild("Axis4");
		this.shape18 = root.getChild("shape18");
		this.TruckBase2 = root.getChild("TruckBase2");
		this.shape22_1 = root.getChild("shape22_1");
		this.TruckBase1 = root.getChild("TruckBase1");
		this.shape25_1 = root.getChild("shape25_1");
		this.shape22_2 = root.getChild("shape22_2");
		this.Base1 = root.getChild("Base1");
		this.shape22_3 = root.getChild("shape22_3");
		this.shape26 = root.getChild("shape26");
		this.shape26_1 = root.getChild("shape26_1");
		this.shape26_2 = root.getChild("shape26_2");
		this.shape30 = root.getChild("shape30");
		this.shape31 = root.getChild("shape31");
		this.shape31_1 = root.getChild("shape31_1");
		this.shape31_2 = root.getChild("shape31_2");
		this.shape34 = root.getChild("shape34");
		this.shape35 = root.getChild("shape35");
		this.shape35_1 = root.getChild("shape35_1");
		this.shape35_2 = root.getChild("shape35_2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Wheel7 = partdefinition.addOrReplaceChild("Wheel7", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 5.0F, -6.0F));

		PartDefinition shape18_2 = partdefinition.addOrReplaceChild("shape18_2", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -20.0F, 6.0F));

		PartDefinition Wheel8 = partdefinition.addOrReplaceChild("Wheel8", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 5.0F, 5.0F));

		PartDefinition shape18_1 = partdefinition.addOrReplaceChild("shape18_1", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -20.0F, 6.0F));

		PartDefinition Wheel5 = partdefinition.addOrReplaceChild("Wheel5", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, -6.0F));

		PartDefinition Wheel6 = partdefinition.addOrReplaceChild("Wheel6", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 5.0F, 5.0F));

		PartDefinition shape18_3 = partdefinition.addOrReplaceChild("shape18_3", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -20.0F, -9.0F));

		PartDefinition Wheel3 = partdefinition.addOrReplaceChild("Wheel3", CubeListBuilder.create().texOffs(8, 0).addBox(-6.0F, 2.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Wheel4 = partdefinition.addOrReplaceChild("Wheel4", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 5.0F, 5.0F));

		PartDefinition Wheel1 = partdefinition.addOrReplaceChild("Wheel1", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, 0.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, 2.0F, -6.0F));

		PartDefinition Wheel2 = partdefinition.addOrReplaceChild("Wheel2", CubeListBuilder.create().texOffs(8, 0).addBox(0.0F, -3.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, 5.0F, 5.0F));

		PartDefinition shape25 = partdefinition.addOrReplaceChild("shape25", CubeListBuilder.create().texOffs(0, 102).addBox(0.0F, 0.0F, 0.0F, 21.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -9.0F, -9.0F));

		PartDefinition Shaft = partdefinition.addOrReplaceChild("Shaft", CubeListBuilder.create().texOffs(56, 19).addBox(-14.0F, 2.0F, -1.0F, 28.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Axis1 = partdefinition.addOrReplaceChild("Axis1", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Axis2 = partdefinition.addOrReplaceChild("Axis2", CubeListBuilder.create().texOffs(4, 0).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Axis3 = partdefinition.addOrReplaceChild("Axis3", CubeListBuilder.create().texOffs(58, 0).addBox(4.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Axis4 = partdefinition.addOrReplaceChild("Axis4", CubeListBuilder.create().texOffs(62, 0).addBox(8.0F, -5.0F, -4.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition shape18 = partdefinition.addOrReplaceChild("shape18", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 1.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -20.0F, -9.0F));

		PartDefinition TruckBase2 = partdefinition.addOrReplaceChild("TruckBase2", CubeListBuilder.create().texOffs(26, 0).addBox(2.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape22_1 = partdefinition.addOrReplaceChild("shape22_1", CubeListBuilder.create().texOffs(9, 75).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -20.0F, -8.9F));

		PartDefinition TruckBase1 = partdefinition.addOrReplaceChild("TruckBase1", CubeListBuilder.create().texOffs(58, 4).addBox(-11.0F, 1.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape25_1 = partdefinition.addOrReplaceChild("shape25_1", CubeListBuilder.create().texOffs(48, 102).addBox(0.0F, 0.0F, 0.0F, 21.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -9.0F, 8.0F));

		PartDefinition shape22_2 = partdefinition.addOrReplaceChild("shape22_2", CubeListBuilder.create().texOffs(29, 98).addBox(0.0F, 0.0F, 0.0F, 1.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.0F, -9.0F, -8.0F));

		PartDefinition Base1 = partdefinition.addOrReplaceChild("Base1", CubeListBuilder.create().texOffs(0, 23).addBox(-13.0F, 0.0F, -9.0F, 26.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shape22_3 = partdefinition.addOrReplaceChild("shape22_3", CubeListBuilder.create().texOffs(9, 75).addBox(0.0F, 0.0F, 0.0F, 1.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -20.0F, 2.9F));

		PartDefinition shape26 = partdefinition.addOrReplaceChild("shape26", CubeListBuilder.create().texOffs(0, 43).addBox(0.0F, 0.0F, 0.0F, 30.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-15.0F, -22.0F, -2.0F));

		PartDefinition shape26_1 = partdefinition.addOrReplaceChild("shape26_1", CubeListBuilder.create().texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 30.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -21.6F, 2.0F, -0.2094F, 0.0F, 0.0F));

		PartDefinition shape26_2 = partdefinition.addOrReplaceChild("shape26_2", CubeListBuilder.create().texOffs(0, 61).addBox(0.0F, 0.0F, -9.0F, 30.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -21.6F, -2.0F, 0.2094F, 0.0F, 0.0F));

		PartDefinition shape30 = partdefinition.addOrReplaceChild("shape30", CubeListBuilder.create().texOffs(24, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -7.0F, -6.0F));

		PartDefinition shape31 = partdefinition.addOrReplaceChild("shape31", CubeListBuilder.create().texOffs(26, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -6.0F, -4.0F));

		PartDefinition shape31_1 = partdefinition.addOrReplaceChild("shape31_1", CubeListBuilder.create().texOffs(26, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -6.0F, -0.5F));

		PartDefinition shape31_2 = partdefinition.addOrReplaceChild("shape31_2", CubeListBuilder.create().texOffs(26, 72).addBox(0.0F, 0.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -6.0F, 3.0F));

		PartDefinition shape34 = partdefinition.addOrReplaceChild("shape34", CubeListBuilder.create().texOffs(79, 44).addBox(0.0F, 0.0F, 0.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -2.0F, -6.0F));

		PartDefinition shape35 = partdefinition.addOrReplaceChild("shape35", CubeListBuilder.create().texOffs(51, 72).addBox(0.0F, 0.0F, 0.0F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -27.0F, -3.5F));

		PartDefinition shape35_1 = partdefinition.addOrReplaceChild("shape35_1", CubeListBuilder.create().texOffs(70, 62).addBox(0.0F, 0.0F, 0.0F, 10.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -28.0F, 0.0F, -0.2443F, 0.0F, 0.0F));

		PartDefinition shape35_2 = partdefinition.addOrReplaceChild("shape35_2", CubeListBuilder.create().texOffs(97, 64).addBox(0.0F, 0.0F, -5.0F, 10.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -28.0F, 0.0F, 0.2443F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T EntityPassengerCart, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Wheel7.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel8.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18_3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Wheel2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape25.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Shaft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Axis4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape18.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape22_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		TruckBase1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape25_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape22_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Base1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape22_3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape26.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape26_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape26_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape30.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape31.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape31_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape31_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape34.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape35.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape35_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shape35_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}