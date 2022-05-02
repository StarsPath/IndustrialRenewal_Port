package com.cassiokf.IndustrialRenewal.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class IRItemDrill extends IRBaseItem{

    private final int maxDamage;

    public IRItemDrill(String name, ItemGroup group, final int maxDamage) {
        super(name, group, new Properties().stacksTo(1).defaultDurability(maxDamage).setNoRepair());
        this.maxDamage = maxDamage;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

}
