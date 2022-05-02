package com.cassiokf.IndustrialRenewal.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class IRItemCartridge extends IRBaseItem{
    public IRItemCartridge(String name) {
        super(name);
    }
    public IRItemCartridge(String name, ItemGroup group) {
        super(name, group);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip.industrialrenewal.cartridge"));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}
