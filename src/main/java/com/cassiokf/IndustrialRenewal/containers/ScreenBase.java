package com.cassiokf.IndustrialRenewal.containers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ScreenBase<T extends Container> extends ContainerScreen<T> {
    public ScreenBase(T p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

    }

    public void drawFluidBar(MatrixStack matrixStack, int xOffset, int yOffset, FluidTank tank)
    {
        int percentage = 58 * tank.getFluidAmount() / tank.getCapacity();

        FluidStack fluid = tank.getFluid();

        if (fluid != null)
        {
            renderFluid(matrixStack, getGuiLeft()+xOffset, getGuiTop()+yOffset, 16, 16, percentage, fluid.getFluid());
        }
    }

    protected void renderFluid(MatrixStack matrixStack, int guiLeft, int guiTop, int x, int y, int level, Fluid fluid)
    {
        if(fluid == null)
            return;
        matrixStack.pushPose();
        int color = fluid.getAttributes().getColor();
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        GlStateManager._color4f(r, g, b,1.0F);

        ResourceLocation stillLocation = fluid.getAttributes().getStillTexture();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(stillLocation);
        ResourceLocation spriteLocation = sprite.getName();
        Minecraft.getInstance().getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);

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
        GlStateManager._color4f(1.0F, 1.0F, 1.0F, 1.0F);
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

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(x, y + height, zLevel).uv(minU, minV + (maxV - minV) * height / 16F).endVertex();
        bufferbuilder.vertex(x + width, y + height, zLevel).uv(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
        bufferbuilder.vertex(x + width, y, zLevel).uv(minU + (maxU - minU) * width / 16F, minV).endVertex();
        bufferbuilder.vertex(x, y, zLevel).uv(minU, minV).endVertex();
        bufferbuilder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(bufferbuilder);
    }
}
