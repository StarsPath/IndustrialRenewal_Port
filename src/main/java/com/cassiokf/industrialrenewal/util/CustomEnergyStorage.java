package com.cassiokf.industrialrenewal.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage{

    public CustomEnergyStorage(int capacity)
    {
        super(capacity, capacity, capacity);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int passEnergy)
    {
        super(capacity, maxReceive, maxExtract, passEnergy);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        int energyReceived = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && energyReceived > 0) onEnergyChange();
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        int energyExtracted = super.extractEnergy(maxExtract, simulate);
        if (!simulate && energyExtracted > 0) onEnergyChange();
        return energyExtracted;
    }

    public void onEnergyChange()
    {
    }

    public void setMaxCapacity(int maxCapacity)
    {
        this.capacity = maxCapacity;
    }

    public void setEnergy(int energy)
    {
        this.energy = energy;
    }

    public int addEnergy(int maxReceive)
    {
        int energyReceived = Math.min(capacity - energy, maxReceive);
        energy += energyReceived;
        return energyReceived;
    }

    public int subtractEnergy(int maxExtract){
        int energyExtracted = Math.min(energy,  maxExtract);
        energy -= energyExtracted;
        return energyExtracted;
    }

//    @Override
//    public Tag serializeNBT() {
//        CompoundTag tag = new CompoundTag();
//        tag.putInt("energy", getEnergyStored());
//        return tag;
//    }


//    @Override
//    public void deserializeNBT(CompoundNBT nbt)
//    {
//        setEnergy(nbt.getInt("energy"));
//    }
}
