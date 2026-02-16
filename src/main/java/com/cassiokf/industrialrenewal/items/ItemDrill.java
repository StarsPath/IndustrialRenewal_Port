package com.cassiokf.industrialrenewal.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class ItemDrill extends IRBaseItem{


    public ItemDrill(Properties props) {
        super(props);
    }

    public ItemDrill(final int maxDamage) {
        super(new Properties().stacksTo(1).defaultDurability(maxDamage).setNoRepair());
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

}
