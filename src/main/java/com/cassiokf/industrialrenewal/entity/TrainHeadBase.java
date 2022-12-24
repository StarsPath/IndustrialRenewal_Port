package com.cassiokf.industrialrenewal.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class TrainHeadBase extends AbstractMinecartContainer {

    public boolean cornerFlip;
    protected int wrongRender;
    protected boolean oldRender;
    protected float lastRenderYaw;
    protected double lastMotionX;
    protected double lastMotionZ;

    protected TrainHeadBase(EntityType<?> p_38213_, Level p_38214_) {
        super(p_38213_, p_38214_);
    }

    protected TrainHeadBase(EntityType<?> p_38207_, double p_38208_, double p_38209_, double p_38210_, Level p_38211_) {
        super(p_38207_, p_38208_, p_38209_, p_38210_, p_38211_);
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_38222_, Inventory p_38223_) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public Type getMinecartType() {
        return null;
    }

    public boolean getRenderFlippedYaw(float yaw)
    {
        yaw %= 360.0f;
        if (yaw < 0.0f)
        {
            yaw += 360.0f;
        }
        if (!oldRender || Math.abs(yaw - lastRenderYaw) < 90.0f || Math.abs(yaw - lastRenderYaw) > 270.0f || (xo > 0.0 && lastMotionX < 0.0) || (zo > 0.0 && lastMotionZ < 0.0)
                || (xo < 0.0 && lastMotionX > 0.0) || (zo < 0.0 && lastMotionZ > 0.0) || wrongRender >= 50)
        {
            lastMotionX = xo;
            lastMotionZ = zo;
            lastRenderYaw = yaw;
            oldRender = true;
            wrongRender = 0;
            return false;
        }
        ++wrongRender;
        return true;
    }

    @Override
    protected void moveAlongTrack(BlockPos pos, BlockState blockState) {
        super.moveAlongTrack(pos, blockState);
        Block b = blockState.getBlock();
        if(b instanceof BaseRailBlock) {
            BaseRailBlock railBlock = (BaseRailBlock) b;
            RailShape railDirection = railBlock.getRailDirection(blockState, level, pos, this);
            cornerFlip = ((railDirection == RailShape.SOUTH_EAST || railDirection == RailShape.SOUTH_WEST) && this.getDeltaMovement().x < 0.0)
                    || ((railDirection == RailShape.NORTH_EAST || railDirection == RailShape.NORTH_WEST) && this.getDeltaMovement().x > 0.0);
        }
    }
}
