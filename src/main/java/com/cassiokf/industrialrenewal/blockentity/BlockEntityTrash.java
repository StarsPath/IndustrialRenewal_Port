package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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

public class BlockEntityTrash extends BlockEntitySyncable {

    public BlockEntityTrash(BlockPos pos, BlockState state){
        super(ModBlockEntity.TRASH_TILE.get(), pos, state);
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
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyOptional.of(()->tank).invalidate();
        LazyOptional.of(()->energyContainer).invalidate();
        LazyOptional.of(()->inventory).invalidate();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
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
