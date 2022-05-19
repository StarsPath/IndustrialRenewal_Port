package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
import com.cassiokf.IndustrialRenewal.init.ModContainers;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x2MachineBase;
import com.cassiokf.IndustrialRenewal.util.CustomItemStackHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityStorageChest extends TileEntity3x3x2MachineBase<TileEntityStorageChest> {

    private static final int slots = 99;
    public final CustomItemStackHandler inventory = new CustomItemStackHandler(slots){
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityStorageChest.this.sync();
            //super.onContentsChanged(slot);
        }
    };

    public int additionalLines;
    public int currentLine;
    public String search = "";
    public boolean searchActive = true;
    public LazyOptional<CustomItemStackHandler> inventoryHandler = LazyOptional.of(()->inventory);

    public TileEntityStorageChest(TileEntityType tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityStorageChest(){
        super(ModTileEntities.STORAGE_CHEST_TILE.get());
    }

    public void guiButtonClick(int id, PlayerEntity player)
    {
        if (id == 1 && currentLine > 0)
        {
            currentLine--;
        }
        else if (id == 2 && currentLine < additionalLines)
        {
            currentLine++;
        }
        Utils.debug("LINE", currentLine);
        openGui(player, false);
    }


    public void openGui(PlayerEntity player, boolean resetLine)
    {
        if (this.isRemoved()) return;
        if (resetLine)
        {
            currentLine = 0;
            search = "";
            searchActive = false;
            //searchActive = IRConfig.MainConfig.Main.searchBarStartFocused;
        }
        if (!level.isClientSide)
        {
            //player.openMenu(IndustrialRenewal.instance, GUIHandler.STORAGECHEST, world, pos.getX(), pos.getY(), pos.getZ());
            //player.openMenu(this);
        }
    }

    public void setLineValues(int currentLine, int additionalLines)
    {
        this.currentLine = currentLine;
        this.additionalLines = additionalLines;
    }

    //@Override
    public void setSize(int i)
    {
        int newCapacity = slots * i;
        if (newCapacity < 0) newCapacity = Integer.MAX_VALUE;

        additionalLines = (newCapacity / 11) - 6;
        currentLine = 0;
        inventory.setSize(newCapacity);
        //NetworkHandler.INSTANCE.sendToAll(new PacketStorageChest(this));
        setChanged();
    }

    @Override
    public void onMasterBreak()
    {
        super.onMasterBreak();
        Utils.dropInventoryItems(level, worldPosition, inventory);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return inventoryHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityStorageChest;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("inv", inventory.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        inventory.deserializeNBT(compound.getCompound("inv"));
        super.load(state, compound);
    }

//    @Override
//    public ITextComponent getDisplayName() {
//        return ITextComponent.nullToEmpty("");
//    }
//
//    @Nullable
//    @Override
//    public Container createMenu(int ID, PlayerInventory inventory, PlayerEntity player) {
//        return new StorageChestContainer(ID, inventory, getMaster());
//    }
}
