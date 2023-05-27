package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.blocks.BlockSteamBoiler;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModFluids;
import com.cassiokf.industrialrenewal.init.ModItems;
import com.cassiokf.industrialrenewal.items.ItemFireBox;
import com.cassiokf.industrialrenewal.items.ItemPowerScrewDrive;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import com.cassiokf.industrialrenewal.util.CustomItemStackHandler;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
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

public class BlockEntitySteamBoiler extends MultiBlockEntity3x3x3MachineBase {

    private int waterTankCapacity = Config.STEAM_BOILER_WATER_TANK_CAPACITY.get();
    private int SteamTankCapacity = Config.STEAM_BOILER_STEAM_TANK_CAPACITY.get();
    private int FuelTankCapacity = Config.STEAM_BOILER_FUEL_TANK_CAPACITY.get();

    public CustomFluidTank waterTank = new CustomFluidTank(waterTankCapacity)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return stack != null && stack.getFluid().equals(Fluids.WATER.getSource());
        }

        @Override
        public void onContentsChanged()
        {
            BlockEntitySteamBoiler.this.sync();
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
            BlockEntitySteamBoiler.this.sync();
        }
    };

    public CustomFluidTank fuelTank = new CustomFluidTank(FuelTankCapacity)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return stack != null && Config.getFuelHash().containsKey(stack.getFluid().getRegistryName().toString());
            //TODO FLUIDSET CONFIG
        }

        @Override
        public void onContentsChanged()
        {
            BlockEntitySteamBoiler.this.sync();
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

    public BlockEntitySteamBoiler(BlockPos pos, BlockState state) {
        super(ModBlockEntity.STEAM_BOILER_TILE.get(), pos, state);
    }

    private IItemHandler createFuelInv()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return FurnaceBlockEntity.isFuel(stack);
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                BlockEntitySteamBoiler.this.sync();
            }
        };
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        solidFuelInv.invalidate();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void onMasterBreak() {
//        dropItemsInGround(getDrop());
//        dropAllItems();
    }

    @Override
    public InteractionResult onUse(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!level.isClientSide){
            ItemStack heldItem = player.getItemInHand(handIn);
            if (heldItem.getItem() instanceof ItemFireBox && getIntType()==0)
            {
                int type = ((ItemFireBox) heldItem.getItem()).type;
                setType(type);
                if (!worldIn.isClientSide && !player.isCreative()) heldItem.shrink(1);
                return InteractionResult.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && getIntType()!=0)
            {
                ItemStack stack = getDrop();
                if (!worldIn.isClientSide && !player.isCreative()) player.addItem(stack);
                setType(0);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    public ItemStack getDrop(){
        return switch (type) {
            case 1 -> new ItemStack(ModItems.FIREBOX_SOLID.get(), 1);
            case 2 -> new ItemStack(ModItems.FIREBOX_FLUID.get(), 1);
            default -> null;
        };
    }

    public boolean canRun(){
        return switch (type) {
            case 1 -> (fuelTime >= solidPerTick && !waterTank.isEmpty());
            case 2 -> (fuelTime >= fluidPerTick && !waterTank.isEmpty());
            default -> false;
        };
    }

    ;private int tick = 0;

    public void tick() {
        if(level == null) return;
        if (!level.isClientSide)
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
                                    fuelTime = ForgeHooks.getBurnTime(fuel, null);
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
                if (heat >= 10000 && this.waterTank.getFluidAmount() > 0 && this.steamTank.getFluidAmount() < this.steamTank.getCapacity())
                {
                    int amount = Math.min(waterTank.getFluidAmount(), waterPtick);
                    float factor = (heat / 100f) / (maxHeat / 100f);
                    amount = Math.round(amount * factor);
                    waterTank.drain(amount, IFluidHandler.FluidAction.SIMULATE);

                    FluidStack steamStack = new FluidStack(ModFluids.STEAM.get(), (int) (amount * steamBoilerConversionConfig));//IRConfig.Main.steamBoilerConversionFactor.get());
                    int amountStack = steamTank.fillInternal(steamStack, IFluidHandler.FluidAction.SIMULATE);

                    waterTank.drain((int)(amountStack/steamBoilerConversionConfig), IFluidHandler.FluidAction.EXECUTE);
                    steamStack = new FluidStack(ModFluids.STEAM.get(), amountStack);
                    steamTank.fillInternal(steamStack, IFluidHandler.FluidAction.EXECUTE);

                    heat -= 2;
                }

                heat -= 2;
                heat = Mth.clamp(heat, 2420, maxHeat);
                fuelTime = Math.max(0, fuelTime);

                //Auto output Steam
                BlockEntity upTE = level.getBlockEntity(worldPosition.above(2));
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


    public int getIntType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        if(level == null) return;
        clearFluidInv();
        dropItemsInGround(solidFuelInv);
        this.fuelTime = 0;
        this.type = type;
        BlockState state = getBlockState().setValue(BlockSteamBoiler.TYPE, type);
        level.setBlockAndUpdate(worldPosition, state);
        //level.setBlockEntity(worldPosition, this);
        this.sync();
    }

    public void clearFluidInv(){
        if(level == null) return;
        if(fuelTank == null)
            return;
        fuelTank.setFluid(FluidStack.EMPTY);
    }

    public void dropAllItems()
    {
        dropItemsInGround(solidFuelInv);
        dropItemsInGround(getDrop());
    }

    private void dropItemsInGround(ItemStack stack)
    {
        if(level == null) return;
        if (stack!= null && !stack.isEmpty())
            Block.popResource(level, worldPosition, stack);
    }

    private void dropItemsInGround(LazyOptional<IItemHandler> inventory)
    {
        if(level == null) return;
        if(inventory == null || !inventory.isPresent())
            return;
        IItemHandler iItemHandler = inventory.orElse(null);
        if(iItemHandler == null)
            return;

        ItemStack stack = iItemHandler.extractItem(0, 64, false);
        if (!stack.isEmpty())
        {
            Block.popResource(level, worldPosition, stack);
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
    protected void saveAdditional(CompoundTag compoundTag) {
        CompoundTag waterTag = new CompoundTag();
        CompoundTag steamTag = new CompoundTag();
        CompoundTag fuelTag = new CompoundTag();
        waterTank.writeToNBT(waterTag);
        steamTank.writeToNBT(steamTag);
        fuelTank.writeToNBT(fuelTag);
        compoundTag.put("water", waterTag);
        compoundTag.put("steam", steamTag);
        compoundTag.put("fluidFuel", fuelTag);
        compoundTag.putInt("type", type);
        solidFuelInv.ifPresent(h ->
        {
            CompoundTag tag = ((INBTSerializable<CompoundTag>) h).serializeNBT();
            compoundTag.put("inv", tag);
        });
        compoundTag.putInt("heat", heat);
        compoundTag.putInt("fueltime", fuelTime);
        compoundTag.putInt("maxtime", maxFuelTime);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        CompoundTag waterTag = compoundTag.getCompound("water");
        CompoundTag steamTag = compoundTag.getCompound("steam");
        CompoundTag fluidFuel = compoundTag.getCompound("fluidFuel");
        waterTank.readFromNBT(waterTag);
        steamTank.readFromNBT(steamTag);
        fuelTank.readFromNBT(fluidFuel);
        type = compoundTag.getInt("type");
        //CompoundNBT invTag = compound.getCompound("inv");
        //fireBoxInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        CompoundTag invTag2 = compoundTag.getCompound("inv");
        solidFuelInv.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(invTag2));
        heat = compoundTag.getInt("heat");
        fuelTime = compoundTag.getInt("fueltime");
        maxFuelTime = compoundTag.getInt("maxtime");
        super.load(compoundTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return super.getCapability(capability, facing);
    }

    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing, BlockPos pos) {
        Direction face = getBlockState().getValue(BlockSteamBoiler.FACING);
        if (facing == null) {
            return super.getCapability(capability, facing);
        }

        if (facing == Direction.UP && pos.equals(getBlockPos().above()) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> steamTank).cast();
        }
        if (facing == face && pos.equals(getBlockPos().below().relative(face)) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> waterTank).cast();
        if (getIntType() == 1 && facing == face.getCounterClockWise() && pos.equals(getBlockPos().below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return solidFuelInv.cast();
        if (getIntType() == 2 && facing == face.getCounterClockWise() && pos.equals(getBlockPos().below().relative(face.getOpposite()).relative(face.getCounterClockWise())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> fuelTank).cast();
        return super.getCapability(capability, facing);
    }
}
