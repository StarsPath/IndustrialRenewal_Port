package com.cassiokf.IndustrialRenewal.entity.render;

import com.cassiokf.IndustrialRenewal.entity.RotatableBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RenderRotatableBase <T extends RotatableBase> extends RenderBase<T>{
    protected RenderRotatableBase(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_) {
        return null;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLightIn) {
        matrixStack.pushPose();
        long i = (long)entity.getId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f1 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f2 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate((double)f, (double)f1, (double)f2);
        double d0 = MathHelper.lerp((double)partialTicks, entity.xOld, entity.getX());
        double d1 = MathHelper.lerp((double)partialTicks, entity.yOld, entity.getY());
        double d2 = MathHelper.lerp((double)partialTicks, entity.zOld, entity.getZ());
        double d3 = (double)0.3F;
        Vector3d vector3d = entity.getPos(d0, d1, d2);
        float f3 = MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot);
        if (vector3d != null) {
            Vector3d vector3d1 = entity.getPosOffs(d0, d1, d2, (double)0.3F);
            Vector3d vector3d2 = entity.getPosOffs(d0, d1, d2, (double)-0.3F);
            if (vector3d1 == null) {
                vector3d1 = vector3d;
            }

            if (vector3d2 == null) {
                vector3d2 = vector3d;
            }

            matrixStack.translate(vector3d.x - d0, (vector3d1.y + vector3d2.y) / 2.0D - d1, vector3d.z - d2);
            Vector3d vector3d3 = vector3d2.add(-vector3d1.x, -vector3d1.y, -vector3d1.z);
            if (vector3d3.length() != 0.0D) {
                vector3d3 = vector3d3.normalize();
                entityYaw = (float)(Math.atan2(vector3d3.z, vector3d3.x) * 180.0D / Math.PI);
                f3 = (float)(Math.atan(vector3d3.y) * 73.0D);
            }
        }

        entityYaw = entityYaw % 360;
        if (entityYaw < 0) {
            entityYaw += 360;
        }
        entityYaw += 360;

        double rotationYaw = (entity.xRot + 180) % 360;
        if (rotationYaw < 0) {
            rotationYaw = rotationYaw + 360;
        }
        rotationYaw = rotationYaw + 360;

        if (Math.abs(entityYaw - rotationYaw) > 90) {
            entityYaw += 180;
            f3 = -f3;
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
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.sin(f5) * f5 * f6 / 10.0F * (float)entity.getHurtDir()));
        }

        boolean flip = entity.getDeltaMovement().x > 0.0 != entity.getDeltaMovement().z > 0.0;
//        Utils.debug("MOTION", entity.getDeltaMovement().x, entity.getDeltaMovement().z);
        if (entity.cornerFlip)
        {
            flip = !flip;
        }
        if (entity.getRenderFlippedYaw(entityYaw + (flip ? 0.0f : 180.0f)))
        {
            flip = !flip;
        }

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(flip ? 0.0f : 180.0f));

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.model.setupAnim(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}
