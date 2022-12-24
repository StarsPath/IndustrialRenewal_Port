package com.cassiokf.industrialrenewal.menus.screens;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.menus.ScreenBase;
import com.cassiokf.industrialrenewal.menus.menu.SteamLocomotiveMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class SteamLocomotiveScreen extends ScreenBase<SteamLocomotiveMenu> {

    private final ResourceLocation GUI = new ResourceLocation(IndustrialRenewal.MODID, "textures/gui/container/tender.png");

    private final Entity locomotiveEntity;

    private final SteamLocomotiveMenu locomotiveMenu;

    public SteamLocomotiveScreen(SteamLocomotiveMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        locomotiveMenu = menu;
        locomotiveEntity = menu.getSteamLocomotive();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderLabels(PoseStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        return;
    }


    @Override
    protected void renderBg(PoseStack poseStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
//        super.renderBg(poseStack, p_230450_2_, p_230450_3_, p_230450_4_);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        this.blit(poseStack, i, j , 0, 0, this.getXSize(), this.getYSize());

    }
}
