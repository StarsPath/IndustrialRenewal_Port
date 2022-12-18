package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityTowerBase;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class BlockEntityIndustrialBatteryBank extends BlockEntityTowerBase<BlockEntityIndustrialBatteryBank> {
    //public ArrayList<TileEntityIndustrialBatteryBank> tower = null;

    public static final int CAPACITY_PER_BATTERY = Config.INDUSTRIAL_BATTERY_BANK_ENERGY_PER_BATTERY.get(); //6480000
    // Typical car battery is 720Wh = 2592000J = 6480000RF
    private static final int maxTransfer = Config.INDUSTRIAL_BATTERY_BANK_TRANSFER_RATE.get();
    private static final int maxBatteries = 24;

    private final CustomEnergyStorage customDummyStorage = new CustomEnergyStorage(0, 0, 0);
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
    private LazyOptional<IEnergyStorage> dummyStorage = LazyOptional.of(()->customDummyStorage);

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

    public BlockEntityIndustrialBatteryBank(BlockPos pos, BlockState state){
        super(ModBlockEntity.INDUSTRIAL_BATTERY_TILE.get(), pos, state);
    }

    @Override
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityIndustrialBatteryBank;
    }


    public int onEnergyIn(int received, boolean simulated)
    {
        if(level == null) return 0;
        if(!level.isClientSide && !simulated && isTop()){
            input += received;
        }
        return received;
    }
//
    private int outPutEnergy(IEnergyStorage container, int maxReceive, boolean simulate)
    {
        if(level == null) return 0;
        BlockPos masterPos = masterTE.getBlockPos();
        Direction face = getMasterFacing();
        BlockEntity te = level.getBlockEntity(masterPos.relative(face.getClockWise()).above(2 + (tower.size()-1)*3));

        int out = 0;
        if (te != null)
        {
            //Utils.debug("OUTPUT ENERGY", te, te.getBlockPos());
            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN).orElse(null);
            if (energyStorage != null)
            {
                if (container == null)
                    out = energyStorage.receiveEnergy(maxReceive, simulate);
                else
                    out = container.extractEnergy(energyStorage.receiveEnergy(container.extractEnergy(maxReceive, true), simulate), simulate);
            }
        }
        return out;
    }

    public boolean placeBattery(Player player, ItemStack batteryStack)
    {
        if(level == null) return false;
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

//    @Override
//    public void onLoad() {
//        super.onLoad();
//    }

    public void setFirstLoad(){
        if(level == null) return;
        if(!level.isClientSide && isMaster()){
            if(isBase()){
                if (tower == null || tower.isEmpty())
                    loadTower();
            }
            else
                this.tower = getBase().tower;
        }
    }

    @Override
    public void loadTower(){
        BlockEntityIndustrialBatteryBank chunk = this;
        tower = new ArrayList<>();
        while(chunk != null){
            if(!tower.contains(chunk))
                tower.add(chunk);
            chunk = chunk.getAbove();
        }
    }

    public void tick() {
        if(level == null) return;
        if (!level.isClientSide && isMaster() && isBase())
        {
            if(!firstLoad){
                firstLoad = true;
                setFirstLoad();
            }
            if (batteries > 0 && customEnergyStorage.getEnergyStored() > 0)
            {
                outPut += outPutEnergy(customEnergyStorage, maxTransfer, false);
            }

            if (tick >= 10)
            {
                BlockEntityIndustrialBatteryBank topBank = getTop();
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
        for(BlockEntityTowerBase<BlockEntityIndustrialBatteryBank> TE : getBase().tower){
            //Utils.debug("TE", TE);
            if(TE instanceof BlockEntityIndustrialBatteryBank bankTE){

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

    public int getInput(){
        if(getTop() == null)
            return 0;
        return getTop().input;
    }

    public int getSumMaxEnergy(){
        //int max = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int max = tower.stream().map(te -> (((BlockEntityIndustrialBatteryBank) te).customEnergyStorage.getMaxEnergyStored())).reduce(0, Integer::sum);
        return max;
    }

    public int getSumCurrentEnergy(){
        //int current = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int current = tower.stream().map(te -> (((BlockEntityIndustrialBatteryBank) te).customEnergyStorage.getEnergyStored())).reduce(0, Integer::sum);
        return current;
    }

    public int fillEnergy(IEnergyStorage storage, int amount){
        for(BlockEntityTowerBase<BlockEntityIndustrialBatteryBank> TE : getMaster().getBase().tower){

            if(TE instanceof BlockEntityIndustrialBatteryBank bankTE){

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
        BlockEntityIndustrialBatteryBank masterTE = getMaster();
        if (masterTE == null) return super.getCapability(cap, side);

        if (side == null)
            return super.getCapability(cap, side);

        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getBlockPos();

        if (cap == CapabilityEnergy.ENERGY && worldPosition.equals(masterPos.relative(face.getCounterClockWise()).above()) && side == Direction.UP) {
            // Input
            return getMaster().energyStorage.cast();
        }
        if (cap == CapabilityEnergy.ENERGY && worldPosition.equals(masterPos.relative(face.getClockWise()).above()) && side == Direction.UP) {
            // Output
            return getMaster().dummyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", customEnergyStorage.getEnergyStored());

        compoundTag.putInt("out", avrOut);
        compoundTag.putInt("in", avrIn);
        compoundTag.putInt("battery", batteries);

        compoundTag.putInt("currentEnergy", currentEnergy);
        compoundTag.putInt("maxEnergy", maxEnergy);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        int current = compoundTag.getInt("energy");

        avrOut = compoundTag.getInt("out");
        avrIn = compoundTag.getInt("in");
        batteries = compoundTag.getInt("battery");

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

        currentEnergy = compoundTag.getInt("currentEnergy");
        maxEnergy = compoundTag.getInt("maxEnergy");
        super.load(compoundTag);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
