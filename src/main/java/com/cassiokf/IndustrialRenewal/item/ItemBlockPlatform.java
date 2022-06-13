package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.blocks.BlockPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockPlatform extends BlockItem {
    public ItemBlockPlatform(Block block, Properties props) {
        super(block, props);
    }

    public BlockItemUseContext updatePlacementContext(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        Block block = this.getBlock();

        if(context.getPlayer().isCrouching()){
            return context;
        }
        else if(state.getBlock() instanceof BlockPlatform){
            if(context.getClickedFace() == Direction.UP){
                BlockPos.Mutable blockpos$mutable = pos.mutable().move(context.getHorizontalDirection());
                if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                    return BlockItemUseContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            }
        }
        return super.updatePlacementContext(context);
    }
}
