package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.BlockWindTurbineHead;
import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.item.ItemWindBlade;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.CustomItemStackHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

//import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;

public class TileEntityWindTurbineHead extends TileEntitySyncable implements ITickableTileEntity {

    public LazyOptional<IItemHandler> bladeInv = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private float rotation;
    private int energyGenerated;

    public static final int energyGeneration = Config.WIND_TURBINE_ENERGY_PER_TICK.get();
    private final int energyCapacity = Config.WIND_TURBINE_CAPACITY.get();
    private final int energyTransfer = Config.WIND_TURBINE_TRANSFER_RATE.get();
    private int tickToDamage;

    private final Random random = new Random();

    public TileEntityWindTurbineHead() {
        super(ModTileEntities.WIND_TURBINE_TILE.get());
    }

//    public static int getMaxGeneration()
//    {
//        return 128;
//    }

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return stack.getItem() instanceof ItemWindBlade;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityWindTurbineHead.this.sync();
                //level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
            }
        };
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(energyCapacity, energyTransfer, energyTransfer)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityWindTurbineHead.this.sync();
            }
        };
    }

    @Override
    public void setRemoved() {
        ItemStack stack = bladeInv.orElse(null).getStackInSlot(0);
        Block.popResource(level, worldPosition, stack);
        super.setRemoved();
    }

    @Override
    public void tick() {
        if (!level.isClientSide)
        {
            //Generate Energy
            IEnergyStorage thisEnergy = energyStorage.orElse(null);
            if (hasBlade())
            {
                int energyGen = Math.round(energyGeneration * getEfficiency());
                energyGenerated = thisEnergy.receiveEnergy(energyGen, false);
                if (++tickToDamage >= 1200 && energyGen > 0)
                {
                    tickToDamage = 0;
                    ItemStack bladeInvStack = bladeInv.orElse(null).getStackInSlot(0);
//                    Utils.debug("damage", bladeInvStack.getDamageValue());

                    if(bladeInvStack != null){
//                        if(bladeInvStack.getDamageValue() <= 0) {
////                            Utils.debug("damage", bladeInvStack.getDamageValue());
//                            bladeInvStack.shrink(1);
////                            Utils.debug("inv", bladeInvStack);
//                        }
                        if(bladeInvStack.hurt(1, new Random(), null))
                        {
                            bladeInvStack.shrink(1);
                        }
                    }
                }
            } else
            {
                energyGenerated = 0;
            }
            //OutPut Energy
            if (thisEnergy.getEnergyStored() > 0)
            {
                TileEntity te = level.getBlockEntity(worldPosition.below());
                if (te != null)
                {
                    IEnergyStorage downE = te.getCapability(CapabilityEnergy.ENERGY, Direction.UP).orElse(null);
                    if (downE != null && downE.canReceive())
                    {
                        thisEnergy.extractEnergy(downE.receiveEnergy(thisEnergy.extractEnergy(1024, true), false), false);
                        this.setChanged();
                    }
                }
            }
        } else
        {
            rotation += 4.5f * getEfficiency();
            if (rotation > 360) rotation = 0;
        }
    }

    public IItemHandler getBladeHandler()
    {
        return bladeInv.orElse(null);
    }

    public float getRotation()
    {
        return -rotation;
    }

    public boolean hasBlade()
    {
        return !bladeInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    private float getEfficiency()
    {
        float weatherModifier;
        if (level.isThundering())
        {
            weatherModifier = 1f;
        } else if (level.isRaining())
        {
            weatherModifier = 0.9f;
        } else
        {
            weatherModifier = 0.8f;
        }

        float heightModifier;
        float posMin = -2040f;
        if (worldPosition.getY() - 62 <= 0) heightModifier = 0;
        else heightModifier = (worldPosition.getY() - posMin) / (255 - posMin);
        heightModifier = MathHelper.clamp(heightModifier, 0, 1);

        return weatherModifier * heightModifier;
    }


    public Direction getBlockFacing()
    {
        return getBlockState().getValue(BlockWindTurbineHead.FACING);
    }

    @Override
    public double getViewDistance() {
        return super.getViewDistance();
    }


//    @Override
//    @Nullable
//    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
//    {
//        if (capability == CapabilityEnergy.ENERGY && facing == Direction.DOWN)
//            return energyStorage.cast();
//        return super.getCapability(capability, facing);
//    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        bladeInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        compound.putInt("generation", this.energyGenerated);
        compound.putInt("damageTick", tickToDamage);

        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("StoredIR")));
        CompoundNBT invTag = compound.getCompound("inv");
        bladeInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        energyGenerated = compound.getInt("generation");
        tickToDamage = compound.getInt("damageTick");

        super.load(state, compound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
