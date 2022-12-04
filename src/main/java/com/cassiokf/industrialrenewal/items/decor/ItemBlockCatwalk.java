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
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ItemBlockCatwalk extends BlockItem {
    public ItemBlockCatwalk(Block block, Properties props) {
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
            if(context.getClickedFace() == Direction.UP){
                BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(context.getHorizontalDirection());
                if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                    return BlockPlaceContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            }
        }
        else if(state.getBlock() instanceof BlockCatwalkStair){
            BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(context.getHorizontalDirection()).move(Direction.UP);
            if(world.getBlockState(blockpos$mutable).getMaterial().isReplaceable())
                return BlockPlaceContext.at(context, blockpos$mutable, context.getHorizontalDirection().getOpposite());
            else
                return null;
        }
//            Vector3d hit = context.getClickLocation();
//            Vector3d hitQuad = hit.subtract(Vector3d.atCenterOf(pos));
        return super.updatePlacementContext(context);
    }

    public Direction quadToDir(Vec3 vector3d){
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
