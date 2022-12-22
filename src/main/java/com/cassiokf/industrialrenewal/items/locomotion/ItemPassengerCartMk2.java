package com.cassiokf.industrialrenewal.items.locomotion;


import com.cassiokf.industrialrenewal.entity.EntityPassengerCartMk2;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;


public class ItemPassengerCartMk2 extends IRCartItemBase{
    public ItemPassengerCartMk2() {
        super();
    }

    @Override
    public AbstractMinecart getEntity(Level world, double x, double y, double z) {
        return new EntityPassengerCartMk2(world, x, y, z);
    }
}
