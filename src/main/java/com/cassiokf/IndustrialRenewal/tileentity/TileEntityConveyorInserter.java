package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityConveyorBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityConveyorInserter extends TileEntityConveyorBase {
    public TileEntityConveyorInserter() {
        super(ModTileEntities.CONVEYOR_INSERTER_TILE.get());
    }

    @Override
    public void tick()
    {
        if (!level.isClientSide)
        {
            insertItem();
        }
        super.tick();
    }

    private void insertItem()
    {
        if (!inventory.orElse(null).getStackInSlot(2).isEmpty())
        {
            Direction facing = getBlockFacing();
            TileEntity te = level.getBlockEntity(worldPosition.relative(facing));
            if (te != null)
            {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()).orElse(null);
                if (itemHandler != null)
                {
                    for (int j = 0; j < itemHandler.getSlots(); j++)
                    {
                        ItemStack stack = inventory.orElse(null).extractItem(2, 64, true);
                        if (!stack.isEmpty() && itemHandler.isItemValid(j, stack))
                        {
                            ItemStack left = itemHandler.insertItem(j, stack, false);
                            if (!ItemStack.isSame(stack, left))
                            {
                                int toExtract = stack.getCount() - left.getCount();
                                inventory.orElse(null).extractItem(2, toExtract, false);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean dropFrontItem(Direction facing, ItemStack frontPositionItem, BlockPos frontPos)
    {
        return false;
    }
}
