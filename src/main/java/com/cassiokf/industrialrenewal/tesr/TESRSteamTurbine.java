package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.BlockEntitySteamTurbine;
import com.cassiokf.industrialrenewal.blocks.BlockSteamBoiler;
import com.cassiokf.industrialrenewal.blocks.BlockSteamTurbine;
import com.cassiokf.industrialrenewal.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRSteamTurbine extends TESRBase<BlockEntitySteamTurbine>{

    private static final ItemStack fire = new ItemStack(ModItems.fire.get());

    public TESRSteamTurbine(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntitySteamTurbine blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        int x = 0;
        int z = 0;
        int y = 0;

        if (blockEntity!= null)
        {
            Direction facing = blockEntity.getBlockState().getValue(BlockSteamTurbine.FACING);
            //STEAM
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(stack, facing, xPos, y + 1.25, zPos, blockEntity.getSteamText(), 0.01F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer,facing, xPos, y + 1.5, zPos, blockEntity.getSteamFill(), pointer, 0.3F);
            //GENERATION
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(stack, facing, xPos, y + 0.5, zPos, blockEntity.getGenerationText(), 0.01F);
            doTheMath(facing, x, z, 1.95, -0.96);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer,facing, xPos, y + 0.67, zPos, blockEntity.getGenerationFill(), pointerLong, 0.5F);
            //WATER
            doTheMath(facing, x, z, 1.95, -1.1);
            renderText(stack, facing, xPos, y - 0.25, zPos, blockEntity.getWaterText(), 0.01F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer,facing, xPos, y + 0.01, zPos, blockEntity.getWaterFill(), pointer, 0.3F);
            //ROTATION
            doTheMath(facing, x, z, 1.95, 0);
            renderText(stack, facing, xPos, y + 1.25, zPos, blockEntity.getRotationText(), 0.01F);
            renderPointer(stack, lighting(blockEntity), combinedOverlay, buffer,facing, xPos, y + 1.5, zPos, blockEntity.getRotationFill(), pointer, 0.3F);
            //ENERGY
            doTheMath(facing, x, z, 1.95, +1.165);
            renderText(stack, facing, xPos, y + 0.1, zPos, blockEntity.getEnergyText(), 0.01F);
            doTheMath(facing, x, z, 1.95, +1.165);
            renderBarLevel(stack, lighting(blockEntity), combinedOverlay, buffer, facing, xPos, y + 0.184, zPos, blockEntity.getEnergyFill(), 1.2F);
        }
        super.render(blockEntity, partialTick, stack, buffer, combinedOverlay, packedLight);
    }
}
