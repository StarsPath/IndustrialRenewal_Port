package com.cassiokf.IndustrialRenewal.handlers;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidGenerator {
    public static int energyPerTick = 32;
    private final TileEntitySyncable attachedTE;
    private final int TANK_CAPACITY = 8000;
    public final FluidTank tank = new FluidTank(TANK_CAPACITY){
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            FluidGenerator.this.sync();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return Config.getFuelHash().get(stack.getFluid().getRegistryName().toString()) != null;
        }
    };
    public final CustomEnergyStorage energyStorage = new CustomEnergyStorage(128, 128, energyPerTick)
    {
        @Override
        public void onEnergyChange()
        {
            FluidGenerator.this.sync();
        }
    };

    public final LazyOptional<FluidTank> tankHandler;
    public final LazyOptional<CustomEnergyStorage> energyHandler;

    public FluidGenerator(TileEntitySyncable te)
    {
        attachedTE = te;
        tankHandler = LazyOptional.of(()->tank);
        energyHandler = LazyOptional.of(()->energyStorage);
    }

    public void setRemoved(){
        tankHandler.invalidate();
        energyHandler.invalidate();
    }

    private boolean canGenerate = false;
    private boolean isGenerating = false;
    private boolean oldRunning = false;
    private int fuelTime = 0;

    public void onTick()
    {
        if (canGenerate)
        {
            updateLiquidFuel();
            if (fuelTime > 0)
            {
                isGenerating = true;
                energyStorage.receiveEnergy(energyPerTick, false);
                fuelTime--;
            }
            else isGenerating = false;
        }
        else isGenerating = false;

        if (isGenerating != oldRunning)
        {
            oldRunning = isGenerating;
            sync();
        }
    }

    private void updateLiquidFuel()
    {
        if (fuelTime > 0) return;
        FluidStack resource = tank.getFluid();
        if (resource.getAmount() <= 0) return;

        //int fuel = IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getName()) != null ? IRConfig.MainConfig.Main.fluidFuel.get(resource.getFluid().getName()) : 0;
//        int fuel = Config.getFuelHash().get(resource.getFluid().getRegistryName().toString()) !=null? Config.getFuelHash().get(resource.getFluid().getRegistryName().toString()) : 0;
        int fuel = resource.getAmount();
//        industrialrenewal.LOGGER.info("fuel: "+fuel);
        if (fuel > 0)
        {
            int amount = Math.min(FluidAttributes.BUCKET_VOLUME, resource.getAmount());
            float norm = Utils.normalizeClamped(amount, 0, FluidAttributes.BUCKET_VOLUME);

//            fuelTime = (int) (fuel * norm) * 4;
            fuelTime = Config.getFuelHash().get(resource.getFluid().getRegistryName().toString()) !=null? Config.getFuelHash().get(resource.getFluid().getRegistryName().toString()) : 0;
            tank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
            //maxFuelTime = fuelTime;
            //fuelName = resource.getLocalizedName();
        }
    }

    public boolean isGenerating()
    {
        return isGenerating;
    }
    public String getTankContent(){
        return tank.getFluid().getTranslationKey();
    }
    public int getTankAmount(){
        return tank.getFluidAmount();
    }
    public int getGenerateAmount(){
        return energyPerTick;
    }


    public CompoundNBT saveGenerator(CompoundNBT compound)
    {
        compound.putInt("FGenerator", fuelTime);
        compound.putBoolean("running", isGenerating);
        compound.putBoolean("generate", canGenerate);
        compound.put("energy", energyStorage.serializeNBT());
        compound.put("tank", tank.writeToNBT(new CompoundNBT()));
        return compound;
    }

    public void loadGenerator(CompoundNBT compound)
    {
        fuelTime = compound.getInt("FGenerator");
        isGenerating = compound.getBoolean("running");
        canGenerate = compound.getBoolean("generate");
        energyStorage.deserializeNBT(compound.getCompound("energy"));
        tank.readFromNBT(compound.getCompound("tank"));
    }

    public void setCanGenerate(boolean value)
    {
        canGenerate = value;
    }

    public void changeCanGenerate()
    {
        canGenerate = !canGenerate;
    }

    public void sync() { attachedTE.sync(); }
    public void setChanged() { attachedTE.setChanged(); }
}
