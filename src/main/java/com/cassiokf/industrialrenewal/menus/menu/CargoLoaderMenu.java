package com.cassiokf.industrialrenewal.menus.menu;

import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityCargoLoader;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.init.ModMenus;
import com.cassiokf.industrialrenewal.menus.MenuBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraftforge.items.SlotItemHandler;

public class CargoLoaderMenu extends MenuBase {
    private final BlockEntityCargoLoader tileEntity;
    private Inventory playerInventory;

    private int[] xPositions = {62, 80, 98, 71, 89};
    private int[] yPositions = {20, 20, 20, 38, 38};

//    protected CargoLoaderContainer(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_) {
//        super(p_i50105_1_, p_i50105_2_);
//    }
    public CargoLoaderMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, (BlockEntityCargoLoader)inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public CargoLoaderMenu(int windowId, Inventory playerInventory, BlockEntityCargoLoader tileEntity){
        super(ModMenus.CARGO_LOADER_CONTAINER.get(), windowId);
        this.tileEntity = tileEntity;
        this.playerInventory = playerInventory;

        for(int i = 0; i < xPositions.length; i++){
            this.addSlot(new SlotItemHandler(tileEntity.getInventory(), i, xPositions[i], yPositions[i]){
                @Override
                public void setChanged()
                {
                    tileEntity.setChanged();
                    super.setChanged();
                }
            });
        }
        drawPlayerInv(playerInventory);
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.CARGO_LOADER.get());
    }

    public BlockEntityCargoLoader getTileEntity(){
        return tileEntity;
    }
}
