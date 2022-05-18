package com.cassiokf.IndustrialRenewal.containers.container;

import com.cassiokf.IndustrialRenewal.containers.ContainerBase;
import com.cassiokf.IndustrialRenewal.containers.screen.StorageChestScreen;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModContainers;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.Objects;

public class StorageChestContainer extends ContainerBase {

    private final TileEntityStorageChest tileEntity;
//    private final int numRows;
//    private final PlayerEntity playerEntity;
//    private final IItemHandler playerInventory;

//    protected StorageChestContainer(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_) {
//        super(p_i50105_1_, p_i50105_2_);
//    }

    public StorageChestContainer(int windowId, PlayerInventory playerInventory, TileEntityStorageChest tileEntity){
        super(ModContainers.STORAGE_CHEST_CONTAINER.get(), windowId);

        this.tileEntity = tileEntity;
        IItemHandler inventory = tileEntity.inventory;
        int startIndex = tileEntity.currentLine * 11;
        int numRows = inventory.getSlots() / 11;
        int limit = 0;
        int xS = 0;
        int yS = 0;
        for (int y = 0; y < numRows; ++y)
        {
            for (int x = 0; x < 11; ++x)
            {
                int index = x + y * 11;
                if (index >= startIndex && limit < 66)
                {
                    limit++;
                    xS = x;
                    this.addSlot(new SlotItemHandler(inventory, index, 8 + xS * 18, 16 + yS * 18));
                }
                else
                    this.addSlot(new SlotItemHandler(inventory, index, Integer.MIN_VALUE, Integer.MIN_VALUE));
            }
            if (xS > 0) yS++;
        }
        drawPlayerInv(playerInventory, 45, 18);


//        this.tileEntity = tileEntity;
//        this.numRows = 6;
//        int playerInventoryStart = numRows * 18;

//        for (int i = 0; i < numRows; i++) {
//            for (int j = 0; j < 9; j++) {
//                addSlot(new SlotItemHandler(tileCounter.getItemHandler(), j + i * 9, 8 + j * 18, 18 + i * 18));
//            }
//        }

//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 9; j++) {
//                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 31 + i * 18 + playerInventoryStart));
//            }
//        }
//
//        for (int i = 0; i < 9; i++) {
//            addSlot(new Slot(playerInventory, i, 8 + i * 18, 89 + playerInventoryStart));
//        }
    }

    //    protected StorageChestContainer(@Nullable ContainerType<?> menuType, int containerID) {
//        super(menuType, containerID);
//    }
//    public StorageChestContainer(final int containerID, final PlayerInventory inv, PacketBuffer data){
//        super(ModContainers.STORAGE_CHEST_CONTAINER.get(), containerID);
////        BlockPos pos = data.readBlockPos();
////        World world = inv.player.level;
//
//        this.tileEntity = getMasterTileEntity(inv, data);
//        this.playerEntity = inv.player;
//        this.playerInventory = new InvWrapper(inv);
//    }

//    public StorageChestContainer(int containerID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity){
//        super(ModContainers.STORAGE_CHEST_CONTAINER.get(), containerID);
//        TileEntity tileEntity = world.getBlockEntity(pos);
//        if(tileEntity instanceof TileEntityStorageChest)
//            this.tileEntity = ((TileEntityStorageChest) tileEntity).getMaster();
//        else this.tileEntity = tileEntity;
//        this.playerEntity = playerEntity;
//        this.playerInventory = new InvWrapper(playerInventory);
//
//        layoutPlayerInventorySlots(8, 86);
//
//        if(tileEntity != null) {
//            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
//                addSlot(new SlotItemHandler(h, 0, 80, 31));
//                addSlot(new SlotItemHandler(h, 1, 80, 53));
//            });
//        }
//    }

//    private static TileEntityStorageChest getMasterTileEntity(final PlayerInventory inv, final PacketBuffer data){
//        Objects.requireNonNull(inv, "Player Inv cannot be null");
//        Objects.requireNonNull(data, "Packet Buffer Data cannot be null");
//        final TileEntity te = inv.player.level.getBlockEntity(data.readBlockPos());
//        if(te instanceof TileEntityStorageChest){
//            return ((TileEntityStorageChest)te).getMaster();
//        }
//        throw new IllegalStateException("Unable to provide Tile Entity");
//    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.Storage_CHEST.get());
    }

    public TileEntityStorageChest getTileEntity(){
        return tileEntity;
    }

//    @Override
//    protected Slot addSlot(Slot slot) {
//        return super.addSlot(slot);
//    }
//
//
//    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
//        for (int i = 0; i < amount; i++) {
//            addSlot(new SlotItemHandler(handler, index, x, y));
//            x += dx;
//            index++;
//        }
//
//        return index;
//    }
//
//    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
//        for (int j = 0; j < verAmount; j++) {
//            index = addSlotRange(handler, index, x, y, horAmount, dx);
//            y += dy;
//        }
//
//        return index;
//    }

//    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
//        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
//
//        topRow += 58;
//        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
//    }
}
