package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWindTurbineHead;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRWindTurbineHead extends TESRBase<TileEntityWindTurbineHead>{

    private static final ItemStack blade = new ItemStack(ModItems.windBlade);

    public TESRWindTurbineHead(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityWindTurbineHead tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        int x = 0;
        int y = 0;
        int z = 0;

        if(tileEntity.hasBlade()){
            Direction facing = tileEntity.getBlockFacing();
            doTheMath(facing, x, z, 0, 0);
            float rotation = smoothAnimation(tileEntity.getRotation(), tileEntity.getRotation(), partialTicks, true);
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), xPos, y + 0.5f, zPos, blade, 12, false, true, rotation, 0, 1, 0, true, false);
        }
    }
}
