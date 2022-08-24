package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.BlockSteamBoiler;
import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModFluids;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.CustomItemStackHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.task.CongregateTask;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntitySteamBoiler extends TileEntity3x3x3MachineBase<TileEntitySteamBoiler> implements ITickableTileEntity {

    private int waterTankCapacity = Config.STEAM_BOILER_WATER_TANK_CAPACITY.get();
    private int SteamTankCapacity = Config.STEAM_BOILER_STEAM_TANK_CAPACITY.get();
    private int FuelTankCapacity = Config.STEAM_BOILER_FUEL_TANK_CAPACITY.get();

    public CustomFluidTank waterTank = new CustomFluidTank(waterTankCapacity)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            //Utils.debug("water tank input", stack, stack.getFluid().equals(Fluids.WATER.getFluid()));
            return stack != null && stack.getFluid().equals(Fluids.WATER.getFluid());
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.sync();
        }
    };
    public CustomFluidTank steamTank = new CustomFluidTank(SteamTankCapacity)
    {
        @Override
        public boolean canFill()
        {
            return false;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.sync();
        }
    };

    public CustomFluidTank fuelTank = new CustomFluidTank(FuelTankCapacity)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
//            Utils.debug("Fluid", stack.getFluid().getRegistryName());
            return stack != null && Config.getFuelHash().containsKey(stack.getFluid().getRegistryName().toString());
            //TODO FLUIDSET CONFIG
