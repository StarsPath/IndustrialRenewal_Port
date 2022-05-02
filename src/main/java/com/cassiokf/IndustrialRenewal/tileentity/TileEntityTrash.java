package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TEBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTrash extends TEBase {
    public TileEntityTrash(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public TileEntityTrash(){
        super(ModTileEntities.TRASH_TILE.get());
    }


    public static final FluidTank tank = new FluidTank(64000){
        @Override
        public int fill(FluidStack resource, FluidAction action) {
            //return super.fill(resource, action);
            return resource != null? resource.getAmount(): 0;
        }
    };

    public static final ItemStackHandler inventory = new ItemStackHandler(10){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return true;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return ItemStack.EMPTY;
        }
    };

    public static final CustomEnergyStorage energyContainer = new CustomEnergyStorage(1000000, 1000000, 1000000){
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return maxReceive;
        }
    };

    @Override
    public void setRemoved() {
        super.setRemoved();
        LazyOptional.of(()->tank).invalidate();
        LazyOptional.of(()->energyContainer).invalidate();
        LazyOptional.of(()->inventory).invalidate();
        setChanged();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(()->tank).cast();
        if (cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(()->energyContainer).cast();
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(()->inventory).cast();
        return super.getCapability(cap, side);
    }
}
