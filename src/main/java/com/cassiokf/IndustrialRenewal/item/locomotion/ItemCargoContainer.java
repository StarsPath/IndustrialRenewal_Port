package com.cassiokf.IndustrialRenewal.item.locomotion;

import com.cassiokf.IndustrialRenewal.entity.EntityCargoContainer;
import com.cassiokf.IndustrialRenewal.init.ModEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.world.World;

public class ItemCargoContainer extends IRCartItemBase{
    public ItemCargoContainer(String name) {
        super(name);
    }

    @Override
    public EntityCargoContainer getEntity(World world, double x, double y, double z) {
        return new EntityCargoContainer(world, x, y, z);
    }
}
