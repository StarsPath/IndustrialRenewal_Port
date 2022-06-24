package com.cassiokf.IndustrialRenewal.containers.screen;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

//import com.cassiokf.IndustrialRenewal.network.NetworkHandler;

public class StorageChestScreen extends ContainerScreen<StorageChestContainer> {

    private final TileEntityStorageChest te;
    StorageChestContainer storageChestContainer;
    private Button upB;
    private Button downB;
    private TextFieldWidget searchField;
    private boolean skip;

    public int actualMouseX;
    public int actualMouseY;

    private final ResourceLocation GUI = new ResourceLocation(References.MODID, "textures/gui/container/storage_chest.png");
    public StorageChestScreen(StorageChestContainer screenContainer, PlayerInventory inv, ITextComponent title) {
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
            ITextComponent.nullToEmpty(I18n.get("gui.industrialrenewal.arrowup")), (button)-> {
                Utils.debug("UP Button Pressed", button);
            storageChestContainer.clickedOn(1);
        });
        downB = new Button(posX1 + 206, posY1 + 105, 10, 18,
            ITextComponent.nullToEmpty(I18n.get("gui.industrialrenewal.arrowdown")), (button)-> {
                Utils.debug("DOWN Button Pressed", button);
                storageChestContainer.clickedOn(2);
        });

        addButton(upB);
        addButton(downB);
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
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bind(GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        this.blit(matrixStack, i, j , 0, 0, this.getXSize(), this.getYSize());
    }
}
