package com.cassiokf.industrialrenewal.items;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blocks.abstracts.IRBaseBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class IRBaseItem extends Item {
    public IRBaseItem(Properties p_41383_) {
        super(p_41383_);
    }

    protected String name;

    public IRBaseItem(){
        super(new Properties());
    }
    public IRBaseItem(String name){
        super(new Properties());
        this.name = name;
        setRegistryName(IndustrialRenewal.MODID, name);
        IndustrialRenewal.registeredIRItems.add(this);
    }
    public IRBaseItem(String name, final CreativeModeTab group) {
        super(new Properties().tab(group));
        this.name = name;
        setRegistryName(IndustrialRenewal.MODID, name);
        IndustrialRenewal.registeredIRItems.add(this);
    }

    public IRBaseItem(String name, Properties props) {
        super(props.tab(IndustrialRenewal.IR_TAB));
        this.name = name;
        setRegistryName(IndustrialRenewal.MODID, name);
        IndustrialRenewal.registeredIRItems.add(this);
    }

    public IRBaseItem(String name, final CreativeModeTab group, Properties props) {
        super(props.tab(group));
        this.name = name;
        setRegistryName(IndustrialRenewal.MODID, name);
        IndustrialRenewal.registeredIRItems.add(this);
    }
}
