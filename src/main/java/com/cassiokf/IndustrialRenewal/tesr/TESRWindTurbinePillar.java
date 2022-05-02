package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWindTurbinePillar;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class TESRWindTurbinePillar extends TESRBase<TileEntityWindTurbinePillar>{

    public TESRWindTurbinePillar(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityWindTurbinePillar tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        int x = 0;
        int y = 0;
        int z = 0;

        if (tileEntity.isBase())
        {
            Direction facing = tileEntity.getBlockFacing();
            doTheMath(facing, x, z, 0.78, 0);
            renderText(matrixStack, facing, xPos, y + 0.72, zPos, tileEntity.getText(), 0.006F);
            doTheMath(facing, x, z, 0.78, 0.1f);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.845, zPos, tileEntity.getGenerationforGauge(), pointerLong, 0.38F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.845, zPos, tileEntity.getEnergyGenerated(), limiter, 0.57F);
        }
        //super.render(p_225616_1_, p_225616_2_, p_225616_3_, p_225616_4_, p_225616_5_, p_225616_6_);
    }
}
