package com.cassiokf.IndustrialRenewal.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public abstract class ContainerBase extends Container {
    protected ContainerBase(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_) {
        super(p_i50105_1_, p_i50105_2_);
    }

    public void drawPlayerInv(IInventory playerInv, int yOffset, int xOffset)
    {
        int xPos = 8 + xOffset;
        int yPos = 84 + yOffset;

        for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 9; ++x)
            {
                this.addSlot(new Slot(playerInv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x)
        {
            this.addSlot(new Slot(playerInv, x, xPos + x * 18, yPos + 58));
        }
    }

    public void drawPlayerInv(IInventory playerInv)
    {
        drawPlayerInv(playerInv, 0, 0);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerEntity, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null)
        {
            ItemStack itemstack1 = slot.getItem().getStack();
            itemstack = itemstack1.copy();

            int containerSlots = slots.size() - playerEntity.inventory.items.size();

            if (index < containerSlots)
            {
                if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0)
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerEntity, itemstack1);
        }

        return itemstack;
        //return super.quickMoveStack(p_82846_1_, p_82846_2_);
    }

//    @Override
//    public ItemStack transferStackInSlot(PlayerEntity player, int index)
//    {
//        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = slots.get(index);
//
//        if (slot != null && slot.getMaxStackSize()>0)
//        {
//            ItemStack itemstack1 = slot.getItem().getStack();
//            itemstack = itemstack1.copy();
//
//            int containerSlots = slots.size() - player.inventory.items.size();
//
//            if (index < containerSlots)
//            {
//                if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true))
//                {
//                    return ItemStack.EMPTY;
//                }
//            }
//            else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false))
//            {
//                return ItemStack.EMPTY;
//            }
//
//            if (itemstack1.getCount() == 0)
//            {
//                slot.set(ItemStack.EMPTY);
//            }
//            else
//            {
//                slot.setChanged();
//            }
//
//            if (itemstack1.getCount() == itemstack.getCount())
//            {
//                return ItemStack.EMPTY;
//            }
//
//            slot.onTake(player, itemstack1);
//        }
//
//        return itemstack;
//    }
}
