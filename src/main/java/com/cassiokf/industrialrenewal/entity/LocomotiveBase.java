package com.cassiokf.industrialrenewal.entity;

import com.cassiokf.industrialrenewal.entity.render.RotatableBase;
import com.cassiokf.industrialrenewal.init.ModItems;
import com.cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public abstract class LocomotiveBase extends TrainHeadBase implements ICoupleCart
{
//    private static final DataParameter<Boolean> PLOW = EntityDataManager.createKey(EntitySteamLocomotive.class, DataSerializers.BOOLEAN);
//    public boolean hasPlowItem;
//
//    public ItemStackHandler inventory = new ItemStackHandler(1)
//    {
//        @Override
//        public boolean isItemValid(int slot, ItemStack stack)
//        {
//            return true;
//        }
//
//        @Override
//        protected void onContentsChanged(int slot)
//        {
////            LocomotiveBase.this.sync();
//        }
//    };

//    public LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(()->inventory);

    protected LocomotiveBase(EntityType<?> p_38213_, Level p_38214_) {
        super(p_38213_, p_38214_);
    }

    protected LocomotiveBase(EntityType<?> p_38207_, double p_38208_, double p_38209_, double p_38210_, Level p_38211_) {
        super(p_38207_, p_38208_, p_38209_, p_38210_, p_38211_);
    }


//    public LocomotiveBase(Level worldIn)
//    {
//        super(worldIn);
//    }
//
//    public LocomotiveBase(Level worldIn, double x, double y, double z)
//    {
//        super(worldIn, x, y, z);
//    }

    public void onLocomotiveUpdate()
    {
    }

    public void moveForward()
    {
        Direction cartDir = this.getDirection();
        double acceleration = 0.08D;
        this.setDeltaMovement(this.getDeltaMovement().x + cartDir.getStepX() * acceleration, this.getDeltaMovement().y, this.getDeltaMovement().z + cartDir.getStepZ() * acceleration);
    }

//    public void setTender(EntityTenderBase tender)
//    {
//        this.tender = tender;
//    }

//    private boolean hasPlowItem()
//    {
//        boolean temp = false;
//        ItemStack stack = this.inventory.getStackInSlot(0);
//        if (!stack.isEmpty())
//        {
//            temp = true;
//        }
//        hasPlowItem = temp;
//        return hasPlowItem;
//    }

//    public void horn()
//    {
//        world.playSound(null, getPosition(), IRSoundRegister.TILEENTITY_TRAINHORN, SoundCategory.NEUTRAL, 2F * IRConfig.MainConfig.Sounds.masterVolumeMult, 1F);
//    }

    @Override
    protected double getMaxSpeed()
    {
        return super.getMaxSpeed();
    }

//    @Override
//    public void destroy(DamageSource source)
//    {
//        super.destroy(source);
//
//        if (!source.isExplosion() && this.level.getGameRules().getBoolean("doEntityDrops"))
//        {
//            this.entityDropItem(new ItemStack(ModItems.steamLocomotive, 1), 0.0F);
//        }
//    }
//    @Override
//    public void destroy(DamageSource p_94095_1_) {
//        this.remove(RemovalReason.KILLED);
//        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
//            ItemStack itemstack = new ItemStack(ModItems.STEAM_LOCOMOTIVE.get());
//            if (this.hasCustomName()) {
//                itemstack.setHoverName(this.getCustomName());
//            }
//            this.spawnAtLocation(itemstack);
////            this.spawnAtLocation(inventory.getStackInSlot(0));
//        }
//    }

//    @Override
//    public boolean save(CompoundTag compound) {
//        compound.put("inventory", this.inventory.serializeNBT());
//        return super.save(compound);
//    }
//
//    @Override
//    public void load(CompoundTag compound) {
//        super.load(compound);
//        this.inventory.deserializeNBT(compound.getCompound("inventory"));
////        this.sync();
//    }

//    @Override
//    public void sync()
//    {
//        if (!this.level.isClientSide)
//        {
//            this.dataManager.set(PLOW, hasPlowItem());
//        }
//    }

//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
//        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY?
//                itemHandler.cast() : super.getCapability(capability, facing);
//    }


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

//    @Override
//    protected void entityInit()
//    {
//        super.entityInit();
//        this.getDataManager().register(PLOW, false);
//    }

//    @Override
//    public void notifyDataManagerChange(DataParameter<?> key)
//    {
//        super.notifyDataManagerChange(key);
//        if (this.world.isRemote && key.equals(PLOW))
//        {
//            this.hasPlowItem = this.dataManager.get(PLOW);
//        }
//    }
}
