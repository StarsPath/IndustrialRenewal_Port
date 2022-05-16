package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntitySteamTurbine;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRSteamTurbine extends TESRBase<TileEntitySteamTurbine>{

    private static final ItemStack fire = new ItemStack(ModItems.fire);

    public TESRSteamTurbine(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntitySteamTurbine tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        int x = 0;
        int z = 0;
        int y = 0;

        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getMasterFacing();
            //STEAM
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(matrixStack, facing, xPos, y + 1.25, zPos, tileEntity.getSteamText(), 0.01F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer,facing, xPos, y + 1.5, zPos, tileEntity.getSteamFill(), pointer, 0.3F);
            //GENERATION
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(matrixStack, facing, xPos, y + 0.5, zPos, tileEntity.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.95, -0.96);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer,facing, xPos, y + 0.67, zPos, tileEntity.getGenerationFill(), pointerLong, 0.5F);
            //WATER
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(matrixStack, facing, xPos, y - 0.25, zPos, tileEntity.getWaterText(), 0.01F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer,facing, xPos, y + 0.01, zPos, tileEntity.getWaterFill(), pointer, 0.3F);
            //ROTATION
            doTheMath(facing, x, z, 1.95, 0);
            renderText(matrixStack, facing, xPos, y + 1.25, zPos, tileEntity.getRotationText(), 0.01F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer,facing, xPos, y + 1.5, zPos, tileEntity.getRotationFill(), pointer, 0.3F);
            //ENERGY
            doTheMath(facing, x, z, 1.95, +1.165);
            renderText(matrixStack, facing, xPos, y + 0.1, zPos, tileEntity.getEnergyText(), 0.01F);
            doTheMath(facing, x, z, 1.955, +1.165);
            renderBarLevel(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.184, zPos, tileEntity.getEnergyFill(), 1.2F);
        }
        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
