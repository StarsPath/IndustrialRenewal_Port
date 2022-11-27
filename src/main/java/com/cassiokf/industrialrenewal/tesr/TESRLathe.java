package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityLathe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TESRLathe extends TESRBase<BlockEntityLathe>{

    public TESRLathe(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityLathe blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;
        if (blockEntity!= null && blockEntity.isMaster())
        {
            Direction facing = blockEntity.getMasterFacing();
            //Result Screen
            ItemStack result = blockEntity.getResultItem();
            if (blockEntity.inProcess)
            {
                doTheMath(facing, x, z, 0.97, 1.1);
                String formatted = "" + ChatFormatting.GREEN + blockEntity.getResultItem().getDisplayName().getString();
                renderText(stack, facing, xPos, y + 1.1, zPos, formatted, 0.005F);
                doTheMath(facing, x, z, 0.97, 1.1);
                render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 1.2, zPos, result, 0.5f, true);
            }
            ItemStackHandler itemStackHandler = blockEntity.inputItemHandler.orElse(null);
            if (itemStackHandler != null)
            {
                ItemStack itemStack = itemStackHandler.getStackInSlot(0);
                if(!itemStack.isEmpty()) {
                    doTheMath(facing, x, z, 0.13, 0);
                    render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 1.05, zPos, itemStack, 1, true);
                }
            }
            //Cutter
            //float progress = smoothAnimation(te.getNormalizedProcess(), te.getOldProcess(), partialTicks, false);
            float progress = blockEntity.renderCutterProcess;
            doTheMath(facing, x, z, 0.5, 0.05 + progress);
            render3dItem(stack, lighting(blockEntity), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y - 0.25, zPos, cutter, 4, true);
        }

        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
