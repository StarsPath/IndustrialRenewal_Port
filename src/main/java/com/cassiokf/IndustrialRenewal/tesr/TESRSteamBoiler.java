package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntitySteamBoiler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRSteamBoiler extends TESRBase<TileEntitySteamBoiler>{

    private static final ItemStack fire = new ItemStack(ModItems.fire);

    public TESRSteamBoiler(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntitySteamBoiler tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        int x = 0;
        int z = 0;
        int y = 0;

        if (tileEntity!= null && tileEntity.isMaster())
        {
            Direction facing = tileEntity.getMasterFacing();
            //WATER
            doTheMath(facing, x, z, 1.9, -0.69);
            //Utils.debug("Water:", tileEntity.GetWaterFill());
            renderText(matrixStack, facing, xPos, y + 0.25, zPos, tileEntity.getWaterText(), 0.01F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.51, zPos, tileEntity.GetWaterFill(), pointer, 0.3F);
            //STEAM
            doTheMath(facing, x, z, 1.9, 0.69);
            //Utils.debug("Steam:", tileEntity.getSteamText());
            renderText(matrixStack, facing, xPos, y + 0.25, zPos, tileEntity.getSteamText(), 0.01F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.51, zPos, tileEntity.GetSteamFill(), pointer, 0.3F);
            //FUEL
            doTheMath(facing, x, z, 1.9, 0);
            //Utils.debug("Fuel:", tileEntity.getFuelText());
            renderText(matrixStack, facing, xPos, y + 0.18, zPos, tileEntity.getFuelText(), 0.01F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.44, zPos, tileEntity.getFuelFill(), pointer, 0.3F);
            //HEAT
            doTheMath(facing, x, z, 1.9, 0);
            //Utils.debug("Heat:", tileEntity.getHeatText());
            renderText(matrixStack, facing, xPos, y + 0.93, zPos, tileEntity.getHeatText(), 0.01F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 1.19, zPos, tileEntity.getHeatFill(), pointer, 0.3F);
            //Fire
            if (tileEntity.getIntType() > 0 && tileEntity.canRun())
            {
                doTheMath(facing, x, z, 1.9, 0);
                render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, y - 0.7, zPos, fire, 1, true);
            }
        }
        super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
    }
}
