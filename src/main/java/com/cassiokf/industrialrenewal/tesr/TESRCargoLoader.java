package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityCargoLoader;
import com.cassiokf.industrialrenewal.init.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class TESRCargoLoader extends TESRBase<BlockEntityCargoLoader>{

    private static final ItemStack arm = new ItemStack(ModItems.tambor.get());

    public TESRCargoLoader(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(BlockEntityCargoLoader tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        //super.render(tileEntity, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn);
        double x = 0, y = 0, z = 0;
        if (tileEntity.isMaster())
        {
            Direction facing = tileEntity.getBlockFacing();

            double armX = x + 0.5;
            double armZ = z + 0.5;
            if (facing == Direction.SOUTH) armZ += 1;
            if (facing == Direction.NORTH) armZ -= 1;
            if (facing == Direction.EAST) armX += 1;
            if (facing == Direction.WEST) armX -= 1;
            //doTheMath(facing, x, z, 0);
//            render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, tileEntity.getLevel(), armX, y + 0.2f, armZ, arm, 2.08f, false);

            doTheMath(facing, x, z, 1.03, 0);
            renderText(matrixStack, facing, xPos, y + 0.93, zPos, tileEntity.getModeText(), 0.006F);

            doTheMath(facing, x, z, 1.03, 0);
            renderText(matrixStack, facing, xPos, y + 0.05, zPos, tileEntity.getTankText(), 0.006F);
            renderPointer(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.26, zPos, tileEntity.getCartFluidAngle(), pointer, 0.2F);
        }
    }
}
