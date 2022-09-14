package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockEnergyCable;
import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockPipeBase;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
//import com.cassiokf.IndustrialRenewal.util.MultiBlockHelper;
//import com.cassiokf.IndustrialRenewal.util.VoltsEnergyContainer;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class TileEntityEnergyCable extends TileEntityMultiBlocksTube<TileEntityEnergyCable> implements ICapabilityProvider {

    public final LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);

    public TileEntityEnergyCable(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(getMaxEnergyToTransport(), getMaxEnergyToTransport(), getMaxEnergyToTransport())
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityEnergyCable.this.setChanged();
            }
        };
    }

    @Override
    public void doTick() {
        // TODO: find a better way to transfer power from slave to master node.
        if(!level.isClientSide && !isMaster()){
            IEnergyStorage storage = energyStorage.orElse(null);
            //Utils.debug("\npos, energy, max isMaster ", worldPosition, storage.getEnergyStored(), storage.getMaxEnergyStored(), isMaster());
//            TileEntityEnergyCable master = getMaster();
//            master.energyStorage.ifPresent();
            getMaster().energyStorage.ifPresent(e->{
                if(e.getEnergyStored() < e.getMaxEnergyStored())
                    e.receiveEnergy(storage.extractEnergy(Math.min(storage.getEnergyStored(), getMaxEnergyToTransport()), false), false);
            });
        }
        if (!level.isClientSide && isMaster())
        {
            final Map<BlockPos, Direction> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            IEnergyStorage thisStorage = energyStorage.orElse(null);
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
        TileEntity te = level.getBlockEntity(otherPos);
        return instanceOf(te);
    }

    public boolean canConnectToCapability(Direction neighborDirection)
    {
        BlockPos otherPos = worldPosition.relative(neighborDirection);
        BlockState state = level.getBlockState(otherPos);
        TileEntity te = level.getBlockEntity(otherPos);
        return !(state.getBlock() instanceof BlockEnergyCable)
                && te != null
                && te.getCapability(CapabilityEnergy.ENERGY, neighborDirection.getOpposite()).isPresent();
    }

    public int moveEnergy(boolean simulate, int validOutputs, Map<BlockPos, Direction> mapPosSet)
    {
        int canAccept = 0;
        int out = 0;
        IEnergyStorage thisStorage = energyStorage.orElse(null);
        if(thisStorage == null)
            return 0;

        int realMaxOutput = Math.min(thisStorage.getEnergyStored() / validOutputs, getMaxEnergyToTransport());
        for (BlockPos posM : mapPosSet.keySet())
        {
            TileEntity te = level.getBlockEntity(posM);
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
            TileEntity te = level.getBlockEntity(currentPos);
            boolean hasMachine = te != null
                    && !(te instanceof TileEntityEnergyCable)
                    && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent();

//            if(te == null)
//                return;
//            IEnergyStorage energyStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
//            if (hasMachine && energyStorage!= null && energyStorage.canReceive())

            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                else if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
        }
    }

    public abstract int getMaxEnergyToTransport();

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (facing == null)
            return super.getCapability(capability, facing);

        if (capability == CapabilityEnergy.ENERGY && getMaster() != null)
            return getMaster().energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compoundNBT.put("energy", tag);
        });
        return super.save(compoundNBT);
    }

    @Override
    public void load(BlockState state, CompoundNBT compoundNBT) {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compoundNBT.getCompound("energy")));
        super.load(state, compoundNBT);
    }
}
