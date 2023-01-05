package com.cassiokf.industrialrenewal.entity.model;
// Made with Blockbench 4.5.2
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelDieselLocomotive<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(IndustrialRenewal.MODID, "modeldiesellocomotive"), "main");
	private final ModelPart Truck;
	private final ModelPart wheel;
	private final ModelPart wheel2;
	private final ModelPart wheel3;
	private final ModelPart wheel4;
	private final ModelPart wheel5;
	private final ModelPart wheel6;
	private final ModelPart shaftr;
	private final ModelPart shaftr2;
	private final ModelPart railing;
	private final ModelPart railing2;
	private final ModelPart railing3;
	private final ModelPart body;
	private final ModelPart roof;

	public ModelDieselLocomotive(ModelPart root) {
		this.Truck = root.getChild("Truck");
		this.wheel = root.getChild("wheel");
		this.wheel2 = root.getChild("wheel2");
		this.wheel3 = root.getChild("wheel3");
		this.wheel4 = root.getChild("wheel4");
		this.wheel5 = root.getChild("wheel5");
		this.wheel6 = root.getChild("wheel6");
		this.shaftr = root.getChild("shaftr");
		this.shaftr2 = root.getChild("shaftr2");
		this.railing = root.getChild("railing");
		this.railing2 = root.getChild("railing2");
		this.railing3 = root.getChild("railing3");
		this.body = root.getChild("body");
		this.roof = root.getChild("roof");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Truck = partdefinition.addOrReplaceChild("Truck", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -25.0F, -9.0F, 31.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(51, 74).addBox(-22.0F, -25.0F, -6.5F, 13.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(71, 88).addBox(-23.0F, -25.0F, -6.0F, 1.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(28, 62).addBox(-25.0F, -24.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 62).addBox(-25.0F, -24.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-26.0F, -24.5F, -5.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-26.0F, -24.5F, 2.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(82, 19).addBox(-15.0F, -23.0F, -5.0F, 9.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 74).addBox(-2.1F, -24.0F, -5.0F, 22.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(85, 27).addBox(-11.5F, -24.0F, -0.875F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(-21.75F, -25.75F, 6.15F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-21.75F, -25.75F, -9.1F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(85, 22).addBox(-22.25F, -22.35F, 6.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 24).addBox(-22.25F, -22.35F, -8.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 80).addBox(-22.25F, -24.9F, 6.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 40).addBox(-22.25F, -24.9F, -8.7F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone = Truck.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(23.0F, -39.0F, -8.0F, 0.4363F, 0.0F, 0.0F));

		PartDefinition bone2 = Truck.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(23.0F, -39.0F, 8.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition bone3 = Truck.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone12 = bone3.addOrReplaceChild("bone12", CubeListBuilder.create(), PartPose.offsetAndRotation(12.5F, -28.5F, 0.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition bone13 = bone3.addOrReplaceChild("bone13", CubeListBuilder.create(), PartPose.offsetAndRotation(12.5F, -28.5F, 0.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, -32.6263F, 0.6364F, -0.7854F, 0.0F, 0.0F));

		PartDefinition bone5 = bone3.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, -31.6364F, -1.6263F, 0.7854F, 0.0F, 0.0F));

		PartDefinition wheel = partdefinition.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(0, 74).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 23).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 17).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(97, 85).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(69, 96).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(61, 96).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(97, 98).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(66, 98).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(62, 98).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 98).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition wheel2 = partdefinition.addOrReplaceChild("wheel2", CubeListBuilder.create().texOffs(72, 19).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(38, 70).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(69, 94).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(61, 94).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 93).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 92).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 56).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 98).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 52).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 98).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 24.0F, 0.0F));

		PartDefinition wheel3 = partdefinition.addOrReplaceChild("wheel3", CubeListBuilder.create().texOffs(36, 66).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 66).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(64, 92).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 92).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 91).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 84).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 48).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(46, 98).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 98).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(38, 98).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(13.0F, 24.0F, 0.0F));

		PartDefinition wheel4 = partdefinition.addOrReplaceChild("wheel4", CubeListBuilder.create().texOffs(0, 66).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 62).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 90).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(64, 90).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 90).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 81).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 95).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(79, 93).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 91).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 91).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(19.0F, 24.0F, 0.0F));

		PartDefinition wheel5 = partdefinition.addOrReplaceChild("wheel5", CubeListBuilder.create().texOffs(8, 44).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(79, 22).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 78).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(72, 27).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 70).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(85, 90).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(90, 74).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(84, 4).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(84, 0).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(25.0F, 24.0F, 0.0F));

		PartDefinition wheel6 = partdefinition.addOrReplaceChild("wheel6", CubeListBuilder.create().texOffs(8, 36).addBox(-15.0F, -23.0F, 5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-15.0F, -23.0F, -6.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 70).addBox(-15.0F, -20.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 48).addBox(-15.0F, -20.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 48).addBox(-15.0F, -24.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 48).addBox(-15.0F, -24.0F, -6.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 80).addBox(-12.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 40).addBox(-12.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 32).addBox(-16.0F, -23.0F, 5.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 14).addBox(-16.0F, -23.0F, -6.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(31.0F, 24.0F, 0.0F));

		PartDefinition shaftr = partdefinition.addOrReplaceChild("shaftr", CubeListBuilder.create().texOffs(70, 98).addBox(5.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(41, 88).addBox(-14.0F, -21.0F, 7.0F, 20.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(98, 60).addBox(-8.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(31, 98).addBox(-1.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(76, 97).addBox(-14.0F, -21.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bone6 = shaftr.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(0, 42).addBox(-5.0F, -1.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -20.0F, 7.5F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone8 = shaftr.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(0, 40).addBox(-5.0F, 0.0F, -0.3F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.7219F, -23.8494F, 7.5F, 0.0F, 0.0F, 0.1745F));

		PartDefinition bone7 = shaftr.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(0, 48).addBox(-2.7F, -1.0F, -0.3F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -21.0F, 7.5F, 0.0F, 0.0F, 1.309F));

		PartDefinition shaftr2 = partdefinition.addOrReplaceChild("shaftr2", CubeListBuilder.create().texOffs(79, 91).addBox(5.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 87).addBox(-14.0F, -23.0F, 7.0F, 20.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(87, 20).addBox(-8.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 62).addBox(-1.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(5, 19).addBox(-14.0F, -23.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -15.0F));

		PartDefinition bone9 = shaftr2.addOrReplaceChild("bone9", CubeListBuilder.create().texOffs(5, 34).addBox(-5.0F, 0.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -23.0F, 7.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition bone10 = shaftr2.addOrReplaceChild("bone10", CubeListBuilder.create().texOffs(0, 32).addBox(-7.0F, -1.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.214F, -20.174F, 7.3F, 0.0F, 0.0F, 0.6109F));

		PartDefinition bone11 = shaftr2.addOrReplaceChild("bone11", CubeListBuilder.create().texOffs(80, 19).addBox(0.0F, -1.0F, -0.7F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -23.0F, 7.5F, 0.0F, 0.0F, 1.309F));

		PartDefinition railing = partdefinition.addOrReplaceChild("railing", CubeListBuilder.create().texOffs(0, 89).addBox(4.0F, -32.0F, -9.0F, 18.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).addBox(21.0F, -31.0F, -9.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 74).addBox(12.0F, -31.0F, -9.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition railing2 = partdefinition.addOrReplaceChild("railing2", CubeListBuilder.create().texOffs(85, 88).addBox(4.0F, -32.0F, 8.0F, 18.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 74).addBox(21.0F, -31.0F, 8.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 62).addBox(12.0F, -31.0F, 8.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition railing3 = partdefinition.addOrReplaceChild("railing3", CubeListBuilder.create().texOffs(14, 7).addBox(-23.0F, -31.0F, 4.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 0).addBox(-23.0F, -31.0F, -5.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 91).addBox(-23.0F, -32.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(48, 48).addBox(4.0F, -37.0F, -7.0F, 18.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-9.0F, -37.0F, -9.0F, 13.0F, 12.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(80, 0).addBox(-21.0F, -34.0F, -4.0F, 12.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 91).addBox(-18.0F, -31.0F, -8.0F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(90, 74).addBox(-18.0F, -31.0F, 4.0F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(85, 90).addBox(-1.5F, 0.0F, 0.0F, 9.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.5F, -31.0F, -8.0F, 0.6435F, 0.0F, 0.0F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(33, 90).addBox(-1.5F, 0.0F, 0.0F, 9.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.5F, -34.0F, 4.0F, -0.6435F, 0.0F, 0.0F));

		PartDefinition roof = partdefinition.addOrReplaceChild("roof", CubeListBuilder.create().texOffs(0, 19).addBox(-1.5F, -3.0F, 4.0F, 31.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -13.0F, -9.0F));

		PartDefinition cube_r3 = roof.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(44, 32).addBox(-1.5F, 0.0F, 0.0F, 31.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6435F, 0.0F, 0.0F));

		PartDefinition cube_r4 = roof.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(44, 40).addBox(-1.5F, 0.0F, 0.0F, 31.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 14.0F, -0.6435F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Truck.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel5.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wheel6.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shaftr.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		shaftr2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		railing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		railing2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		railing3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		roof.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}