package com.cassiokf.industrialrenewal.items.locomotion;

import com.cassiokf.industrialrenewal.entity.EntityPassengerCart;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public class ItemPassengerCart extends IRCartItemBase{
    public ItemPassengerCart() {
        super();
    }

    @Override
    public AbstractMinecart getEntity(Level world, double x, double y, double z) {
        return new EntityPassengerCart(world, x, y, z);
    }
}
