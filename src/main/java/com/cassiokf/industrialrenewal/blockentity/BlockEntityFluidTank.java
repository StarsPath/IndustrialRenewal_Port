package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityTowerBase;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class BlockEntityFluidTank extends BlockEntityTowerBase<BlockEntityFluidTank> {


    public static final int CAPACITY = Config.FLUID_TANK_CAPACITY.get();
    private CustomFluidTank tank = new CustomFluidTank(CAPACITY){
        @Override
        public void onFluidChange() {
            sync();
            super.onFluidChange();
        }
    };
    private LazyOptional<FluidTank> tankHandler = LazyOptional.of(()->tank);
    private static final int MAX_TRANSFER = Config.FLUID_TANK_TRANSFER_RATE.get();

    private boolean firstLoad = false;
    private int tick = 0;

    private int maxCapcity = 0;
    private int sumCurrent = 0;

    public BlockEntityFluidTank(BlockPos pos, BlockState state){
        super( ModBlockEntity.FLUID_TANK_TILE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

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
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityFluidTank;
    }

    @Override
    public void loadTower(){
        BlockEntityFluidTank chunk = this;
        tower = new ArrayList<>();
        while(chunk != null){
            if(!tower.contains(chunk))
                tower.add(chunk);
            chunk = chunk.getAbove();
        }
    }

    @Override
    public void invalidateCaps() {
        tankHandler.invalidate();
        super.invalidateCaps();
    }

    public void tick() {
        if(level == null) return;
        if (!level.isClientSide && isMaster())
        {
            if (!firstLoad) {
                firstLoad = true;
                setFirstLoad();
            }
            if(isBase()){
                if (tank.getFluidAmount() > 0) {
                    BlockEntity te = level.getBlockEntity(worldPosition.below().relative(getMasterFacing().getOpposite(), 2));
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
            if(tick >= 5){
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
        if(getBase().tower != null && !getBase().tower.isEmpty()) {
            for(BlockEntityTowerBase<BlockEntityFluidTank> TE : getBase().tower){
                //Utils.debug("TE", TE);
                if(TE instanceof BlockEntityFluidTank bankTE){

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
    }

    public boolean isFull(){
        return tank.getFluidAmount() >= tank.getCapacity();
    }

    public int getSumMaxFluid(){
        //int max = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int max = tower.stream().map(te -> (((BlockEntityFluidTank) te).tank.getCapacity())).reduce(0, Integer::sum);
        return max;
    }

    public int getSumCurrentFluid(){
        //int current = 0;
        if(tower == null || tower.isEmpty())
            return 0;

        int current = tower.stream().map(te -> (((BlockEntityFluidTank) te).tank.getFluidAmount())).reduce(0, Integer::sum);
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
        BlockEntityFluidTank masterTE = getMaster();
        if (masterTE == null) return super.getCapability(cap, side);
        Direction face = masterTE.getMasterFacing();

        if (side == null)
            return super.getCapability(cap, side);

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (side == face.getOpposite() && this.worldPosition.equals(masterTE.getBlockPos().below().relative(face.getOpposite())))
                return masterTE.tankHandler.cast();
            if (side == Direction.UP && this.worldPosition.equals(masterTE.getBlockPos().above()))
                return masterTE.tankHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("max", maxCapcity);
        compoundTag.putInt("current", sumCurrent);
        tank.writeToNBT(compoundTag);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        maxCapcity = compoundTag.getInt("max");
        sumCurrent = compoundTag.getInt("current");
        tank.readFromNBT(compoundTag);
        super.load(compoundTag);
    }
}
