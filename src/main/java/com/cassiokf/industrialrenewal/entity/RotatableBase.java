package com.cassiokf.industrialrenewal.entity;


import com.cassiokf.industrialrenewal.util.CouplingHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class RotatableBase extends AbstractMinecart {

    public boolean cornerFlip;
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;

    protected RotatableBase(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    protected RotatableBase(EntityType<?> p_38090_, Level p_38091_, double p_38092_, double p_38093_, double p_38094_) {
        super(p_38090_, p_38091_, p_38092_, p_38093_, p_38094_);
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
    public void tick() {
        super.tick();
//        CouplingHandler.onMinecartTick(this);
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

    @Override
    public Type getMinecartType() {
        return null;
    }
}
