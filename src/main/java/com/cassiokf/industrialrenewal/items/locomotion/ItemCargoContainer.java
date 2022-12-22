package com.cassiokf.industrialrenewal.items.locomotion;


import com.cassiokf.industrialrenewal.entity.EntityCargoContainer;
import net.minecraft.world.level.Level;

public class ItemCargoContainer extends IRCartItemBase{
    public ItemCargoContainer() {
        super();
    }

    @Override
    public EntityCargoContainer getEntity(Level world, double x, double y, double z) {
        return new EntityCargoContainer(x, y, z, world);
    }
}
