package com.cassiokf.IndustrialRenewal.network;

import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityCargoLoader;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ServerBoundCargoLoaderPacket {

    public final BlockPos loaderPos;
    public final int mode;
    public ServerBoundCargoLoaderPacket(BlockPos pos, int mode){
        this.loaderPos = pos;
        this.mode = mode;
    }

    public ServerBoundCargoLoaderPacket(PacketBuffer buffer){
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
            if(te instanceof TileEntityCargoLoader){
                if(this.mode == 1) {
                    ((TileEntityCargoLoader) te).toggleUnload();
                }
                else if(this.mode ==2) {
                    Utils.debug("CYCLE MODE");
                    ((TileEntityCargoLoader) te).cycleMode();
                }
            }
        });
        ctx.setPacketHandled(true);
        return false;
    }
}
