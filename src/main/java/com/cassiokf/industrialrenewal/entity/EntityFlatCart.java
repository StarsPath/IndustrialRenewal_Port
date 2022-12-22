package com.cassiokf.industrialrenewal.entity;


import com.cassiokf.industrialrenewal.init.ModEntity;
import com.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityFlatCart extends AbstractMinecart {
    public EntityFlatCart(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    public EntityFlatCart(Level world, double x, double y, double z) {
        super(ModEntity.FLAT_CART.get(), world, x, y, z);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.FLAT_CART.get());
    }

    @Override
    public void destroy(DamageSource p_38115_) {
        this.remove(RemovalReason.KILLED);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.FLAT_CART.get());
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
        }
        super.destroy(p_38115_);
    }

//    @Override
//    public void tick() {
//        super.tick();
//        CouplingHandler.onMinecartTick(this);
//    }

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
        return Type.RIDEABLE;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //    @Override
//    public IPacket<?> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
}
