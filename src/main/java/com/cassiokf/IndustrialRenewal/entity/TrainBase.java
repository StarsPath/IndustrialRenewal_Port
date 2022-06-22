package com.cassiokf.IndustrialRenewal.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.world.World;

public abstract class TrainBase extends AbstractMinecartEntity {

    public float length;

    public TrainBase(EntityType<?> p_i48538_1_, World p_i48538_2_) {
        super(p_i48538_1_, p_i48538_2_);
    }

    public TrainBase(EntityType<?> p_i48538_1_, World world, double x, double y, double z) {
        super(p_i48538_1_, world, x, y, z);
    }

    @Override
    public Type getMinecartType() {
        return null;
    }
}