//            return stack != null && IRConfig.Main.fluidFuel.containsKey(stack.getFluid().getDefaultState().getBlockState().getBlock().getNameTextComponent().getString());
//            return true;
        }

        @Override
        public void onContentsChanged()
        {
            TileEntitySteamBoiler.this.sync();
        }
    };

    //public LazyOptional<IItemHandler> fireBoxInv = LazyOptional.of(this::createFireboxInv);

    public LazyOptional<IItemHandler> solidFuelInv = LazyOptional.of(this::createFuelInv);

    private int type;

    private int maxHeat = Config.STEAM_BOILER_MAX_HEAT.get();
    private int heat;
    private int oldHeat;
    private int waterPtick = Config.STEAM_BOILER_WATER_PER_TICK.get();
    //private int waterPtick = IRConfig.Main.steamBoilerWaterPerTick.get();

    private int fuelTime;
    private int maxFuelTime;
    private int oldFuelTime;

    private int solidPerTick = Config.STEAM_BOILER_SOLID_FUEL_PER_TICK.get();
    private int fluidPerTick = Config.STEAM_BOILER_LIQUID_FUEL_PER_TICK.get();

    private boolean firstLoad = false;
    private boolean fuelLoaded = false;

    float steamBoilerConversionConfig = Config.STEAM_BOILER_WATER_STEAM_CONVERSION.get();

    public TileEntitySteamBoiler() {
        super(ModTileEntities.STEAM_BOILER_TILE.get());
    }

    public TileEntitySteamBoiler(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    private IItemHandler createFuelInv()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return FurnaceTileEntity.isFuel(stack);
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntitySteamBoiler.this.sync();
            }
        };
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        //fireBoxInv.invalidate();
        solidFuelInv.invalidate();
    }

    @Override
    public void onMasterBreak() {
        dropItemsInGround(getDrop());
        super.onMasterBreak();
    }

    public ItemStack getDrop(){
        switch (type){
            case 1: return new ItemStack(ModItems.fireBoxSolid, 1);
            case 2: return new ItemStack(ModItems.fireBoxFluid, 1);
        }
        return null;
    }

    public boolean canRun(){
        switch (type){
            case 1:
                if(fuelTime >= solidPerTick && !waterTank.isEmpty())
                    return true;
                break;
            case 2:
                if(fuelTime >= fluidPerTick && !waterTank.isEmpty())
                    return true;
                break;
        }
        return false;
    }

    ;private int tick = 0;

    @Override
    public void tick() {
        if (this.isMaster() && !level.isClientSide)
        {
            if(this.type == 0) fuelLoaded = false;
            if (this.type > 0)
            {
                //Fuel to Heat
                this.sync();
                switch (this.type)
                {
                    default:
                    case 1:
                        IItemHandler iItemHandler = solidFuelInv.orElse(null);
                        if(iItemHandler != null) {
                            if (fuelTime >= solidPerTick || !iItemHandler.getStackInSlot(0).isEmpty()) {
                                ItemStack fuel = iItemHandler.getStackInSlot(0);
                                if (fuelTime <= 0) {
                                    fuelTime = ForgeHooks.getBurnTime(fuel);
                                    maxFuelTime = fuelTime;
                                    fuel.shrink(1);
                                }
                                heat += 8;
                                fuelTime -= solidPerTick;
                            } else heat -= 2;
                        }
                        break;
                    case 2:
                        if (fuelTime >= fluidPerTick || this.fuelTank.getFluidAmount() > 0)
                        {
                            FluidStack fuel = this.fuelTank.getFluid();
                            if (fuelTime <= 0)
                            {
                                //TODO Fluid Set Config
                                fuelTime = Config.getFuelHash().get(fuel.getFluid().getRegistryName().toString());//IRConfig.Main.fluidFuel.get(fuel.getFluid().getRegistryName().toString()) != null ? IRConfig.Main.fluidFuel.get(fuel.getFluid().getRegistryName().toString()) : 0;
                                maxFuelTime = fuelTime;
                                fuel.setAmount(fuel.getAmount() - FluidAttributes.BUCKET_VOLUME);
                                this.setChanged();
                            }
                            heat += 8;
                            fuelTime -= fluidPerTick;
                        } else heat -= 2;
                        break;
                }

                //Water to Steam
                if (heat >= 10000 && this.waterTank.getFluidAmount() >= waterPtick && this.steamTank.getFluidAmount() < this.steamTank.getCapacity())
                {
                    int amount = waterPtick;
                    float factor = (heat / 100f) / (maxHeat / 100f);
                    amount = Math.round(amount * factor);
                    waterTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);

                    FluidStack steamStack = new FluidStack(ModFluids.STEAM.get(), (int) (amount * steamBoilerConversionConfig));//IRConfig.Main.steamBoilerConversionFactor.get());
                    int amountStack = steamTank.fillInternal(steamStack, IFluidHandler.FluidAction.EXECUTE);
//                    Utils.debug("generating", steamStack.getAmount(), amountStack);
                    heat -= 2;
                }

                heat -= 2;
                heat = MathHelper.clamp(heat, 2420, maxHeat);
                fuelTime = Math.max(0, fuelTime);

                //Auto output Steam
                TileEntity upTE = level.getBlockEntity(worldPosition.above(2));
                if (this.steamTank.getFluidAmount() > 0 && upTE != null && upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).isPresent())
                {
                    if(upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).isPresent()) {
                        IFluidHandler upTank = upTE.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                        FluidStack stack = this.steamTank.drain(upTank.fill(this.steamTank.drain(10000, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
//                        Utils.debug("Auto outputing", stack.getAmount());
                    }
                }

                //Steam to Water if no Heat
                if (this.steamTank.getFluidAmount() > 0 && heat < 9000)
                {
                    FluidStack stack = this.steamTank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                    stack.setAmount((int) (stack.getAmount() / steamBoilerConversionConfig));//IRConfig.Main.steamBoilerConversionFactor.get());
                    waterTank.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                }
            } else if (heat > 2420) heat -= 6;
            //Sync with Client
            if (oldHeat != heat || fuelTime != oldFuelTime)
            {
                oldHeat = heat;
                oldFuelTime = fuelTime;
                this.sync();
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntitySteamBoiler;
    }

    public int getIntType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        if (!this.isMaster())
        {
            this.getMaster().setType(type);
            return;
        }
        dropItemsInGround(solidFuelInv);
        this.fuelTime = 0;
        this.type = type;
        BlockState state = getBlockState().setValue(BlockSteamBoiler.TYPE, type);
        level.setBlockAndUpdate(worldPosition, state);
        //level.setBlockEntity(worldPosition, this);
        this.sync();
    }

    public void dropAllItems()
    {
        dropItemsInGround(solidFuelInv);
        //dropItemsInGround(fireBoxInv);
    }

    private void dropItemsInGround(ItemStack stack)
    {
        if (stack!= null && !stack.isEmpty())
        {
            ItemEntity item = new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
            //Utils.debug("spawning item entity");
            level.addFreshEntity(item);
        }
    }

    private void dropItemsInGround(LazyOptional<IItemHandler> inventory)
    {
        if(inventory == null || !inventory.isPresent())
            return;
        IItemHandler iItemHandler = inventory.orElse(null);
        if(iItemHandler == null)
            return;

        ItemStack stack = iItemHandler.getStackInSlot(0);
        if (!stack.isEmpty())
        {
            ItemEntity item = new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
            inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(0, ItemStack.EMPTY));
            level.addFreshEntity(item);
        }
    }

    public String getWaterText()
    {
        return Blocks.WATER.getName().getString();
    }

    public String getSteamText()
    {
        return ModFluids.STEAM_BLOCK.get().getName().getString();
    }

    public String getFuelText()
    {
        switch (getIntType())
        {
            default:
            case 0:
                return "No Firebox";
            case 1:
                IItemHandler handler = solidFuelInv.orElse(null);
                if(solidFuelInv.isPresent()) {
                    int energy = handler.getStackInSlot(0).getCount();
                    return energy == 0 ? "No Fuel" : energy + " Fuel";
                }
                return "NULL Fuel";
            case 2:
                if(fuelTank == null)
                    return "No Fuel";
                return fuelTank.getFluidAmount() > 0 ? fuelTank.getFluid().getDisplayName().getString() : "No Fuel";
        }
    }

    public String getHeatText()
    {
        return (int) Utils.getConvertedTemperature(heat / 100F) + Utils.getTemperatureUnit();
    }

    public float getFuelFill() //0 ~ 180
    {
        switch (getIntType())
        {
            default:
            case 0:
                return 0;
            case 1:
                float currentAmount = (float) fuelTime;
                currentAmount = currentAmount / (float) maxFuelTime;
                return currentAmount * 180f;
            case 2:
                float amount = fuelTank.getFluidAmount() / 1000f;
                float totalCapacity = fuelTank.getCapacity() / 1000f;
                amount = amount / totalCapacity;
                return amount * 180f;
        }
    }

    public float GetWaterFill() //0 ~ 180
    {
        float currentAmount = waterTank.getFluidAmount() / 1000F;
        float totalCapacity = waterTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float GetSteamFill() //0 ~ 180
    {
        float currentAmount = steamTank.getFluidAmount() / 1000F;
        float totalCapacity = steamTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getHeatFill() //0 ~ 180
    {
        float currentAmount = heat;
        float totalCapacity = maxHeat;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 140f;
    }

    public int getFuelTime()
    {
        return fuelTime;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT waterTag = new CompoundNBT();
        CompoundNBT steamTag = new CompoundNBT();
        CompoundNBT fuelTag = new CompoundNBT();
        waterTank.writeToNBT(waterTag);
        steamTank.writeToNBT(steamTag);
        fuelTank.writeToNBT(fuelTag);
        compound.put("water", waterTag);
        compound.put("steam", steamTag);
        compound.put("fluidFuel", fuelTag);
        compound.putInt("type", type);
//        fireBoxInv.ifPresent(h ->
//        {
//            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
//            compound.put("inv", tag);
//        });
        solidFuelInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        compound.putInt("heat", heat);
        compound.putInt("fueltime", fuelTime);
        compound.putInt("maxtime", maxFuelTime);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        CompoundNBT waterTag = compound.getCompound("water");
        CompoundNBT steamTag = compound.getCompound("steam");
        CompoundNBT fluidFuel = compound.getCompound("fluidFuel");
        waterTank.readFromNBT(waterTag);
        steamTank.readFromNBT(steamTag);
        fuelTank.readFromNBT(fluidFuel);
        type = compound.getInt("type");
        //CompoundNBT invTag = compound.getCompound("inv");
        //fireBoxInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        CompoundNBT invTag2 = compound.getCompound("inv");
        solidFuelInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag2));
        heat = compound.getInt("heat");
        fuelTime = compound.getInt("fueltime");
        maxFuelTime = compound.getInt("maxtime");
        super.load(state, compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        TileEntitySteamBoiler masterTE = getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();

        if (facing == Direction.UP && worldPosition.equals(masterTE.getBlockPos().above()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.steamTank).cast();
        if (facing == face && worldPosition.equals(masterTE.getBlockPos().below().relative(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (masterTE.getIntType() == 1 && facing == face.getCounterClockWise() && worldPosition.equals(masterTE.getBlockPos().below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return masterTE.solidFuelInv.cast();
        if (masterTE.getIntType() == 2 && facing == face.getCounterClockWise() && worldPosition.equals(masterTE.getBlockPos().below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.fuelTank).cast();
        return super.getCapability(capability, facing);
    }
}
