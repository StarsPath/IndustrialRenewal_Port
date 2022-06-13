package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.blocks.BlockCatwalk;
import com.cassiokf.IndustrialRenewal.blocks.BlockCatwalkStair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemBlockCatwalkStair extends BlockItem {
    public ItemBlockCatwalkStair(Block block, Properties props) {
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
            BlockPos.Mutable blockpos$mutable = pos.mutable().move(context.getHorizontalDirection());
            if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                return BlockItemUseContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            else
                return null;
        }
        else if (state.getBlock() instanceof BlockCatwalkStair){
            BlockPos.Mutable blockpos$mutable = pos.mutable().move(context.getHorizontalDirection()).move(Direction.UP);
            if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                return BlockItemUseContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
        }
        return super.updatePlacementContext(context);
    }
}
