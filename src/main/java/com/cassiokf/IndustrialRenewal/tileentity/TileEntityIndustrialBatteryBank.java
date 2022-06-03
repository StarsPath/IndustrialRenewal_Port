package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

// TODO: need to optimize energy transfer
public class TileEntityIndustrialBatteryBank extends TileEntityTowerBase<TileEntityIndustrialBatteryBank> implements ITickableTileEntity {
    //public ArrayList<TileEntityIndustrialBatteryBank> tower = null;

    public TileEntityIndustrialBatteryBank(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public static final int CAPACITY_PER_BATTERY = 6480000; //6480000
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
            //return TileEntityIndustrialBatteryBank.this.onEnergyIn(maxReceive, simulate);
            return onEnergyIn(super.receiveEnergy(maxReceive, simulate), simulate);
        }
    };

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(()->customEnergyStorage);

    public int input;
    private int avrIn;
    private int oldIn;
    private int outPut;
    private int avrOut;
    private int oldOutPut;
    private int batteries = 0;

    private int tick;
    public boolean firstLoad = false;

    private int currentEnergy = 0;
    private int maxEnergy = 0;

    public TileEntityIndustrialBatteryBank(){
        super(ModTileEntities.INDUSTRIAL_BATTERY_TILE.get());
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityIndustrialBatteryBank;
    }


    public int onEnergyIn(int received, boolean simulated)
    {
        if(!level.isClientSide && !simulated && isTop()){
            input += received;
        }
        return received;
    }
//
    private int outPutEnergy(IEnergyStorage container, int maxReceive, boolean simulate)
    {
        BlockPos masterPos = masterTE.getBlockPos();
        Direction face = getMasterFacing();
        TileEntity te = level.getBlockEntity(masterPos.relative(face.getClockWise()).above(2 + (tower.size()-1)*3));

        int out = 0;
        if (te != null)
        {
            //Utils.debug("OUTPUT ENERGY", te, te.getBlockPos());
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
        //Utils.debug("PLACE BATTERY CALLED", batteries, energyStorage.orElse(null).getMaxEnergyStored(), energyStorage.orElse(null).getEnergyStored());
        if (!level.isClientSide)
        {
            if (batteries >= maxBatteries) return false;
            batteries++;
            if (!player.isCreative()) batteryStack.shrink(1);
            customEnergyStorage.setMaxCapacity(batteries * CAPACITY_PER_BATTERY);
            sync();
        }
        return true;
    }

    @Override
    public void onLoad() {
        if(!level.isClientSide && isMaster()){
            TileEntityIndustrialBatteryBank masterTE = getMaster();
            Direction face = masterTE.getMasterFacing();

            // Input
            TileEntityIndustrialBatteryBank energyInputTile = (TileEntityIndustrialBatteryBank) level.getBlockEntity(getBlockPos().relative(face.getCounterClockWise()).above(1));
            if(energyInputTile != null)
                energyInputTile.energyStorage = masterTE.energyStorage;
//                energyInputTile.energyStorage = LazyOptional.of(()-> new CustomEnergyStorage(0, maxTransfer, maxTransfer){
//                    @Override
//                    public int receiveEnergy(int maxReceive, boolean simulate) {
//                        return fillEnergy(this, super.receiveEnergy(maxReceive, simulate));
//                    }
//                });

            if(isBase()){
                if (tower == null || tower.isEmpty()) {
                    //tower = new ArrayList<>();
                    loadTower();
                }
            }
            else{
                this.tower = getBase().tower;
            }
        }

        super.onLoad();
    }

    @Override
    public void loadTower(){
        TileEntityIndustrialBatteryBank chunk = this;
        tower = new ArrayList<>();
        while(chunk != null){
            if(!tower.contains(chunk))
                tower.add(chunk);
            chunk = chunk.getAbove();
        }
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
            if (batteries > 0 && customEnergyStorage.getEnergyStored() > 0)
            {
                outPut += outPutEnergy(customEnergyStorage, maxTransfer, false);
            }

            if (tick >= 10)
            {
                TileEntityIndustrialBatteryBank topBank = getTop();
                tick = 0;
                input = topBank.input;
                avrIn = input / 10;
                avrOut = outPut / 10;
                topBank.input = 0;
                outPut = 0;

                if (avrOut != oldOutPut || avrIn != oldIn)
                {
                    oldOutPut = avrOut;
                    oldIn = avrIn;
                }
                currentEnergy = getSumCurrentEnergy();
                maxEnergy = getSumMaxEnergy();
                sync();
            }
            tick++;
        }
        else if(!level.isClientSide && isMaster() && isTop()){
            if(!firstLoad){
                firstLoad = true;
                this.onLoad();
            }
            passEnergyDown();
        }
    }

    public void passEnergyDown(){
        for(TileEntityTowerBase<TileEntityIndustrialBatteryBank> TE : getBase().tower){
            //Utils.debug("TE", TE);
            if(TE instanceof TileEntityIndustrialBatteryBank){
                TileEntityIndustrialBatteryBank bankTE = ((TileEntityIndustrialBatteryBank) TE);

                //Utils.debug("condition 3", bankTE, !bankTE.isFull());
                if(!bankTE.isFull() && bankTE != this) {
                    //Utils.debug("condition 3", bankTE);
                    bankTE.customEnergyStorage.receiveEnergy(this.customEnergyStorage.extractEnergy(maxTransfer, false), false);
                    break;
                }
                else continue;
            }
        }
    }

