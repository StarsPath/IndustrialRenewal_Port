package com.cassiokf.IndustrialRenewal.util.interfaces;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;

public interface ICoupleCart
{
    default float getMaxCouplingDistance(AbstractMinecartEntity cart)
    {
        return 1.0f;
    }

    default float getFixedDistance(AbstractMinecartEntity cart)
    {
        return 0.87f;
    }
}
