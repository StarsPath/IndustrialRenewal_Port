package com.cassiokf.industrialrenewal.menus;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ScreenBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public ScreenBase(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void renderBg(PoseStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

    }

    public void drawFluidBar(PoseStack matrixStack, int xOffset, int yOffset, FluidTank tank)
    {
        int percentage = 58 * tank.getFluidAmount() / tank.getCapacity();

        FluidStack fluid = tank.getFluid();

        if (fluid != null)
        {
            renderFluid(matrixStack, getGuiLeft()+xOffset, getGuiTop()+yOffset, 16, 16, percentage, fluid.getFluid());
        }
    }

    protected void renderFluid(PoseStack matrixStack, int guiLeft, int guiTop, int x, int y, int level, Fluid fluid)
    {
        if(fluid == null)
            return;
        matrixStack.pushPose();
        int color = fluid.getAttributes().getColor();
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        GlStateManager._clearColor(r, g, b,1.0F);

        ResourceLocation stillLocation = fluid.getAttributes().getStillTexture();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillLocation);
        ResourceLocation spriteLocation = sprite.getName();
        Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);

        drawTiledTexture(guiLeft, guiTop-level, sprite, 16, level);
        matrixStack.popPose();
    }

    public void drawTiledTexture(int x, int y, TextureAtlasSprite icon, int width, int height) {
        int i;
        int j;

        int drawHeight;
        int drawWidth;

        for (i = 0; i < width; i += 16) {
            for (j = 0; j < height; j += 16) {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                drawScaledTexturedModelRectFromIcon(x + i, y + j, icon, drawWidth, drawHeight);
            }
        }
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void drawScaledTexturedModelRectFromIcon(int x, int y, TextureAtlasSprite icon, int width, int height) {
        if ( icon == null ) {
            return;
        }
        float minU = icon.getU0();
        float maxU = icon.getU1();
        float minV = icon.getV0();
        float maxV = icon.getV1();

        float zLevel = 1.0f;

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        float p_85949_ = minV + (maxV - minV) * height / 16F;
        bufferbuilder.vertex(x, y + height, zLevel).uv(minU, p_85949_).endVertex();
        float p_85948_ = minU + (maxU - minU) * width / 16F;
        bufferbuilder.vertex(x + width, y + height, zLevel).uv(p_85948_, p_85949_).endVertex();
        bufferbuilder.vertex(x + width, y, zLevel).uv(p_85948_, minV).endVertex();
        bufferbuilder.vertex(x, y, zLevel).uv(minU, minV).endVertex();
        bufferbuilder.end();
//        RenderSystem.enableAlphaTest();
        BufferUploader.end(bufferbuilder);
    }
}
