package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityMiner;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRMining extends TESRBase<TileEntityMiner>{
    public TESRMining(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityMiner tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        int x = 0;
        int y = 0;
        int z = 0;

        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getMasterFacing();
            if (tileEntity.hasDrill())
            {
                render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), x + 0.5, y - 0.9f - tileEntity.getSlide(), z + 0.5, tileEntity.getDrill(), 4.5f, false, tileEntity.getRotation(), 0, 1, 0);
            }

            doTheMath(facing, x, z, 1.99, -1.29);
            renderScreenTexts(matrixStack, facing, xPos, y + 1.4, zPos, tileEntity.getScreenTexts(), 0.1f, 0.004F);

            doTheMath(facing, x, z, 1.96, -1.15);
            renderText(matrixStack, facing, xPos, y + 0.165, zPos, tileEntity.getEnergyText(2), 0.007F);
            doTheMath(facing, x, z, 1.97, -1.15);
            renderBarLevel(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.22, zPos, tileEntity.getEnergyFill(), 0.7f);

            doTheMath(facing, x, z, 1.95, +1.16);
            renderText(matrixStack, facing, xPos, y + 0.18, zPos, tileEntity.getWaterText(1), 0.008F);
            renderText(matrixStack, facing, xPos, y + 0.07, zPos, tileEntity.getWaterText(2), 0.008F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.43, zPos, tileEntity.getWaterFill(), pointer, 0.3F);

            //indicator
            doTheMath(facing, x, z, 1.96, -0.8);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, y + 0.5, zPos, getIndicator(tileEntity.isRunning()), 1f, true);
            //switch
            doTheMath(facing, x, z, 1.96, -0.8);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, y + 0.32, zPos, getSwitch(tileEntity.isRunning()), 1f, true);
        }
        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
