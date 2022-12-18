package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x2x3MachineBase;
import com.cassiokf.industrialrenewal.blocks.BlockTransformer;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockEntityTransformer extends BlockEntity3x2x3MachineBase<BlockEntityTransformer> {

    public int MAX_CAPACITY = Config.TRANSFORMER_TRANSFER_RATE.get();
    public int TRANSFER_SPEED = Config.TRANSFORMER_TRANSFER_RATE.get();
    private int tick = 0;

    public CustomEnergyStorage energyStorage = new CustomEnergyStorage(MAX_CAPACITY, TRANSFER_SPEED, TRANSFER_SPEED){
        @Override
        public void onEnergyChange() {
            super.onEnergyChange();
            sync();
        }
    };
    public LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);

    public CustomEnergyStorage energyDummy = new CustomEnergyStorage(0);
    public LazyOptional<CustomEnergyStorage> dummyHandler = LazyOptional.of(()->energyDummy);

    public BlockEntityTransformer(BlockPos pos, BlockState state) {
        super(ModBlockEntity.TRANSFORMER_TILE.get(), pos, state);
    }

    public void tick() {
        if(!level.isClientSide && isMaster()){
            if(extract()){
                requestEnergy();
                extractEnergy();
            }
        }
    }

    public boolean extract(){
        return getBlockState().is(ModBlocks.TRANSFORMER.get()) && getBlockState().getValue(BlockTransformer.OUTPUT) == 2;
    }

    public Direction getFacing(){
        return getBlockState().is(ModBlocks.TRANSFORMER.get())? getBlockState().getValue(BlockTransformer.FACING): Direction.NORTH;
    }

    public void extractEnergy(){
        if(level == null) return;
        BlockPos targetLocation = worldPosition.below().relative(getFacing().getOpposite(), 2);
        BlockEntity te = level.getBlockEntity(targetLocation);
        if(te != null){
            IEnergyStorage teEnergyStorage = te.getCapability(CapabilityEnergy.ENERGY, getFacing()).orElse(null);
            if(teEnergyStorage != null){
                int amount = teEnergyStorage.receiveEnergy(energyStorage.extractEnergy(TRANSFER_SPEED, true), true);
                teEnergyStorage.receiveEnergy(energyStorage.extractEnergy(amount, false), false);
            }
        }
    }

    public void requestEnergy(){
        BlockEntityHVIsolator isolator = getIsolator();
        if(isolator != null){
            Set<BlockPos> allNodesPos = isolator.allNodes;
            Set<BlockEntityTransformer> availableTransformers = allNodesPos.stream()
                    .filter(x -> level.getBlockEntity(x.below()) instanceof BlockEntityTransformer)
                    .map(x -> (BlockEntityTransformer) level.getBlockEntity(x.below())).filter(Objects::nonNull)
                    .filter(x -> x.isMaster() && !x.extract())
                    .collect(Collectors.toSet());

            int quota = Math.min(TRANSFER_SPEED, this.energyStorage.getMaxEnergyStored() - this.energyStorage.getEnergyStored());

            for (BlockEntityTransformer transformer : availableTransformers){
                EnergyStorage e = transformer.energyStorageHandler.orElse(null);
                if(e != null){
                    int amount = this.energyStorage.receiveEnergy(e.extractEnergy(quota, true), true);
                    this.energyStorage.receiveEnergy(e.extractEnergy(quota, false), false);
//                    Utils.debug("REQUEST", worldPosition, transformer, quota, amount, this.energyStorage.getMaxEnergyStored()-this.energyStorage.getEnergyStored());
                    quota -= amount;
                }
                if(quota <= 0)
                    break;
            }
        }
    }

    public boolean hasIsolator(){
        if(level == null) return false;
        return level.getBlockEntity(worldPosition.above()) instanceof BlockEntityHVIsolator;
    }

    public BlockEntityHVIsolator getIsolator(){
        if(level == null) return null;
        BlockEntity TE = level.getBlockEntity(worldPosition.above());
        if(TE instanceof BlockEntityHVIsolator)
            return (BlockEntityHVIsolator)TE;
        return null;
    }

    public String getGenerationText(){
        return extract()? "EXTRACTING" : "INSERTING";
    }

    public float getGenerationFill(){
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage == null)
            return 0;
        float currentAmount = iEnergyStorage.getEnergyStored();
        float totalCapacity = iEnergyStorage.getMaxEnergyStored();
//        Utils.debug("G", currentAmount, totalCapacity);
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 90f;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction masterFacing = getFacing();
        BlockPos masterPos = super.masterPos!=null ? super.masterPos : worldPosition;

        if (side == null)
            return super.getCapability(cap, side);

        if(cap == CapabilityEnergy.ENERGY){
            if(worldPosition.equals(masterPos.below().relative(masterFacing.getOpposite())) && side == masterFacing.getOpposite()){
                if(getMaster() != null){
                    if(getMaster().extract())
                        return getMaster().dummyHandler.cast();
                    else
                        return getMaster().energyStorageHandler.cast();
                }
                //return dummyHandler.cast();
            }
        }


        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        super.load(compoundTag);
    }
}
