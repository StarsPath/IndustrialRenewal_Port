package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.network.ServerBoundCargoLoaderPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler {
    private static final String PROTOCOL_VERSIION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(References.MODID, "main"),
           ()->PROTOCOL_VERSIION, PROTOCOL_VERSIION::equals, PROTOCOL_VERSIION::equals);

    private PacketHandler(){

    }

    public static void init(){
        int index = 0;
        INSTANCE.messageBuilder(ServerBoundCargoLoaderPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ServerBoundCargoLoaderPacket::encode)
                .decoder(ServerBoundCargoLoaderPacket::new)
                .consumer(ServerBoundCargoLoaderPacket::handle)
                .add();


    }
}
