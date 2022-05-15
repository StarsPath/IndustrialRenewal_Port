package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TileEntityLocker extends LockableLootTileEntity {

    private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);

    protected TileEntityLocker(TileEntityType<?> p_i48284_1_) {
        super(p_i48284_1_);
    }

    public TileEntityLocker() {
        super(ModTileEntities.LOCKER_TILE.get());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> p_199721_1_) {
        this.chestContents = p_199721_1_;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return this.hasCustomName() ? this.getCustomName() : new StringTextComponent("Locker");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        //fillWithLoot(player.player);
        unpackLootTable(player.player);
        //return new ChestContainer(player, "minecraft:chest", player);
        return ChestContainer.threeRows(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        if (this.hasCustomName())
        {
            compound.putString("CustomName", this.getCustomName().getString());
        }
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        this.chestContents = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.contains("CustomName", 8))
        {
            this.setCustomName(new StringTextComponent(compound.getString("CustomName")));
        }
    }
}
