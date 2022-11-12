package com.cassiokf.industrialrenewal.items;

import com.cassiokf.industrialrenewal.IndustrialRenewal;

public class ItemWindBlade extends IRBaseItem{

    public static final int MAX_DAMAGE =48 * 60;

    public ItemWindBlade() {
        super(new Properties().durability(MAX_DAMAGE).tab(IndustrialRenewal.IR_TAB));
    }

    public ItemWindBlade(Properties props) {
        super(props);
    }

}
