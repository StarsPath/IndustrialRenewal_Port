package com.cassiokf.IndustrialRenewal.containers.container;

import com.cassiokf.IndustrialRenewal.containers.ContainerBase;
import com.cassiokf.IndustrialRenewal.containers.screen.StorageChestScreen;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModContainers;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
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
    private int currentPage = 0;
    private int slotsPerPage;
    private PlayerInventory playerInventory;

    public StorageChestContainer(int windowId, PlayerInventory playerInventory, TileEntityStorageChest tileEntity){
        super(ModContainers.STORAGE_CHEST_CONTAINER.get(), windowId);
        this.tileEntity = tileEntity;
        this.slotsPerPage = tileEntity.slotsPerPage;
        this.playerInventory = playerInventory;
        IItemHandler inventory = tileEntity.inventory;

        drawContainer(inventory);
        drawPlayerInv(playerInventory, 45, 18);
    }

    public void clickedOn(int id){
        if (id == 1 && currentPage > 0){
            currentPage--;
        }
        else if(id == 2 && currentPage < tileEntity.maxPage-1){
            currentPage++;
        }

        slots.clear();
        drawContainer(tileEntity.inventory);
        drawPlayerInv(playerInventory, 45, 18);
        this.broadcastChanges();

    }

    public void drawContainer(IItemHandler inventory){
        int limit = 0;
        int xS = 0;
        int yS = 0;
        int startIndex = currentPage * slotsPerPage;
        int numRows = inventory.getSlots() / 11;

        for (int y = 0; y < numRows; ++y) {
            for (int x = 0; x < 11; ++x) {
                int index = x + y * 11;
                if (index >= startIndex && limit < 66) {
                    limit++;
                    xS = x;
                    this.addSlot(new SlotItemHandler(inventory, index, 8 + xS * 18, 16 + yS * 18));
                } else
                    this.addSlot(new SlotItemHandler(inventory, index, Integer.MIN_VALUE, Integer.MIN_VALUE));
            }
            if (xS > 0) yS++;
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.Storage_CHEST.get());
    }

    public TileEntityStorageChest getTileEntity(){
        return tileEntity;
    }
}
