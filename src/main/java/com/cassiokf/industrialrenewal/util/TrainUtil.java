package com.cassiokf.industrialrenewal.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

public class TrainUtil {

    public static Direction getTravelDirection(Vec3 vector){
        if(vector.x > 0 )
            return Direction.EAST;
        if(vector.x < 0 )
            return Direction.WEST;
        if(vector.z > 0)
            return Direction.SOUTH;
        if(vector.z < 0)
            return Direction.NORTH;

        return null;
    }

//    public static void conformToRailDirection(AbstractMinecart minecart){
//        int k = Mth.floor(minecart.getX());
//        int i = Mth.floor(minecart.getY());
//        int j = Mth.floor(minecart.getZ());
//
//        if (minecart.level.getBlockState(new BlockPos(k, i - 1, j)).is(BlockTags.RAILS)) {
//            --i;
//        }
//        BlockPos blockpos = new BlockPos(k, i, j);
//        BlockState blockstate = minecart.level.getBlockState(blockpos);
//
//        if(blockstate.getBlock() instanceof RailBlock railBlock) {
//            switch (blockstate.getValue(RailBlock.SHAPE)){
//                case NORTH_SOUTH -> {}
//                case EAST_WEST -> {}
//            }
//        }
//
//    }
}
