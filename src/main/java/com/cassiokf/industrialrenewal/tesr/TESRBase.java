package com.cassiokf.industrialrenewal.tesr;

import com.cassiokf.industrialrenewal.init.ModItems;
import com.cassiokf.industrialrenewal.util.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public abstract class TESRBase <T extends BlockEntity> implements BlockEntityRenderer<T> {

    public static final ItemStack cutter = new ItemStack(ModItems.cutter.get());
    public static final ItemStack energyBarLevel = new ItemStack(ModItems.barLevel.get());
    public static final ItemStack pointerLong = new ItemStack(ModItems.pointerLong.get());
    public static final ItemStack pointer = new ItemStack(ModItems.pointer.get());
    public static final ItemStack limiter = new ItemStack(ModItems.limiter.get());
    public static final ItemStack indicator_on = new ItemStack(ModItems.indicator_on.get());
    public static final ItemStack indicator_off = new ItemStack(ModItems.indicator_off.get());
    public static final ItemStack switch_on = new ItemStack(ModItems.switch_on.get());
    public static final ItemStack switch_off = new ItemStack(ModItems.switch_off.get());
    public static final ItemStack push_button = new ItemStack(ModItems.push_button.get());
    public static final ItemStack label_5 = new ItemStack(ModItems.label_5.get());


    public double xPos = 0D;
    public double zPos = 0D;

    @Override
    public void render(T blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {

    }

    public TESRBase(BlockEntityRendererProvider.Context context){

    }

    public static void renderScreenTexts(PoseStack matrixStack, Direction facing, double x, double y, double z, String[] text, float spacing, float scale)
    {
        double lY = y;
        for (String line : text)
        {
            renderText(matrixStack, facing, x, lY, z, Color.GREEN.getRGB() + line, scale, false);
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

    public static void renderText(PoseStack matrixStack, Direction facing, double x, double y, double z, String st, float scale)
    {
        renderText(matrixStack, facing, x, y, z, st, scale, true);
    }

    private static void renderText(PoseStack matrixStack, Direction facing, double x, double y, double z, String st, float scale, boolean centerText)
    {
        matrixStack.pushPose();
        matrixStack.translate(x,y,z);

//        RenderHelper.turnOff();
        rotateAccordingly(facing, matrixStack);

        matrixStack.mulPose(new Quaternion(180, 0, 0, true));

        matrixStack.scale(scale, scale, 1f);
        Font font = Minecraft.getInstance().font;
//        FontRenderer fontRenderer = Minecraft.getInstance().font;
        float xh = centerText ? (-(float)font.width(st) / 2) : 0;
        font.draw(matrixStack, st, xh, 0, Color.CYAN.getRGB());

//        RenderHelper.turnBackOn();
        matrixStack.popPose();
    }

    public static void renderBarLevel(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, double x, double y, double z, float fill, float scale)
    {
        matrixStack.pushPose();
        matrixStack.translate(x,y,z+0.007);
        rotateAccordingly(facing, matrixStack);
        matrixStack.scale(scale, fill * scale, 0.05f);

        combinedLightIn = combinedOverlayIn;
        Minecraft.getInstance().getItemRenderer().renderStatic(energyBarLevel, ItemTransforms.TransformType.GUI, combinedLightIn, combinedOverlayIn, matrixStack, buffetIn, 0);
        matrixStack.popPose();
    }

    public static void renderPointer(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, double x, double y, double z, float angle, ItemStack pointer, float scale)
    {
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);
        rotateAccordingly(facing, matrixStack);
        matrixStack.mulPose(new Quaternion(0, 0, 90, true));
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(new Quaternion(0, 0, -angle, true));
        Minecraft.getInstance().getItemRenderer().renderStatic(pointer, ItemTransforms.TransformType.GUI, combinedLightIn, combinedOverlayIn, matrixStack, buffetIn, 0);
        matrixStack.popPose();
    }

    public static void render3dItem(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, Level world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight)
    {
        render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, buffetIn, facing, world, x, y, z, stack, scale, disableLight, false, 0, 0, 0, 0, false, false);
    }

    public static void render3dItem(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, Level world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, float rotation, float rX, float rY, float rZ)
    {
        render3dItem(matrixStack, combinedLightIn, combinedOverlayIn, buffetIn, facing, world, x, y, z, stack, scale, disableLight, true, rotation, rX, rY, rZ, false, false);
    }

    public static void render3dItem(PoseStack matrixStack, int combinedLightIn, int combinedOverlayIn, MultiBufferSource buffetIn, Direction facing, Level world, double x, double y, double z, ItemStack stack, float scale, boolean disableLight, boolean applyRotation, float rotation, float rX, float rY, float rZ, boolean rotateHorizontal, boolean rotateVertical)
    {
        matrixStack.pushPose();
        matrixStack.translate(x, y, z);

        rotateAccordingly(facing, matrixStack);
        matrixStack.scale(scale, scale, scale);

        if (rotateHorizontal) matrixStack.mulPose(new Quaternion(90, 0, 0, true));
        if (rotateVertical) matrixStack.mulPose(new Quaternion(0, 90, 0, true));
        if (applyRotation) matrixStack.mulPose(new Quaternion(rotation * rX, rotation * rY, rotation * rZ, true));

        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, world, null, 0);
        model = ForgeHooksClient.handleCameraTransforms(matrixStack, model, ItemTransforms.TransformType.GROUND, false);

        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, matrixStack, buffetIn, combinedLightIn, combinedOverlayIn, model);

        matrixStack.popPose();
    }

    public static float smoothAnimation(float rotation, float oldRotation, float partialTick, boolean invert)
    {
        //shift = shiftOld + (shift - shiftOld) * partialTick
        float r = oldRotation + (rotation - oldRotation) * partialTick;
        return invert ? -r : r;
    }

    public static void rotateAccordingly(Direction facing, PoseStack matrixStack)
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
