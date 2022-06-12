package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.blocks.BlockCatwalk;
import com.cassiokf.IndustrialRenewal.blocks.BlockCatwalkStair;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemBlockCatwalk extends BlockItem {
    public ItemBlockCatwalk(Block block, Properties props) {
        super(block, props);
    }

    @Nullable
    @Override
    public BlockItemUseContext updatePlacementContext(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        Block block = this.getBlock();

        if(context.getPlayer().isCrouching()){
            return context;
        }
        else if(state.getBlock() instanceof BlockCatwalk){
            if(context.getClickedFace() == Direction.UP){
                BlockPos.Mutable blockpos$mutable = pos.mutable().move(context.getHorizontalDirection());
                if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                    return BlockItemUseContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            }
        }
        else if(state.getBlock() instanceof BlockCatwalkStair){
            BlockPos.Mutable blockpos$mutable = pos.mutable().move(context.getHorizontalDirection()).move(Direction.UP);
            if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                return BlockItemUseContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            else
                return null;
        }
//            Vector3d hit = context.getClickLocation();
//            Vector3d hitQuad = hit.subtract(Vector3d.atCenterOf(pos));
        return super.updatePlacementContext(context);
    }

    public Direction quadToDir(Vector3d vector3d){
        if(vector3d.z > vector3d.x && vector3d.z > -vector3d.x)
            return Direction.SOUTH;
        if(vector3d.z < vector3d.x && vector3d.z < -vector3d.x)
            return Direction.NORTH;
        if(vector3d.z > vector3d.x && vector3d.z < -vector3d.x)
            return Direction.WEST;
        if(vector3d.z < vector3d.x && vector3d.z > -vector3d.x)
            return Direction.EAST;
        return Direction.NORTH;
    }
}
