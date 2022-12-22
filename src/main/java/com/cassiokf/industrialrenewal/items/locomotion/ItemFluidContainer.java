package com.cassiokf.industrialrenewal.items.locomotion;

import com.cassiokf.industrialrenewal.entity.EntityFluidContainer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public class ItemFluidContainer extends IRCartItemBase{
    public ItemFluidContainer() {
        super();
    }

    @Override
    public AbstractMinecart getEntity(Level world, double x, double y, double z) {
        return new EntityFluidContainer(world, x, y, z);
    }
}
