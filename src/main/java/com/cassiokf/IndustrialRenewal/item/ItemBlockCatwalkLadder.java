package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.blocks.BlockCatwalk;
import com.cassiokf.IndustrialRenewal.blocks.BlockCatwalkLadder;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemBlockCatwalkLadder extends BlockItem {
    public ItemBlockCatwalkLadder(Block block, Properties props) {
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
        else if(state.getBlock() instanceof BlockCatwalkLadder){
            Direction direction = Direction.UP;
            BlockPos.Mutable blockpos$mutable = pos.mutable().move(direction);

            while(true) {
                if (!world.isClientSide && !World.isInWorldBounds(blockpos$mutable)) {
                    PlayerEntity playerentity = context.getPlayer();
                    int j = world.getMaxBuildHeight();
                    if (playerentity instanceof ServerPlayerEntity && blockpos$mutable.getY() >= j) {
                        SChatPacket schatpacket = new SChatPacket((new TranslationTextComponent("build.tooHigh", j)).withStyle(TextFormatting.RED), ChatType.GAME_INFO, Util.NIL_UUID);
                        ((ServerPlayerEntity)playerentity).connection.send(schatpacket);
                    }
                    break;
                }

                state = world.getBlockState(blockpos$mutable);
                if (!state.is(this.getBlock())) {
                    if (state.canBeReplaced(context)) {
                        //Utils.debug("return context", context.getClickedPos());
                        return BlockItemUseContext.at(context, blockpos$mutable, direction);
                    }
                    break;
                }

                blockpos$mutable.move(direction);
            }
        }


        return super.updatePlacementContext(context);
    }
}
