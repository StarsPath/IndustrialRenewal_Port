package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModFluids;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

public class BlockEntitySteamTurbine extends BlockEntity3x3x3MachineBase<BlockEntitySteamTurbine> {

    private final int waterTankCapacity = Config.STEAM_TURBINE_WATER_TANK_CAPACITY.get();
    private final int steamTankCapacity = Config.STEAM_TURBINE_STEAM_TANK_CAPACITY.get();
    private final int energyCapacity = Config.STEAM_TURBINE_ENERGY_CAPACITY.get();
    private final int energyCapacityExtract = Config.STEAM_TURBINE_ENERGY_EXTRACT.get();


    public CustomFluidTank waterTank = new CustomFluidTank(waterTankCapacity)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            BlockEntitySteamTurbine.this.sync();
        }
    };
    public CustomFluidTank steamTank = new CustomFluidTank(steamTankCapacity)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            //TODO MAKE STEAM TAG
            return stack != null && stack.getFluid().is(ModFluids.STEAM_TAG);
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            BlockEntitySteamTurbine.this.sync();
        }
    };

    private float volume = 0.8f;//IRConfig.MAIN.TurbineVolume.get().floatValue();
    private final int maxRotation = Config.STEAM_TURBINE_MAX_ROTATION.get();
    private int rotation;
    private final int energyPerTick = Config.STEAM_TURBINE_ENERGY_PER_TICK.get();//IRConfig.Main.steamTurbineEnergyPerTick.get();
    private int oldRotation;
    private final int steamPerTick = Config.STEAM_TURBINE_STEAM_PER_TICK.get();//IRConfig.Main.steamTurbineSteamPerTick.get();

    private final float steamBoilerConversionFactor = Config.STEAM_TURBINE_STEAM_WATER_CONVERSION.get();
    private boolean firstLoad = false;


    public BlockEntitySteamTurbine(BlockPos pos, BlockState state)
    {
        super(ModBlockEntity.STEAM_TURBINE_TILE.get(), pos, state);
    }

    private CustomEnergyStorage energyStorage = new CustomEnergyStorage(energyCapacity, 0, energyCapacityExtract)
    {
        @Override
        public void onEnergyChange()
        {
            BlockEntitySteamTurbine.this.sync();
        }
    };

    private LazyOptional<IEnergyStorage> energyStorageHandler = LazyOptional.of(()->energyStorage);


    public void tick() {
        if(level == null) return;
        if (!level.isClientSide)
        {
            if (this.isMaster())
            {
                this.sync();
                if (this.steamTank.getFluidAmount() > 0)
                {
                    FluidStack stack = steamTank.drainInternal(steamPerTick, IFluidHandler.FluidAction.EXECUTE);
                    float amount = stack != null ? stack.getAmount() : 0f;
                    FluidStack waterStack = new FluidStack(Fluids.WATER, Math.round(amount / steamBoilerConversionFactor));
                    waterTank.fillInternal(waterStack, IFluidHandler.FluidAction.EXECUTE);
                    float factor = amount / (float) steamPerTick;
                    if(amount >= steamPerTick)
                        rotation += (10 * factor);
                } else rotation -= 4;

                IEnergyStorage thisEnergy = energyStorageHandler.orElse(null);
                if (energyStorageHandler.isPresent() && rotation >= 6000 && thisEnergy.getEnergyStored() < thisEnergy.getMaxEnergyStored())
                {
                    //int energy = Math.min(thisEnergy.getMaxEnergyStored(), thisEnergy.getEnergyStored() + getEnergyProduction());
                    int energy = getEnergyProduction();
                    energyStorageHandler.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(energy));
                    rotation -= 4;
                }

                rotation -= 2;
                rotation = Mth.clamp(rotation, 0, maxRotation);


                Direction facing = getMasterFacing();
                BlockEntity eTE = level.getBlockEntity(worldPosition.relative(facing.getOpposite()).below().relative(facing.getCounterClockWise(), 2));
                if (eTE != null && thisEnergy.getEnergyStored() > 0)
                {
                    eTE.getCapability(CapabilityEnergy.ENERGY, facing.getClockWise()).ifPresent(tank ->{
                        thisEnergy.extractEnergy(tank.receiveEnergy(thisEnergy.extractEnergy(10240, true), false), false);
                    });
                }
                BlockEntity wTE = level.getBlockEntity(worldPosition.relative(facing, 2).below());
                if (wTE != null && waterTank.getFluidAmount() > 0)
                {
                    wTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).ifPresent(tank ->{
                        waterTank.drain(tank.fill(waterTank.drain(2000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    });

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

    @Override
    public boolean instanceOf(BlockEntity tileEntity)
    {
        return tileEntity instanceof BlockEntitySteamTurbine;
    }

    private int getEnergyProduction()
    {
        int energy = Math.round(energyPerTick * getRotation());
        float factor = waterTank.getFluidAmount() == 0 ? 1f : Math.max(0.5f, Math.min(1f, ((float) waterTank.getCapacity() / (float) this.waterTank.getFluidAmount()) - 0.5f));
        energy = Math.round(energy * factor);
        energy = Mth.clamp(energy, 0, energyPerTick);
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
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage == null)
            return  "NULL /t";
        int energy = (rotation >= 6000 && iEnergyStorage.getEnergyStored() < iEnergyStorage.getMaxEnergyStored()) ? getEnergyProduction() : 0;
        return Utils.formatEnergyString(energy) + "/t";
    }

    public String getEnergyText()
    {
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage == null)
            return  "NULL";
        int energy = iEnergyStorage.getEnergyStored();
        return Utils.formatEnergyString(energy);
    }

    public String getRotationText()
    {
        return rotation / 10 + " rpm";
    }

    public float getEnergyFill() //0 ~ 1
    {
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage == null)
            return 0;
        float currentAmount = iEnergyStorage.getEnergyStored() / 1000F;
        float totalCapacity = iEnergyStorage.getMaxEnergyStored() / 1000F;
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
        IEnergyStorage iEnergyStorage = energyStorageHandler.orElse(null);
        if(iEnergyStorage == null)
            return 0;
        float currentAmount = ((rotation >= 6000 && iEnergyStorage.getEnergyStored() < iEnergyStorage.getMaxEnergyStored()) ? getEnergyProduction() : 0) / 100f;
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
    protected void saveAdditional(CompoundTag compoundTag) {
        CompoundTag waterTag = new CompoundTag();
        CompoundTag steamTag = new CompoundTag();
        waterTank.writeToNBT(waterTag);
        steamTank.writeToNBT(steamTag);
        compoundTag.put("water", waterTag);
        compoundTag.put("steam", steamTag);
        compoundTag.putInt("energy", energyStorage.getEnergyStored());
        compoundTag.putInt("heat", rotation);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        CompoundTag waterTag = compoundTag.getCompound("water");
        CompoundTag steamTag = compoundTag.getCompound("steam");
        waterTank.readFromNBT(waterTag);
        steamTank.readFromNBT(steamTag);
        energyStorage.setEnergy(compoundTag.getInt("energy"));
        this.rotation = compoundTag.getInt("heat");
        super.load(compoundTag);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        BlockEntitySteamTurbine masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = getMasterFacing();
        BlockPos masterPos = masterTE.getBlockPos();

        if (facing == null)
            return super.getCapability(capability, facing);

        if (facing == Direction.UP && worldPosition.equals(masterPos.above()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.steamTank).cast();
        if (facing == face && worldPosition.equals(masterPos.below().relative(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (facing == face.getCounterClockWise() && worldPosition.equals(masterPos.below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == CapabilityEnergy.ENERGY)
            return masterTE.energyStorageHandler.cast();

        return super.getCapability(capability, facing);
    }
}
