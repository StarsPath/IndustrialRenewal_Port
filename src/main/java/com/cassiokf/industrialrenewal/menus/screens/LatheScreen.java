package com.cassiokf.industrialrenewal.menus.screens;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityLathe;
import com.cassiokf.industrialrenewal.menus.ScreenBase;
import com.cassiokf.industrialrenewal.menus.menu.LatheMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class LatheScreen extends ScreenBase<LatheMenu> {

    private final ResourceLocation GUI = new ResourceLocation(IndustrialRenewal.MODID, "textures/gui/container/lathe.png");
    private final BlockEntityLathe tileEntity;
    private final LatheMenu container;

    public LatheScreen(LatheMenu latheContainer, Inventory playerInventory, Component title) {
        super(latheContainer, playerInventory, title);
        this.tileEntity = latheContainer.getTileEntity();
        this.container = latheContainer;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        return;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
//        RenderSystem.clearColor(1f, 1f, 1f, 1f);
//        this.minecraft.getTextureManager().bindForSetup(GUI);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        this.blit(matrixStack, i, j , 0, 0, this.getXSize(), this.getYSize());

//        IEnergyStorage energyStorage = tileEntity.getEnergyStorage();
//        int energyFill = (int) (Utils.normalizeClamped(energyStorage.getEnergyStored(), 0, energyStorage.getMaxEnergyStored()) * 69);
//        Utils.debug("energy", energyStorage.getEnergyStored(), energyFill);
        int energyFill = container.getEnergyFill();
        this.blit(matrixStack, this.getGuiLeft() + 8, (this.getGuiTop() + 78) - energyFill, 176, 0, 16, energyFill);

        int progress = (int) (container.getProgress() * 43);
        this.blit(matrixStack, (this.getGuiLeft() + 81) + progress, this.getGuiTop() + 50, 176, 70, 7, 13);

        ItemStackHandler stack = tileEntity.inputItemHandler.orElse(null);
        if (stack != null)
        {
            ItemStack itemStack = stack.getStackInSlot(0);
            //itemRender.renderItemIntoGUI(stack, this.guiLeft + 96, this.guiTop + 33);
            //itemRenderer.renderAndDecorateFakeItem(stack, this.getGuiLeft() + 96, this.getGuiTop() + 33);
            if(!itemStack.isEmpty())
                itemRenderer.renderGuiItem(itemStack, this.getGuiLeft() + 96, this.getGuiTop() + 33);
        }
    }
}
