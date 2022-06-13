package com.cassiokf.IndustrialRenewal.item;

public class ItemWindBlade extends IRBaseItem{

    public static final int MAX_DAMAGE =48 * 60;

    public ItemWindBlade(String name) {
        super(name, new Properties().durability(MAX_DAMAGE));
    }
}
