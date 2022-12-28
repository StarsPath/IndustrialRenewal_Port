package com.cassiokf.industrialrenewal.menus.screens;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityBaseLoader;
import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityFluidLoader;
import com.cassiokf.industrialrenewal.init.PacketHandler;
import com.cassiokf.industrialrenewal.menus.ScreenBase;
import com.cassiokf.industrialrenewal.menus.menu.FluidLoaderMenu;
import com.cassiokf.industrialrenewal.network.ServerBoundLoaderPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

//public class FluidLoaderScreen extends ContainerScreen<FluidLoaderContainer> {
public class FluidLoaderScreen extends ScreenBase<FluidLoaderMenu> {

    private final ResourceLocation GUI = new ResourceLocation(IndustrialRenewal.MODID, "textures/gui/container/fluidloader.png");

    private Button B1;
    private Button B2;

    private FluidLoaderMenu container;
    private BlockEntityFluidLoader tileEntity;
    private boolean unload;
    private BlockEntityBaseLoader.waitEnum waitE;

    public FluidLoaderScreen(FluidLoaderMenu fluidLoaderContainer, Inventory playerInventory, Component textComponent) {
        super(fluidLoaderContainer, playerInventory, textComponent);
        this.container = fluidLoaderContainer;
        this.tileEntity = fluidLoaderContainer.getTileEntity();
        this.unload = tileEntity.unload;
        this.waitE = tileEntity.waitE;
    }

    private String getGUIButtonText()
    {
        String s;
        switch (waitE)
        {
            case WAIT_FULL:
                s = I18n.get("gui.industrialrenewal.button.waitfull");
                break;
            case WAIT_EMPTY:
                s = I18n.get("gui.industrialrenewal.button.waitempty");
                break;
            case NO_ACTIVITY:
                s = I18n.get("gui.industrialrenewal.button.noactivity");
                break;
            default:
            case NEVER:
                s = I18n.get("gui.industrialrenewal.button.never");
                break;
        }
        return s;
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
//        this.buttonList.add(new GuiButton(0, posX1 + 7, posY1 + 53, 61, 18, ""));
//        this.buttonList.add(new GuiButton(1, posX1 + 7, posY1 + 18, 52, 18, ""));
        B1 = new Button(posX1 + 7, posY1 + 53, 61, 18,
                new TextComponent(getGUIButtonText()),
                (button)-> {
//                    Utils.debug("Cycle Wait mode B1 Pressed", button);
//                tileEntity.waitE = TileEntityBaseLoader.waitEnum.cycle(tileEntity.waitE);
                    waitE = BlockEntityBaseLoader.waitEnum.cycle(waitE);
                    tileEntity.waitE = waitE;
                    PacketHandler.INSTANCE.sendToServer(new ServerBoundLoaderPacket(tileEntity.getBlockPos(), 2));
                    button.setMessage(new TextComponent(getGUIButtonText()));

                },
                (button, matrixStack, mouseX, mouseY)->{
                    onToolTip(matrixStack, mouseX, mouseY);
                }
        );
        B2 = new Button(posX1 + 7, posY1 + 18, 52, 18,
                new TextComponent(getGUIModeText()),
                (button)-> {
//                    Utils.debug("Setting load/unload B2 Pressed", button);
                    unload = !unload;
                    PacketHandler.INSTANCE.sendToServer(new ServerBoundLoaderPacket(tileEntity.getBlockPos(), 1));
                    button.setMessage(new TextComponent(getGUIModeText()));
                    //unload = tileEntity.unload;
                }
        );

        addRenderableWidget(B1);
        addRenderableWidget(B2);
    }

    public void onToolTip(PoseStack matrixStack, int actualMouseX, int actualMouseY){
        String waitE = getGUIButtonText();
        String mode = getGUIModeText();

        TextComponent text = new TextComponent(ChatFormatting.GRAY + I18n.get("gui.industrialrenewal.button.cargoloaderbutton0") + " " + ChatFormatting.DARK_GREEN + waitE);
        this.renderTooltip(matrixStack, text, actualMouseX, actualMouseY);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        drawFluidBar(matrixStack, 80, 79, tileEntity.tank);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        this.blit(matrixStack, i, j , 0, 0, this.getXSize(), this.getYSize());
    }
}
