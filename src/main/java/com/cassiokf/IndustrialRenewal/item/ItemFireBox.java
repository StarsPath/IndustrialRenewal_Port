package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.industrialrenewal;

public class ItemFireBox extends IRBaseItem{
    public int type;

    public ItemFireBox(String name, int type)
    {
        super(name, industrialrenewal.IR_TAB);
        this.type = type;
    }
}
