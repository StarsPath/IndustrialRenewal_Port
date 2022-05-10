package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.industrialrenewal;
import net.minecraft.item.Item;

public class ItemFireBox extends IRBaseItem{
    public int type;

    public ItemFireBox(String name, int type)
    {
        super(name, industrialrenewal.IR_TAB);
        this.type = type;
    }
}
