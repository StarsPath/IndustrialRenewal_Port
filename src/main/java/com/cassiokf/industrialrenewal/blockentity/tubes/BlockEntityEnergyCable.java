package com.cassiokf.industrialrenewal.blockentity.tubes;

import com.cassiokf.industrialrenewal.blocks.transport.BlockEnergyCable;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class BlockEntityEnergyCable extends BlockEntityMultiBlocksTube<BlockEntityEnergyCable> implements ICapabilityProvider {


    public final CustomEnergyStorage energyStorage = new CustomEnergyStorage(getMaxEnergyToTransport(), getMaxEnergyToTransport(), getMaxEnergyToTransport())
    {
        @Override
        public void onEnergyChange()
        {
            sync();
        }
    };

    public final LazyOptional<CustomEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);


    public BlockEntityEnergyCable(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state);
    }


    public void tick() {
        if(!level.isClientSide && !isMaster()){
            IEnergyStorage storage = energyStorageHandler.orElse(null);
//            Utils.debug("\npos, energy, max isMaster ", worldPosition, storage.getEnergyStored(), storage.getMaxEnergyStored(), isMaster());
//            TileEntityEnergyCable master = getMaster();
//            master.energyStorage.ifPresent();
            getMaster().energyStorageHandler.ifPresent(e->{
                if(e.getEnergyStored() < e.getMaxEnergyStored())
                    e.receiveEnergy(storage.extractEnergy(Math.min(storage.getEnergyStored(), getMaxEnergyToTransport()), false), false);
            });
        }
        if (!level.isClientSide && isMaster())
        {
            final Map<BlockPos, Direction> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            IEnergyStorage thisStorage = energyStorageHandler.orElse(null);
//            Utils.debug("\npos, energy, max isMaster ", worldPosition, thisStorage.getEnergyStored(), thisStorage.getMaxEnergyStored(), isMaster());

//            energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(getMaxEnergyToTransport() * quantity, thisStorage.getEnergyStored())));

            if (quantity > 0)
            {
                int canAccept = moveEnergy(true, 1, mapPosSet);
                outPut = canAccept > 0 ? moveEnergy(false, canAccept, mapPosSet) : 0;
            } else outPut = 0;

            outPutCount = quantity;
            if ((oldOutPut != outPut) || (oldOutPutCount != outPutCount))
            {
                oldOutPut = outPut;
                oldOutPutCount = outPutCount;
                this.sync();
            }
        }
    }

    public boolean canConnectToPipe(Direction neighborDirection)
    {
        BlockPos otherPos = worldPosition.relative(neighborDirection);
        BlockEntity te = level.getBlockEntity(otherPos);
        return instanceOf(te);
    }

    public boolean canConnectToCapability(Direction neighborDirection)
    {
        BlockPos otherPos = worldPosition.relative(neighborDirection);
        BlockState state = level.getBlockState(otherPos);
        BlockEntity te = level.getBlockEntity(otherPos);
        return !(state.getBlock() instanceof BlockEnergyCable)
                && te != null
                && te.getCapability(CapabilityEnergy.ENERGY, neighborDirection.getOpposite()).isPresent();
    }

    public int moveEnergy(boolean simulate, int validOutputs, Map<BlockPos, Direction> mapPosSet)
    {
        int canAccept = 0;
        int out = 0;
        IEnergyStorage thisStorage = energyStorageHandler.orElse(null);
        if(thisStorage == null)
            return 0;

        int realMaxOutput = Math.min(thisStorage.getEnergyStored() / validOutputs, getMaxEnergyToTransport());
        for (BlockPos posM : mapPosSet.keySet())
        {
            BlockEntity te = level.getBlockEntity(posM);
            Direction face = mapPosSet.get(posM).getOpposite();
            if (te != null)
            {
                IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face).orElse(null);
                if (energyStorage != null && energyStorage.canReceive())
                {
                    int energy = energyStorage.receiveEnergy(thisStorage.extractEnergy(realMaxOutput, true), simulate);
                    if (simulate)
                    {
                        if (energy > 0) canAccept++;
                    } else
                    {
                        out += energy;
                        thisStorage.extractEnergy(energy, false);
                    }
                }
            }
        }
        return simulate ? canAccept : out;
    }

    @Override
    public void checkForOutPuts(BlockPos bPos)
    {
        if (level.isClientSide) return;
        for (Direction face : Direction.values())
        {
            BlockPos currentPos = worldPosition.relative(face);
            BlockEntity te = level.getBlockEntity(currentPos);
            boolean hasMachine = te != null
                    && !(te instanceof BlockEntityEnergyCable)
                    && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();

            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                else if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
        }
    }

    @Override
    public void invalidateCaps() {
        energyStorageHandler.invalidate();
        super.invalidateCaps();
    }

    public abstract int getMaxEnergyToTransport();

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (facing == null)
            return super.getCapability(capability, facing);

        if (capability == CapabilityEnergy.ENERGY && getMaster() != null)
            return getMaster().energyStorageHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        super.load(compoundTag);
    }
}
