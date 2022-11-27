package com.cassiokf.industrialrenewal.menus.menu;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityStorageChest;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.init.ModMenus;
import com.cassiokf.industrialrenewal.menus.MenuBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class StorageChestMenu extends MenuBase {

    private BlockEntityStorageChest tileEntity;
    private int currentPage = 0;
    private int slotsPerPage;
    private Inventory playerInventory;

    public StorageChestMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, (BlockEntityStorageChest) inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }


    public StorageChestMenu(int windowId, Inventory playerInventory, BlockEntityStorageChest tileEntity){
        super(ModMenus.STORAGE_CHEST_CONTAINER.get(), windowId);
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
    public boolean stillValid(Player playerEntity) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.STORAGE_CHEST.get());
    }

    public BlockEntityStorageChest getTileEntity(){
        return tileEntity;
    }
}
