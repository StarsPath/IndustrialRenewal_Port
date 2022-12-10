package com.cassiokf.industrialrenewal.blockentity.dam;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDamGenerator extends BlockEntity3x3x3MachineBase<BlockEntityDamGenerator> {

    private final int energyCapacity = Config.DAM_GENERATOR_ENERGY_CAPACITY.get();
    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(energyCapacity){
        @Override
        public void onEnergyChange() {
            BlockEntityDamGenerator.this.sync();
        }
    };
    public LazyOptional<CustomEnergyStorage> energyHandler = LazyOptional.of(()-> energyStorage);
    private int oldGeneration;
    private int generation;
    private int rotation;

    public static final int maxGeneration = Config.DAM_GENERATOR_RF_PER_TICK.get();//IRConfig.MainConfig.Main.damGeneratorEnergyPerTick;
    public static final int transferRate = Config.DAM_GENERATOR_TRANSFER_RATE.get();


    public BlockEntityDamGenerator(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DAM_GENERATOR.get(), pos, state);
    }

    public void tick() {
        if(!level.isClientSide && isMaster()){
            generation = (int) (Utils.normalizeClamped(rotation, 0, BlockEntityDamTurbine.MAX_PROCESSING/40f) * maxGeneration);
            if (generation > 0)
                energyStorage.receiveEnergy(generation, false);
            BlockEntity te = level.getBlockEntity(worldPosition.above(2));
            if (te != null)
            {
                te.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN).ifPresent(iEnergyStorage -> {
                    int amount = iEnergyStorage.receiveEnergy(transferRate, true);
                    iEnergyStorage.receiveEnergy(energyStorage.extractEnergy(amount, false), false);
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
    public boolean instanceOf(BlockEntity tileEntity) {
        return tileEntity instanceof BlockEntityDamGenerator;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        compoundTag.putInt("rotation", rotation);
        compoundTag.putInt("generation", generation);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        rotation = compoundTag.getInt("rotation");
        generation = compoundTag.getInt("generation");
        super.load(compoundTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockEntityDamGenerator masterTE = getMaster();
        if (masterTE == null || side == null) return super.getCapability(cap, side);

        if(cap == CapabilityEnergy.ENERGY &&
                side == Direction.UP &&
                worldPosition.equals(masterTE.getBlockPos().above()))
            return getMaster().energyHandler.cast();
        return super.getCapability(cap, side);
    }
}
