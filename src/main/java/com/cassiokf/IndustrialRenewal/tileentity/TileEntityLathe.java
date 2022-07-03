package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModRecipes;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.recipes.LatheRecipe;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x2x2MachineBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class TileEntityLathe extends TileEntity3x2x2MachineBase<TileEntityLathe> implements ITickableTileEntity {
    public TileEntityLathe(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    //TODO: Add to config
    private static final int energyPTick = 128;//IRConfig.MainConfig.Main.energyPerTickLatheMachine;
    //private static final float volume = 0.2f * IRConfig.MainConfig.Sounds.masterVolumeMult;
    private CustomEnergyStorage energyContainer;
    private ItemStackHandler input;
    private ItemStackHandler output;
    private ItemStackHandler hold;
    public boolean inProcess = false;
    private boolean oldInProcess;
    private int tick;
    private int processTime;
    public float renderCutterProcess;
    private float oldProcessTime;
    private boolean stopping = false;

    public LazyOptional<CustomEnergyStorage> energyHandler;
    public LazyOptional<ItemStackHandler> inputItemHandler;
    public LazyOptional<ItemStackHandler> outputItemHandler;
    public LazyOptional<ItemStackHandler> holdHandler;

    public int currentEnergy;
    public final int MAX_ENERGY = 10240;

    public TileEntityLathe(){
        super(ModTileEntities.LATHE_TILE.get());
        this.energyContainer = new CustomEnergyStorage(MAX_ENERGY, 256, 256){
            @Override
            public void onEnergyChange() {
                //super.onEnergyChange();
                currentEnergy = this.getEnergyStored();
                TileEntityLathe.this.sync();
            }
        };

        this.input = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                TileEntityLathe.this.sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };

        this.output = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                TileEntityLathe.this.sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };

        this.hold = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                TileEntityLathe.this.sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }
        };

        this.energyHandler = LazyOptional.of(()-> energyContainer);
        this.inputItemHandler = LazyOptional.of(()-> input);
        this.outputItemHandler = LazyOptional.of(()-> output);
        this.holdHandler = LazyOptional.of(()-> hold);

    }


    @Override
    public void tick() {
        if(!level.isClientSide){
            if(isMaster()){

                ItemStack inputStack = input.getStackInSlot(0);
                oldProcessTime = renderCutterProcess;
                if (!inProcess && !inputStack.isEmpty() && valid(output.getStackInSlot(0)))
                {
                    getProcessFromInputItem();
                }
                else if (inProcess)
                {
                    getProcessFromInputItem();
                    process();
                }
                if(inputStack.isEmpty() || !valid(output.getStackInSlot(0))){
                    interrupt();
                }
                if (!inProcess && !oldInProcess) stopping = true;
                oldInProcess = inProcess;
                renderCutterProcess = processTime > 0 ? Utils.normalizeClamped(tick, 0, processTime) * 0.8f : 0;
                tryOutPutItem();
                sync();
            }
        }
    }
    public void interrupt(){
        inProcess = false;
        processTime = 0;
        tick = 0;
        stopping = true;
        hold.setStackInSlot(0, ItemStack.EMPTY);
    }

    private boolean valid(ItemStack outputSlot){
        if(outputSlot.isEmpty())
            return true;

        Inventory inv = new Inventory(1);
        inv.setItem(0, input.getStackInSlot(0));

        Optional<LatheRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipes.LATHE_RECIPE, inv, level);

        IRecipe iRecipe = recipe.orElse(null);
        if(iRecipe == null)
            return false;
        ItemStack resultItem = iRecipe.getResultItem();
        return resultItem.sameItem(outputSlot) && outputSlot.isStackable() && outputSlot.getCount() < outputSlot.getMaxStackSize();
    }

    private void process()
    {
        if (energyContainer.getEnergyStored() < energyPTick) return;
        energyContainer.extractEnergy(energyPTick, false);
        tick++;
        if (tick >= processTime)
        {
            tick = 0;
            processTime = 0;
            inProcess = false;

            {
                if (!level.isClientSide) {
                    input.extractItem(0, 1, false);
                    output.insertItem(0, hold.extractItem(0, 1, false), false);
                }
            }
        }
    }

    private void getProcessFromInputItem()
    {
        Inventory inv = new Inventory(1);
        inv.setItem(0, input.getStackInSlot(0));

        Optional<LatheRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipes.LATHE_RECIPE, inv, level);

        recipe.ifPresent(iRecipe -> {
            processTime = iRecipe.getProcessTime();
            ItemStack resultItem = iRecipe.getResultItem();
            hold.setStackInSlot(0, resultItem);
            inProcess = true;
        });

        sync();
    }

    private void tryOutPutItem()
    {
        if (!level.isClientSide && !output.getStackInSlot(0).isEmpty())
        {
            Direction facing = getMasterFacing().getClockWise();
            TileEntity te = level.getBlockEntity(worldPosition.relative(facing, 2));
            if (te != null)
            {
                IItemHandler outputCap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if (outputCap != null)
                {
                    Utils.moveItemsBetweenInventories(output, outputCap);
                }
            }
        }
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityLathe;
    }

    @Override
    public void onMasterBreak() {
        Utils.dropInventoryItems(level, worldPosition.relative(getMasterFacing().getCounterClockWise()), input);
        Utils.dropInventoryItems(level, worldPosition.relative(getMasterFacing().getClockWise()), output);
        super.onMasterBreak();
    }

    public IItemHandler getInputInv()
    {
        return input;
    }

    public IItemHandler getOutputInv()
    {
        return output;
    }

    public ItemStack getResultItem()
    {
        return hold.getStackInSlot(0);
    }

    public float getNormalizedProcess()
    {
        return renderCutterProcess;
    }

    public float getOldProcess()
    {
        return oldProcessTime;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        TileEntityLathe masterTE = getMaster();
        if (masterTE == null || facing == null) return super.getCapability(capability, facing);
        if (capability.equals(CapabilityEnergy.ENERGY)
                && facing.equals(getMasterFacing())
                && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing()).relative(getMasterFacing().getCounterClockWise())))

            return getMaster().energyHandler.cast();
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            if (facing.equals(getMasterFacing().getCounterClockWise())
                    && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing().getCounterClockWise())))
                return getMaster().inputItemHandler.cast();
            if (facing.equals(getMasterFacing().getClockWise())
                    && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing().getClockWise())))
                return getMaster().outputItemHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("current", this.currentEnergy);
        compound.putFloat("progress", this.renderCutterProcess);
        compound.putInt("tick", this.tick);
        compound.putInt("processTime", this.processTime);
        compound.putBoolean("processing", this.inProcess);

        inputItemHandler.ifPresent(in -> {
            CompoundNBT tag = in.serializeNBT();
            compound.put("input", tag);
        });

        outputItemHandler.ifPresent(out -> {
            CompoundNBT tag = out.serializeNBT();
            compound.put("output", tag);
        });

        holdHandler.ifPresent(handler -> {
            CompoundNBT tag = handler.serializeNBT();
            compound.put("hold", tag);
        });

        energyHandler.ifPresent(energy ->{
            CompoundNBT tag = energy.serializeNBT();
            compound.put("energy", tag);
        });

        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        this.currentEnergy = compound.getInt("current");
        this.renderCutterProcess = compound.getFloat("progress");
        this.tick = compound.getInt("tick");
        this.processTime = compound.getInt("processTime");
        this.inProcess = compound.getBoolean("processing");

        inputItemHandler.ifPresent(input->input.deserializeNBT(compound.getCompound("input")));
        outputItemHandler.ifPresent(output->output.deserializeNBT(compound.getCompound("output")));
        energyHandler.ifPresent(energy->energy.deserializeNBT(compound.getCompound("energy")));
        holdHandler.ifPresent(handler->handler.deserializeNBT(compound.getCompound("hold")));

        super.load(state, compound);
    }
}
