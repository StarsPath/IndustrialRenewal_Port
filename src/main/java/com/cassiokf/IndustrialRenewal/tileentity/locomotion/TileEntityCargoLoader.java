package com.cassiokf.IndustrialRenewal.tileentity.locomotion;

import com.cassiokf.IndustrialRenewal.blocks.locomotion.BlockCargoLoader;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityCargoLoader extends TileEntityBaseLoader implements ITickableTileEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(5){
        @Override
        protected void onContentsChanged(int slot)
        {
            TileEntityCargoLoader.this.sync();
        }
    };

    public final LazyOptional<ItemStackHandler> inventoryHandler = LazyOptional.of(()->inventory);

    //TODO: add to config
    private final int itemsPerTick = 4;//IRConfig.MainConfig.Railroad.maxLoaderItemPerTick;

    private int intUnloadActivity = 0;
    private boolean checked = false;
    private boolean master;
    private int noActivity = 0;

    public TileEntityCargoLoader() {
        super(ModTileEntities.CARGO_LOADER.get());
    }

    public TileEntityCargoLoader(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public Direction getBlockFacing() {
        if (blockFacing == null) blockFacing = level.getBlockState(worldPosition).getValue(BlockCargoLoader.FACING);
        return blockFacing;
    }

    @Override
    public boolean isUnload() {
        return unload;
    }

    @Override
    public boolean onMinecartPass(AbstractMinecartEntity entityMinecart) {
        if (!level.isClientSide)
        {
            cartActivity = 10;
            IItemHandler cartCapability = entityMinecart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
            if (cartCapability != null)
            {
                if (isUnload()) //From cart to inventory
                {
                    if (Utils.moveItemsBetweenInventories(cartCapability, inventory, itemsPerTick))
                    {
                        noActivity = 0;
                        intUnloadActivity = 0;
                        loading = true;
                        return true;
                    }

                    intUnloadActivity++;
                    loading = false;
                    if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return !Utils.IsInventoryEmpty(cartCapability);
                    }
                    else if (waitE == waitEnum.WAIT_FULL)
                    {
                        return intUnloadActivity < 10 || !Utils.IsInventoryFull(cartCapability);
                    }
                }
                else //From inventory to cart
                {
                    if (Utils.moveItemsBetweenInventories(inventory, cartCapability, itemsPerTick))
                    {
                        noActivity = 0;
                        intUnloadActivity = 0;
                        loading = true;
                        return true;
                    }

                    intUnloadActivity++;
                    loading = false;
                    if (waitE == waitEnum.WAIT_FULL)
                    {
                        return intUnloadActivity < 10 || !Utils.IsInventoryFull(cartCapability);
                    }
                    else if (waitE == waitEnum.WAIT_EMPTY)
                    {
                        return !Utils.IsInventoryEmpty(cartCapability);
                    }
                }
                if (waitE == waitEnum.NO_ACTIVITY)
                {
                    noActivity++;
                    return noActivity < 10;
                }
            }
        }
        return waitE == waitEnum.NEVER; //false
    }

    @Override
    public void tick() {
        if (!level.isClientSide && isMaster())
        {
            if (isUnload())
            {
                if (cartActivity > 0)
                {
                    cartActivity--;
                    sync();
                }
                TileEntity te = level.getBlockEntity(worldPosition.below().relative(getBlockFacing().getOpposite()));
                if (te != null)
                {
//                    IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing()).orElse(null);
//                    if (handler != null)
//                    {
//                        Utils.moveItemsBetweenInventories(inventory, handler);
//                    }
                    te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getBlockFacing()).ifPresent(handler->{
                        Utils.moveItemsBetweenInventories(inventory, handler);
                    });
                }
            }
        }
    }

    public boolean isMaster()
    {
        if (!checked)
        {
            master = level.getBlockState(worldPosition).getValue(BlockCargoLoader.MASTER);
            checked = true;
        }
        return master;
    }

    private BlockPos getMasterPos()
    {
        if (level.getBlockState(worldPosition).getBlock() instanceof BlockCargoLoader
                && level.getBlockState(worldPosition).getValue(BlockCargoLoader.MASTER))
            return worldPosition;
        return BlockCargoLoader.getMasterPos(level, worldPosition, getBlockFacing());
    }

    public TileEntityCargoLoader getMaster(){
        TileEntity te = level.getBlockEntity(getMasterPos());
        if(te != null && te instanceof TileEntityCargoLoader)
            return (TileEntityCargoLoader) te;
        return null;
    }

    public IItemHandler getInventory()
    {
        return inventory;
    }

    public String getModeText()
    {
        String mode = I18n.get("tesr.ir.mode") + ": ";
        if (isUnload()) return mode + I18n.get("gui.industrialrenewal.button.unloader_mode");
        return mode + I18n.get("gui.industrialrenewal.button.loader_mode");
    }

    public String getTankText()
    {
        return I18n.get("tesr.ir.inventory");
    }

    public float getCartFluidAngle()
    {
        return Utils.getInvNorm(inventory) * 180f;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        inventoryHandler.ifPresent(inventory->compound.put("inventory", inventory.serializeNBT()));
        compound.putInt("activity", cartActivity);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        inventoryHandler.ifPresent(inventory->inventory.deserializeNBT(compound.getCompound("inventory")));
        cartActivity = compound.getInt("activity");
        super.load(state, compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockPos masterPos = getMasterPos();
        if (masterPos == null) return super.getCapability(cap, side);
        TileEntityCargoLoader te = (TileEntityCargoLoader) level.getBlockEntity(masterPos);
        if (te != null && worldPosition.equals(masterPos.above()) && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventoryHandler.cast();
        return super.getCapability(cap, side);
    }
}
