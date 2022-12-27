package com.cassiokf.industrialrenewal.items.locomotion;


import com.cassiokf.industrialrenewal.entity.EntityPassengerCartMk2;
import com.cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;


public class ItemSteamLocomotive extends IRCartItemBase{
    public ItemSteamLocomotive() {
        super();
    }

    @Override
    public AbstractMinecart getEntity(Level world, double x, double y, double z) {
        return new EntitySteamLocomotive(world, x, y, z);
    }
}
