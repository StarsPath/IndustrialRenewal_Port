package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityConveyor;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityConveyorBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class TESRConveyor extends TESRBase<TileEntityConveyorBase>{
    public TESRConveyor(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityConveyorBase tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        double x = 0, y = 0, z = 0;

        ItemStack stack1 = tileEntity.getStackInSlot(0);
        ItemStack stack2 = tileEntity.getStackInSlot(1);
        ItemStack stack3 = tileEntity.getStackInSlot(2);
        Direction facing = tileEntity.getBlockFacing();
        int mode = tileEntity.getMode();

        if (!stack3.isEmpty())
        {
//            float offset = tileEntity.getStackOffset(2, false);
//            float oldOffset = tileEntity.getStackOffset(2, true);
//            if (offset < 0.2f) oldOffset = 0;
//            float stack3Progress = smoothAnimation(offset, oldOffset, partialTicks, false);
            double stack3Progress = tileEntity.stack3Pos;
            doTheMath(facing, x, z, 1 - (1 * stack3Progress), 0);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, (y + tileEntity.getMinYOffset(2, mode)) + (tileEntity.getMaxYOffset(mode) * stack3Progress), zPos, stack3, 1, false, 90, 1, 0, 0);
        }
        if (!stack2.isEmpty())
        {
            double stack2Pos = tileEntity.stack2Pos;
            //float stack2Pos = smoothAnimation(tileEntity.getStackOffset(1, false), tileEntity.getStackOffset(1, true), partialTicks, false);
            doTheMath(facing, x, z, 1 - (1 * stack2Pos), 0);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, (y + tileEntity.getMinYOffset(1, mode)) + (tileEntity.getMaxYOffset(mode) * stack2Pos), zPos, stack2, 1, false, 90, 1, 0, 0);
        }
        if (!stack1.isEmpty())
        {
            double stack1Pos = tileEntity.stack1Pos;
//            float stack1Pos = smoothAnimation(tileEntity.getStackOffset(0, false), tileEntity.getStackOffset(0, true), partialTicks, false);
            doTheMath(facing, x, z, 1 - (1 * stack1Pos), 0);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, (y + tileEntity.getMinYOffset(0, mode)) + (tileEntity.getMaxYOffset(mode) * stack1Pos), zPos, stack1, 1, false, 90, 1, 0, 0);
        }
        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
