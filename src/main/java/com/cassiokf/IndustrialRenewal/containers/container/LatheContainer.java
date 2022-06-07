package com.cassiokf.IndustrialRenewal.containers.container;

import com.cassiokf.IndustrialRenewal.containers.ContainerBase;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModContainers;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityLathe;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class LatheContainer extends ContainerBase {
    private final TileEntityLathe tileEntity;
    private PlayerInventory playerInventory;

//    protected LatheContainer(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_) {
//        super(p_i50105_1_, p_i50105_2_);
//    }

    public LatheContainer(int windowId, PlayerInventory playerInventory, TileEntityLathe tileEntity){
        super(ModContainers.LATHE_CONTAINER.get(), windowId);
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
            public void setChanged() {
                tileEntity.setChanged();
                super.setChanged();
            }
        });

        drawPlayerInv(playerInventory);
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.LATHE.get());
    }
}
