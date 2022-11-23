package com.cassiokf.industrialrenewal.items;


import com.cassiokf.industrialrenewal.IndustrialRenewal;

public class ItemFireBox extends IRBaseItem{
    public int type;

    public ItemFireBox(Properties props, int type)
    {
        super(props);
        this.type = type;
    }
}
