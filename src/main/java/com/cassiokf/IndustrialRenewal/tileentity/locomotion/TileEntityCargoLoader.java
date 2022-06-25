package com.cassiokf.IndustrialRenewal.tileentity.locomotion;

import com.cassiokf.IndustrialRenewal.blocks.locomotion.BlockCargoLoader;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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

    public void toggleUnload(){
        this.unload = !unload;
    }

    public void cycleMode(){
        waitE = waitEnum.cycle(this.waitE);
    }

    @Override
    public boolean onMinecartPass(AbstractMinecartEntity entityMinecart) {
        return false;
//        if (!level.isClientSide)
//        {
//            cartActivity = 10;
//            IItemHandler cartCapability = entityMinecart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(null);
//            if (cartCapability != null)
//            {
//                if (isUnload()) //From cart to inventory
//                {
//                    if (Utils.moveItemsBetweenInventories(cartCapability, inventory, itemsPerTick))
//                    {
//                        noActivity = 0;
//                        intUnloadActivity = 0;
//                        loading = true;
//                        return true;
//                    }
//
//                    intUnloadActivity++;
//                    loading = false;
//                    if (waitE == waitEnum.WAIT_EMPTY)
//                    {
//                        return !Utils.IsInventoryEmpty(cartCapability);
//                    }
//                    else if (waitE == waitEnum.WAIT_FULL)
//                    {
//                        return intUnloadActivity < 10 || !Utils.IsInventoryFull(cartCapability);
//                    }
//                }
//                else //From inventory to cart
//                {
//                    if (Utils.moveItemsBetweenInventories(inventory, cartCapability, itemsPerTick))
//                    {
//                        noActivity = 0;
//                        intUnloadActivity = 0;
//                        loading = true;
//                        return true;
//                    }
//
//                    intUnloadActivity++;
//                    loading = false;
//                    if (waitE == waitEnum.WAIT_FULL)
//                    {
//                        return intUnloadActivity < 10 || !Utils.IsInventoryFull(cartCapability);
//                    }
//                    else if (waitE == waitEnum.WAIT_EMPTY)
//                    {
//                        return !Utils.IsInventoryEmpty(cartCapability);
//                    }
//                }
//                if (waitE == waitEnum.NO_ACTIVITY)
//                {
//                    noActivity++;
//                    return noActivity < 10;
//                }
//            }
//        }
//        return waitE == waitEnum.NEVER; //false
    }

    @Override
    public void tick() {
        if (!level.isClientSide && isMaster())
        {
            BlockPos loaderPosition = worldPosition.relative(getBlockFacing());
            IInventory containerInventory = getContainerAt(level, loaderPosition.getX(), loaderPosition.getY(), loaderPosition.getZ());
            if(isUnload()) { // from cart to cargoLoader
                if(containerInventory!=null){
                    Utils.moveItemsBetweenInventories(containerInventory, inventory);
                }
            }
            else if (!isUnload()) { // from cargoLoader to cart
                if(containerInventory!=null){
                    Utils.moveItemsBetweenInventories(inventory, containerInventory);
                }
            }
            switch (waitE){
                case WAIT_FULL:
                    if(containerInventory!=null)
                        level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, Utils.isInventoryFull(containerInventory)), 3);
                case WAIT_EMPTY:
                    if(containerInventory!=null)
                        level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, containerInventory.isEmpty()), 3);
                case NO_ACTIVITY:
                    level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, true), 3);
                case NEVER:
                    level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, false), 3);
            }
        }
    }

    public IInventory getContainerAt(World world, double x, double y, double z){
        IInventory inventory = null;

        List<Entity> list = world.getEntities((Entity)null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.CONTAINER_ENTITY_SELECTOR);
        if (!list.isEmpty()) {
            inventory = (IInventory)list.get(world.random.nextInt(list.size()));
        }
        return inventory;
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
