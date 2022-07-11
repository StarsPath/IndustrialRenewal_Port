package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import com.cassiokf.IndustrialRenewal.blocks.BlockConveyor;
import com.cassiokf.IndustrialRenewal.util.CustomItemStackHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class TileEntityConveyorBase extends TileEntitySyncable implements ICapabilityProvider, ITickableTileEntity {

    public double stack3Pos;
    public double stack2Pos;
    public double stack1Pos;
    public double stack3YPos;
    public double stack2YPos;
    public double stack1YPos;
    private int tick;
    private boolean getInThisTick;

    public LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

    private IItemHandler createHandler()
    {
        return new CustomItemStackHandler(3)
        {
            @Override
            public int getSlots()
            {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityConveyorBase.this.sync();
                if (slot == 0)
                {
                    TileEntityConveyorBase.this.getInThisTick = true;
                }
            }
        };
    }

    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory)
    {
        for (int i = 0; i < 3; ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                Utils.spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }

    public TileEntityConveyorBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (level.isClientSide)
        {
            doAnimation();
        }

        if (tick == 1) getInThisTick = false;
        if (tick % 4 == 0)
        {
            tick = 0;
            if (!level.isClientSide)
            {
                moveItem();
            }
        }
        tick++;
    }

    private void doAnimation()
    {
        ItemStack stack1 = getStackInSlot(0);
        ItemStack stack2 = getStackInSlot(1);
        ItemStack stack3 = getStackInSlot(2);
        int mode = getMode();
        float yPos;

        float speed = 0.33f/4;
        if (!stack3.isEmpty())
        {
//            stack3Pos = Utils.lerp(stack3Pos, 0.99D, speed);
            stack3Pos += speed;
            stack3Pos = MathHelper.clamp(stack3Pos, 0.66f, 1f);
            yPos = mode == 0 ? 0.47f : mode == 1 ? 1.3f : 0.65f;
            if (mode == 0) stack3YPos = yPos;
            else stack3YPos = Utils.lerp(stack3YPos, yPos, speed);
        } else
        {
            stack3Pos = 0.66f;
            stack3YPos = mode == 0 ? 0.47f : 0.97f;
        }
        if (!stack2.isEmpty())
        {
//            stack2Pos = Utils.lerp(stack2Pos, 0.66D, speed);
            stack2Pos += speed;
            stack2Pos = MathHelper.clamp(stack2Pos, 0.33f, 0.66f);
            yPos = mode == 0 ? 0.47f : 0.97f;
            if (mode == 0) stack2YPos = yPos;
            else stack2YPos = Utils.lerp(stack2YPos, yPos, speed);
        } else
        {
            stack2Pos = 0.33f;
            if (mode == 1) stack2YPos = 0.65f;
            if (mode == 2) stack2YPos = 1.3f;
        }
        if (!stack1.isEmpty())
        {
//            stack1Pos = Utils.lerp(stack1Pos, 0.33D, speed);
            stack1Pos += speed;
            stack1Pos = MathHelper.clamp(stack1Pos, 0, 0.33f);
            yPos = mode == 0 ? 0.47f : mode == 1 ? 0.65f : 1.3f;
            if (mode == 0) stack1YPos = yPos;
            else stack1YPos = Utils.lerp(stack1YPos, yPos, speed);
        } else
        {
            stack1Pos = 0f;
            if (mode == 1) stack1YPos = 0.3f;
            if (mode == 2) stack1YPos = 1.65f;
        }
    }

    private void moveItem()
    {
        ItemStack frontPositionItem = inventory.orElse(null).getStackInSlot(2);
        BlockState ownState = level.getBlockState(worldPosition);
        if (!(ownState.getBlock() instanceof BlockConveyor)) return;

        Direction facing = ownState.getValue(BlockConveyor.FACING);
        if (!frontPositionItem.isEmpty())
        {
            BlockPos frontPos = worldPosition.relative(facing);
            int mode = ownState.getValue(BlockConveyor.MODE);
            BlockPos targetConveyorPos = frontConveyor(facing, mode);
            if (targetConveyorPos != null)
            {
                TileEntityConveyorBase te = null;
                if (level.getBlockEntity(targetConveyorPos) instanceof TileEntityConveyorBase)
                {
                    te = (TileEntityConveyorBase) level.getBlockEntity(targetConveyorPos);
                }

                if (te != null)
                {
                    if (te.getBlockFacing() == getBlockFacing() && te.transferItem(frontPositionItem, false))
                    { // IF IS STRAIGHT
                        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
                        frontPositionItem = ItemStack.EMPTY;
                    } else if (te.getBlockFacing() != getBlockFacing().getOpposite() && te.getStackInSlot(1).isEmpty())
                    { // IF IS CORNER
                        if (te.transferItem(frontPositionItem, 1, false))
                        {
                            inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
                            frontPositionItem = ItemStack.EMPTY;
                        }
                    }
                }
            } else if (level.getBlockState(frontPos).getBlock().isAir(level.getBlockState(frontPos), level, frontPos))
            {
                if (dropFrontItem(facing, frontPositionItem, frontPos))
                {
                    frontPositionItem = ItemStack.EMPTY;
                }
            }
        }
        ItemStack MiddlePositionItem = inventory.orElse(null).getStackInSlot(1);
        if (frontPositionItem.isEmpty() && !MiddlePositionItem.isEmpty())
        {
            moveItemInternaly(1, 2);
            MiddlePositionItem = ItemStack.EMPTY;
        }
        ItemStack backPositionItem = inventory.orElse(null).getStackInSlot(0);
        if (!backPositionItem.isEmpty() && MiddlePositionItem.isEmpty() && !getInThisTick)
        {
            moveItemInternaly(0, 1);
        }
        getInThisTick = false;
    }

    private void moveItemInternaly(int from, int to)
    {
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(to, inventory.orElse(null).getStackInSlot(from)));
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(from, ItemStack.EMPTY));
    }

    private BlockPos frontConveyor(Direction facing, int mode)
    {
        BlockPos frontPos = worldPosition.relative(facing);
        if (mode == 1 || !(level.getBlockState(frontPos).getBlock() instanceof BlockConveyor))
        {
            if (mode == 1)
            {
                frontPos = worldPosition.relative(facing).above();
            } else
            {
                frontPos = worldPosition.relative(facing).below();
            }
        } else
        {
            return frontPos;
        }
        BlockState frontState = level.getBlockState(frontPos);
        return (frontState.getBlock() instanceof BlockConveyor
                && frontState.getValue(BlockConveyor.FACING) == getBlockFacing())
                ? frontPos : null;
    }

    public boolean transferItem(ItemStack stack, boolean simulate)
    {
        return transferItem(stack, 0, simulate);
    }

    public boolean transferItem(ItemStack stack, int slot, boolean simulate)
    {
        if (inventory.orElse(null).getStackInSlot(0).isEmpty() && !stack.isEmpty())
        {
            if (!simulate) inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(slot, stack));
            getInThisTick = true;
            sync();
            return true;
        }
        return false;
    }

    public boolean dropFrontItem(Direction facing, ItemStack frontPositionItem, BlockPos frontPos)
    {
        double multiplierX = BlockConveyor.getMotionX(facing);
        double multiplierZ = BlockConveyor.getMotionZ(facing);
        ItemEntity entityitem = new ItemEntity(level, frontPos.getX() + 0.5D, frontPos.getY() + 0.5D, frontPos.getZ() + 0.5D, frontPositionItem);
        entityitem.setDeltaMovement(multiplierX * 0.2, 0, multiplierZ * 0.2);
        level.addFreshEntity(entityitem);
        inventory.ifPresent(e -> ((CustomItemStackHandler) e).setStackInSlot(2, ItemStack.EMPTY));
        return true;
    }

    public void dropInventory()
    {
        dropInventoryItems(level, worldPosition, inventory.orElse(null));
    }

    @Override
    public void setRemoved() {
        dropInventory();
        super.setRemoved();
    }

    public Direction getBlockFacing()
    {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof BlockConveyor) return state.getValue(BlockConveyor.FACING);
        return Direction.NORTH;
    }

    public int getMode()
    {
        BlockState state = getBlockState();
        return state.getBlock() instanceof BlockConveyor ? state.getValue(BlockConveyor.MODE) : 0;
    }

    public ItemStack getStackInSlot(int slot)
    {
        return inventory.orElse(null).getStackInSlot(slot).copy();
    }

    public float getMinYOffset(int slot, int mode)
    {
        switch (slot)
        {
            default:
            case 2:
                return mode == 0 ? 0.60f : 1.1f;
            case 1:
                if (mode == 1) return 0.78f;
                if (mode == 2) return 1.43f;
                return 0.61f;
            case 0:
                if (mode == 1) return 0.43f;
                if (mode == 2) return 1.78f;
                return 0.60f;
        }
    }

    public float getMaxYOffset(int mode)
    {
        if (mode == 0) return 0;
        else if (mode == 1) return 0.46f;
        return -0.46f;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (facing == getBlockFacing().getOpposite() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        inventory.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        CompoundNBT invTag = compound.getCompound("inv");
        inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.load(state, compound);
    }
}
