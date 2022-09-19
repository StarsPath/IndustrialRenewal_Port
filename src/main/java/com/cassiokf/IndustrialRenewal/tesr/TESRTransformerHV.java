package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityTransformer;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class TESRTransformerHV extends TESRBase<TileEntityTransformer>{
    public TESRTransformerHV(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityTransformer tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        double x = 0, y = 0, z = 0;

        if (tileEntity.isMaster())
        {
//            Utils.debug("GENERATION", tileEntity.getGenerationFill());
            Direction facing = tileEntity.getMasterFacingDirect();
            //GENERATION
            doTheMath(facing, x, z, 1.86, 0);
            renderText(matrixStack, facing, xPos, y + 0.16, zPos, tileEntity.getGenerationText(), 0.008F);
            doTheMath(facing, x, z, 1.84, 0.13);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.36, zPos, tileEntity.getGenerationFill(), pointerLong, 0.5F);
        }

        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
