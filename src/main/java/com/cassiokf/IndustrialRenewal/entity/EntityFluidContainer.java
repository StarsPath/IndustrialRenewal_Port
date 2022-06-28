package com.cassiokf.IndustrialRenewal.entity;

import com.cassiokf.IndustrialRenewal.init.ModEntity;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.util.CouplingHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityFluidContainer extends AbstractMinecartEntity {

    private static final DataParameter<CompoundNBT> FLUID_TAG = EntityDataManager.defineId(EntityFluidContainer.class, DataSerializers.COMPOUND_TAG);
    boolean changed_flag;

    public EntityFluidContainer(EntityType<EntityFluidContainer> p_i48538_1_, World p_i48538_2_) {
        super(p_i48538_1_, p_i48538_2_);
    }

    public EntityFluidContainer(World world, double x, double y, double z) {
        super(ModEntity.FLUID_CONTAINER.get(), world, x, y, z);
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
        CompoundNBT compound = new CompoundNBT();
        ((FluidTank)fluid_handler.orElse(null)).writeToNBT(compound);
        EntityFluidContainer.this.entityData.set(FLUID_TAG, compound);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLUID_TAG, new CompoundNBT());
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        ((FluidTank)fluid_handler.orElse(null)).writeToNBT(compound);
    }


    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
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
        CouplingHandler.onMinecartTick(this);
    }

    public FluidStack getFluidStack() {
        if (!level.isClientSide) return fluid_handler.orElse(null).getFluidInTank(0);
        return ((FluidTank)fluid_handler.orElse(null)).readFromNBT(entityData.get(FLUID_TAG)).getFluid();
    }

    @Override
    public ActionResultType interact(PlayerEntity playerEntity, Hand hand) {
        if(!level.isClientSide){
            boolean acceptFluid = FluidUtil.interactWithFluidHandler(playerEntity, hand, this.fluid_handler.orElse(null));
            if(!acceptFluid)
                playerEntity.sendMessage(new StringTextComponent(I18n.get(getFluidStack().getTranslationKey()) + ": " + getFluidStack().getAmount() + "/40000"), playerEntity.getUUID());
        }
        return ActionResultType.SUCCESS;
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
        this.remove();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.fluidContainer);
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
