package com.cassiokf.industrialrenewal.blockentity.locomotion;

import com.cassiokf.industrialrenewal.blocks.locomotion.BlockCargoLoader;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModEntity;
import com.cassiokf.industrialrenewal.menus.menu.CargoLoaderMenu;
import com.cassiokf.industrialrenewal.menus.menu.StorageChestMenu;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockEntityCargoLoader extends BlockEntityBaseLoader implements MenuProvider {

    public final ItemStackHandler inventory = new ItemStackHandler(5){
        @Override
        protected void onContentsChanged(int slot)
        {
            BlockEntityCargoLoader.this.sync();
        }
    };

    public LazyOptional<ItemStackHandler> inventoryHandler = LazyOptional.of(()->inventory);

    //TODO: add to config
    private final int itemsPerTick = 4;//IRConfig.MainConfig.Railroad.maxLoaderItemPerTick;

    private int intUnloadActivity = 0;
    private boolean checked = false;
    private boolean master;
    private int noActivity = 0;
    private boolean firstLoad = true;

    public BlockEntityCargoLoader(BlockPos pos, BlockState state) {
        super(ModBlockEntity.CARGO_LOADER.get(), pos, state);
    }


    @Override
    public Direction getBlockFacing() {
        if (blockFacing == null) blockFacing = level.getBlockState(worldPosition).getValue(BlockCargoLoader.FACING);
        return blockFacing;
    }

//    @Override
//    public void setRemoved() {
//        super.setRemoved();
//        Utils.dropInventoryItems(level, worldPosition, inventoryHandler.orElse(null));
//    }

    public void firstLoad(){
        BlockEntityCargoLoader masterTE = getMaster();

        BlockEntityCargoLoader itemInputTile = (BlockEntityCargoLoader) level.getBlockEntity(getBlockPos().above());
        if(itemInputTile != null)
            masterTE.inventoryHandler = itemInputTile.inventoryHandler;
    }


    public void tick() {
        if (!level.isClientSide && isMaster())
        {
            if(firstLoad){
                firstLoad();
                firstLoad = false;
            }
            BlockPos loaderPosition = worldPosition.relative(getBlockFacing());
            ItemStackHandler inv = inventoryHandler.orElse(null);
            if(inv == null)
                return;
            Container containerInventory = getContainerAt(level, loaderPosition.getX(), loaderPosition.getY(), loaderPosition.getZ());
//            Utils.debug("CONTAINER", containerInventory);

            if(isUnload()) { // from cart to cargoLoader
                if(containerInventory!=null){
                    Utils.moveItemsBetweenInventories(containerInventory, inv);
                }
            }
            else if (!isUnload()) { // from cargoLoader to cart
                if(containerInventory!=null){
                    Utils.moveItemsBetweenInventories(inv, containerInventory);
                }
            }
            if(containerInventory == null){
                level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, true), 3);
            }
            else
                switch (waitE){
                    case WAIT_FULL:
                        if(containerInventory!=null) {
                            boolean setBool = Utils.isInventoryFull(containerInventory);
                            if(level.getBlockState(loaderPosition.below()).getValue(BlockStateProperties.POWERED) != setBool)
                                level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, setBool), 3);
                        }
                        else{
                            if(level.getBlockState(loaderPosition.below()).getValue(BlockStateProperties.POWERED))
                                level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, false), 3);
                        }
                        break;
                    case WAIT_EMPTY:
                        if(containerInventory!=null) {
                            boolean setBool = containerInventory.isEmpty();
                            if(level.getBlockState(loaderPosition.below()).getValue(BlockStateProperties.POWERED) != setBool)
                                level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, setBool), 3);
                        }
                        else{
                            if(level.getBlockState(loaderPosition.below()).getValue(BlockStateProperties.POWERED))
                                level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, false), 3);
                        }
                        break;
                    case NO_ACTIVITY: {
                        if(!level.getBlockState(loaderPosition.below()).getValue(BlockStateProperties.POWERED))
                            level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, true), 3);
                        break;
                    }
                    case NEVER: {
                        if(level.getBlockState(loaderPosition.below()).getValue(BlockStateProperties.POWERED))
                            level.setBlock(loaderPosition.below(), level.getBlockState(loaderPosition.below()).setValue(BlockStateProperties.POWERED, false), 3);
                        break;
                    }
                }
        }
    }

    public Container getContainerAt(Level world, double x, double y, double z){
        Container inventory = null;

        List<Entity> list = world.getEntities((Entity)null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelector.CONTAINER_ENTITY_SELECTOR);
        if (!list.isEmpty()) {
            inventory = (Container)list.get(level.random.nextInt(list.size()));
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

    public BlockEntityCargoLoader getMaster(){
        BlockEntity te = level.getBlockEntity(getMasterPos());
        if(te != null && te instanceof BlockEntityCargoLoader)
            return (BlockEntityCargoLoader) te;
        return null;
    }

    public IItemHandler getInventory()
    {
        return inventoryHandler.orElse(null);
    }

    public void dropContents(){
        Utils.dropInventoryItems(level, worldPosition, inventory);
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
        return Utils.getInvNorm(inventoryHandler.orElse(null)) * 180f;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        inventoryHandler.ifPresent(inventory->compoundTag.put("inventory", inventory.serializeNBT()));
        compoundTag.putInt("activity", cartActivity);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        inventoryHandler.ifPresent(inventory->inventory.deserializeNBT(compoundTag.getCompound("inventory")));
        cartActivity = compoundTag.getInt("activity");
        super.load(compoundTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockPos masterPos = getMasterPos();
        if (masterPos == null) return super.getCapability(cap, side);
        BlockEntityCargoLoader te = (BlockEntityCargoLoader) level.getBlockEntity(masterPos);
        if (te != null && worldPosition.equals(masterPos.above()) && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventoryHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Cargo Loader");
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player p_39956_) {
        return new CargoLoaderMenu(id, inv, this);
    }
}
