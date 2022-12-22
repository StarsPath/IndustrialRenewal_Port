package com.cassiokf.industrialrenewal.entity.model;// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntityFlatCart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ModelCartFlat<T extends EntityFlatCart> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(IndustrialRenewal.MODID+ "flat_cart"), "main");
	private final ModelPart Weels;
	private final ModelPart Shafts;
	private final ModelPart Truck;

	public ModelCartFlat(ModelPart root) {
		this.Weels = root.getChild("Weels");
		this.Shafts = root.getChild("Shafts");
		this.Truck = root.getChild("Truck");
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

		PartDefinition Shafts = partdefinition.addOrReplaceChild("Shafts", CubeListBuilder.create().texOffs(1, 6).addBox(8.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(1, 6).addBox(4.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(1, 6).addBox(-5.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(1, 6).addBox(-9.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(56, 19).addBox(-14.0F, -22.0F, -1.0F, 28.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Truck = partdefinition.addOrReplaceChild("Truck", CubeListBuilder.create().texOffs(26, 0).addBox(2.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(58, 4).addBox(-11.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-13.0F, -24.0F, -9.0F, 26.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Weels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Shafts.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Truck.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}