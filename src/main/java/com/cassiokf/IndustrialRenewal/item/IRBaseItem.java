package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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

//    public void registerItemModel(){
//        industrialrenewal.proxy.registerItemRenderer(this, 0, this.name);
//    }

    //    @Override
//    public boolean isRepairable(@Nonnull ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public boolean isEnchantable(ItemStack stack) {
//        return false;
//    }
}
