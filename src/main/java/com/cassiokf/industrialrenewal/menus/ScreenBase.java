package com.cassiokf.industrialrenewal.menus;

import com.cassiokf.industrialrenewal.util.Utils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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

//        Utils.debug("TANK", tank.getFluidAmount(), tank.getFluid().getFluid());
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
        RenderSystem.setShaderColor(r, g, b, 1.0f);

//        GlStateManager._clearColor(r, g, b,1.0F);

        ResourceLocation stillLocation = fluid.getAttributes().getStillTexture();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillLocation);
//        ResourceLocation spriteLocation = sprite.getName();
//        Minecraft.getInstance().getTextureManager().bindForSetup(InventoryMenu.BLOCK_ATLAS);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        int yCount = level / 16;
        int yRemainder = level % 16;

        for (int i = 1; i <= yCount; i++) {
            blit(matrixStack, guiLeft, guiTop - 16 * i, 0, 16, 16, sprite);
        }
        blit(matrixStack, guiLeft, guiTop - 16 * yCount - yRemainder, 0, 16, yRemainder, sprite);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        matrixStack.popPose();
    }
}
