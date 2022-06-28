package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.containers.container.CargoLoaderContainer;
import com.cassiokf.IndustrialRenewal.containers.container.FluidLoaderContainer;
import com.cassiokf.IndustrialRenewal.containers.container.LatheContainer;
import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityLathe;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityCargoLoader;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityFluidLoader;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModContainers {

    public static DeferredRegister<ContainerType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MODID);

    public static final RegistryObject<ContainerType<StorageChestContainer>> STORAGE_CHEST_CONTAINER =
            CONTAINERS.register("storage_chest_container", ()-> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inv.player.level.getBlockEntity(pos);
                return new StorageChestContainer(windowId, inv, ((TileEntityStorageChest) Objects.requireNonNull(tileEntity)).getMaster());
            })));

    public static final RegistryObject<ContainerType<LatheContainer>> LATHE_CONTAINER =
            CONTAINERS.register("lathe_container", ()-> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inv.player.level.getBlockEntity(pos);
                return new LatheContainer(windowId, inv, ((TileEntityLathe) Objects.requireNonNull(tileEntity)).getMaster());
            })));

    public static final RegistryObject<ContainerType<CargoLoaderContainer>> CARGO_LOADER_CONTAINER =
            CONTAINERS.register("cargo_loader_container", ()-> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inv.player.level.getBlockEntity(pos);
                return new CargoLoaderContainer(windowId, inv, ((TileEntityCargoLoader) Objects.requireNonNull(tileEntity)).getMaster());
            })));

    public static final RegistryObject<ContainerType<FluidLoaderContainer>> FLUID_LOADER_CONTAINER =
            CONTAINERS.register("fluid_loader_container", ()-> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inv.player.level.getBlockEntity(pos);
                return new FluidLoaderContainer(windowId, inv, ((TileEntityFluidLoader) Objects.requireNonNull(tileEntity)).getMaster());
            })));

    public static void register(IEventBus bus){
        CONTAINERS.register(bus);
    }
}
