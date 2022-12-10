package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamTurbine;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class TESRDamTurbine extends TESRBase<BlockEntityDamTurbine>{

    public TESRDamTurbine(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityDamTurbine blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;
        if (blockEntity.isMaster())
        {
            Direction facing = blockEntity.getMasterFacing();
            doTheMath(facing, x, z, 1.98, 0);
            renderText(stack, facing, xPos, y + 0.36, zPos, blockEntity.getRotationText(), 0.008F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.61, zPos, blockEntity.getRotationFill(), pointer, 0.3F);
        }
//        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
