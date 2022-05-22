package com.cassiokf.IndustrialRenewal.containers.screen;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
//import com.cassiokf.IndustrialRenewal.network.NetworkHandler;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

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

//    private void sendToServer(int id)
//    {
//        te.guiButtonClick(id, null);
//        NetworkHandler.sendToServer(new PacketReturnTEStorageChest(this.te, id, mc.player.getEntityId()));
//    }

//    public void scrollPressed(boolean up)
//    {
//        if (up && upB.active) sendToServer(1);
//        else if (!up && downB.active) sendToServer(2);
//    }

    @Override
    protected void init() {
        super.init();
        //resize(getMinecraft(), 220, 211);
        int posX1 = ((this.width - this.getXSize()) / 2);
        int posY1 = ((this.height - this.getYSize()) / 2);
        upB = new Button(posX1 + 206, posY1 + 15, 10, 18,
            ITextComponent.nullToEmpty("UP"), (button)-> {
                Utils.debug("UP Button Pressed", button);
            storageChestContainer.clickedOn(1);
                //te.guiButtonClick(1, null);
                //storageChestContainer.drawContainer(te.inventory, te, 1);
                //sendToServer(1);
        });
        downB = new Button(posX1 + 206, posY1 + 105, 10, 18,
            ITextComponent.nullToEmpty("DN"), (button)-> {
                Utils.debug("DOWN Button Pressed", button);
                storageChestContainer.clickedOn(2);
                //te.guiButtonClick(2, null);
                //storageChestContainer.drawContainer(te.inventory, te, 2);
                //sendToServer(2);
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

//    public static void open() {
//        Minecraft.getInstance().setScreen(new StorageChestScreen());
//    }
}
