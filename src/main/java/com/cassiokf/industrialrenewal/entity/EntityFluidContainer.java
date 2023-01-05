package com.cassiokf.industrialrenewal.entity;


import com.cassiokf.industrialrenewal.init.ModEntity;
import com.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.NetworkHooks;

public class EntityFluidContainer extends CartBase {

    private static final EntityDataAccessor<CompoundTag> FLUID_TAG = SynchedEntityData.defineId(EntityFluidContainer.class, EntityDataSerializers.COMPOUND_TAG);
    boolean changed_flag;

    public EntityFluidContainer(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    public EntityFluidContainer(Level p_38091_, double p_38092_, double p_38093_, double p_38094_) {
        super(ModEntity.FLUID_CONTAINER.get(), p_38091_, p_38092_, p_38093_, p_38094_);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.FLUID_CART.get());
    }

    public class CartTank extends FluidTank {
        public CartTank(int capacity) {
            super(capacity);
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            if (level != null && !EntityFluidContainer.this.level.isClientSide) {
                updateSynchedData();
            }
        }
    }

    LazyOptional<IFluidHandler> fluid_handler = LazyOptional.of(() -> new CartTank(40000));

    protected void updateSynchedData() {
        CompoundTag compound = new CompoundTag();
        ((FluidTank)fluid_handler.orElse(null)).writeToNBT(compound);
        EntityFluidContainer.this.entityData.set(FLUID_TAG, compound);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLUID_TAG, new CompoundTag());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        ((FluidTank)fluid_handler.orElse(null)).writeToNBT(compound);
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        FluidTank tank = ((FluidTank)fluid_handler.resolve().get());
        tank.setFluid(tank.readFromNBT(compound).getFluid());
        changed_flag = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (changed_flag && !level.isClientSide) {
            updateSynchedData();
            changed_flag = false;
        }
//        CouplingHandler.onMinecartTick(this);
    }

    public FluidStack getFluidStack() {
        if (!level.isClientSide) return fluid_handler.orElse(null).getFluidInTank(0);
        return ((FluidTank)fluid_handler.orElse(null)).readFromNBT(entityData.get(FLUID_TAG)).getFluid();
    }


    @Override
    public InteractionResult interact(Player playerEntity, InteractionHand hand) {
        if(!level.isClientSide){
            boolean acceptFluid = FluidUtil.interactWithFluidHandler(playerEntity, hand, this.fluid_handler.orElse(null));
            if(!acceptFluid)
                playerEntity.sendMessage(new TextComponent(I18n.get(getFluidStack().getTranslationKey()) + ": " + getFluidStack().getAmount() + "/40000"), playerEntity.getUUID());
        }
        return InteractionResult.SUCCESS;
        //return super.interact(p_184230_1_, p_184230_2_);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluid_handler.cast();
        }
        return super.getCapability(cap);
    }

    public void destroy(DamageSource p_94095_1_) {
        this.remove(RemovalReason.KILLED);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.FLUID_CART.get());
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
        }
    }

//    @Override
//    protected double getMaxSpeed() {
//        return 0.20d;
////        return super.getMaxSpeed();
//    }
//
//    @Override
//    public float getMaxCartSpeedOnRail() {
//        return 0.20f;
//    }

    @Override
    public Type getMinecartType() {
        return Type.CHEST;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
