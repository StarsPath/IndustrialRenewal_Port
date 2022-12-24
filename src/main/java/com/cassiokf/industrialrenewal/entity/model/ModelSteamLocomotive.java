package com.cassiokf.industrialrenewal.entity.model;
// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ModelSteamLocomotive<T extends EntitySteamLocomotive> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(IndustrialRenewal.MODID, "steam_locomotive"), "main");
	private final ModelPart Truck;
	private final ModelPart wheel;
	private final ModelPart wheel2;
	private final ModelPart wheel3;
	private final ModelPart wheel4;
	private final ModelPart shaftr;
	private final ModelPart shaftr2;

	public ModelSteamLocomotive(ModelPart root) {
		this.Truck = root.getChild("Truck");
		this.wheel = root.getChild("wheel");
		this.wheel2 = root.getChild("wheel2");
		this.wheel3 = root.getChild("wheel3");
		this.wheel4 = root.getChild("wheel4");
		this.shaftr = root.getChild("shaftr");
		this.shaftr2 = root.getChild("shaftr2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Truck = partdefinition.addOrReplaceChild("Truck", CubeListBuilder.create().texOffs(0, 38).addBox(-9.0F, -25.0F, -9.0F, 18.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(74, 20).addBox(-22.0F, -25.0F, -6.5F, 13.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(106, 106).addBox(-23.0F, -25.0F, -6.0F, 1.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(84, 110).addBox(-22.0F, -35.0F, -5.0F, 1.0F, 8.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(-22.25F, -34.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(38, 98).addBox(11.0F, -32.0F, 8.25F, 10.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 81).addBox(11.0F, -32.0F, -9.25F, 10.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(111, 11).addBox(-25.0F, -24.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(27, 111).addBox(-25.0F, -24.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 92).addBox(-22.0F, -38.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 38).addBox(-26.0F, -24.5F, -5.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(-26.0F, -24.5F, 2.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(62, 91).addBox(-15.0F, -23.0F, -5.0F, 9.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(64, 78).addBox(-2.1F, -24.0F, -5.0F, 15.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(67, 24).addBox(-11.5F, -24.0F, -0.875F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(67, 1).addBox(9.0F, -23.0F, -9.0F, 13.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 68).addBox(9.0F, -37.0F, -9.0F, 1.0F, 14.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(38, 81).addBox(9.0F, -40.0F, -8.0F, 1.0F, 1.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(62, 75).addBox(9.0F, -39.0F, -9.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(74, 28).addBox(9.0F, -39.0F, 6.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 111).addBox(9.0F, -41.0F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(20, 68).addBox(8.0F, -41.2F, -6.0F, 15.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(34, 114).addBox(-21.75F, -25.75F, 6.15F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 83).addBox(-21.75F, -25.75F, -9.1F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, 76).addBox(-22.25F, -22.35F, 6.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 68).addBox(-22.25F, -22.35F, -8.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(26, 68).addBox(-22.25F, -24.9F, 6.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 0).addBox(-22.25F, -24.9F, -8.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(113, 20).addBox(10.0F, -33.0F, 8.0F, 12.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 91).addBox(20.0F, -39.0F, 8.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(20.0F, -39.0F, -9.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 57).addBox(10.0F, -39.0F, 8.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 38).addBox(10.0F, -39.0F, -9.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(111, 0).addBox(10.0F, -33.0F, -9.0F, 12.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(74, 34).addBox(-19.0F, -34.0F, 7.0F, 28.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(67, 20).addBox(-6.0F, -38.5F, -0.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(67, 61).addBox(-19.0F, -34.0F, -8.0F, 28.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(82, 113).addBox(-19.0F, -34.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 112).addBox(-8.0F, -34.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(13, 49).addBox(2.0F, -37.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 110).addBox(-19.0F, -34.0F, -7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(33, 111).addBox(-8.0F, -34.0F, -7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone = Truck.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(106, 91).addBox(-15.0F, -1.15F, -2.25F, 15.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -39.0F, -8.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition bone2 = Truck.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(104, 78).addBox(-15.0F, -1.15F, -2.75F, 15.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, -39.0F, 8.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition bone3 = Truck.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 19).addBox(-21.0F, -37.0F, -3.5F, 30.0F, 12.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(14, 111).addBox(10.0F, -37.0F, -3.5F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-21.0F, -34.5F, -6.0F, 30.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(88, 91).addBox(10.0F, -34.5F, -6.0F, 3.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(64, 103).addBox(10.0F, -24.5F, -6.0F, 3.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(56, 91).addBox(10.0F, -27.5F, -6.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(70, 53).addBox(13.0F, -29.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 44).addBox(12.0F, -29.0F, 5.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(13.0F, -24.0F, 6.75F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(82, 103).addBox(14.0F, -36.0F, 5.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(96, 91).addBox(14.0F, -30.0F, 6.75F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(74, 22).addBox(10.0F, -27.5F, 3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(72, 0).addBox(-19.0F, -41.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 76).addBox(-19.5F, -44.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 68).addBox(-9.5F, -41.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(38, 103).addBox(-5.5F, -32.0F, 4.75F, 15.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(54, 49).addBox(-19.5F, -29.0F, -6.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 49).addBox(-19.5F, -29.0F, 3.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 100).addBox(-5.5F, -32.0F, -9.25F, 15.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone12 = bone3.addOrReplaceChild("bone12", CubeListBuilder.create().texOffs(20, 68).addBox(0.0F, 0.0F, -4.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.5F, -28.5F, 0.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition bone13 = bone3.addOrReplaceChild("bone13", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.5F, -28.5F, 0.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(67, 50).addBox(-15.0F, -0.6237F, -6.0164F, 30.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 57).addBox(-15.0F, -0.1737F, 0.4136F, 30.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(62, 68).addBox(15.0F, -0.6237F, -6.0364F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -32.6263F, 0.6364F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone5 = bone3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(67, 67).addBox(-15.0F, -0.2F, -6.0F, 30.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(54, 38).addBox(-15.0F, -0.6136F, 0.4263F, 30.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(56, 81).addBox(15.0F, -0.6136F, 4.4263F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -31.6364F, -1.6263F, 0.7854F, 0.0F, 0.0F));

		PartDefinition wheel = partdefinition.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(111, 15).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(85, 110).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(45, 114).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 65).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(113, 100).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 98).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 111).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 91).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 111).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 81).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition wheel2 = partdefinition.addOrReplaceChild("wheel2", CubeListBuilder.create().texOffs(0, 111).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 97).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(113, 31).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 65).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 86).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 88).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 91).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 81).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 78).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 76).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 24.0F, 0.0F));

		PartDefinition wheel3 = partdefinition.addOrReplaceChild("wheel3", CubeListBuilder.create().texOffs(104, 110).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 84).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(112, 84).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 84).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 101).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 103).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 49).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(38, 103).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 68).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 24.0F, 0.0F));

		PartDefinition wheel4 = partdefinition.addOrReplaceChild("wheel4", CubeListBuilder.create().texOffs(96, 110).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(46, 92).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 65).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 81).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 99).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(79, 28).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 100).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 68).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 100).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(19.0F, 24.0F, 0.0F));

		PartDefinition shaftr = partdefinition.addOrReplaceChild("shaftr", CubeListBuilder.create().texOffs(68, 98).addBox(5.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(74, 63).addBox(-14.0F, -21.0F, 7.0F, 20.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(83, 22).addBox(-8.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(68, 95).addBox(-1.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 85).addBox(-14.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone6 = shaftr.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(72, 9).addBox(-5.0F, -1.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -20.0F, 7.5F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone8 = shaftr.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(72, 7).addBox(-5.0F, 0.0F, -0.3F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.7219F, -23.8494F, 7.5F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone7 = shaftr.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(67, 22).addBox(-2.7F, -1.0F, -0.3F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -21.0F, 7.5F, 0.0F, 0.0F, 1.309F));

		PartDefinition shaftr2 = partdefinition.addOrReplaceChild("shaftr2", CubeListBuilder.create().texOffs(82, 30).addBox(5.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(74, 36).addBox(-14.0F, -23.0F, 7.0F, 20.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(81, 0).addBox(-8.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 78).addBox(-1.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 77).addBox(-14.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -15.0F));

		PartDefinition bone9 = shaftr2.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(0, 9).addBox(-5.0F, 0.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -23.0F, 7.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition bone10 = shaftr2.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(74, 65).addBox(-7.0F, -1.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.214F, -20.174F, 7.3F, 0.0F, 0.0F, 0.6109F));

		PartDefinition bone11 = shaftr2.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(20, 77).addBox(0.0F, -1.0F, -0.7F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -23.0F, 7.5F, 0.0F, 0.0F, 1.309F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(EntitySteamLocomotive entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Truck.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shaftr.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shaftr2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}