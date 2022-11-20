package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityConveyorBase;
import com.cassiokf.industrialrenewal.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class TESRConveyor extends TESRBase<BlockEntityConveyorBase>{


    public TESRConveyor(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityConveyorBase blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        double x = 0, y = 0, z = 0;

        ItemStack stack1 = blockEntity.getStackInSlot(0);
        ItemStack stack2 = blockEntity.getStackInSlot(1);
        ItemStack stack3 = blockEntity.getStackInSlot(2);
        Direction facing = blockEntity.getBlockFacing();
        int mode = blockEntity.getMode();

//        Utils.debug("light", Utils.getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), packedLight);

        if (!stack3.isEmpty())
        {
//            float offset = tileEntity.getStackOffset(2, false);
//            float oldOffset = tileEntity.getStackOffset(2, true);
//            if (offset < 0.2f) oldOffset = 0;
//            float stack3Progress = smoothAnimation(offset, oldOffset, partialTicks, false);
            double stack3Progress = blockEntity.stack3Pos;
            doTheMath(facing, x, z, 1 - (1 * stack3Progress), 0);
            render3dItem(stack, Utils.getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, (y + blockEntity.getMinYOffset(2, mode)) + (blockEntity.getMaxYOffset(mode) * stack3Progress), zPos, stack3, 1, false, 90, 1, 0, 0);
        }
        if (!stack2.isEmpty())
        {
            double stack2Pos = blockEntity.stack2Pos;
            //float stack2Pos = smoothAnimation(tileEntity.getStackOffset(1, false), tileEntity.getStackOffset(1, true), partialTicks, false);
            doTheMath(facing, x, z, 1 - (1 * stack2Pos), 0);
            render3dItem(stack, Utils.getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, (y + blockEntity.getMinYOffset(1, mode)) + (blockEntity.getMaxYOffset(mode) * stack2Pos), zPos, stack2, 1, false, 90, 1, 0, 0);
        }
        if (!stack1.isEmpty())
        {
            double stack1Pos = blockEntity.stack1Pos;
//            float stack1Pos = smoothAnimation(tileEntity.getStackOffset(0, false), tileEntity.getStackOffset(0, true), partialTicks, false);
            doTheMath(facing, x, z, 1 - (1 * stack1Pos), 0);
            render3dItem(stack, Utils.getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, (y + blockEntity.getMinYOffset(0, mode)) + (blockEntity.getMaxYOffset(mode) * stack1Pos), zPos, stack1, 1, false, 90, 1, 0, 0);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
