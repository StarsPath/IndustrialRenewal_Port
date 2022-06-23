package com.cassiokf.IndustrialRenewal.entity;

import com.cassiokf.IndustrialRenewal.init.ModEntity;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.util.CouplingHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityFlatCart extends AbstractMinecartEntity {
    public EntityFlatCart(EntityType<EntityFlatCart> p_i48538_1_, World p_i48538_2_) {
        super(p_i48538_1_, p_i48538_2_);
    }

    public EntityFlatCart(World world, double x, double y, double z) {
        super(ModEntity.FLAT_CART.get(), world, x, y, z);
    }

    public void destroy(DamageSource p_94095_1_) {
        this.remove();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.flatCart);
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
        }
    }

    @Override
    public void tick() {
        super.tick();
        CouplingHandler.onMinecartTick(this);
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
        return Type.RIDEABLE;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
