package com.cassiokf.industrialrenewal.tesr;


import com.cassiokf.industrialrenewal.blockentity.BlockEntityTransformer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class TESRTransformerHV extends TESRBase<BlockEntityTransformer>{

    public TESRTransformerHV(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityTransformer blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;

        if (blockEntity!=null && blockEntity.isMaster())
        {
//            Utils.debug("GENERATION", tileEntity.getGenerationFill());
            Direction facing = blockEntity.getMasterFacingDirect();
            //GENERATION
            doTheMath(facing, x, z, 1.86, 0);
            renderText(stack, facing, xPos, y + 0.16, zPos, blockEntity.getGenerationText(), 0.008F);
            doTheMath(facing, x, z, 1.84, 0.13);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.36, zPos, blockEntity.getGenerationFill(), pointerLong, 0.5F);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
