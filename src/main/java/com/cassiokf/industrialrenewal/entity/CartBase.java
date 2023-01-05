package com.cassiokf.industrialrenewal.entity;

import com.cassiokf.industrialrenewal.util.CouplingHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;

public class CartBase extends AbstractMinecart {
    protected CartBase(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    protected CartBase(EntityType<?> p_38090_, Level p_38091_, double p_38092_, double p_38093_, double p_38094_) {
        super(p_38090_, p_38091_, p_38092_, p_38093_, p_38094_);
    }

    @Override
    public Type getMinecartType() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        CouplingHandler.onMinecartTick(this);
    }
}
