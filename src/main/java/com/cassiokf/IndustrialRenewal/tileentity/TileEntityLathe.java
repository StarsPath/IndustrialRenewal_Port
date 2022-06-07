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
import net.minecraftforge.energy.IEnergyStorage;
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

    private Boolean firstLoad = false;
    public LazyOptional<CustomEnergyStorage> energyHandler;
    public LazyOptional<ItemStackHandler> inputItemHandler;
    public LazyOptional<ItemStackHandler> outputItemHandler;

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
                return true;
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
                return true;
                //return super.isItemValid(slot, stack);
            }
        };

        this.energyHandler = LazyOptional.of(()-> energyContainer);
        this.inputItemHandler = LazyOptional.of(()-> input);
        this.outputItemHandler = LazyOptional.of(()-> output);

    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void setFirstLoad(){
        TileEntityLathe masterTE = getMaster();
        Direction face = masterTE.getMasterFacing();
        TileEntityLathe energyInputTile = (TileEntityLathe) level.getBlockEntity(getBlockPos().relative(face).relative(face.getCounterClockWise()));
        if(energyInputTile != null)
            energyInputTile.energyHandler = masterTE.energyHandler;

        TileEntityLathe itemInputTile = (TileEntityLathe) level.getBlockEntity(getBlockPos().relative(getMasterFacing().getCounterClockWise()));
        if(itemInputTile != null)
            itemInputTile.inputItemHandler = masterTE.inputItemHandler;
    }


    @Override
    public void tick() {
        if(!level.isClientSide){
            if(isMaster()){

                if (!firstLoad) {
                    //Utils.debug("CALLING ONLOAD", worldPosition);
                    firstLoad = true;
                    setFirstLoad();
                }

                ItemStack inputStack = input.getStackInSlot(0);
                oldProcessTime = renderCutterProcess;
                if (!inProcess && !inputStack.isEmpty() && valid(output.getStackInSlot(0)))
//                        && output.getStackInSlot(0).sam)
                        //&& LatheRecipe.CACHED_RECIPES.containsKey(inputStack.getItem()))
                {
                    getProcessFromInputItem();
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

    private boolean valid(ItemStack outputSlot){
        if(outputSlot.isEmpty())
            return true;

        Inventory inv = new Inventory(1);
        inv.setItem(0, input.getStackInSlot(0));

        Optional<LatheRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipes.LATHE_RECIPE, inv, level);

        IRecipe iRecipe = recipe.orElse(null);
        ItemStack resultItem = iRecipe.getResultItem();
        if(resultItem.sameItem(outputSlot) && outputSlot.isStackable() && outputSlot.getCount() < outputSlot.getMaxStackSize())
            return true;
        return false;
    }

    private void process()
    {
        //Utils.debug("PROCESS... Energy", energyContainer.getEnergyStored());
        if (energyContainer.getEnergyStored() < energyPTick) return;
        energyContainer.extractEnergy(energyPTick, false);
        tick++;
        if (tick >= processTime)
        {
            tick = 0;
            processTime = 0;
            inProcess = false;
            processingItem = null;

            {
                if (!level.isClientSide) {
                    //Utils.debug("PRE OUTPUTING...", hold, output.getStackInSlot(0));
                    output.insertItem(0, hold, false);
                    hold = ItemStack.EMPTY;
                    //Utils.debug("OUTPUTING...", hold, output.getStackInSlot(0));
                }
            }
        }
    }

    private void getProcessFromInputItem()
    {
        Inventory inv = new Inventory(1);
        inv.setItem(0, input.getStackInSlot(0));
//        inv.setItem(1, output.getStackInSlot(0));

        Optional<LatheRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipes.LATHE_RECIPE, inv, level);

        recipe.ifPresent(iRecipe -> {
            processTime = iRecipe.getProcessTime();
            ItemStack resultItem = iRecipe.getResultItem();
            processingItem = input.extractItem(0, 1, false);
            hold = resultItem;
            Utils.debug("HOLD", hold, resultItem);
            //hold = output.insertItem(0, resultItem, false);
            inProcess = true;
        });

        sync();
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
    }

    private void tryOutPutItem()
    {
        if (!level.isClientSide && !output.getStackInSlot(0).isEmpty())
        {
            //Utils.debug("TRYING OUTPUT ITEM");
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

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("input", this.input.serializeNBT());
        compound.put("output", this.output.serializeNBT());
        compound.put("hold", this.hold.serializeNBT());
        compound.put("energy", this.energyContainer.serializeNBT());
        compound.putBoolean("processing", inProcess);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        this.input.deserializeNBT(compound.getCompound("input"));
        this.output.deserializeNBT(compound.getCompound("output"));
        this.hold.deserializeNBT(compound.getCompound("hold"));
        this.energyContainer.deserializeNBT(compound.getCompound("StoredIR"));
        inProcess = compound.getBoolean("processing");
        super.load(state, compound);
    }
}
