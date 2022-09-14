//package com.cassiokf.IndustrialRenewal.tileentity.abstracts;
//
//import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.energy.IEnergyStorage;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class HvNode {
//    public BlockPos pos;
//    public Set<HvNode> neighbor = new HashSet<>();
//    public Set<HvNode> allNodes = new HashSet<>();
//
//    public boolean isProvider;
//    public IEnergyStorage energyStorage;
//
//    public HvNode(BlockPos pos){
//        this.pos = pos;
//    }
//    public HvNode(IEnergyStorage energyStorage){
//        this.energyStorage = energyStorage;
//        isProvider = false;
//    }
//
//    public HvNode(IEnergyStorage energyStorage, boolean isProvider, BlockPos pos){
//        this.energyStorage = energyStorage;
//        this.isProvider = isProvider;
//        this.pos = pos;
//    }
//
//    public void searchNetwork(){
//        allNodes.clear();
//        allNodes.add(this);
//        for(HvNode node : allNodes){
//            for(HvNode neighborNode: node.neighbor){
//                if(!allNodes.contains(neighborNode)){
//                    allNodes.add(neighborNode);
//                }
//            }
//        }
//    }
//
//    public Set<IEnergyStorage> getEnergyStorageProviders(){
//        Set<IEnergyStorage> providers = allNodes.stream().filter(element -> element.isProvider).map(element->element.energyStorage).collect(Collectors.toSet());
//        return providers;
//    }
//
//    public void killNode(){
//        for(HvNode node : neighbor){
//            node.neighbor.remove(this);
//            node.searchNetwork();
//        }
//    }
//
//    public void toggleProvider(){
//        isProvider = !isProvider;
//    }
//
//    public void setEnergyStorage(IEnergyStorage energyStorage){
//        this.energyStorage = energyStorage;
//    }
//
//    public void setProvider(boolean toggle){
//        isProvider = toggle;
//    }
//
//    public Set<HvNode> getNeighbor(){
//        return neighbor;
//    }
//
//    public static void link(HvNode A, HvNode B){
//        A.neighbor.add(B);
//        B.neighbor.add(A);
//
//        A.searchNetwork();
//        B.searchNetwork();
//    }
//
//    public void link(HvNode A){
//        neighbor.add(A);
//        A.neighbor.add(this);
//
//        A.searchNetwork();
//        this.searchNetwork();
//    }
//
//}
