package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityFluidLoader;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class TESRFluidLoader extends TESRBase<TileEntityFluidLoader>{
    private static final ItemStack arm = new ItemStack(ModItems.fluidLoaderArm);

    public TESRFluidLoader(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityFluidLoader tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        double x = 0, y = 0, z = 0;
        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getBlockFacing();
            double armX = x + 0.5;
            double armZ = z + 0.5;
            if (facing == Direction.SOUTH) armZ += tileEntity.getSlide();
            if (facing == Direction.NORTH) armZ -= tileEntity.getSlide();
            if (facing == Direction.EAST) armX += tileEntity.getSlide();
            if (facing == Direction.WEST) armX -= tileEntity.getSlide();
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), armX, y - 0.5f, armZ, arm, 4.5f, false);

            doTheMath(facing, x, z, 1.01, 0);
            renderText(matrixStack, facing, xPos, y + 1.425, zPos, tileEntity.getCartName(), 0.004F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 1.57, zPos, tileEntity.getCartFluidAngle(), pointer, 0.14F);

            doTheMath(facing, x, z, 1.01, 0);
            renderText(matrixStack, facing, xPos, y + 1.05, zPos, tileEntity.getTankText(), 0.004F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 1.2, zPos, tileEntity.getTankFluidAngle(), pointer, 0.14F);
        }
    }
}
