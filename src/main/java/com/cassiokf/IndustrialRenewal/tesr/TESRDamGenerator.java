package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamGenerator;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class TESRDamGenerator extends TESRBase<TileEntityDamGenerator>{
    public TESRDamGenerator(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityDamGenerator tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
//        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        double x = 0, y = 0, z = 0;
        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getMasterFacing();
            //GENERATION
            doTheMath(facing, x, z, 1.98, 0);
            renderText(matrixStack, facing, xPos, y + 0.43, zPos, tileEntity.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.98, 0.115);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.58, zPos, tileEntity.getGenerationFill(), pointerLong, 0.5F);
        }
    }
}
