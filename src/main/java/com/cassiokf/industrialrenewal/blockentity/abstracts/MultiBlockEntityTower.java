package com.cassiokf.industrialrenewal.blockentity.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public abstract class MultiBlockEntityTower<TE extends MultiBlockEntityTower<?>> extends MultiBlockEntity3x3x3MachineBase{

    public ArrayList<TE> tower = null;

    public MultiBlockEntityTower(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        initTower();
    }

    public boolean isBot(){
        if(level == null) return false;
        BlockEntity blockEntity = level.getBlockEntity(worldPosition.below(3));
        return !(blockEntity instanceof MultiBlockEntityTower<?>);
    }

    public boolean isTop(){
        if(level == null) return false;
        BlockEntity blockEntity = level.getBlockEntity(worldPosition.above(3));
        return !(blockEntity instanceof MultiBlockEntityTower<?>);
    }

    public MultiBlockEntityTower<TE> getBelow(){
        if(level == null) return null;
        BlockEntity blockEntity = level.getBlockEntity(worldPosition.below(3));
        if(blockEntity instanceof MultiBlockEntityTower<?>)
            return (MultiBlockEntityTower<TE>) blockEntity;
        else
            return null;
    }

    public MultiBlockEntityTower<TE> getBottom(){
        if(isBot()){
            return this;
        }
        else{
            return getBelow().getBottom();
        }
    }

    public void addTower(TE t){
        if(tower == null){
            initTower();
        }
        tower.add(t);
    }

    public boolean initTower(){
        if(isBot()){
            if(tower == null){
                tower = new ArrayList<>();
                TE to = (TE) this;
                tower.add(to);
                return true;
            }
        }
        else{
            TE to = (TE) this;
            getBottom().addTower(to);
            return true;
        }

        return false;
    }
}
