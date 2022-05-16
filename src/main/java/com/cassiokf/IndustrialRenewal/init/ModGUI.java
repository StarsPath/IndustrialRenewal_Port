package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModGUI {

    public static DeferredRegister<ContainerType<?>> CONTIANERS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, References.MODID);

    public static void register(IEventBus bus){
        CONTIANERS.register(bus);
    }
}
