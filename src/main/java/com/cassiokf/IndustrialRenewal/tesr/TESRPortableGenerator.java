package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityPortableGenerator;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRPortableGenerator extends TESRBase<TileEntityPortableGenerator>{
    public TESRPortableGenerator(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityPortableGenerator tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        int x = 0;
        int y = 0;
        int z = 0;

        Direction facing = tileEntity.getBlockFacing();

        doTheMath(facing, x, z, 1.02, -0.26);
        renderText(matrixStack, facing, xPos, y + 0.514, zPos, tileEntity.getTankText(), 0.005F);
        //renderText(facing, xPos, y + 0.514, zPos, tileEntity.getTankText(), 0.005F);
        renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.68, zPos, tileEntity.getTankFill(), pointer, 0.2F);

        doTheMath(facing, x, z, 1.02, 0.27);
        renderText(matrixStack, facing, xPos, y + 0.514, zPos, tileEntity.getEnergyText(), 0.005F);
        doTheMath(facing, x, z, 1.02, 0.332);
        renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.627, zPos, tileEntity.getEnergyFill(), pointerLong, 0.35F);
        //indicator
        //doTheMath(facing, x, z, 1.02, 0);
        //render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getThisWorld(), xPos, y + 0.77, zPos, getIndicator(tileEntity.isWorking()), 1f, true);
        //switch
        //doTheMath(facing, x, z, 1.02, 0);
        //render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getThisWorld(), xPos, y + 0.62, zPos, getSwitch(tileEntity.isWorking()), 1f, true);

        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
