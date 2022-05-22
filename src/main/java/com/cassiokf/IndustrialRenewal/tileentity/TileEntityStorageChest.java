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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
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

//    public CustomItemStackHandler[] createItemStackHandler(int maxPage, int slotsPerPage){
//        CustomItemStackHandler[] output = new CustomItemStackHandler[maxPage];
//        for(int i =0; i < maxPage; i++){
//            output[i] = new CustomItemStackHandler(slotsPerPage){
//                @Override
//                protected void onContentsChanged(int slot) {
//                    super.onContentsChanged(slot);
//                }
//            };
//        }
//        return output;
//    }

    public LazyOptional<CustomItemStackHandler> inventoryHandler = LazyOptional.of(()->inventory);

    public TileEntityStorageChest(TileEntityType tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityStorageChest(){
        super(ModTileEntities.STORAGE_CHEST_TILE.get());
        //data = new IntArray(1);
        //data.set(0, currentPage);
    }

//    public void guiButtonClick(int id, PlayerEntity player)
//    {
//        if (id == 1 && currentPage > 0)
//        {
//            currentPage--;
//            //sync();
//        }
//        else if (id == 2 && currentPage < maxPage-1)
//        {
//            currentPage++;
//            //sync();
//        }
//        //data.set(0, currentPage);
//        Utils.debug("LINE", currentPage);
//        openGui(player, false);
//    }


//    public void openGui(PlayerEntity player, boolean resetLine)
//    {
//        if (this.isRemoved()) return;
//        if (resetLine)
//        {
//            currentPage = 0;
//            search = "";
//            searchActive = false;
//            //data.set(0, currentPage);
//            //searchActive = IRConfig.MainConfig.Main.searchBarStartFocused;
//        }
//        if (!level.isClientSide)
//        {
//            //player.openMenu(IndustrialRenewal.instance, GUIHandler.STORAGECHEST, world, pos.getX(), pos.getY(), pos.getZ());
//            //player.openMenu(this);
//            NetworkHooks.openGui((ServerPlayerEntity) player, createContainerProvider(player.level, getMaster().getBlockPos()));
//        }
//    }

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

//    public void setLineValues(int currentPage, int maxPage)
//    {
//        this.currentPage = currentPage;
//        this.maxPage = maxPage;
//    }

//    public void setLineValues(int currentPage, int additionalLines)
//    {
//        this.currentPage = currentPage;
//        this.additionalLines = additionalLines;
//    }

//    //@Override
//    public void setSize(int i)
//    {
//        int newCapacity = slots * i;
//        if (newCapacity < 0) newCapacity = Integer.MAX_VALUE;
//
//        maxPage = (newCapacity / 11) - 6;
//        currentPage = 0;
//        inventory[].setSize(newCapacity);
//        //NetworkHandler.INSTANCE.sendToAll(new PacketStorageChest(this));
//        setChanged();
//    }

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
