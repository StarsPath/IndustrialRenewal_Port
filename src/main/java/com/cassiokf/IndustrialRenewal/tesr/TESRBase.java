package com.cassiokf.IndustrialRenewal.tesr;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
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
    public void render(T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

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

        RenderHelper.turnOff();
        rotateAccordingly(facing, matrixStack);

        matrixStack.mulPose(new Quaternion(180, 0, 0, true));

        matrixStack.scale(scale, scale, 1f);
        FontRenderer fontRenderer = Minecraft.getInstance().font;
        float xh = centerText ? (-(float)fontRenderer.width(st) / 2) : 0;
        fontRenderer.draw(matrixStack, st, xh, 0, TextFormatting.BLUE.getColor());

        RenderHelper.turnBackOn();
        matrixStack.popPose();
    }

    public static void renderBarLevel(MatrixStack matrixStack, int combinedLightIn, int combinedOverlayIn, IRenderTypeBuffer buffetIn, Direction facing, double x, double y, double z, float fill, float scale)
    {
        matrixStack.pushPose();
        matrixStack.translate(x,y,z);
        rotateAccordingly(facing, matrixStack);
        matrixStack.scale(scale, fill * scale, 0.05f);

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
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);

        rotateAccordingly(facing, matrixStack);
        matrixStack.scale(scale, scale, scale);

        if (rotateHorizontal) matrixStack.mulPose(new Quaternion(90, 0, 0, true));
        if (rotateVertical) matrixStack.mulPose(new Quaternion(0, 90, 0, true));
        if (applyRotation) matrixStack.mulPose(new Quaternion(rotation * rX, rotation * rY, rotation * rZ, true));

        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, world, null);
        model = ForgeHooksClient.handleCameraTransforms(matrixStack, model, ItemCameraTransforms.TransformType.GROUND, false);

        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack, buffetIn, combinedLightIn, combinedOverlayIn, model);

        matrixStack.popPose();
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
                matrixStack.mulPose(new Quaternion(0, 180, 0, true));
                break;
            case NORTH:
                break;
            case WEST:
                matrixStack.mulPose(new Quaternion(0, 90, 0, true));
                break;
            case EAST:
                matrixStack.mulPose(new Quaternion(0, -90, 0, true));
                break;
        }
    }
}
