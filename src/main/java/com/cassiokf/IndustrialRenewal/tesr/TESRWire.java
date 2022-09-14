package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWireIsolator;
//import com.cassiokf.IndustrialRenewal.tileentity.abstracts.HvNode;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TESRWire extends TESRBase<TileEntityWireIsolator>{
    public TESRWire(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    private static final Color c = new Color(56, 56, 56, 255);
    private static final Color c2 = new Color(43, 43, 43, 255);
//
//    public static void renderWire(MatrixStack matrixStack, int combinedLightIn, int combinedOverlayIn, IRenderTypeBuffer buffetIn, BlockPos startPos, BlockPos endTE, double x, double y, double z)
//    {
//        if (startPos.getY() > endTE.getY()) return;
//        boolean sameLevel = startPos.getY() == endTE.getY();
//
//        y -= 0.97D;
//        x += 0.5D;
//        z += 0.5D;
//
//        matrixStack.pushPose();
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferbuilder = tessellator.getBuilder();
//        IVertexBuilder builder = buffetIn.getBuffer(RenderType.LINES);
//
//        double d6 = endTE.getX();
//        double d7 = endTE.getY();
//        double d8 = endTE.getZ();
//
//        double d10 = startPos.getX();
//        double d11 = startPos.getY();
//        double d12 = startPos.getZ();
//
//        double d13 = d6 - d10;
//        //double d14 = d7 - (d11 - 1.33D);
//        double d14 = d7 - d11 + 1.33D;
//        double d15 = d8 - d12;
//
//        int i = 24;
//
////        GlStateManager._disableLighting();
//        GlStateManager._disableTexture();
//        GlStateManager._disableCull();
//
//        //RenderSystem.lineWidth(8);
////        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
//
//        float percentage = 1 / 24f;
//
//        for (int j = 0; j <= i; ++j)
//        {
//            if (!sameLevel || j < (i / 2) + 1)
//            {
//                float f = Utils.normalizeClamped(c.getRed(), 0, 255);
//                float f1 = Utils.normalizeClamped(c.getGreen(), 0, 255);
//                float f2 = Utils.normalizeClamped(c.getBlue(), 0, 255);
//
//                if (j % 2 == 0)
//                {
//                    f = Utils.normalizeClamped(c2.getRed(), 0, 255);
//                    f1 = Utils.normalizeClamped(c2.getGreen(), 0, 255);
//                    f2 = Utils.normalizeClamped(c2.getBlue(), 0, 255);
//                }
//
//                float f3 = (float) j / 24.0F;
//                double v = (d14 * (f3 * f3 + f3)) * 0.5D;
//                double b = ((24.0F - j) / 18.0F + 0.125F);
//
//                float f4 = (float) (j + 1) / 24.0F;
//                double v1 = (d14 * (f4 * f4 + f4)) * 0.5D;
//                double b1 = ((24.0F - j + 1) / 18.0F + 0.125F);
//
//
//                builder.vertex(matrixStack.last().pose(), (float)(x), (float)(y + 1.33D), (float)(z))
//                        .color(f, f1, f2, c.getTransparency()).endVertex();
//                builder.vertex(matrixStack.last().pose(), (float)(x + d13), (float)(y + d14), (float)(z + d15))
//                        .color(f, f1, f2, c.getTransparency()).endVertex();
//
////                builder.vertex(matrixStack.last().pose(), (float)(x + d13 * f3), (float)(y + v + b), (float)(z + d15 * f3))
////                        .color(f, f1, f2, c.getTransparency()).endVertex();
////                builder.vertex(matrixStack.last().pose(), (float)(x + d13 * f4), (float)(y + v1 + b1), (float)(z + d15 * f4))
////                        .color(f, f1, f2, c.getTransparency()).endVertex();
//
////                builder.vertex(matrixStack.last().pose(), (float)(x + d13 * f3), (float)(y + v + b), (float)(z + d15 * f3))
////                        .color(f, f1, f2, c.getTransparency()).endVertex();
////                builder.vertex(matrixStack.last().pose(), (float)(x + d13 * f3 + 0.025D), (float)(y + v + b + 0.025D), (float)(z + d15 * f3))
////                        .color(f, f1, f2, c.getTransparency()).endVertex();
//            }
//        }
////        GlStateManager._lineWidth(8);
////        tessellator.end();
//
//        //RenderSystem.lineWidth(8);
////        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
//
////        for (int k = 0; k <= i; ++k)
////        {
////            if (!sameLevel || k < (i / 2) + 1)
////            {
////                float f4 = Utils.normalizeClamped(c.getRed(), 0, 255);
////                float f5 = Utils.normalizeClamped(c.getGreen(), 0, 255);
////                float f6 = Utils.normalizeClamped(c.getBlue(), 0, 255);
////
////                if (k % 2 == 0)
////                {
////                    f4 = Utils.normalizeClamped(c2.getRed(), 0, 255);
////                    f5 = Utils.normalizeClamped(c2.getGreen(), 0, 255);
////                    f6 = Utils.normalizeClamped(c2.getBlue(), 0, 255);
////                }
////
////                float f7 = (float) k / 24.0F;
////                double v = d14 * (f7 * f7 + f7) * 0.5D;
////                double b = ((24.0F - k) / 18.0F + 0.125F);
////
////                builder.vertex(matrixStack.last().pose(),(float)(x + d13 * f7), (float)(y + v + b + 0.025D), (float)(z + d15 * f7))
////                        .color(f4, f5, f6, c.getTransparency()).endVertex();
////                builder.vertex(matrixStack.last().pose(), (float)(x + d13 * f7 + 0.025D), (float)(y + v + b), (float)(z + d15 * f7 + 0.025D))
////                        .color(f4, f5, f6, c.getTransparency()).endVertex();
////            }
////        }
////        GlStateManager._lineWidth(8);
////        tessellator.end();
//
//
////        float f = Utils.normalizeClamped(c.getRed(), 0, 255);
////        float f1 = Utils.normalizeClamped(c.getGreen(), 0, 255);
////        float f2 = Utils.normalizeClamped(c.getBlue(), 0, 255);
////
////        builder.vertex(matrixStack.last().pose(), (float)(x), (float)(y), (float)(z))
////                .color(f, f1, f2, c.getTransparency()).endVertex();
////        builder.vertex(matrixStack.last().pose(), (float)(x + d13), (float)(y + d14), (float)(z + d15))
////                .color(f, f1, f2, c.getTransparency()).endVertex();
//
//
//
//
//
//
////        GlStateManager._enableLighting();
//        GlStateManager._enableTexture();
//        GlStateManager._enableCull();
//
//        matrixStack.popPose();
//    }

//    public static void renderLine(MatrixStack matrixStack, Vector3d vecStart, Vector3d vecEnd, Color color, int lineWidth)
//    {
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferbuilder = tessellator.getBuilder();
////        MatrixStack matrixStack = event.getMatrixStack();
//        Vector3d projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
//
//        matrixStack.pushPose();
//        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
//        RenderSystem.lineWidth(lineWidth);
//
//        Matrix4f matrix = matrixStack.last().pose();
//
//        bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
//        drawLine(matrix, bufferbuilder, vecStart, vecEnd, color);
//        tessellator.end();
//
//        GlStateManager._lineWidth(lineWidth);
//        matrixStack.popPose();
//    }
//
//    private static void drawLine(Matrix4f matrix, BufferBuilder buffer, Vector3d p1, Vector3d p2, Color color)
//    {
//        buffer.vertex(matrix, (float)p1.x + 0.5f, (float)p1.y + 1.0f, (float)p1.z + 0.5f)
//                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
//                .endVertex();
//        buffer.vertex(matrix, (float)p2.x + 0.5f, (float)p2.y, (float)p2.z + 0.5f)
//                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
//                .endVertex();
//    }

    public void renderWire(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, BlockPos startPos, BlockPos endPos, double x, double y, double z){
        float xOffset = 0.5f;
        float zOffset = 0.5f;
        float yOffset = 0.5f;

        Vector3d source =  new Vector3d(startPos.getX(), startPos.getY(), startPos.getZ());
        Vector3d destination = new Vector3d(endPos.getX(), endPos.getY(), endPos.getZ());
        Vector3d start = new Vector3d(source.x, source.y, source.z);

        Vector3d midPoint = Utils.midPoint(source, destination);
        midPoint = new Vector3d(midPoint.x, Math.min(Math.min(midPoint.y, source.y), destination.y) - 0.5, midPoint.z);


        float f = Utils.normalizeClamped(c2.getRed(), 0, 255);
        float f1 = Utils.normalizeClamped(c2.getGreen(), 0, 255);
        float f2 = Utils.normalizeClamped(c2.getBlue(), 0, 255);

        float finalDeltaX = source.y == destination.y ? (float)(midPoint.x - source.x) : (float)(destination.x - source.x);
        float finalDeltaY = source.y == destination.y ? (float)(midPoint.y - source.y) : (float)(destination.y - source.y);
        float finalDeltaZ = source.y == destination.y ? (float)(midPoint.z - source.z) : (float)(destination.z - source.z);

        Vector3d currentPos = new Vector3d(0, 0, 0);
        Vector3d finalStartPos = new Vector3d(0, 0, 0);
        Vector3d finalEndPos = new Vector3d(finalDeltaX, finalDeltaY, finalDeltaZ);

        float lerpY = 0f;

        if(source.y >= destination.y){
            int numSegments = 12;
            matrixStack.pushPose();
            IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.LINES);

            for(int i = 0 ; i < numSegments; i++){

                builder.vertex(matrixStack.last().pose(), (float)currentPos.x + xOffset, lerpY + yOffset, (float)currentPos.z + zOffset)
                        .color(f, f1, f2, c.getTransparency()).endVertex();

                currentPos = Utils.lerp(finalStartPos, finalEndPos, (float)(i+1) / numSegments);
                lerpY = Utils.lerp(lerpY, finalDeltaY, source.y == destination.y ? 0.25f : 0.33f);

                builder.vertex(matrixStack.last().pose(), (float)currentPos.x + xOffset, lerpY + yOffset, (float)currentPos.z + zOffset)
                        .color(f, f1, f2, c.getTransparency()).endVertex();
            }
            matrixStack.popPose();
        }
    }

    @Override
    public void render(TileEntityWireIsolator tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
//        Utils.debug("RUNNING RENDER");
        BlockPos source = tileEntity.getPosition();
//        Vector3d sourcePos = Vector3d.atCenterOf(new Vector3i(source.getX(), source.getY()- 0.97D, source.getZ()));
        for(BlockPos node : tileEntity.neighbors) {
//            Vector3d nodePos = Vector3d.atCenterOf(new Vector3i(node.getX(), node.getY()- 0.97D, node.getZ()));
            //renderWire(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, source, node, 0, 0, 0);
//            renderLine(matrixStack, sourcePos, nodePos, c, 4);
//            Utils.debug("RENDERING");
            renderWire(matrixStack, renderTypeBuffer, source, node, 0,0,0);
        }

        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityWireIsolator p_188185_1_) {
        //return super.shouldRenderOffScreen(p_188185_1_);
        return true;
    }
}