//    @Override
//    public void breakMultiBlocks() {
//        super.breakMultiBlocks();
//    }

//    public void addToTower(TileEntityIndustrialBatteryBank tile, ArrayList<TileEntityIndustrialBatteryBank> list){
//        if(tower == null){
//            tower = new ArrayList<>();
//        }
//        this.tower.add(tile);
//        if(list != null){
//            tower.addAll(list);
//        }
//    }
//
//    public void removeTower(TileEntityIndustrialBatteryBank tile){
//        if(tower.contains(tile)){
//            int index = tower.indexOf(tile);
//            ArrayList<TileEntityIndustrialBatteryBank> newTower = new ArrayList<>(tower.subList(0, index));
//            this.tower = newTower;
//        }
//    }
    public int getInput(){
        return input;
    }

    public int getSumMaxEnergy(){
        //int max = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int max = tower.stream().map(te -> (((TileEntityIndustrialBatteryBank) te).customEnergyStorage.getMaxEnergyStored())).reduce(0, Integer::sum);
        return max;
    }

    public int getSumCurrentEnergy(){
        //int current = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int current = tower.stream().map(te -> (((TileEntityIndustrialBatteryBank) te).customEnergyStorage.getEnergyStored())).reduce(0, Integer::sum);
        return current;
    }

    public int fillEnergy(IEnergyStorage storage, int amount){
        for(TileEntityTowerBase<TileEntityIndustrialBatteryBank> TE : getMaster().getBase().tower){

            if(TE instanceof TileEntityIndustrialBatteryBank){
                TileEntityIndustrialBatteryBank bankTE = ((TileEntityIndustrialBatteryBank) TE);

                if(!bankTE.isFull() && bankTE != this) {
                    bankTE.customEnergyStorage.receiveEnergy(storage.extractEnergy(amount, false), false);
                    break;
                }
                else continue;
            }
        }
        return amount;
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
        //return Utils.formatEnergyString(customEnergyStorage.getEnergyStored()).replace(" FE", "") + " / " + Utils.formatEnergyString(customEnergyStorage.getMaxEnergyStored());
        return Utils.formatEnergyString(currentEnergy).replace(" FE", "") + " / " + Utils.formatEnergyString(maxEnergy);
    }

    public float getBatteryFill()
    {
        //return Utils.normalizeClamped(customEnergyStorage.getEnergyStored(), 0, customEnergyStorage.getMaxEnergyStored());
        return Utils.normalizeClamped(currentEnergy, 0, maxEnergy);
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
        return "Input";
    }

    public String getOutPutIndicatorText()
    {
        return "Output";
    }

    public Boolean isFull(){
        return customEnergyStorage.getEnergyStored() >= customEnergyStorage.getMaxEnergyStored();
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        TileEntityIndustrialBatteryBank masterTE = getMaster();
        if (masterTE == null) return super.getCapability(cap, side);
        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getBlockPos();

        if (cap == CapabilityEnergy.ENERGY && worldPosition.equals(masterPos.relative(face.getCounterClockWise()).above()) && side == Direction.UP) {
            // Input
            return energyStorage.cast();
        }
        if (cap == CapabilityEnergy.ENERGY && worldPosition.equals(masterPos.relative(face.getClockWise()).above()) && side == Direction.UP) {
            // Output
            return energyStorage.cast();
        }
        return super.getCapability(cap, side);
    }


    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("energy", customEnergyStorage.getEnergyStored());

        compound.putInt("out", avrOut);
        compound.putInt("in", avrIn);
        compound.putInt("battery", batteries);

        compound.putInt("currentEnergy", currentEnergy);
        compound.putInt("maxEnergy", maxEnergy);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        //energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        int current = compound.getInt("energy");

        avrOut = compound.getInt("out");
        avrIn = compound.getInt("in");
        batteries = compound.getInt("battery");

        customEnergyStorage = new CustomEnergyStorage(batteries * CAPACITY_PER_BATTERY, maxTransfer, maxTransfer, current){
            @Override
            public void onEnergyChange() {
                sync();
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                //return TileEntityIndustrialBatteryBank.this.onEnergyIn(maxReceive, simulate);
                return onEnergyIn(super.receiveEnergy(maxReceive, simulate), simulate);
            }
        };

        currentEnergy = compound.getInt("currentEnergy");
        maxEnergy = compound.getInt("maxEnergy");
        super.load(state, compound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
