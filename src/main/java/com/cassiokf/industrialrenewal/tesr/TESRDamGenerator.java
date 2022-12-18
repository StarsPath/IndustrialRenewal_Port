package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamGenerator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class TESRDamGenerator extends TESRBase<BlockEntityDamGenerator>{

    public TESRDamGenerator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityDamGenerator blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
//        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
        double x = 0, y = 0, z = 0;
        if (blockEntity!=null && blockEntity.isMaster())
        {
            Direction facing = blockEntity.getMasterFacing();
            //GENERATION
            doTheMath(facing, x, z, 1.98, 0);
            renderText(stack, facing, xPos, y + 0.43, zPos, blockEntity.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.98, 0.115);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.58, zPos, blockEntity.getGenerationFill(), pointerLong, 0.5F);
        }
    }
}
