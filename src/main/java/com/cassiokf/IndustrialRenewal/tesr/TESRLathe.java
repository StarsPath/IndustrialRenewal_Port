package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityLathe;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.ItemStackHandler;

public class TESRLathe extends TESRBase<TileEntityLathe>{
    public TESRLathe(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityLathe te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        double x = 0, y = 0, z = 0;
        if (te!= null && te.isMaster())
        {
            Direction facing = te.getMasterFacing();
            //Result Screen
            ItemStack result = te.getResultItem();
            if (te.inProcess)
            {
                doTheMath(facing, x, z, 0.97, 1.1);
                String formatted = "" + TextFormatting.GREEN + te.getResultItem().getDisplayName().getString();
                renderText(matrixStack, facing, xPos, y + 1.1, zPos, formatted, 0.005F);
                doTheMath(facing, x, z, 0.97, 1.1);
                render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, te.getLevel(), xPos, y + 1.2, zPos, result, 0.5f, true);
            }
            ItemStackHandler stack = te.inputItemHandler.orElse(null);
            if (stack != null)
            {
                ItemStack itemStack = stack.getStackInSlot(0);
                if(!itemStack.isEmpty()) {
                    doTheMath(facing, x, z, 0.13, 0);
                    render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, te.getLevel(), xPos, y + 1.05, zPos, itemStack, 1, true);
                }
            }
            //Cutter
            //float progress = smoothAnimation(te.getNormalizedProcess(), te.getOldProcess(), partialTicks, false);
            float progress = te.renderCutterProcess;
            doTheMath(facing, x, z, 0.5, 0.05 + progress);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, te.getLevel(), xPos, y - 0.25, zPos, cutter, 4, true);
        }

        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
