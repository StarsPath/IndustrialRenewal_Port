package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

@OnlyIn(Dist.CLIENT)
public abstract class TESRBase <T extends TileEntity> extends TileEntityRenderer<T> {

    public static final ItemStack cutter = new ItemStack(ModItems.cutter);
    public static final ItemStack energyBarLevel = new ItemStack(ModItems.barLevel);
    public static final ItemStack pointerLong = new ItemStack(ModItems.pointerLong);
    public static final ItemStack pointer = new ItemStack(ModItems.pointer);
    public static final ItemStack limiter = new ItemStack(ModItems.limiter);
    public static final ItemStack indicator_on = new ItemStack(ModItems.indicator_on);
    public static final ItemStack indicator_off = new ItemStack(ModItems.indicator_off);
    public static final ItemStack switch_on = new ItemStack(ModItems.switch_on);
    public static final ItemStack switch_off = new ItemStack(ModItems.switch_off);
    public static final ItemStack push_button = new ItemStack(ModItems.push_button);
    public static final ItemStack label_5 = new ItemStack(ModItems.label_5);


    public double xPos = 0D;
    public double zPos = 0D;

    public TESRBase(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(T p_225616_1_, float p_225616_2_, MatrixStack p_225616_3_, IRenderTypeBuffer p_225616_4_, int p_225616_5_, int p_225616_6_) {

    }

    public static void renderScreenTexts(MatrixStack matrixStack, Direction facing, double x, double y, double z, String[] text, float spacing, float scale)
    {
        double lY = y;
        for (String line : text)
        {
            renderText(matrixStack, facing, x, lY, z, TextFormatting.GREEN + line, scale, false);
            lY -= spacing;
        }
    }

    public void doTheMath(Direction facing, double x, double z, double offset, double sidePlus)
    {
        switch (facing)
        {
            case SOUTH:
                xPos = x + (0.5 - sidePlus);
                zPos = z + (1 - offset);
                return;
            case NORTH:
                xPos = x + (0.5 + sidePlus);
                zPos = z + offset;
                return;
            case EAST:
                xPos = x + (1 - offset);
                zPos = z + (0.5 + sidePlus);
                return;
            case WEST:
                xPos = x + offset;
                zPos = z + (0.5 - sidePlus);
        }
    }

    public static ItemStack getIndicator(boolean value)
    {
        return value ? indicator_on : indicator_off;
    }

    public static ItemStack getSwitch(boolean value)
    {
        return value ? switch_on : switch_off;
    }

    public static void renderText(MatrixStack matrixStack, Direction facing, double x, double y, double z, String st, float scale)
    {
        renderText(matrixStack, facing, x, y, z, st, scale, true);
    }

    private static void renderText(MatrixStack matrixStack, Direction facing, double x, double y, double z, String st, float scale, boolean centerText)
    {
        matrixStack.pushPose();
        matrixStack.translate(x,y,z);
        //RenderHelper.disableStandardItemLighting();
        RenderHelper.turnOff();
        rotateAccordingly(facing, matrixStack);
        //matrixStack.mulPose(new Quaternion(180, 1, 0, 0));
        matrixStack.mulPose(new Quaternion(180, 0, 0, true));
        //GlStateManager._rotatef(180, 1, 0, 0);
        matrixStack.scale(scale, scale, 1f);
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        float xh = centerText ? (-(float)fontRenderer.width(st) / 2) : 0;
        fontRenderer.draw(matrixStack, st, xh, 0, TextFormatting.BLUE.getColor());
        //RenderHelper.enableStandardItemLighting();
        RenderHelper.turnBackOn();
        matrixStack.popPose();
    }

    public static void renderBarLevel(MatrixStack matrixStack, int combinedLightIn, int combinedOverlayIn, IRenderTypeBuffer buffetIn, Direction facing, double x, double y, double z, float fill, float scale)
    {
        matrixStack.pushPose();
        matrixStack.translate(x,y,z);
        rotateAccordingly(facing, matrixStack);
        matrixStack.scale(scale, fill * scale, 0.05f);
        //stack, TransformType.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn
        //Minecraft.getInstance().getItemRenderer().renderGuiItem(energyBarLevel, combinedLightIn, OverlayTexture.NO_OVERLAY);
        RenderHelper.turnOff();
        Minecraft.getInstance().getItemRenderer().renderStatic(energyBarLevel, ItemCameraTransforms.TransformType.GUI, combinedLightIn, combinedOverlayIn, matrixStack, buffetIn);
        RenderHelper.turnBackOn();
        matrixStack.popPose();
    }

    public static void renderPointer(MatrixStack matrixStack, int combinedLightIn, int combinedOverlayIn, IRenderTypeBuffer buffetIn, Direction facing, double x, double y, double z, float angle, ItemStack pointer, float scale)
    {
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        rotateAccordingly(facing, matrixStack);
        matrixStack.mulPose(new Quaternion(0, 0, 90, true));
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(new Quaternion(0, 0, -angle, true));
        Minecraft.getInstance().getItemRenderer().renderStatic(pointer, ItemCameraTransforms.TransformType.GUI, combinedLightIn, combinedOverlayIn, matrixStack, buffetIn);
        matrixStack.popPose();


        //GlStateManager.pushMatrix();
        //GlStateManager.translate(x, y, z);
        //rotateAccordingly(facing);
        //GlStateManager.rotate(90, 0, 0, 1);
        //GlStateManager.scale(scale, scale, scale);
        //GlStateManager.rotate(-angle, 0, 0, 1);
        //Minecraft.getMinecraft().getRenderItem().renderItem(pointer, ItemCameraTransforms.TransformType.GUI);
        //GlStateManager.popMatrix();
    }

    public static void render3dItem(MatrixStack matrixStack, int combinedLightIn, int combinedOverlayIn, IRenderTypeBuffer buffetIn, Direction facing, World world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight)
    {
        render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, buffetIn, facing, world, x, y, z, stack, scale, disableLight, false, 0, 0, 0, 0, false, false);
    }

