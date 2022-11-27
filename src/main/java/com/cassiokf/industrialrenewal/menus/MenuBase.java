package com.cassiokf.industrialrenewal.menus;

import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class MenuBase extends AbstractContainerMenu {

    protected MenuBase(@Nullable MenuType<?> p_38851_, int p_38852_) {
        super(p_38851_, p_38852_);
    }

    public void drawPlayerInv(Inventory playerInv, int yOffset, int xOffset)
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

    public void drawPlayerInv(Inventory playerInv)
    {
        drawPlayerInv(playerInv, 0, 0);
    }

    @Override
    public ItemStack quickMoveStack(Player playerEntity, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        ItemStack itemstack1 = slot.getItem();
        itemstack = itemstack1.copy();

        int containerSlots = slots.size() - Inventory.INVENTORY_SIZE;

        Utils.debug("MOVED", index, containerSlots, slots.size(), Inventory.INVENTORY_SIZE, itemstack);
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

        return itemstack;
        //return super.quickMoveStack(p_82846_1_, p_82846_2_);
    }
}
