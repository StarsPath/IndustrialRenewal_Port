package com.cassiokf.IndustrialRenewal.item.locomotion;

import com.cassiokf.IndustrialRenewal.entity.EntityPassengerCartMk2;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;

public class ItemPassengerCartMk2 extends IRCartItemBase{
    public ItemPassengerCartMk2(String name) {
        super(name);
    }

    @Override
    public AbstractMinecartEntity getEntity(World world, double x, double y, double z) {
        return new EntityPassengerCartMk2(world, x, y, z);
    }
}
