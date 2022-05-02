package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockEnergyCable;
import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockPipeBase;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
//import com.cassiokf.IndustrialRenewal.util.MultiBlockHelper;
//import com.cassiokf.IndustrialRenewal.util.VoltsEnergyContainer;
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
        return new CustomEnergyStorage(10240, getMaxEnergyToTransport(), getMaxEnergyToTransport())
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityEnergyCable.this.setChanged();
            }
        };
    }

//    public TileEntityEnergyCable(){
//        super();
//        //super(ModTileEntities.ENERGYCABLE_LV_TILE.get());
//        this.energyContainer = new VoltsEnergyContainer(getMaxEnergyToTransport(), getMaxEnergyToTransport(), getMaxEnergyToTransport())
//        {
//            @Override
//            public int receiveEnergy(int maxReceive, boolean simulate)
//            {
//                return TileEntityEnergyCable.this.onEnergyReceived(maxReceive, simulate);
//            }
//        };
//    }

    @Override
    public void doTick() {
        if (!level.isClientSide && isMaster())
        {
            final Map<BlockPos, Direction> mapPosSet = getPosSet();
            int quantity = mapPosSet.size();
            IEnergyStorage thisStorage = energyStorage.orElse(null);
            energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).setMaxCapacity(Math.max(getMaxEnergyToTransport() * quantity, thisStorage.getEnergyStored())));

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

//    @Nonnull
//    @Override
//    public IModelData getModelData()
//    {
//        ModelDataMap.Builder builder = new ModelDataMap.Builder()
//                .withInitial(MASTER, IRConfig.Main.showMaster.get() && isMaster())
//                .withInitial(SOUTH, canConnectToPipe(Direction.SOUTH))
//                .withInitial(NORTH, canConnectToPipe(Direction.NORTH))
//                .withInitial(EAST, canConnectToPipe(Direction.EAST))
//                .withInitial(WEST, canConnectToPipe(Direction.WEST))
//                .withInitial(UP, canConnectToPipe(Direction.UP))
//                .withInitial(DOWN, canConnectToPipe(Direction.DOWN))
//                .withInitial(CSOUTH, canConnectToCapability(Direction.SOUTH))
//                .withInitial(CNORTH, canConnectToCapability(Direction.NORTH))
//                .withInitial(CEAST, canConnectToCapability(Direction.EAST))
//                .withInitial(CWEST, canConnectToCapability(Direction.WEST))
//                .withInitial(CUP, canConnectToCapability(Direction.UP))
//                .withInitial(CDOWN, canConnectToCapability(Direction.DOWN));
//
//        Block block = getBlockState().getBlock();
//        if (block instanceof BlockFloorCable)
//        {
//            builder = builder
//                    .withInitial(WSOUTH, BlockIndustrialFloor.canConnect(world, pos, Direction.SOUTH))
//                    .withInitial(WNORTH, BlockIndustrialFloor.canConnect(world, pos, Direction.NORTH))
//                    .withInitial(WEAST, BlockIndustrialFloor.canConnect(world, pos, Direction.EAST))
//                    .withInitial(WWEST, BlockIndustrialFloor.canConnect(world, pos, Direction.WEST))
//                    .withInitial(WUP, BlockIndustrialFloor.canConnect(world, pos, Direction.UP))
//                    .withInitial(WDOWN, BlockIndustrialFloor.canConnect(world, pos, Direction.DOWN));
//        } else if (block instanceof BlockPillarEnergyCable)
//        {
//            builder = builder
//                    .withInitial(WSOUTH, BlockPillar.canConnect(world, pos, Direction.SOUTH))
//                    .withInitial(WNORTH, BlockPillar.canConnect(world, pos, Direction.NORTH))
//                    .withInitial(WEAST, BlockPillar.canConnect(world, pos, Direction.EAST))
//                    .withInitial(WWEST, BlockPillar.canConnect(world, pos, Direction.WEST))
//                    .withInitial(WUP, BlockPillar.canConnect(world, pos, Direction.UP))
//                    .withInitial(WDOWN, BlockPillar.canConnect(world, pos, Direction.DOWN));
//        }
//
//        return builder.build();
//    }

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
            if (hasMachine && te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null).canReceive())
                if (!isMasterInvalid()) getMaster().addMachine(currentPos, face);
                else if (!isMasterInvalid()) getMaster().removeMachine(worldPosition, currentPos);
        }
    }

    public abstract int getMaxEnergyToTransport();

//    public int onEnergyReceived(int maxReceive, boolean simulate)
//    {
//        if(level != null)
//        if (level.isClientSide) return 0;
//        if (!isMaster()) return getMaster().onEnergyReceived(maxReceive, simulate);
//
//        if (inUse) return 0; //to prevent stack overflow (IE)
//        inUse = true;
//        int maxTransfer = Math.min(maxReceive, this.energyContainer.getMaxOutput());
//        if (!simulate) potentialEnergy = maxTransfer;
//        if (maxReceive <= 0)
//        {
//            inUse = false;
//            return 0;
//        }
//        List<Integer> out = MultiBlockHelper.outputEnergy(this, maxTransfer, energyContainer.getMaxOutput(), simulate, level);
//        if (!simulate) outPut += out.get(0);
//        outPutCount = out.get(1);
//        inUse = false;
//        return out.get(0);
//    }

//    @Override
//    public void checkForOutPuts()
//    {
//        if(level != null)
//        if (level.isClientSide()) return;
//        for (Direction face : Direction.values())
//        {
//            BlockPos currentPos = worldPosition.relative(face);
//            TileEntity te = level.getBlockEntity(currentPos);
//            boolean hasMachine = te != null && !(te instanceof TileEntityEnergyCable);
////            IEnergyStorage eStorage = null;
////            if (hasMachine) eStorage = te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
////            if (hasMachine && eStorage != null && eStorage.canReceive())
////                addMachine(te, face);
//            if(hasMachine)
//            te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).ifPresent((cap)->{
//                if(cap.canReceive())
//                    addMachine(te, face);
//            });
//        }
//    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && getMaster() != null)
            return energyStorage.cast();
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
