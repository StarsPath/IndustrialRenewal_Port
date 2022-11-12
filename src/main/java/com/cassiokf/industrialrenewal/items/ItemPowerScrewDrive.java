package com.cassiokf.industrialrenewal.items;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class ItemPowerScrewDrive extends IRBaseItem{

    public ItemPowerScrewDrive() {
        super(new Properties().stacksTo(1).tab(IndustrialRenewal.IR_TAB));
    }

    public ItemPowerScrewDrive(Properties props) {
        super(props);
    }
}
