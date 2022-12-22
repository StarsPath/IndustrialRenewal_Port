package com.cassiokf.industrialrenewal.items.locomotion;

import com.cassiokf.industrialrenewal.entity.EntityFlatCart;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public class ItemFlatCart extends IRCartItemBase{
    public ItemFlatCart() {
        super();
    }

    @Override
    public AbstractMinecart getEntity(Level world, double x, double y, double z) {
        return new EntityFlatCart(world, x, y, z);
    }
}
