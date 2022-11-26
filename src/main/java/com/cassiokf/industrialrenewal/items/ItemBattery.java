package com.cassiokf.industrialrenewal.items;

import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ItemBattery extends IRBaseItem implements ICapabilityProvider {

    private final CustomEnergyStorage container;
    protected LazyOptional<CustomEnergyStorage> handler;

    public ItemBattery(Properties props, int energy, int io){
        super(props);
        this.container = new CustomEnergyStorage(energy, io, io);
        this.handler = LazyOptional.of(()->this.container);
    }

//    @Override
//    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//        tooltip.add(new TranslationTextComponent("Energy: " + this.container.getEnergyStored() + " / " + this.container.getMaxEnergyStored()));
//        super.appendHoverText(stack, world, tooltip, flagIn);
//    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityEnergy.ENERGY? handler.cast() : null;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return cap == CapabilityEnergy.ENERGY? handler.cast() : null;
    }
}
