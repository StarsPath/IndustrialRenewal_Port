package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityStorageChest extends TileEntity3x3x2MachineBase<TileEntityStorageChest> {

    public int maxPage = 4;
    //public int currentPage = 0;
    public int slotsPerPage = 66;
    public String search = "";
    public boolean searchActive = true;
    public int maxNumSlots = slotsPerPage * maxPage;
    //public IIntArray data;

    //public final CustomItemStackHandler[] inventory = createItemStackHandler(maxPage, slotsPerPage);
    //private static final int slots = 99;
    public final CustomItemStackHandler inventory = new CustomItemStackHandler(maxNumSlots){
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityStorageChest.this.sync();
            //super.onContentsChanged(slot);
        }
    };

    public LazyOptional<CustomItemStackHandler> inventoryHandler = LazyOptional.of(()->inventory);

    public TileEntityStorageChest(TileEntityType tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityStorageChest(){
        super(ModTileEntities.STORAGE_CHEST_TILE.get());
        //data = new IntArray(1);
        //data.set(0, currentPage);
    }

    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
        return  new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                TileEntity te = world.getBlockEntity(pos);
                TileEntityStorageChest teMaster = te instanceof TileEntityStorageChest? ((TileEntityStorageChest) te).getMaster() : null;
                return new StorageChestContainer(i, playerInventory, teMaster);
            }
        };
    }

    @Override
    public void onMasterBreak()
    {
        super.onMasterBreak();
        //for(CustomItemStackHandler inv : inventory)
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
        //for(int i = 0 ; i < inventory.length; i++)
        //compound.putInt("currentPage", currentPage);
        compound.put("inv", inventory.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        //for(int i = 0 ; i < inventory.length; i++)
        //currentPage = compound.getInt("currentPage");
        inventory.deserializeNBT(compound.getCompound("inv"));
        super.load(state, compound);
    }

}
