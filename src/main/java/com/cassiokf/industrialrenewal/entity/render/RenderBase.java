package com.cassiokf.industrialrenewal.entity.render;

import com.cassiokf.industrialrenewal.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;


public abstract class RenderBase<T extends AbstractMinecart> extends EntityRenderer<T> {
    private static final ItemStack pointer = new ItemStack(ModItems.pointer.get());

//    protected EntityModel<T> model = new MinecartModel<>();
    protected EntityModel<T> model;

    protected RenderBase(EntityRendererProvider.Context context, ModelLayerLocation location) {
        super(context);
        this.shadowRadius = 0.5f;
        this.model = new MinecartModel<>(context.bakeLayer(location));
    }

//    protected RenderBase(EntityRendererManager renderManagerIn) {
//        super(renderManagerIn);
//        this.shadowRadius = 0.5f;
//    }

    private static void renderText(PoseStack matrixStack, String text, double x, double y, double z)
    {
        matrixStack.pushPose();
        matrixStack.scale(0.1F, 0.1F, 0.1F);
        matrixStack.scale(0.07F, 0.07F, 1F);
        int xh = -Minecraft.getInstance().font.width(text) / 2;
        matrixStack.translate(x, y, z);
        Minecraft.getInstance().font.draw(matrixStack, text, (float)xh, (float)0, 0xFFFFFFFF);
        matrixStack.popPose();
    }

    public static void renderPointer(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, double x, double y, double z, float angle, ItemStack pointer)
    {
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        matrixStack.scale(0.15F, 0.15F, 1.0F);
        matrixStack.mulPose(new Quaternion(0, 0, -90, true));
        matrixStack.mulPose(new Quaternion(180, 0, 0, true));
        matrixStack.mulPose(new Quaternion(0, 0, -angle, true));
        Minecraft.getInstance().getItemRenderer().renderStatic(pointer, ItemTransforms.TransformType.GUI, combinedLightIn, combinedOverlayIn, matrixStack, buffetIn, 0);
        matrixStack.popPose();
    }

    public void renderExtra(T p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_){

    }

    public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLightIn)
    {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLightIn);
        matrixStack.pushPose();
        long i = (long)entity.getId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f1 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f2 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate((double)f, (double)f1, (double)f2);
        double d0 = Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
        double d1 = Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
        double d2 = Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
        double d3 = (double)0.3F;
        Vec3 vector3d = entity.getPos(d0, d1, d2);
        float f3 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        if (vector3d != null) {
            Vec3 vector3d1 = entity.getPosOffs(d0, d1, d2, (double)0.3F);
            Vec3 vector3d2 = entity.getPosOffs(d0, d1, d2, (double)-0.3F);
            if (vector3d1 == null) {
                vector3d1 = vector3d;
            }

            if (vector3d2 == null) {
                vector3d2 = vector3d;
            }

            matrixStack.translate(vector3d.x - d0, (vector3d1.y + vector3d2.y) / 2.0D - d1, vector3d.z - d2);
            Vec3 vector3d3 = vector3d2.add(-vector3d1.x, -vector3d1.y, -vector3d1.z);
            if (vector3d3.length() != 0.0D) {
                vector3d3 = vector3d3.normalize();
                entityYaw = (float)(Math.atan2(vector3d3.z, vector3d3.x) * 180.0D / Math.PI);
                f3 = (float)(Math.atan(vector3d3.y) * 73.0D);
            }
        }

        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-f3));
        float f5 = (float)entity.getHurtTime() - partialTicks;
        float f6 = entity.getDamage() - partialTicks;
        if (f6 < 0.0F) {
            f6 = 0.0F;
        }

        if (f5 > 0.0F) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f5) * f5 * f6 / 10.0F * (float)entity.getHurtDir()));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.setupAnim(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}
