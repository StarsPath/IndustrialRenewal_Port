package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityConveyorBase;
import com.cassiokf.IndustrialRenewal.util.CustomItemStackHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityConveyorHopper extends TileEntityConveyorBase {

    private LazyOptional<IItemHandler> hopperInv = LazyOptional.of(this::createHopperHandler);
    private int tick2;

    private IItemHandler createHopperHandler()
    {
        return new CustomItemStackHandler(1)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityConveyorHopper.this.sync();
            }
        };
    }

    public TileEntityConveyorHopper() {
        super(ModTileEntities.CONVEYOR_HOPPER_TILE.get());
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!level.isClientSide)
        {
            if (tick2 % 8 == 0)
            {
                tick2 = 0;
                if (!getInvAbove()) getEntityItemAbove();
                hopperToConveyor();
            }
            tick2++;
        }
    }

    private boolean getInvAbove()
    {
        if (hopperInv.orElse(null).getStackInSlot(0).isEmpty())
        {
            TileEntity te = level.getBlockEntity(worldPosition.above());
            if (te != null)
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                if (itemHandler != null)
                {
                    int itemsPerTick = 8;
                    for (int i = 0; i < itemHandler.getSlots(); i++)
                    {
                        ItemStack stack = itemHandler.extractItem(i, itemsPerTick, true);
                        ItemStack left = hopperInv.orElse(null).insertItem(0, stack, false);
                        if (!ItemStack.isSame(stack, left))
                        {
                            int toExtract = stack.getCount() - left.getCount();
                            itemHandler.extractItem(i, toExtract, false);
                            sync();
                            break;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void hopperToConveyor()
    {
        if (!hopperInv.orElse(null).getStackInSlot(0).isEmpty())
        {
            ItemStack stack = hopperInv.orElse(null).getStackInSlot(0).copy();
            ItemStack stack1 = inventory.orElse(null).insertItem(1, stack, false);
            hopperInv.orElse(null).getStackInSlot(0).shrink(stack.getCount() - stack1.getCount());
        }
    }

    private void getEntityItemAbove()
    {
        if (hopperInv.orElse(null).getStackInSlot(0).isEmpty())
        {
            BlockPos posAbove = worldPosition.above();
            List<Entity> list = level.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(posAbove.getX(), posAbove.getY(), posAbove.getZ(), posAbove.getX() + 2D, posAbove.getY() + 1D, posAbove.getZ() + 1D), EntityPredicates.ENTITY_STILL_ALIVE);
            if (!list.isEmpty() && list.get(0) instanceof ItemEntity)
            {
                ItemEntity entityItem = (ItemEntity) list.get(0);
                ItemStack stack = entityItem.getItem().copy();
                ItemStack stack1 = hopperInv.orElse(null).insertItem(0, stack, false);
                if (stack1.isEmpty()) entityItem.remove();
                else entityItem.setItem(stack1);
            }
        }
    }

//    @Override
//    public void dropInventory()
//    {
//        Utils.dropInventoryItems(level, worldPosition, hopperInv.orElse(null));
//        super.dropInventory();
//    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        hopperInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        CompoundNBT invTag = compound.getCompound("inv");
        hopperInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.load(state, compound);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) && facing != Direction.DOWN)
            return hopperInv.cast();
        return super.getCapability(capability, facing);
    }
}
