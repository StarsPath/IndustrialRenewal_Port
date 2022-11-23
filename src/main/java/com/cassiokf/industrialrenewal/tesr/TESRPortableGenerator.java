package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityPortableGenerator;
import com.cassiokf.industrialrenewal.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRPortableGenerator extends TESRBase<BlockEntityPortableGenerator>{


    public TESRPortableGenerator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityPortableGenerator blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0;
        int y = 0;
        int z = 0;

        if(blockEntity!= null){
            Direction facing = blockEntity.getBlockFacing();

            doTheMath(facing, x, z, 1.02, -0.26);
            renderText(stack, facing, xPos, y + 0.514, zPos, blockEntity.getTankText(), 0.005F);
            //renderText(facing, xPos, y + 0.514, zPos, tileEntity.getTankText(), 0.005F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.68, zPos, blockEntity.getTankFill(), pointer, 0.2F);

            doTheMath(facing, x, z, 1.02, 0.27);
            renderText(stack, facing, xPos, y + 0.514, zPos, blockEntity.getEnergyText(), 0.005F);
            doTheMath(facing, x, z, 1.02, 0.332);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.627, zPos, blockEntity.getEnergyFill(), pointerLong, 0.35F);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
