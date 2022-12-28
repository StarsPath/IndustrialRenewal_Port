package com.cassiokf.industrialrenewal.tesr;


import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityFluidLoader;
import com.cassiokf.industrialrenewal.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class TESRFluidLoader extends TESRBase<BlockEntityFluidLoader>{
    private static final ItemStack arm = new ItemStack(ModItems.fluidLoaderArm.get());

    public TESRFluidLoader(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BlockEntityFluidLoader tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        double x = 0, y = 0, z = 0;
        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getBlockFacing();
            double armX = x + 0.5;
            double armZ = z + 0.5;
            if (facing == Direction.SOUTH) armZ += tileEntity.getSlide();
            if (facing == Direction.NORTH) armZ -= tileEntity.getSlide();
            if (facing == Direction.EAST) armX += tileEntity.getSlide();
            if (facing == Direction.WEST) armX -= tileEntity.getSlide();
            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), armX, y - 0.5f, armZ, arm, 4.5f, false);

            doTheMath(facing, x, z, 1.01, 0);
            renderText(matrixStack, facing, xPos, y + 1.425, zPos, tileEntity.getCartName(), 0.004F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 1.57, zPos, tileEntity.getCartFluidAngle(), pointer, 0.14F);

            doTheMath(facing, x, z, 1.01, 0);
            renderText(matrixStack, facing, xPos, y + 1.05, zPos, tileEntity.getTankText(), 0.004F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 1.2, zPos, tileEntity.getTankFluidAngle(), pointer, 0.14F);
        }
    }
}
