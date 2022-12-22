//package com.cassiokf.industrialrenewal.entity.model;// Made with Blockbench 4.5.2
//// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
//// Paste this class into your mod and generate all required imports
//
//
//import net.minecraft.client.model.EntityModel;
//
//public class ModelFlatCart<T extends Entity> extends EntityModel<T> {
//	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
//	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "modelflatcart"), "main");
//	private final ModelPart wheels;
//	private final ModelPart shafts;
//	private final ModelPart truck;
//	private final ModelPart bb_main;
//
//	public ModelFlatCart(ModelPart root) {
//		this.wheels = root.getChild("wheels");
//		this.shafts = root.getChild("shafts");
//		this.truck = root.getChild("truck");
//		this.bb_main = root.getChild("bb_main");
//	}
//
//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition wheels = partdefinition.addOrReplaceChild("wheels", CubeListBuilder.create().texOffs(8, 0).addBox(7.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(16, 0).addBox(3.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(24, 0).addBox(-6.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(32, 0).addBox(-10.0F, -22.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(66, 0).addBox(7.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(74, 0).addBox(3.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(82, 0).addBox(-6.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(90, 0).addBox(-10.0F, -22.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//		PartDefinition shafts = partdefinition.addOrReplaceChild("shafts", CubeListBuilder.create().texOffs(1, 6).addBox(8.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
//		.texOffs(1, 6).addBox(4.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
//		.texOffs(1, 6).addBox(-5.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
//		.texOffs(1, 6).addBox(-9.0F, -21.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
//		.texOffs(56, 19).addBox(-14.0F, -22.0F, -1.0F, 28.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//		PartDefinition truck = partdefinition.addOrReplaceChild("truck", CubeListBuilder.create().texOffs(26, 0).addBox(2.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
//		.texOffs(58, 4).addBox(-11.0F, -23.0F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
//		.texOffs(0, 22).addBox(-13.0F, -24.0F, -9.0F, 26.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 87).addBox(-13.0F, -47.0F, -9.0F, 16.0F, 23.0F, 18.0F, new CubeDeformation(0.0F))
//		.texOffs(72, 97).addBox(3.0F, -37.0F, -9.0F, 10.0F, 13.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
//
//		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 41).addBox(-5.0F, 3.0F, -9.0F, 14.0F, 8.0F, 18.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(8.7574F, -45.4853F, 0.0F, 0.0F, 0.0F, 0.7854F));
//
//		return LayerDefinition.create(meshdefinition, 128, 128);
//	}
//
//	@Override
//	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//	}
//
//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		wheels.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//		shafts.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//		truck.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
//}