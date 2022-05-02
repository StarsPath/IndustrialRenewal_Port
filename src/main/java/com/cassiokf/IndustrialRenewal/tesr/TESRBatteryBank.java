package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.blocks.BlockBatteryBank;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityBatteryBank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRBatteryBank extends TESRBase<TileEntityBatteryBank> {

    public TESRBatteryBank(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(TileEntityBatteryBank tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        int x = 0;
        int y = 0;
        int z = 0;
//        int x = tileEntity.getBlockPos().getX();
//        int y = tileEntity.getBlockPos().getY();
//        int z = tileEntity.getBlockPos().getZ();
        //Direction facing = Utils.intToDir(tileEntity.serializeNBT().getInt("face"));
        //Direction facing = tileEntity.getBlockFacing();
        Direction facing = tileEntity.getBlockState().getValue(BlockBatteryBank.FACING);
        doTheMath(facing, x, z, 1.023, 0);
//        CompoundNBT StoredIR = tileEntity.serializeNBT().getCompound("StoredIR");
//        int energy = StoredIR.getInt("energy");
////        int IROutput = StoredIR.getInt("IROutput");
////        int IRInput = StoredIR.getInt("IRInput");
////        int IRStored = StoredIR.getInt("IRStored");
////        int IRCapacity = StoredIR.getInt("IRCapacity");
//        industrialrenewal.LOGGER.info(StoredIR);
//        industrialrenewal.LOGGER.info("IRStored: " + tileEntity.serializeNBT().getCompound("IRStored").getInt("energy"));
        renderText(matrixStack, facing, xPos, y + 0.43, zPos, tileEntity.getText(), 0.005F);
        renderBarLevel(matrixStack, combinedLightIn, combinedOverlayIn, renderTypeBuffer, facing, xPos, y + 0.49, zPos, tileEntity.getTankFill(), 0.7F);
    }

    //TESRBase
//    @Override
//    public void render(TileEntityBatteryBank te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
//        Direction facing = te.getBlockFacing();
//        doTheMath(facing, x, z, 1.023, 0);
//        renderText(facing, xPos, y + 0.43, zPos, te.GetText(), 0.005F);
//        renderBarLevel(facing, xPos, y + 0.49, zPos, te.getBatteryFill(), 0.7F);
//    }
}
