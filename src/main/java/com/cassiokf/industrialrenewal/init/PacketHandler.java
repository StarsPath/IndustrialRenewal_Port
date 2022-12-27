package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.network.ServerBoundLoaderPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandler {
    private static final String PROTOCOL_VERSIION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(IndustrialRenewal.MODID, "main"),
           ()->PROTOCOL_VERSIION, PROTOCOL_VERSIION::equals, PROTOCOL_VERSIION::equals);

    private PacketHandler(){

    }

    public static void init(){
        int index = 0;
        INSTANCE.messageBuilder(ServerBoundLoaderPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ServerBoundLoaderPacket::encode)
                .decoder(ServerBoundLoaderPacket::new)
                .consumer(ServerBoundLoaderPacket::handle)
                .add();
    }
}
