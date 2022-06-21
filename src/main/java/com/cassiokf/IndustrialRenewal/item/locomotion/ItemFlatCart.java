package com.cassiokf.IndustrialRenewal.item.locomotion;

import com.cassiokf.IndustrialRenewal.entity.EntityFlatCart;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;

public class ItemFlatCart extends IRCartItemBase{
    public ItemFlatCart(String name) {
        super(name);
    }

    @Override
    public AbstractMinecartEntity getEntity(World world, double x, double y, double z) {
        return new EntityFlatCart(world, x, y, z);
    }
}
