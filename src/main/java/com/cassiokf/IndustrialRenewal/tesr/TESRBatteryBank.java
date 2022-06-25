package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.blocks.BlockBatteryBank;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityBatteryBank;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRBatteryBank extends TESRBase<TileEntityBatteryBank> {

    public TESRBatteryBank(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityBatteryBank tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        int x = 0;
        int y = 0;
        int z = 0;

        if(tileEntity!=null){
            Direction facing = tileEntity.getBlockState().getValue(BlockBatteryBank.FACING);
            doTheMath(facing, x, z, 1.023, 0);
            renderText(matrixStack, facing, xPos, y + 0.43, zPos, tileEntity.getText(), 0.005F);
            renderBarLevel(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.49, zPos, tileEntity.getTankFill(), 0.7F);
        }
    }
}
