package com.cassiokf.IndustrialRenewal.item.locomotion;

import com.cassiokf.IndustrialRenewal.entity.EntityPassengerCar;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;

public class ItemPassengerCar extends IRCartItemBase{
    public ItemPassengerCar(String name) {
        super(name);
    }

    @Override
    public AbstractMinecartEntity getEntity(World world, double x, double y, double z) {
        return new EntityPassengerCar(world, x, y, z);
    }
}
