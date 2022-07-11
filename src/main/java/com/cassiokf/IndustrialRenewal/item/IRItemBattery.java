package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class IRItemBattery extends IRBaseItem implements ICapabilityProvider {

    private final CustomEnergyStorage container;
    protected LazyOptional<CustomEnergyStorage> handler;

    public IRItemBattery(final String name, ItemGroup group){
        super(name, group, new Properties().stacksTo(1));
        this.container = new CustomEnergyStorage(10000, 1000, 1000);
        this.handler = LazyOptional.of(()->this.container);
    }

//    @Override
//    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//        tooltip.add(new TranslationTextComponent("Energy: " + this.container.getEnergyStored() + " / " + this.container.getMaxEnergyStored()));
//        super.appendHoverText(stack, world, tooltip, flagIn);
//    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        if(cap == CapabilityEnergy.ENERGY)
//            return this.handler.cast();
        return null;
    }
}
