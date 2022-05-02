package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.industrialrenewal;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class IRBlockItem extends BlockItem {
    public IRBlockItem(Block b, Properties props) {
        super(b, props);
    }
    public IRBlockItem(Block b)
    {
        this(b, new Properties().tab(industrialrenewal.IR_TAB));
        setRegistryName(b.getRegistryName());
    }
}
