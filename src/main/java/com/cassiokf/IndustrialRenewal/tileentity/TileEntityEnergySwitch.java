package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockPipeSwitchBase;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityEnergySwitch extends TileEntitySyncable implements ITickableTileEntity {
    public TileEntityEnergySwitch() {
        super(ModTileEntities.ENERGY_SWITCH_TILE.get());
    }

    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(0);
    public LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);

    @Override
    public void tick() {
        if(isOpen()){
            transferEnergy();
        }
    }

    public void transferEnergy(){
        Direction facing = getFacing();
        TileEntity outputTile = level.getBlockEntity(worldPosition.relative(facing));
        TileEntity inputTile = level.getBlockEntity(worldPosition.relative(facing.getOpposite()));

        if(inputTile != null && outputTile != null){
            IEnergyStorage inputTileHandler = inputTile.getCapability(CapabilityEnergy.ENERGY, facing).orElse(null);
            IEnergyStorage outputTileHandler = outputTile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
            if(inputTileHandler != null && outputTileHandler != null){
                int amount = outputTileHandler.receiveEnergy(inputTileHandler.extractEnergy(Integer.MAX_VALUE, true), true);
                outputTileHandler.receiveEnergy(inputTileHandler.extractEnergy(amount, false), false);
            }
        }
    }

    public boolean isOpen(){
        return level.getBlockState(worldPosition).getValue(BlockPipeSwitchBase.ON_OFF);
    }

    public Direction getFacing(){
        return level.getBlockState(worldPosition).getValue(BlockPipeSwitchBase.FACING);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction facing = getFacing();

        if (side == null)
            return super.getCapability(cap, side);

        if(cap == CapabilityEnergy.ENERGY && (side == facing.getOpposite() || side == facing))
            return energyStorageHandler.cast();
        return super.getCapability(cap, side);
    }
}
