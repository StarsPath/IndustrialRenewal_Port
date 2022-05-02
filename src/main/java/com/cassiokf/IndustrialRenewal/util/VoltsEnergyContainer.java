//package com.cassiokf.IndustrialRenewal.util;
//
//import com.cassiokf.IndustrialRenewal.industrialrenewal;
//import net.minecraft.command.arguments.NBTCompoundTagArgument;
//import net.minecraft.command.arguments.NBTTagArgument;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraftforge.common.util.INBTSerializable;
//import net.minecraftforge.energy.IEnergyStorage;
//
//public class VoltsEnergyContainer implements IEnergyStorage, INBTSerializable<CompoundNBT>{
//
//    protected int stored;
//    protected int capacity;
//    protected int input;
//    protected int output;
//
//    public VoltsEnergyContainer() {
//        this(1000, 1000, 1000);
//    }
//
//    public VoltsEnergyContainer(final int capacity, final int input, final int output) {
//        this(0, capacity, input, output);
//    }
//
//    public VoltsEnergyContainer(final int power, final int capacity, final int input, final int output) {
//        this.stored = power;
//        this.capacity = capacity;
//        this.input = input;
//        this.output = output;
//    }
//
//    public void onEnergyChange() {
//        //industrialrenewal.LOGGER.info(this.stored);
//    }
//
//    public int getMaxInput() {
//        return this.input;
//    }
//
//    public void setMaxInput(final int input) {
//        this.input = input;
//    }
//
//    public int getMaxOutput() {
//        return this.output;
//    }
//
//    public void setMaxOutput(final int output) {
//        this.output = output;
//    }
//
//    public int receiveInternally(final int maxReceive, final boolean simulate) {
//        final int acceptedPower = Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), Math.min(this.getMaxInput(), maxReceive));
//        if (!simulate) {
//            this.stored += acceptedPower;
//            this.onEnergyChange();
//        }
//        return acceptedPower;
//    }
//
//    @Override
//    public int receiveEnergy(int maxReceive, boolean simulate) {
//        final int acceptedPower = Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), Math.min(this.getMaxInput(), maxReceive));
//        if (!simulate) {
//            this.stored += acceptedPower;
//            this.onEnergyChange();
//        }
//        //industrialrenewal.LOGGER.info(this.stored);
//        return this.canReceive() ? acceptedPower : 0;
//    }
//
//    @Override
//    public int extractEnergy(int maxExtract, boolean simulate) {
//        final int removedPower = Math.min(this.getEnergyStored(), Math.min(this.getMaxOutput(), maxExtract));
//        if (!simulate) {
//            this.stored -= removedPower;
//            this.onEnergyChange();
//        }
//        return this.canExtract() ? removedPower : 0;
//    }
//
//    public int extractEnergyInternally(int maxExtract, boolean simulate)
//    {
//        final int removedPower = Math.min(this.getEnergyStored(), Math.min(this.getMaxOutput(), maxExtract));
//        if (!simulate)
//        {
//            this.stored -= removedPower;
//            onEnergyChange();
//        }
//        return removedPower;
//    }
//
//    @Override
//    public int getEnergyStored() {
//        //industrialrenewal.LOGGER.info(this.stored);
//        return this.stored;
//    }
//
//    @Override
//    public int getMaxEnergyStored() {
//        return this.capacity;
//    }
//
//    @Override
//    public boolean canExtract() {
//        return this.getMaxOutput() > 0 && this.stored > 0;
//    }
//
//    @Override
//    public boolean canReceive() {
//        return this.getMaxInput() > 0;
//    }
//
//    @Override
//    public CompoundNBT serializeNBT() {
//        final CompoundNBT datatag = new CompoundNBT();
//        this.serializeNBT(datatag);
//        return datatag;
//    }
//
//    public void serializeNBT(final CompoundNBT compound) {
//        compound.putInt("IRStored", this.stored);
//        compound.putInt("IRCapacity", this.capacity);
//        compound.putInt("IRInput", this.input);
//        compound.putInt("IROutput", this.output);
//    }
//
//    @Override
//    public void deserializeNBT(CompoundNBT nbt) {
//        if (nbt.contains("IRStored")) {
//            this.stored = nbt.getInt("IRStored");
//        }
//        if (nbt.contains("IRCapacity")) {
//            this.capacity = nbt.getInt("IRCapacity");
//        }
//        if (nbt.contains("IRInput")) {
//            this.input = nbt.getInt("IRInput");
//        }
//        if (nbt.contains("IROutput")) {
//            this.output = nbt.getInt("IROutput");
//        }
//        if (this.stored > this.getMaxEnergyStored()) {
//            this.stored = this.getMaxEnergyStored();
//        }
//    }
//}
