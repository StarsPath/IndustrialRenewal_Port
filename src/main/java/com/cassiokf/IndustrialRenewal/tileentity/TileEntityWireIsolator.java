package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
//import com.cassiokf.IndustrialRenewal.tileentity.abstracts.HvNode;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.stream.Collectors;

public class TileEntityWireIsolator extends TileEntitySyncable {

//    public HvNode node;
    public Set<BlockPos> neighbors;
    public Set<BlockPos> allNodes;

    public TileEntityWireIsolator() {
        super(ModTileEntities.ISOLATOR_TILE.get());
        neighbors = new HashSet<>();
        allNodes = new HashSet<>();
        allNodes.add(this.worldPosition);
    }

    @Override
    public void setRemoved() {
        if(!level.isClientSide){
            Block.popResource(level, worldPosition, new ItemStack(ModItems.coilHV, neighbors.size()));
            unlinkAll();
        }
        super.setRemoved();
    }

    public boolean link(TileEntityWireIsolator neighbor){
        if(neighbors.contains(neighbor.worldPosition) && neighbor.neighbors.contains(this.worldPosition)){
            return false;
        }

        neighbors.add(neighbor.worldPosition);
        neighbor.neighbors.add(this.worldPosition);

        search();
        propagate();

        update();
        neighbor.update();
        return true;
    }

    public void unlinkAll(){
        for(BlockPos neighbor : neighbors){
            Utils.debug("UNLINKING AT", neighbor);
            TileEntity te = level.getBlockEntity(neighbor);
            if(te != null && te instanceof TileEntityWireIsolator){
                TileEntityWireIsolator nodeTE = (TileEntityWireIsolator) te;
                nodeTE.neighbors.remove(this.worldPosition);
                nodeTE.search();
                nodeTE.propagate();
                nodeTE.update();
            }
        }
    }

    public void search(){
        allNodes.clear();
        allNodes.add(this.worldPosition);

        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(this.worldPosition);
        //Iterator<BlockPos> i = queue.iterator();

        while(!queue.isEmpty()){
            BlockPos node = queue.poll();
            Utils.debug("SEARCH", node);
            TileEntity te = level.getBlockEntity(node);
            if(te != null){
                TileEntityWireIsolator nodeTE = (TileEntityWireIsolator) te;
                for(BlockPos neighborNodes : nodeTE.neighbors){
                    if(!allNodes.contains(neighborNodes)){
                        allNodes.add(neighborNodes);
                        queue.add(neighborNodes);
                    }
                }
            }
        }
        this.update();
    }

    public void propagate(){
        Set<BlockPos> clone = new HashSet<BlockPos>(allNodes);
        for(BlockPos node : this.allNodes){
            TileEntity te = level.getBlockEntity(node);
            if(te != null){
                TileEntityWireIsolator nodeTE = (TileEntityWireIsolator) te;
                nodeTE.allNodes = clone;
                nodeTE.update();
            }
        }
    }

    public void update(){
        this.sync();
    }

    public long[] saveNeighbors(){
        return neighbors.stream().map(element -> element.asLong()).mapToLong(i -> i).toArray();
    }

    public long[] saveAllNodes(){
        return allNodes.stream().map(element -> element.asLong()).mapToLong(i -> i).toArray();
    }

    public void loadNeighbors(long[] savedNeighbors){
        if(neighbors != null)
            neighbors.clear();
        else
            neighbors = new HashSet<>();

        for(long blockPos : savedNeighbors){
            neighbors.add(BlockPos.of(blockPos));
        }
    }

    public void loadAllNodes(long[] savedAllNodes){
        if(allNodes != null)
            allNodes.clear();
        else
            allNodes = new HashSet<>();

        for(long blockPos : savedAllNodes){
            allNodes.add(BlockPos.of(blockPos));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putLongArray("neighbors", saveNeighbors());
        compound.putLongArray("all_nodes", saveAllNodes());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        loadNeighbors(compound.getLongArray("neighbors"));
        loadAllNodes(compound.getLongArray("all_nodes"));
        super.load(state, compound);
    }

    @Override
    public String toString() {
        return "TileEntityWireIsolator " + worldPosition;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
