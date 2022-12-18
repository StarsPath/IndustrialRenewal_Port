package com.cassiokf.industrialrenewal.tesr;


import com.cassiokf.industrialrenewal.blockentity.BlockEntitySolarPanelFrame;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public class TESRSolarPanelFrame extends TESRBase<BlockEntitySolarPanelFrame>{


    public TESRSolarPanelFrame(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntitySolarPanelFrame blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;
        if (blockEntity!=null && blockEntity.hasPanel())
        {
            Direction facing = blockEntity.getBlockFacing();
            doTheMath(facing, x, z, 0.4, 0);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.45f, zPos, blockEntity.getPanel(), 4, false, 22.5f, 1, 0, 0);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }

}
