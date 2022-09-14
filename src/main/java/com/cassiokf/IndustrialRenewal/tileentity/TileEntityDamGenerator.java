package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityDamGenerator extends TileEntity3x3x3MachineBase<TileEntityDamGenerator> implements ITickableTileEntity {

    private final int energyCapacity = Config.DAM_GENERATOR_ENERGY_CAPACITY.get();
    public CustomEnergyStorage bank = new CustomEnergyStorage(energyCapacity){
        @Override
        public void onEnergyChange() {
            TileEntityDamGenerator.this.sync();
        }
    };
    public LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional.of(()->bank);
    private int oldGeneration;
    private int generation;
    private int rotation;

    public static final int maxGeneration = Config.DAM_GENERATOR_RF_PER_TICK.get();//IRConfig.MainConfig.Main.damGeneratorEnergyPerTick;
    public static final int transferRate = Config.DAM_GENERATOR_TRANSFER_RATE.get();


    public TileEntityDamGenerator() {
        super(ModTileEntities.DAM_GENERATOR.get());
    }

    @Override
    public void tick() {
        if(!level.isClientSide && isMaster()){
            generation = (int) (Utils.normalizeClamped(rotation, 0, TileEntityDamTurbine.MAX_PROCESSING/40f) * maxGeneration);
            if (generation > 0)
                bank.receiveEnergy(generation, false);
            TileEntity te = level.getBlockEntity(worldPosition.above(2));
            if (te != null)
            {
                te.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN).ifPresent(iEnergyStorage -> {
                    int amount = iEnergyStorage.receiveEnergy(transferRate, true);
                    iEnergyStorage.receiveEnergy(bank.extractEnergy(amount, false), false);
                });
            }
            if (generation != oldGeneration)
            {
                oldGeneration = generation;
                sync();
            }
            rotation = 0;
        }
    }

    public void updateRotation(int newRotation){
        if(rotation != newRotation)
            rotation = newRotation;
    }

    public String getGenerationText()
    {
        return Utils.formatEnergyString(generation) + "/t";
    }

    public float getGenerationFill()
    {
        return Utils.normalizeClamped(generation, 0, maxGeneration) * 90;
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityDamGenerator;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        energyHandler.ifPresent(energy->compound.put("energy", energy.serializeNBT()));
        compound.putInt("rotation", rotation);
        compound.putInt("generation", generation);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        energyHandler.ifPresent(energy->energy.deserializeNBT(compound.getCompound("energy")));
        rotation = compound.getInt("rotation");
        generation = compound.getInt("generation");
        super.load(state, compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        TileEntityDamGenerator masterTE = getMaster();
        if (masterTE == null || side == null) return super.getCapability(cap, side);

        if(cap == CapabilityEnergy.ENERGY &&
                side == Direction.UP &&
                worldPosition.equals(masterTE.getBlockPos().above()))
            return getMaster().energyHandler.cast();
        return super.getCapability(cap, side);
    }
}
