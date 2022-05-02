package com.cassiokf.IndustrialRenewal.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPowerScrewDrive extends IRBaseItem{
    public ItemPowerScrewDrive(String name) {
        super(name);
    }
    public ItemPowerScrewDrive(String name, Properties props) {
        super(name, props.stacksTo(1));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //tooltip.add(new TranslationTextComponent("Energy: " + this.container.getEnergyStored() + " / " + this.container.getMaxEnergyStored()));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }
}
