package com.cassiokf.IndustrialRenewal.network;

import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityBaseLoader;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundLoaderPacket {

    public final BlockPos loaderPos;
    public final int mode;
    public ServerBoundLoaderPacket(BlockPos pos, int mode){
        this.loaderPos = pos;
        this.mode = mode;
    }

    public ServerBoundLoaderPacket(PacketBuffer buffer){
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(PacketBuffer buffer){
        buffer.writeBlockPos(this.loaderPos);
        buffer.writeInt(this.mode);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context){
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(()->{
            ServerPlayerEntity player = ctx.getSender();
            ServerWorld world = player.getLevel();
            TileEntity te = world.getBlockEntity(loaderPos);
            if(te instanceof TileEntityBaseLoader){
                if(this.mode == 1) {
                    ((TileEntityBaseLoader) te).changeUnload();
                }
                else if(this.mode ==2) {
                    ((TileEntityBaseLoader) te).setNextWaitEnum();
                }
            }
        });
        ctx.setPacketHandled(true);
        return false;
    }
}
