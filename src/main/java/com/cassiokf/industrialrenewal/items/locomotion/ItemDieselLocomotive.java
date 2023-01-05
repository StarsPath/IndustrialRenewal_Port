package com.cassiokf.industrialrenewal.items.locomotion;


import com.cassiokf.industrialrenewal.entity.EntityDieselLocomotive;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;


public class ItemDieselLocomotive extends IRCartItemBase{
    public ItemDieselLocomotive() {
        super();
    }

    @Override
    public AbstractMinecart getEntity(Level world, double x, double y, double z) {
        return new EntityDieselLocomotive(world, x, y, z);
    }
}
