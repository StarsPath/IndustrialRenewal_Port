package com.cassiokf.IndustrialRenewal.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RotatableBase extends TrainBase{

    public boolean cornerFlip;
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;

    public RotatableBase(EntityType<?> p_i48538_1_, World p_i48538_2_) {
        super(p_i48538_1_, p_i48538_2_);
    }

    public RotatableBase(EntityType<?> p_i48538_1_, World world, double x, double y, double z) {
        super(p_i48538_1_, world, x, y, z);
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
        RailShape railDirection = ((RailBlock) blockState.getBlock()).getRailDirection(blockState, level, pos, this);
        cornerFlip = ((railDirection == RailShape.SOUTH_EAST || railDirection == RailShape.SOUTH_WEST) && this.getDeltaMovement().x < 0.0)
                || ((railDirection == RailShape.NORTH_EAST || railDirection == RailShape.NORTH_WEST) && this.getDeltaMovement().x > 0.0);
    }
}
