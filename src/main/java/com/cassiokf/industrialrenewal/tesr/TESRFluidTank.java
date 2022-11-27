package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityFluidTank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class TESRFluidTank extends TESRBase<BlockEntityFluidTank>{

    public TESRFluidTank(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityFluidTank blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;
        if (blockEntity!= null && blockEntity.isMaster() && blockEntity.isBase())
        {
            Direction facing = blockEntity.getMasterFacing();
            doTheMath(facing, x, z, 1.98, 0);
            renderText(stack, facing, xPos, y + 0.36, zPos, blockEntity.getFluidName(), 0.008F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.63, zPos, blockEntity.getFluidAngle(), pointer, 0.3F);
        }
    }
}
