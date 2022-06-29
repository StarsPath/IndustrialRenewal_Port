package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityCargoLoader;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class TESRCargoLoader extends TESRBase<TileEntityCargoLoader>{

    private static final ItemStack arm = new ItemStack(ModItems.tambor);

    public TESRCargoLoader(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityCargoLoader tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        double x = 0, y = 0, z = 0;
        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getBlockFacing();

            double armX = x + 0.5;
            double armZ = z + 0.5;
            if (facing == Direction.SOUTH) armZ += 1;
            if (facing == Direction.NORTH) armZ -= 1;
            if (facing == Direction.EAST) armX += 1;
            if (facing == Direction.WEST) armX -= 1;
            //doTheMath(facing, x, z, 0);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), armX, y + 0.2f, armZ, arm, 2.08f, false);

            doTheMath(facing, x, z, 1.03, 0);
            renderText(matrixStack, facing, xPos, y + 0.93, zPos, tileEntity.getModeText(), 0.006F);

            doTheMath(facing, x, z, 1.03, 0);
            renderText(matrixStack, facing, xPos, y + 0.05, zPos, tileEntity.getTankText(), 0.006F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.26, zPos, tileEntity.getCartFluidAngle(), pointer, 0.2F);
        }
    }
}
