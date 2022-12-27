package com.cassiokf.industrialrenewal.network;


import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityBaseLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundLoaderPacket {

    public final BlockPos loaderPos;
    public final int mode;
    public ServerBoundLoaderPacket(BlockPos pos, int mode){
        this.loaderPos = pos;
        this.mode = mode;
    }

    public ServerBoundLoaderPacket(FriendlyByteBuf buffer){
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeBlockPos(this.loaderPos);
        buffer.writeInt(this.mode);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context){
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(()->{
            ServerPlayer player = ctx.getSender();
            ServerLevel world = player.getLevel();
            BlockEntity te = world.getBlockEntity(loaderPos);
            if(te instanceof BlockEntityBaseLoader){
                if(this.mode == 1) {
                    ((BlockEntityBaseLoader) te).changeUnload();
                }
                else if(this.mode ==2) {
                    ((BlockEntityBaseLoader) te).setNextWaitEnum();
                }
            }
        });
        ctx.setPacketHandled(true);
        return false;
    }
}
