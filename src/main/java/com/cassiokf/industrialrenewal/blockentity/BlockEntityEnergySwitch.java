package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockPipeSwitchBase;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityEnergySwitch extends BlockEntitySyncable {
    public BlockEntityEnergySwitch(BlockPos pos, BlockState state) {
        super(ModBlockEntity.ENERGY_SWITCH_TILE.get(), pos, state);
    }

    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(0);
    public LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);

    public void tick() {
        if(isOpen()){
            transferEnergy();
        }
    }

    public void transferEnergy(){
        if(level == null) return;
        Direction facing = getFacing();
        BlockEntity outputTile = level.getBlockEntity(worldPosition.relative(facing));
        BlockEntity inputTile = level.getBlockEntity(worldPosition.relative(facing.getOpposite()));

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
        return getBlockState().is(ModBlocks.ENERGY_SWITCH.get())? getBlockState().getValue(BlockPipeSwitchBase.ON_OFF) : false;
    }

    public Direction getFacing(){
        return getBlockState().is(ModBlocks.ENERGY_SWITCH.get())? getBlockState().getValue(BlockPipeSwitchBase.FACING) : Direction.NORTH;
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
