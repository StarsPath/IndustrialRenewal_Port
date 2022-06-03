package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityFluidTank extends TileEntityTowerBase<TileEntityFluidTank> implements ITickableTileEntity {


    public static final int CAPACITY = 640000;
    private CustomFluidTank tank = new CustomFluidTank(CAPACITY){
        @Override
        public void onFluidChange() {
            sync();
            super.onFluidChange();
        }
    };
    private LazyOptional<FluidTank> tankHandler = LazyOptional.of(()->tank);
    private static final int MAX_TRANSFER = 128000;

    private boolean firstLoad = false;
    private int tick = 0;

    private int maxCapcity = 0;
    private int sumCurrent = 0;

    public TileEntityFluidTank(){
        super( ModTileEntities.FLUID_TANK_TILE.get());
    }

    public TileEntityFluidTank(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onLoad() {
        if(!level.isClientSide && isMaster()){
            TileEntityFluidTank masterTE = getMaster();
            TileEntityFluidTank inputTile = (TileEntityFluidTank) level.getBlockEntity(getBlockPos().above());
            //Utils.debug("INPUT TILE", getBlockPos().above(), inputTile);
            if(inputTile!= null) {
                inputTile.tank = masterTE.tank;
                inputTile.tankHandler = masterTE.tankHandler;
            }

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
        TileEntityFluidTank chunk = this;
        tower = new ArrayList<>();
        while(chunk != null){
            if(!tower.contains(chunk))
                tower.add(chunk);
            chunk = chunk.getAbove();
        }
    }

    @Override
    public void tick() {
        if (!level.isClientSide && isMaster())
        {
            if (!firstLoad) {
                //Utils.debug("CALLING ONLOAD", worldPosition);
                firstLoad = true;
                this.onLoad();
            }
            if(isBase()){
                if (tank.getFluidAmount() > 0) {
                    TileEntity te = level.getBlockEntity(worldPosition.below().relative(getMasterFacing().getOpposite(), 2));
                    if (te != null) {
                        IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getMasterFacing()).orElse(null);
                        if (handler != null) {
                            tank.drainInternal(handler.fill(tank.drainInternal(MAX_TRANSFER, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
            else if(isTop()){
                passFluidDown();
            }
            if(tick >= 20){
                tick = 0;
                //Utils.debug("tank", worldPosition, tank.getFluidAmount());
                maxCapcity = getSumMaxFluid();
                sumCurrent = getSumCurrentFluid();
                sync();
            }
            tick++;
        }
    }

    public void passFluidDown(){
        if(getBase().tower != null && !getBase().tower.isEmpty())
        for(TileEntityTowerBase<TileEntityFluidTank> TE : getBase().tower){
            //Utils.debug("TE", TE);
            if(TE instanceof TileEntityFluidTank){
                TileEntityFluidTank bankTE = ((TileEntityFluidTank) TE);

                //Utils.debug("condition 3", bankTE, !bankTE.isFull());
                if(!bankTE.isFull() && bankTE != this) {
                    //Utils.debug("condition 3", bankTE);
                    this.tank.drainInternal(bankTE.tank.fill(this.tank.drainInternal(MAX_TRANSFER, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    break;
                }
                else continue;
            }
        }
    }

    public boolean isFull(){
        return tank.getFluidAmount() >= tank.getCapacity();
    }

    public int getSumMaxFluid(){
        //int max = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int max = tower.stream().map(te -> (((TileEntityFluidTank) te).tank.getCapacity())).reduce(0, Integer::sum);
        return max;
    }

    public int getSumCurrentFluid(){
        //int current = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int current = tower.stream().map(te -> (((TileEntityFluidTank) te).tank.getFluidAmount())).reduce(0, Integer::sum);
        return current;
    }

    public String getFluidName()
    {
        String name = tank.getFluidAmount() > 0 ? tank.getFluid().getDisplayName().getString() : "Empty";
        return name + ": " + Utils.formatEnergyString((sumCurrent / FluidAttributes.BUCKET_VOLUME)).replace("FE", "B") + " / " + Utils.formatEnergyString((maxCapcity / FluidAttributes.BUCKET_VOLUME)).replace("FE", "B");
    }

    public float getFluidAngle()
    {
        return Utils.normalizeClamped(sumCurrent, 0, maxCapcity) * 180f;
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction downFace = getMasterFacing().getOpposite();
        TileEntityFluidTank master = getMaster();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (side == downFace && this.worldPosition.equals(master.worldPosition.below().relative(downFace)))
                return tankHandler.cast();
            if (side == Direction.UP && this.worldPosition.equals(master.worldPosition.above()))
                return tankHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("max", maxCapcity);
        compound.putInt("current", sumCurrent);
        tank.writeToNBT(compound);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        maxCapcity = compound.getInt("max");
        sumCurrent = compound.getInt("current");
        tank.readFromNBT(compound);
        super.load(state, compound);
    }
}
