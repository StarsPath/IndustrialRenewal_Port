package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSound {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS
            = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, IndustrialRenewal.MODID);

    public static final RegistryObject<SoundEvent> SCREW_SOUND = SOUND_EVENTS.register("drill", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "drill")));

    public static final RegistryObject<SoundEvent> GATE_SOUND = SOUND_EVENTS.register("gate_closing", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "gate_closing")));

    public static final RegistryObject<SoundEvent> LATHE_SOUND = SOUND_EVENTS.register("lathe", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "lathe")));

    public static final RegistryObject<SoundEvent> MINING_SOUND = SOUND_EVENTS.register("mining", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "mining")));

    public static final RegistryObject<SoundEvent> VALVE_SOUND = SOUND_EVENTS.register("valve", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "valve")));

    public static final RegistryObject<SoundEvent> PUMP_ROTATION_SOUND = SOUND_EVENTS.register("pump_rotation", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "pump_rotation")));

    public static final RegistryObject<SoundEvent> MOTOR_ROTATION_SOUND = SOUND_EVENTS.register("motor_rotation", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "motor_rotation")));

    public static final RegistryObject<SoundEvent> PORTABLE_GENERATOR_SOUND = SOUND_EVENTS.register("port_generator", () ->
            new SoundEvent(new ResourceLocation(IndustrialRenewal.MODID, "port_generator")));


    public static void register(IEventBus bus){
        SOUND_EVENTS.register(bus);
    }
}
