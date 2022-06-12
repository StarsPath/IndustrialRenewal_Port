package com.cassiokf.IndustrialRenewal.item;

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

        if (!state.is(block) || context.getPlayer().isCrouching()) {
            return context;
        }
        else{


        }
        return super.updatePlacementContext(context);
    }
}
