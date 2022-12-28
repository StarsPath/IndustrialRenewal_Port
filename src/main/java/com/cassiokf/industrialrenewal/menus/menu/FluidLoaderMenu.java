package com.cassiokf.industrialrenewal.menus.menu;

import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityFluidLoader;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.init.ModMenus;
import com.cassiokf.industrialrenewal.menus.MenuBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class FluidLoaderMenu extends MenuBase {

    private final BlockEntityFluidLoader tileEntity;
    private Inventory playerInventory;

    public FluidLoaderMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, (BlockEntityFluidLoader)inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public FluidLoaderMenu(int windowId, Inventory playerInventory, BlockEntityFluidLoader tileEntity){
        super(ModMenus.FLUID_LOADER_CONTAINER.get(), windowId);
        this.tileEntity = tileEntity;
        this.playerInventory = playerInventory;

        drawPlayerInv(playerInventory);
    }

    public BlockEntityFluidLoader getTileEntity(){
        return tileEntity;
    }

    @Override
    public boolean stillValid(Player playerEntity) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.FLUID_LOADER.get());
    }


}
