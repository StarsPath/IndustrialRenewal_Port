package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityIndustrialBatteryBank extends TileEntityTowerBase<TileEntityIndustrialBatteryBank> implements ITickableTileEntity {
    public TileEntityIndustrialBatteryBank(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public static final int CAPACITY_PER_BATTERY = 5000000; //6480000
    // Typical car battery is 720Wh = 2592000J = 6480000RF
    private static final int maxTransfer = 102400;
    private static final int maxBatteries = 24;

    private CustomEnergyStorage customEnergyStorage = new CustomEnergyStorage(0, maxTransfer, maxTransfer){
        @Override
        public void onEnergyChange() {
            sync();
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return TileEntityIndustrialBatteryBank.this.onEnergyIn(maxReceive, simulate);
            //return super.receiveEnergy(maxReceive, simulate);
        }
    };

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(()->customEnergyStorage);

//    private IEnergyStorage createEnergy()
//    {
//        return new CustomEnergyStorage(0, maxTransfer ,maxTransfer)
//        {
//            @Override
//            public void onEnergyChange()
//            {
//                TileEntityIndustrialBatteryBank.this.sync();
//            }
//        };
//    }

//    private IEnergyStorage createEnergy(int maxCapacity, int passEnergy)
//    {
//        return new CustomEnergyStorage(maxCapacity, maxTransfer ,maxTransfer, passEnergy)
//        {
//            @Override
//            public void onEnergyChange()
//            {
//                TileEntityIndustrialBatteryBank.this.sync();
//            }
//        };
//    }

    private int input;
    private int avrIn;
    private int oldIn;
    private int outPut;
    private int avrOut;
    private int oldOutPut;
    private int batteries = 0;

    private int tick;
    public boolean firstLoad = false;

    public TileEntityIndustrialBatteryBank(){
        super(ModTileEntities.INDUSTRIAL_BATTERY_TILE.get());
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityIndustrialBatteryBank;
        //return super.instanceOf(tileEntity);
    }

    public int onEnergyIn(int maxReceive, boolean simulate)
    {
        maxReceive = Math.min(maxReceive, maxTransfer);
        int in;
        if (batteries > 0)
            in = customEnergyStorage.receiveEnergy(maxReceive, simulate);
        else
        {
            in = outPutEnergy(null, maxReceive, simulate);
            if (!simulate && !level.isClientSide) outPut += in;
        }
        if (!simulate && !level.isClientSide) input += in;
        return in;
    }
//
    private int outPutEnergy(IEnergyStorage container, int maxReceive, boolean simulate)
    {
        BlockPos masterPos = masterTE.getBlockPos();
        Direction face = getMasterFacing();
        TileEntity te = level.getBlockEntity(masterPos.relative(face.getClockWise()).above(2));

        int out = 0;
        if (te != null)
        {
            Utils.debug("OUTPUT ENERGY", te, te.getBlockPos());
            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN).orElse(null);
            if (energyStorage != null)
            {
                if (container == null) out = energyStorage.receiveEnergy(maxReceive, simulate);
                else
                    out = container.extractEnergy(energyStorage.receiveEnergy(container.extractEnergy(maxReceive, true), simulate), simulate);
            }
        }
        return out;
    }

    public boolean placeBattery(PlayerEntity player, ItemStack batteryStack)
    {
        Utils.debug("PLACE BATTERY CALLED", batteries, energyStorage.orElse(null).getMaxEnergyStored(), energyStorage.orElse(null).getEnergyStored());
        if (!level.isClientSide)
        {
            if (batteries >= maxBatteries) return false;
            batteries++;
            Utils.debug("BATTERIES", batteries);
            if (!player.isCreative()) batteryStack.shrink(1);
            int currentAmount = energyStorage.orElse(null).getEnergyStored();
            customEnergyStorage.setMaxCapacity(batteries * CAPACITY_PER_BATTERY);
            //Utils.debug("Currrent energy, max Energy", currentAmount, energyStorage.orElse(null).getMaxEnergyStored());
            //energyStorage = LazyOptional.of(()->this.createEnergy(batteries * CAPACITY_PER_BATTERY, currentAmount));
            sync();
            //Utils.debug("New energy", energyStorage.orElse(null).getMaxEnergyStored());
            onLoad();
            //reachTop();
        }
        return true;
    }

    @Override
    public void onLoad() {
        if(!level.isClientSide && isMaster()){
            TileEntityIndustrialBatteryBank masterTE = getMaster();
            Direction face = masterTE.getMasterFacing();

            // Input
            TileEntityIndustrialBatteryBank energyInputTile = (TileEntityIndustrialBatteryBank) level.getBlockEntity(getBlockPos().relative(face.getCounterClockWise()).above());
            if(energyInputTile != null)
                energyInputTile.energyStorage = masterTE.energyStorage;

//            TileEntityIndustrialBatteryBank energyOutputTile = (TileEntityIndustrialBatteryBank) level.getBlockEntity(getBlockPos().relative(face.getClockWise()).above());
//            if(energyInputTile != null)
//                energyOutputTile.energyStorage = masterTE.energyStorage;
        }

        super.onLoad();
    }

    @Override
    public void tick() {
        if (!level.isClientSide && isMaster() && isBase())
        {

            if(!firstLoad){
                //Utils.debug("CALLING ONLOAD");
                firstLoad = true;
                this.onLoad();
            }
            //IEnergyStorage storage = energyStorage.orElse(null);
            Utils.debug("IENERGYSTORAGE", customEnergyStorage.getEnergyStored(), customEnergyStorage.getMaxEnergyStored());
            if (batteries > 0 && customEnergyStorage.getEnergyStored() > 0)
            {
                outPut += outPutEnergy(customEnergyStorage, maxTransfer, false);
            }

            if (tick >= 10)
            {
                tick = 0;
                avrIn = input / 10;
                avrOut = outPut / 10;
                input = 0;
                outPut = 0;

                if (avrOut != oldOutPut || avrIn != oldIn)
                {
                    oldOutPut = avrOut;
                    oldIn = avrIn;
                }
                sync();
            }
            tick++;
        }
    }

    public int getRealCapacity()
    {
        return CAPACITY_PER_BATTERY * batteries;
    }

    public int getBatteries()
    {
        return batteries;
    }

    public String getEnergyText()
    {
        return Utils.formatEnergyString(customEnergyStorage.getEnergyStored()).replace(" FE", "") + " / " + Utils.formatEnergyString(customEnergyStorage.getMaxEnergyStored());
    }

    public float getBatteryFill()
    {
        return Utils.normalizeClamped(customEnergyStorage.getEnergyStored(), 0, customEnergyStorage.getMaxEnergyStored());
    }

    public float getOutPutAngle()
    {
        return Utils.normalizeClamped(avrOut, 0, 10240) * 90;
    }

    public String getOutPutText()
    {
        return Utils.formatPreciseEnergyString(avrOut) + "/t";
    }

    public float getInPutAngle()
    {
        return Utils.normalizeClamped(avrIn, 0, 10240) * 90;
    }

    public String getInPutText()
    {
        return Utils.formatPreciseEnergyString(avrIn) + "/t";
    }

    public String getInPutIndicatorText()
    {
        //return TextFormatting.BLACK + I18n.get("tesr.indr.in");
        return "Input";
    }

    public String getOutPutIndicatorText()
    {
        //return TextFormatting.BLACK + I18n.get("tesr.indr.out");
        return "Output";
    }

//    private BlockPos getOutPutPos()
//    {
//        return getTopTE().pos.up(2).offset(getMasterFacing().rotateY());
//    }
//
//    public TileEntityIndustrialBatteryBank getBottomTE()
//    {
//        return bottomTE;
//    }
//
//    public TileEntityIndustrialBatteryBank getTopTE()
//    {
//        return topTE;
//    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        TileEntityIndustrialBatteryBank masterTE = getMaster();
        if (masterTE == null) return super.getCapability(cap, side);
        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getBlockPos();

        if (cap == CapabilityEnergy.ENERGY && worldPosition.equals(masterPos.relative(face.getCounterClockWise()).above())) {
            // Input
            return energyStorage.cast();
        }
        if (cap == CapabilityEnergy.ENERGY && worldPosition.equals(masterPos.relative(face.getClockWise()).above())) {
            // Output
            return energyStorage.cast();
        }
        return super.getCapability(cap, side);
    }


    @Override
    public CompoundNBT save(CompoundNBT compound) {
//        energyStorage.ifPresent(h ->
//        {
//            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
//            compound.put("StoredIR", tag);
//        });
//        CompoundNBT tag = customEnergyStorage.serializeNBT();
//        compound.put("energy", tag);
        compound.putInt("energy", customEnergyStorage.getEnergyStored());

        compound.putInt("out", avrOut);
        compound.putInt("in", avrIn);
        compound.putInt("battery", batteries);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        //energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        int currentEnergy = compound.getInt("energy");

        customEnergyStorage.deserializeNBT(compound.getCompound("energy"));
        avrOut = compound.getInt("out");
        avrIn = compound.getInt("in");
        batteries = compound.getInt("battery");

        customEnergyStorage = new CustomEnergyStorage(batteries * CAPACITY_PER_BATTERY, maxTransfer, maxTransfer, currentEnergy);
        super.load(state, compound);
    }
}
