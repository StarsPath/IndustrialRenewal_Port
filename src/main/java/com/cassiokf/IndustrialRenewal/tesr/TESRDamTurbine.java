package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamTurbine;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class TESRDamTurbine extends TESRBase<TileEntityDamTurbine>{
    public TESRDamTurbine(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityDamTurbine tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
//        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        double x = 0, y = 0, z = 0;
        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getMasterFacing();
            doTheMath(facing, x, z, 1.98, 0);
            renderText(matrixStack, facing, xPos, y + 0.36, zPos, tileEntity.getRotationText(), 0.008F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.61, zPos, tileEntity.getRotationFill(), pointer, 0.3F);
        }
    }
}
