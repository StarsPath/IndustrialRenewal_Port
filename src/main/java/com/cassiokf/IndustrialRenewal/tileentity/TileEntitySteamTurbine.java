package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModFluids;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class TileEntitySteamTurbine extends TileEntity3x3MachineBase<TileEntitySteamTurbine> implements ITickableTileEntity {

    public CustomFluidTank waterTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamTurbine.this.sync();
        }
    };
    public CustomFluidTank steamTank = new CustomFluidTank(320000)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            //TODO MAKE STEAM TAG
            return stack != null;
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamTurbine.this.sync();
        }
    };

    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);

    //TODO: add to config
    private float volume = 0.8f;//IRConfig.MAIN.TurbineVolume.get().floatValue();
    private int maxRotation = 16000;
    private int rotation;
    private int energyPerTick = 512;//IRConfig.Main.steamTurbineEnergyPerTick.get();
    private int oldRotation;
    private int steamPerTick = 250;//IRConfig.Main.steamTurbineSteamPerTick.get();

    private int steamBoilerConversionFactor = 5;
    private boolean firstLoad = false;


    public TileEntitySteamTurbine()
    {
        super(ModTileEntities.STEAM_TURBINE_TILE.get());
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(100000, 0, 10240)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntitySteamTurbine.this.sync();
            }
        };
    }

    public TileEntitySteamTurbine(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onLoad() {
        if(!level.isClientSide && isMaster()){
            TileEntitySteamTurbine masterTE = getMaster();
            Direction face = masterTE.getMasterFacing();
            TileEntitySteamTurbine steamTankTile = ((TileEntitySteamTurbine) level.getBlockEntity(getBlockPos().above()));
            if (steamTankTile != null) steamTank = steamTankTile.steamTank;

            TileEntitySteamTurbine waterOutputTile = ((TileEntitySteamTurbine) level.getBlockEntity(getBlockPos().below().relative(face)));
            if(waterOutputTile != null) waterTank = waterOutputTile.waterTank;

//            TileEntitySteamTurbine energyOutputTile = ((TileEntitySteamTurbine) level.getBlockEntity(getBlockPos().below().relative(face.getOpposite()).relative(face.getCounterClockWise())));
//            if(energyOutputTile != null) energyStorage = energyOutputTile.energyStorage;
        }
        super.onLoad();
    }

    @Override
    public void tick() {
        if (!level.isClientSide)
        {
            if (this.isMaster())
            {
                if(!firstLoad){
                    firstLoad = true;
                    this.onLoad();
                }
                this.sync();
                //Utils.debug("energy tile", energyStorage, energyStorage.orElse(null).getEnergyStored(), energyStorage.orElse(null).getMaxEnergyStored());
                if (this.steamTank.getFluidAmount() > 0)
                {
                    FluidStack stack = steamTank.drainInternal(steamPerTick, IFluidHandler.FluidAction.EXECUTE);
                    float amount = stack != null ? stack.getAmount() : 0f;
                    FluidStack waterStack = new FluidStack(Fluids.WATER, Math.round(amount / (float) steamBoilerConversionFactor));
                    waterTank.fillInternal(waterStack, IFluidHandler.FluidAction.EXECUTE);
                    float factor = amount / (float) steamPerTick;
                    rotation += (10 * factor);
                } else rotation -= 4;

                IEnergyStorage thisEnergy = energyStorage.orElse(null);
                if (rotation >= 6000 && thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored())
                {
                    //int energy = Math.min(thisEnergy.getMaxEnergyStored(), thisEnergy.getEnergyStored() + getEnergyProduction());
                    int energy = getEnergyProduction();
                    energyStorage.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(energy));
                    rotation -= 4;
                }

                rotation -= 2;
                rotation = MathHelper.clamp(rotation, 0, maxRotation);


                Direction facing = getMasterFacing();
                TileEntity eTE = level.getBlockEntity(worldPosition.relative(facing.getOpposite()).below().relative(facing.getCounterClockWise(), 2));
                if (eTE != null && thisEnergy.getEnergyStored() > 0)
                {
                    IEnergyStorage upTank = eTE.getCapability(CapabilityEnergy.ENERGY, facing.getClockWise()).orElse(null);
                    thisEnergy.extractEnergy(upTank.receiveEnergy(thisEnergy.extractEnergy(10240, true), false), false);
                }
                TileEntity wTE = level.getBlockEntity(worldPosition.relative(facing, 2).below());
                if (wTE != null && waterTank.getFluidAmount() > 0)
                {
                    IFluidHandler wTank = wTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                    waterTank.drain(wTank.fill(waterTank.drain(2000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                }

                if (oldRotation != rotation)
                {
                    this.sync();
                    oldRotation = rotation;
                }
            }
            else
            {
                //updateSound(getPitch());
            }
        }
    }

//    @Override
//    public float getPitch()
//    {
//        return Math.max(getRotation(), 0.1F);
//    }

//    @Override
//    public float getVolume()
//    {
//        return volume;
//    }

//    private void updateSound(float pitch)
//    {
//        if (!world.isRemote) return;
//        if (this.rotation > 0)
//        {
//            IRSoundHandler.playRepeatableSound(this, SoundsRegistration.MOTOR_ROTATION.get(), volume, pitch);
//        } else
//        {
//            IRSoundHandler.stopTileSound(pos);
//        }
//    }

//    @Override
//    public void onMasterBreak()
//    {
//        if (world.isRemote) IRSoundHandler.stopTileSound(pos);
//    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntitySteamTurbine;
    }

    private int getEnergyProduction()
    {
        int energy = Math.round(energyPerTick * getRotation());
        float factor = waterTank.getFluidAmount() == 0 ? 1f : Math.max(0.5f, Math.min(1f, ((float) waterTank.getCapacity() / (float) this.waterTank.getFluidAmount()) - 0.5f));
        energy = Math.round(energy * factor);
        energy = MathHelper.clamp(energy, 0, energyPerTick);
        return energy;
    }

    public String getWaterText()
    {
        return Blocks.WATER.getName().getString();
    }

    public String getSteamText()
    {
        return ModFluids.STEAM_BLOCK.get().getName().getString();
    }


    public String getGenerationText()
    {
        int energy = (rotation >= 6000 && energyStorage.orElse(null).getEnergyStored() < energyStorage.orElse(null).getMaxEnergyStored()) ? getEnergyProduction() : 0;
        return Utils.formatEnergyString(energy) + "/t";
    }

    public String getEnergyText()
    {
        int energy = energyStorage.orElse(null).getEnergyStored();
        return Utils.formatEnergyString(energy);
    }

    public String getRotationText()
    {
        return rotation / 10 + " rpm";
    }

    public float getEnergyFill() //0 ~ 1
    {
        float currentAmount = energyStorage.orElse(null).getEnergyStored() / 1000F;
        float totalCapacity = energyStorage.orElse(null).getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        //Utils.debug("energy", currentAmount, totalCapacity);
        return currentAmount;
    }

    private float getRotation()
    {
        return Utils.normalize(this.rotation, 0, this.maxRotation);
    }

    public float getGenerationFill() //0 ~ 180
    {
        float currentAmount = ((rotation >= 6000 && energyStorage.orElse(null).getEnergyStored() < energyStorage.orElse(null).getMaxEnergyStored()) ? getEnergyProduction() : 0) / 100f;
        float totalCapacity = energyPerTick / 100f;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 90f;
    }

    public float getWaterFill() //0 ~ 180
    {
        float currentAmount = waterTank.getFluidAmount() / 1000f;
        float totalCapacity = waterTank.getCapacity() / 1000f;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getSteamFill() //0 ~ 180
    {
        float currentAmount = steamTank.getFluidAmount() / 1000f;
        float totalCapacity = steamTank.getCapacity() / 1000f;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getRotationFill() //0 ~ 180
    {
        return getRotation() * 140f;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT waterTag = new CompoundNBT();
        CompoundNBT steamTag = new CompoundNBT();
        waterTank.writeToNBT(waterTag);
        steamTank.writeToNBT(steamTag);
        compound.put("water", waterTag);
        compound.put("steam", steamTag);
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        compound.putInt("heat", rotation);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        CompoundNBT waterTag = compound.getCompound("water");
        CompoundNBT steamTag = compound.getCompound("steam");
        waterTank.readFromNBT(waterTag);
        steamTank.readFromNBT(steamTag);
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("energy")));
        this.rotation = compound.getInt("heat");
        super.load(state, compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        TileEntitySteamTurbine masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getBlockPos();

        if (facing == Direction.UP && worldPosition.equals(masterPos.above()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.steamTank).cast();
        if (facing == face && worldPosition.equals(masterPos.below().relative(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (facing == face.getCounterClockWise() && worldPosition.equals(masterPos.below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == CapabilityEnergy.ENERGY)
            return masterTE.energyStorage.cast();

        return super.getCapability(capability, facing);
    }
}
