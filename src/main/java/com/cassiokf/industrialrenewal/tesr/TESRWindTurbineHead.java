package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbineHead;
import com.cassiokf.industrialrenewal.init.ModItems;
import com.cassiokf.industrialrenewal.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRWindTurbineHead extends TESRBase<BlockEntityWindTurbineHead>{

    private static final ItemStack blade = new ItemStack(ModItems.WIND_BLADE.get());

    public TESRWindTurbineHead(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityWindTurbineHead blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0;
        int y = 0;
        int z = 0;

        if(blockEntity!=null && blockEntity.hasBlade()){
            Direction facing = blockEntity.getBlockFacing();
            doTheMath(facing, x, z, 0, 0);
            float rotation = smoothAnimation(blockEntity.getRotation(), blockEntity.getRotation(), partialTick, true);
            render3dItem(stack, Utils.getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), combinedOverlay, buffer, facing, blockEntity.getLevel(), xPos, y + 0.5f, zPos, blade, 12, false, true, rotation, 0, 1, 0, true, false);
        }
    }
}
