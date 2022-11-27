package com.cassiokf.industrialrenewal.menus.screens;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityStorageChest;
import com.cassiokf.industrialrenewal.menus.ScreenBase;
import com.cassiokf.industrialrenewal.menus.menu.StorageChestMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

//import com.cassiokf.IndustrialRenewal.network.NetworkHandler;

public class StorageChestScreen extends ScreenBase<StorageChestMenu> {

    private final BlockEntityStorageChest te;
    StorageChestMenu storageChestContainer;
    private Button upB;
    private Button downB;
//    private TextFieldWidget searchField;
    private boolean skip;

    public int actualMouseX;
    public int actualMouseY;

    private final ResourceLocation GUI = new ResourceLocation(IndustrialRenewal.MODID, "textures/gui/container/storage_chest.png");
    public StorageChestScreen(StorageChestMenu screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
        this.storageChestContainer = screenContainer;
        te = screenContainer.getTileEntity();
        this.imageWidth = 220;
        this.imageHeight = 211;
    }

    @Override
    protected void init() {
        super.init();
        //resize(getMinecraft(), 220, 211);
        int posX1 = ((this.width - this.getXSize()) / 2);
        int posY1 = ((this.height - this.getYSize()) / 2);
        upB = new Button(posX1 + 206, posY1 + 15, 10, 18,
            new TextComponent(I18n.get("gui.industrialrenewal.arrowup")), (button)-> {
//                Utils.debug("UP Button Pressed", button);
            storageChestContainer.clickedOn(1);
        });
        downB = new Button(posX1 + 206, posY1 + 105, 10, 18,
            new TextComponent(I18n.get("gui.industrialrenewal.arrowdown")), (button)-> {
//                Utils.debug("DOWN Button Pressed", button);
                storageChestContainer.clickedOn(2);
        });

        addRenderableWidget(upB);
        addRenderableWidget(downB);
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
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
//        RenderSystem.clearColor(1f, 1f, 1f, 1f);
//        this.minecraft.getTextureManager().bindForSetup(GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        this.blit(matrixStack, i, j , 0, 0, this.getXSize(), this.getYSize());
    }
}
