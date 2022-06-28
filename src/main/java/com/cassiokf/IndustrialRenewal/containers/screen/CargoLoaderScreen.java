package com.cassiokf.IndustrialRenewal.containers.screen;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.containers.container.CargoLoaderContainer;
import com.cassiokf.IndustrialRenewal.init.PacketHandler;
import com.cassiokf.IndustrialRenewal.network.ServerBoundLoaderPacket;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityBaseLoader;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityCargoLoader;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CargoLoaderScreen extends ContainerScreen<CargoLoaderContainer> {

    private final ResourceLocation GUI = new ResourceLocation(References.MODID, "textures/gui/container/cargoloader.png");

    private Button B1;
    private Button B2;

    private final TileEntityCargoLoader tileEntity;
    private final CargoLoaderContainer container;
    private boolean unload;
    private TileEntityBaseLoader.waitEnum waitE;

    public CargoLoaderScreen(CargoLoaderContainer cargoLoaderContainer, PlayerInventory playerInventory, ITextComponent textComponent) {
        super(cargoLoaderContainer, playerInventory, textComponent);
        this.container = cargoLoaderContainer;
        this.tileEntity = cargoLoaderContainer.getTileEntity();
        this.unload = tileEntity.unload;
        this.waitE = tileEntity.waitE;
    }

    private String getGUIButtonText()
    {
        String msg;
        switch (waitE)
        {
            case WAIT_FULL:
                msg = I18n.get("gui.industrialrenewal.button.waitfull");
                break;
            case WAIT_EMPTY:
                msg = I18n.get("gui.industrialrenewal.button.waitempty");
                break;
            case NO_ACTIVITY:
                msg = I18n.get("gui.industrialrenewal.button.noactivity");
                break;
            default:
            case NEVER:
                msg = I18n.get("gui.industrialrenewal.button.never");
                break;
        }
        return msg;
    }

    private String getGUIModeText()
    {
        if (unload) return I18n.get("gui.industrialrenewal.button.unloader_mode");
        return I18n.get("gui.industrialrenewal.button.loader_mode");
    }

    @Override
    protected void init() {
        super.init();
        int posX1 = ((this.width - this.getXSize()) / 2);
        int posY1 = ((this.height - this.getYSize()) / 2);
        B1 = new Button(posX1 + 7, posY1 + 53, 61, 18,
                ITextComponent.nullToEmpty(getGUIButtonText()),
            (button)-> {
                Utils.debug("Cycle Wait mode B1 Pressed", button);
                waitE = TileEntityBaseLoader.waitEnum.cycle(waitE);
                tileEntity.waitE = waitE;
                PacketHandler.INSTANCE.sendToServer(new ServerBoundLoaderPacket(tileEntity.getBlockPos(), 2));
                button.setMessage(ITextComponent.nullToEmpty(getGUIButtonText()));

            },
            (button, matrixStack, mouseX, mouseY)->{
                onToolTip(matrixStack, mouseX, mouseY);
            }
        );
        B2 = new Button(posX1 + 7, posY1 + 18, 52, 18,
                ITextComponent.nullToEmpty(getGUIModeText()),
            (button)-> {
                Utils.debug("Setting load/unload B2 Pressed", button);
                unload = !unload;
                PacketHandler.INSTANCE.sendToServer(new ServerBoundLoaderPacket(tileEntity.getBlockPos(), 1));
                button.setMessage(ITextComponent.nullToEmpty(getGUIModeText()));
            }
        );

        addButton(B1);
        addButton(B2);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public void onToolTip(MatrixStack matrixStack, int actualMouseX, int actualMouseY){
        String waitE = getGUIButtonText();
        String mode = getGUIModeText();

        ITextComponent text = new StringTextComponent(TextFormatting.GRAY + I18n.get("gui.industrialrenewal.button.cargoloaderbutton0") + " " + TextFormatting.DARK_GREEN + waitE);
        this.renderTooltip(matrixStack, text, actualMouseX, actualMouseY);
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
    }
}
