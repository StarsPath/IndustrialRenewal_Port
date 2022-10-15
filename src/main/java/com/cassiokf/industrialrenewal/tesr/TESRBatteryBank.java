package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityBatteryBank;
import com.cassiokf.industrialrenewal.blocks.BlockBatteryBank;
import com.cassiokf.industrialrenewal.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRBatteryBank extends TESRBase<BlockEntityBatteryBank> {

    public TESRBatteryBank(BlockEntityRendererProvider.Context context){
        super(context);
    }

    @Override
    public void render(BlockEntityBatteryBank blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0;
        int y = 0;
        int z = 0;

        if(blockEntity!=null){
            Direction facing = blockEntity.getBlockState().getValue(BlockBatteryBank.FACING);
            doTheMath(facing, x, z, 1.023, 0);
            renderText(stack, facing, xPos, y + 0.43, zPos, blockEntity.getText(), 0.005F);
//            Utils.debug("RENDERING", blockEntity.getTankFill());
            renderBarLevel(stack, packedLight, combinedOverlay, buffer, facing, xPos, y + 0.49, zPos, blockEntity.getTankFill(), 0.7F);
        }
    }
}
