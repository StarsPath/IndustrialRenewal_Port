package com.cassiokf.industrialrenewal.items.decor;

import com.cassiokf.industrialrenewal.blocks.decor.BlockCatwalk;
import com.cassiokf.industrialrenewal.blocks.decor.BlockCatwalkStair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ItemBlockCatwalkStair extends BlockItem {
    public ItemBlockCatwalkStair(Block block, Properties props) {
        super(block, props);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        Block block = this.getBlock();

        if(context.getPlayer().isCrouching()){
            return context;
        }
        else if(state.getBlock() instanceof BlockCatwalk){
            BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(context.getHorizontalDirection());
            if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                return BlockPlaceContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            else
                return null;
        }
        else if (state.getBlock() instanceof BlockCatwalkStair){
            BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(context.getHorizontalDirection()).move(Direction.UP);
            if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                return BlockPlaceContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
        }
        return super.updatePlacementContext(context);
    }
}
