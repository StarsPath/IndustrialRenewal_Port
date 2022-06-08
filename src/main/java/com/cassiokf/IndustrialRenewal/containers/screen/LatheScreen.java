package com.cassiokf.IndustrialRenewal.containers.screen;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.containers.container.LatheContainer;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityLathe;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class LatheScreen extends ContainerScreen<LatheContainer> {

    private final ResourceLocation GUI = new ResourceLocation(References.MODID, "textures/gui/container/lathe.png");
    private TileEntityLathe tileEntity;
    private LatheContainer container;

    public LatheScreen(LatheContainer latheContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(latheContainer, playerInventory, title);
        this.tileEntity = latheContainer.getTileEntity();
        this.container = latheContainer;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        return;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
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
