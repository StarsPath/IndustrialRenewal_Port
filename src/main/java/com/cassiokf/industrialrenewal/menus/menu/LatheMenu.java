package com.cassiokf.industrialrenewal.menus.menu;


import com.cassiokf.industrialrenewal.blockentity.BlockEntityLathe;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityStorageChest;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.init.ModMenus;
import com.cassiokf.industrialrenewal.menus.MenuBase;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class LatheMenu extends MenuBase {
    private final BlockEntityLathe tileEntity;
    private Inventory playerInventory;

//    protected LatheContainer(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_) {
//        super(p_i50105_1_, p_i50105_2_);
//    }
    public LatheMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, (BlockEntityLathe) inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public LatheMenu(int windowId, Inventory playerInventory, BlockEntityLathe tileEntity){
        super(ModMenus.LATHE_CONTAINER.get(), windowId);
        this.tileEntity = tileEntity;
        this.playerInventory = playerInventory;
        //IItemHandler inventory = tileEntity.inventory;

        //drawContainer(inventory);
        this.addSlot(new SlotItemHandler(tileEntity.getInputInv(), 0, 44, 30)
        {
            @Override
            public void setChanged() {
                tileEntity.setChanged();
                super.setChanged();
            }
        });
        this.addSlot(new SlotItemHandler(tileEntity.getOutputInv(), 0, 134, 30)
        {
            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                return false;
            }

            @Override
            public void setChanged() {
                tileEntity.setChanged();
                super.setChanged();
            }
        });

        drawPlayerInv(playerInventory);
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.LATHE.get());
    }

    public int getEnergyFill(){
        return (int) (Utils.normalizeClamped(tileEntity.currentEnergy, 0, tileEntity.MAX_ENERGY) * 69);
    }

    public float getProgress(){
        return tileEntity.renderCutterProcess;
    }

    public BlockEntityLathe getTileEntity(){
        return tileEntity;
    }
}
