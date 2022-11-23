package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbinePillar;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRWindTurbinePillar extends TESRBase<BlockEntityWindTurbinePillar>{


    public TESRWindTurbinePillar(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(BlockEntityWindTurbinePillar blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0;
        int y = 0;
        int z = 0;

        if (blockEntity!=null && blockEntity.isBase())
        {
            Direction facing = blockEntity.getBlockFacing();
            doTheMath(facing, x, z, 0.78, 0);
            renderText(stack, facing, xPos, y + 0.72, zPos, blockEntity.getText(), 0.006F);
            doTheMath(facing, x, z, 0.78, 0.1f);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.845, zPos, blockEntity.getGenerationforGauge(), pointerLong, 0.38F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.845, zPos, blockEntity.getEnergyGenerated(), limiter, 0.57F);
        }
    }
}
