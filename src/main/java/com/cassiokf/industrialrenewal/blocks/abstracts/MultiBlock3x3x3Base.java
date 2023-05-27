package com.cassiokf.industrialrenewal.blocks.abstracts;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityDummy;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityMachineBase;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MultiBlock3x3x3Base extends MultiBlockBase{
    public MultiBlock3x3x3Base(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(MASTER, false);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!world.isClientSide)
        {
            Direction facing = state.getValue(FACING);
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (isValidPosition(world, pos, facing)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
                for (int y = 0; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            if (!(x == 0 && y == 1 && z == 0)) {
                                BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false));
                                BlockEntity be = world.getBlockEntity(currentPos);
                                if(be instanceof MultiBlockEntityDummy dummy){
                                    dummy.masterPos = pos.above();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    

//    @Override
//    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
//        if(!world.isClientSide())
//        {
//            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);
//            for(BlockPos blockPos : blocks){
//                BlockEntity te = world.getBlockEntity(blockPos);
//                if(te != null){
//                    if(te instanceof MultiBlockEntityMachineBase){
//                        ((MultiBlockEntityMachineBase)te).breakMultiBlocks();
//                        popResource((Level) world, te.getBlockPos(), new ItemStack(this.asItem()));
//                    }
//                }
//            }
//        }
//        super.destroy(world, pos, state);
//    }

    public boolean isValidPosition(Level worldIn, BlockPos pos, Direction facing)
    {
        Player player = worldIn.getNearestPlayer(TargetingConditions.forNonCombat(), pos.getX(), pos.getY(), pos.getZ());
        if (player == null) return false;
        for (int y = 0; y < 3; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    BlockPos currentPos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.getMaterial().isReplaceable())
                        return false;
                }
            }
        }
        //Utils.debug("valid for placement");
        return true;
    }
}
