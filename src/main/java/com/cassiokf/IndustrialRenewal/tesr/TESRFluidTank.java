package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityFluidTank;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class TESRFluidTank extends TESRBase<TileEntityFluidTank>{
    public TESRFluidTank(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityFluidTank te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        double x = 0, y = 0, z = 0;
        if (te.isMaster() && te.isBase())
        {
            Direction facing = te.getMasterFacing();
            doTheMath(facing, x, z, 1.98, 0);
            renderText(matrixStack, facing, xPos, y + 0.36, zPos, te.getFluidName(), 0.008F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.63, zPos, te.getFluidAngle(), pointer, 0.3F);
        }
        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
