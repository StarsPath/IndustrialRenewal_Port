package com.cassiokf.industrialrenewal.entity;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public abstract class TrainBase extends AbstractMinecart {

    public float length;

    protected TrainBase(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    protected TrainBase(EntityType<?> p_38090_, Level p_38091_, double p_38092_, double p_38093_, double p_38094_) {
        super(p_38090_, p_38091_, p_38092_, p_38093_, p_38094_);
    }

//    public TrainBase(EntityType<?> p_i48538_1_, World p_i48538_2_) {
//        super(p_i48538_1_, p_i48538_2_);
//    }
//
//    public TrainBase(EntityType<?> p_i48538_1_, World world, double x, double y, double z) {
//        super(p_i48538_1_, world, x, y, z);
//    }

    @Override
    public Type getMinecartType() {
        return null;
    }
}
