package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntitySolarPanelFrame;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class TESRSolarPanelFrame extends TESRBase<TileEntitySolarPanelFrame>{
    public TESRSolarPanelFrame(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntitySolarPanelFrame tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        double x = 0, y = 0, z = 0;
        if (tileEntity.hasPanel())
        {
            Direction facing = tileEntity.getBlockFacing();
            doTheMath(facing, x, z, 0.4, 0);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, y + 0.45f, zPos, tileEntity.getPanel(), 4, false, 22.5f, 1, 0, 0);
        }
        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
