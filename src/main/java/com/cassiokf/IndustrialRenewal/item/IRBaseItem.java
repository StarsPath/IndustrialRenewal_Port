package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;


public class IRBaseItem extends Item {
    protected String name;

    public IRBaseItem(String name){
        super(new Properties());
        this.name = name;
        setRegistryName(References.MODID, name);
        industrialrenewal.registeredIRItems.add(this);
    }
    public IRBaseItem(String name, final ItemGroup group) {
        super(new Properties().tab(group));
        this.name = name;
        setRegistryName(References.MODID, name);
        industrialrenewal.registeredIRItems.add(this);
    }

    public IRBaseItem(String name, Properties props) {
        super(props.tab(industrialrenewal.IR_TAB));
        this.name = name;
        setRegistryName(References.MODID, name);
        industrialrenewal.registeredIRItems.add(this);
    }

    public IRBaseItem(String name, final ItemGroup group, Properties props) {
        super(props.tab(group));
        this.name = name;
        setRegistryName(References.MODID, name);
        industrialrenewal.registeredIRItems.add(this);
    }
}
