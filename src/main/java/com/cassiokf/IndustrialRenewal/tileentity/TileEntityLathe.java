package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.recipes.LatheRecipe;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x2x2MachineBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    public boolean inProcess = false;
    private ItemStack hold = ItemStack.EMPTY;
    private boolean oldInProcess;
    private int tick;
    private int processTime;
    private float renderCutterProcess;
    private float oldProcessTime;
    private ItemStack processingItem;
    private boolean stopping = false;
    private boolean stopped = true;
    private boolean oldStopping = false;

    private LazyOptional<CustomEnergyStorage> energyHandler;
    private LazyOptional<ItemStackHandler> inputItemHandler;
    private LazyOptional<ItemStackHandler> outputItemHandler;

    public TileEntityLathe(){
        super(ModTileEntities.LATHE_TILE.get());
        this.energyContainer = new CustomEnergyStorage(10240, 256, 256){
            @Override
            public void onEnergyChange() {
                //super.onEnergyChange();
                TileEntityLathe.this.sync();
            }
        };

        this.input = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                //super.onContentsChanged(slot);
                TileEntityLathe.this.sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return false;
                //return super.isItemValid(slot, stack);
            }
        };

        this.output = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                //super.onContentsChanged(slot);
                TileEntityLathe.this.sync();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return false;
                //return super.isItemValid(slot, stack);
            }
        };

        this.energyHandler = LazyOptional.of(()-> energyContainer);
        this.inputItemHandler = LazyOptional.of(()-> input);
        this.outputItemHandler = LazyOptional.of(()-> output);

    }

    @Override
    public void tick() {
        if(!level.isClientSide){
            if(isMaster()){
                ItemStack inputStack = input.getStackInSlot(0);
                oldProcessTime = renderCutterProcess;
                if (!inProcess
                        && !inputStack.isEmpty())
                        //&& LatheRecipe.CACHED_RECIPES.containsKey(inputStack.getItem()))
                {
                    //getProcessFromInputItem(inputStack);
                    Utils.debug("PROCESSING...");
                }
                else if (inProcess)
                {
                    process();
                }
                if (!inProcess && !oldInProcess) stopping = true;
                oldInProcess = inProcess;
                renderCutterProcess = processTime > 0 ? Utils.normalizeClamped(tick, 0, processTime) * 0.8f : 0;
                tryOutPutItem();
            }
        }
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
            processingItem = null;

            if (!level.isClientSide) output.insertItem(0, hold, false);
        }
    }

//    private void getProcessFromInputItem(ItemStack inputStack)
//    {
//        LatheRecipe recipe = LatheRecipe.CACHED_RECIPES.get(inputStack.getItem());
//        if (recipe != null)
//        {
//            ItemStack result = recipe.getRecipeOutput();
//            if (result != null
//                    && !result.isEmpty()
//                    && output.insertItem(0, result, true).isEmpty()
//                    && energyContainer.getEnergyStored() >= energyPTick)
//            {
//                processTime = recipe.getProcessTime();
//                inProcess = true;
//                processingItem = inputStack;
//                if (!level.isClientSide)
//                {
//                    inputStack.shrink(recipe.getInput().get(0).getCount());
//                }
//                hold = result;
//            }
//        }
//    }

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

    public IEnergyStorage getEnergyStorage()
    {
        return energyContainer;
    }

    public ItemStack getResultItem()
    {
        return hold;
    }

    public ItemStack getProcessingItem()
    {
        return processingItem;
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

            return energyHandler.cast();
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))
        {
            if (facing.equals(getMasterFacing().getCounterClockWise())
                    && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing().getCounterClockWise())))
                return inputItemHandler.cast();
                //return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(masterTE.input);
            if (facing.equals(getMasterFacing().getClockWise())
                    && worldPosition.equals(masterTE.getBlockPos().relative(getMasterFacing().getClockWise())))
                return outputItemHandler.cast();
                //return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(masterTE.outPut);
        }
        return super.getCapability(capability, facing);
    }
}
