package com.cassiokf.industrialrenewal.entity;

import com.cassiokf.industrialrenewal.init.ModItems;
import com.cassiokf.industrialrenewal.util.Utils;
import com.cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class LocomotiveBase extends AbstractMinecartContainer implements ICoupleCart
{

    public boolean cornerFlip;
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;

    private int tick;

    public Direction directionOverride = Direction.UP;

    protected LocomotiveBase(EntityType<?> p_38213_, Level p_38214_) {
        super(p_38213_, p_38214_);
    }

    protected LocomotiveBase(EntityType<?> p_38207_, double p_38208_, double p_38209_, double p_38210_, Level p_38211_) {
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

    public void onLocomotiveUpdate()
    {
    }

    @Override
    public void tick() {
        super.tick();
        if(directionOverride != null){
            tick++;
            if(tick >= 10){
                tick = 0;
                directionOverride = Direction.UP;
            }
        }
    }

    public void moveForward()
    {
        if(!level.isClientSide){
            double acceleration = 0.1D;
            if (directionOverride != Direction.UP) {
                setYRot(directionOverride.toYRot()+90);
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
//                double yaw = getYRot() * Math.PI / 180D;
//                double xMovement = this.getDeltaMovement().x + acceleration * Math.cos(yaw);
//                double zMovement = this.getDeltaMovement().z + acceleration * Math.sin(yaw);
//                this.setDeltaMovement(xMovement, this.getDeltaMovement().y, zMovement);
//                return;
            }

            double yaw = getYRot() * Math.PI / 180D;
//            Utils.debug("YAW", getYRot(), directionOverride);

            double xMovement = this.getDeltaMovement().x + acceleration * Math.cos(yaw);
            double zMovement = this.getDeltaMovement().z + acceleration * Math.sin(yaw);
            this.setDeltaMovement(xMovement, this.getDeltaMovement().y, zMovement);
        }
    }

//    public void horn()
//    {
//        world.playSound(null, getPosition(), IRSoundRegister.TILEENTITY_TRAINHORN, SoundCategory.NEUTRAL, 2F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1F);
//    }


    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if(!level.isClientSide){
            if(hand == InteractionHand.MAIN_HAND && player.getMainHandItem().is(ModItems.SCREW_DRIVE.get())){
                Utils.debug("RIGHT CLICKED WITH SCREW DRIVER", this.getYRot());
                this.setYRot(this.getYRot() + 180f);
                Utils.debug("", this.getYRot());
            }
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    @Override
    protected double getMaxSpeed()
    {
        return super.getMaxSpeed();
    }


    @Override
    public Type getMinecartType() {
        return Type.FURNACE;
    }


    @Override
    public float getMaxCouplingDistance(AbstractMinecart cart)
    {
        return 2.0f;
    }

    @Override
    public float getFixedDistance(AbstractMinecart cart)
    {
        return 1.6f;
    }


    @Override
    public double getDragAir() {
        return 0.5f;
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
        if(b instanceof RailBlock railBlock) {
            RailShape railDirection = railBlock.getRailDirection(blockState, level, pos, this);
//            cornerFlip = ((railDirection == RailShape.SOUTH_EAST || railDirection == RailShape.SOUTH_WEST) && this.getDeltaMovement().x < 0.0)
//                    || ((railDirection == RailShape.NORTH_EAST || railDirection == RailShape.NORTH_WEST) && this.getDeltaMovement().x > 0.0);
            cornerFlip = (railDirection == RailShape.SOUTH_EAST || railDirection == RailShape.SOUTH_WEST || railDirection == RailShape.NORTH_WEST);
//            if(cornerFlip)
//                Utils.debug("SHOULD CORNER FLIP", railDirection);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("corner_flip", cornerFlip);
        tag.putInt("dir", directionOverride.get3DDataValue());
        super.addAdditionalSaveData(tag);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        cornerFlip = tag.getBoolean("corner_flip");
        directionOverride = Direction.from3DDataValue(tag.getInt("dir"));
        super.readAdditionalSaveData(tag);
    }
}
