package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWireIsolator;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.awt.*;

public class TESRWire extends TESRBase<TileEntityWireIsolator>{
    public TESRWire(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    private static final Color c = new Color(56, 56, 56, 255);
    private static final Color c2 = new Color(43, 43, 43, 255);

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
        BlockPos source = tileEntity.getBlockPos();
        for(BlockPos node : tileEntity.neighbors) {
            renderWire(matrixStack, renderTypeBuffer, source, node, 0,0,0);
        }

        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
