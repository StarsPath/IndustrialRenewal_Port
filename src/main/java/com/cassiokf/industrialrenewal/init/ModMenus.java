package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityStorageChest;
import com.cassiokf.industrialrenewal.menus.menu.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, IndustrialRenewal.MODID);

    public static final RegistryObject<MenuType<StorageChestMenu>> STORAGE_CHEST_CONTAINER =
            registerMenuType(StorageChestMenu::new, "storage_chest_container");

    public static final RegistryObject<MenuType<LatheMenu>> LATHE_CONTAINER =
            registerMenuType(LatheMenu::new, "lathe_container");

    public static final RegistryObject<MenuType<CargoLoaderMenu>> CARGO_LOADER_CONTAINER =
            registerMenuType(CargoLoaderMenu::new, "cargo_loader_container");


    public static final RegistryObject<MenuType<SteamLocomotiveMenu>> STEAM_LOCOMOTIVE_MENU =
            registerMenuType(SteamLocomotiveMenu::new, "steam_locomotive_menu");

    public static final RegistryObject<MenuType<FluidLoaderMenu>> FLUID_LOADER_CONTAINER =
            registerMenuType(FluidLoaderMenu::new, "fluid_loader_container");



    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
