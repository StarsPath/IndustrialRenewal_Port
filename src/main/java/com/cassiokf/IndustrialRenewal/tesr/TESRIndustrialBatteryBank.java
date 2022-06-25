package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityIndustrialBatteryBank;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class TESRIndustrialBatteryBank extends TESRBase<TileEntityIndustrialBatteryBank>{

    private static final ItemStack lBattery = new ItemStack(ModItems.battery_lithium);

    public TESRIndustrialBatteryBank(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityIndustrialBatteryBank te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        double x = 0;
        double y = 0;
        double z = 0;

        if (te != null && te.isMaster())
        {
            Direction facing = te.getMasterFacing();
            if (te!=null && te.isMaster() && te.isBase())
            {
                doTheMath(facing, x, z, 1.97, -0.586);
                renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.486, zPos, te.getInPutAngle(), pointerLong, 0.6F);
                doTheMath(facing, x, z, 1.97, -0.72);
                renderText(matrixStack, facing, xPos, y + 0.21, zPos, te.getInPutText(), 0.008F);
                render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, te.getLevel(), xPos, y + 0.96f, zPos, label_5, 1.6f, false);
                renderText(matrixStack, facing, xPos, y + 0.984f, zPos, te.getInPutIndicatorText(), 0.008F);

                doTheMath(facing, x, z, 1.98, 0);
                renderText(matrixStack, facing, xPos, y + 1.0, zPos, te.getEnergyText(), 0.006F);
                renderBarLevel(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 1.14, zPos, te.getBatteryFill(), 1.2F);

                doTheMath(facing, x, z, 1.97, 0.846f);
                renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.486, zPos, te.getOutPutAngle(), pointerLong, 0.6F);
                doTheMath(facing, x, z, 1.97, 0.72f);
                renderText(matrixStack, facing, xPos, y + 0.21, zPos, te.getOutPutText(), 0.008F);
                render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, te.getLevel(), xPos, y + 0.96f, zPos, label_5, 1.6f, false);
                renderText(matrixStack, facing, xPos, y + 0.984f, zPos, te.getOutPutIndicatorText(), 0.008F);
            }
            int quantity = te.getBatteries();
            renderBatteries(matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn, te.getLevel(), quantity, facing, x, y, z);
        }
        super.render(te, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }

    private void renderBatteries(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn, World world, int quantity, Direction facing, Double x, Double y, Double z)
    {
        if (quantity > 0)
        {
            float offset = 1.3f;
            float spacing = 0.83f;
            float yOffset = 0.46f;
            float ySpacing = 0.67f;

            //Left Side
            float yOff = yOffset;
            for (int r = 0; r < 4; r++)
            {
                float off = offset;
                for (int zb = 0; zb < 3; zb++)
                {
                    doTheMath(facing, x, z, off, -0.29f);
                    render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, world, xPos, y - yOff, zPos, lBattery, 1.7f, false, true, -90, 1, 0, 0, false, true);
                    off -= spacing;
                    quantity--;
                    if (quantity == 0) return;
                }
                yOff -= ySpacing;
            }
            //Right Side
            yOff = yOffset;
            for (int r = 0; r < 4; r++)
            {
                float off = offset;
                for (int zb = 0; zb < 3; zb++)
                {
                    doTheMath(facing, x, z, off, 0.29f);
                    render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, world, xPos, y - yOff, zPos, lBattery, 1.7f, false, true, 90, 1, 0, 0, false, true);
                    off -= spacing;
                    quantity--;
                    if (quantity == 0) return;
                }
                yOff -= ySpacing;
            }
        }
    }
}
