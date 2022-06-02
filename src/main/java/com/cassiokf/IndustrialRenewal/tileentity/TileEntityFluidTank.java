package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFluidTank extends TileEntityTowerBase<TileEntityFluidTank> implements ITickableTileEntity {

    //1,000,000,000 mb
    public static final int CAPACITY = 1000000000;
    private CustomFluidTank tank = new CustomFluidTank(CAPACITY);
    private LazyOptional<FluidTank> tankHandler = LazyOptional.of(()->tank);


    public TileEntityFluidTank(){
        super( ModTileEntities.FLUID_TANK_TILE.get());
    }

    public TileEntityFluidTank(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onLoad() {
        if(!level.isClientSide && isMaster()){
            TileEntityFluidTank inputTile = (TileEntityFluidTank) level.getBlockEntity(getBlockPos().above());
            if(inputTile!= null)
                inputTile.tankHandler = this.tankHandler;
        }
        super.onLoad();
    }

    @Override
    public void tick() {

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
}
