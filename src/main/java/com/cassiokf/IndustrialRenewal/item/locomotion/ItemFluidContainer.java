package com.cassiokf.IndustrialRenewal.item.locomotion;

import com.cassiokf.IndustrialRenewal.entity.EntityFluidContainer;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;

public class ItemFluidContainer extends IRCartItemBase{
    public ItemFluidContainer(String name) {
        super(name);
    }

    @Override
    public AbstractMinecartEntity getEntity(World world, double x, double y, double z) {
        return new EntityFluidContainer(world, x, y, z);
    }
}
