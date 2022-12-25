package com.cassiokf.industrialrenewal.entity;


import com.cassiokf.industrialrenewal.init.ModEntity;
import com.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityCargoContainer extends AbstractMinecartContainer {
    public EntityCargoContainer(EntityType<?> p_38213_, Level p_38214_) {
        super(p_38213_, p_38214_);
    }

    public EntityCargoContainer(double p_38208_, double p_38209_, double p_38210_, Level p_38211_) {
        super(ModEntity.CARGO_CONTAINER.get(), p_38208_, p_38209_, p_38210_, p_38211_);
    }

    @Override
    public Type getMinecartType() {
        return Type.CHEST;
    }


    @Override
    protected AbstractContainerMenu createMenu(int p_38222_, Inventory p_38223_) {
        return new ChestMenu(MenuType.GENERIC_9x4, p_38222_, p_38223_, this,4);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.CARGO_CART.get());
    }

    @Override
    public int getContainerSize() {
        return 36;
    }

    public void destroy(DamageSource p_94095_1_) {
        this.remove(RemovalReason.KILLED);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.CARGO_CART.get());
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
        }
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