    public static void render3dItem(MatrixStack matrixStack, int combinedLightIn, int combinedOverlayIn, IRenderTypeBuffer buffetIn, Direction facing, World world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, float rotation, float rX, float rY, float rZ)
    {
        render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, buffetIn, facing, world, x, y, z, stack, scale, disableLight, true, rotation, rX, rY, rZ, false, false);
    }

    public static void render3dItem(MatrixStack matrixStack, int combinedLightIn, int combinedOverlayIn, IRenderTypeBuffer buffetIn, Direction facing, World world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, boolean applyRotation, float rotation, float rX, float rY, float rZ, boolean rotateHorizontal, boolean rotateVertical)
    {
//        GlStateManager.enableRescaleNormal();
//        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
//        GlStateManager.enableBlend();
//        if (disableLight) RenderHelper.disableStandardItemLighting();
//        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
//        GlStateManager.pushMatrix();


        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        //GlStateManager.translate(x, y, z);
        rotateAccordingly(facing, matrixStack);
        matrixStack.scale(scale, scale, scale);
        //RenderHelper.turnOff();

//        GlStateManager.scale(scale, scale, scale);
//        if (rotateHorizontal) GlStateManager.rotate(90, 1, 0, 0);
//        if (rotateVertical) GlStateManager.rotate(90, 0, 1, 0);
//        if (applyRotation) GlStateManager.rotate(rotation, rX, rY, rZ);
        //matrixStack.mulPose(new Quaternion(180, 0, 0, true));

        if (rotateHorizontal) matrixStack.mulPose(new Quaternion(90, 0, 0, true));
        if (rotateVertical) matrixStack.mulPose(new Quaternion(0, 90, 0, true));
        if (applyRotation) matrixStack.mulPose(new Quaternion(rotation * rX, rotation * rY, rotation * rZ, true));

        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, world, null);
        model = ForgeHooksClient.handleCameraTransforms(matrixStack, model, ItemCameraTransforms.TransformType.GROUND, false);

        //Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        //Minecraft.getInstance().getRenderItem().renderItem(stack, model);
        //Minecraft.getInstance().getTextureManager().getTexture(new ResourceLocation(References.MODID, "textures/block"));
        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack, buffetIn, combinedLightIn, combinedOverlayIn, model);

        //RenderHelper.turnBackOn();
        matrixStack.popPose();

//        GlStateManager.popMatrix();
//        GlStateManager.disableRescaleNormal();
//        if (disableLight) RenderHelper.enableStandardItemLighting();
//        GlStateManager.disableBlend();
    }

    public static float smoothAnimation(float rotation, float oldRotation, float partialTick, boolean invert)
    {
        //shift = shiftOld + (shift - shiftOld) * partialTick
        float r = oldRotation + (rotation - oldRotation) * partialTick;
        return invert ? -r : r;
    }

    public static void rotateAccordingly(Direction facing, MatrixStack matrixStack)
    {
        switch (facing)
        {
            default:
            case SOUTH:
                //GlStateManager._rotatef(180F, 0, 1, 0);
                matrixStack.mulPose(new Quaternion(0, 180, 0, true));
                break;
            case NORTH:
                break;
            case WEST:
                //GlStateManager._rotatef(90F, 0, 1, 0);
                matrixStack.mulPose(new Quaternion(0, 90, 0, true));
                break;
            case EAST:
                //GlStateManager._rotatef(-90F, 0, 1, 0);
                matrixStack.mulPose(new Quaternion(0, -90, 0, true));
                break;
        }
    }
}
