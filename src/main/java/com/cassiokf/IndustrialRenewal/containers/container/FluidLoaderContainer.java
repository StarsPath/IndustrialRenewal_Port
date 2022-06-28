package com.cassiokf.IndustrialRenewal.containers.container;

import com.cassiokf.IndustrialRenewal.containers.ContainerBase;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModContainers;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityCargoLoader;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityFluidLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class FluidLoaderContainer extends ContainerBase {

    private final TileEntityFluidLoader tileEntity;
    private PlayerInventory playerInventory;

    public FluidLoaderContainer(int windowId, PlayerInventory playerInventory, TileEntityFluidLoader tileEntity){
        super(ModContainers.FLUID_LOADER_CONTAINER.get(), windowId);
        this.tileEntity = tileEntity;
        this.playerInventory = playerInventory;

        drawPlayerInv(playerInventory);
    }

    public TileEntityFluidLoader getTileEntity(){
        return tileEntity;
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                playerEntity, ModBlocks.FLUID_LOADER.get());
    }


}
