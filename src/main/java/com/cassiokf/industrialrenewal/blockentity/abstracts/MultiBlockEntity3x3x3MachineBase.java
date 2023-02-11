package com.cassiokf.industrialrenewal.blockentity.abstracts;

import com.cassiokf.industrialrenewal.blocks.abstracts.MultiBlock3x3x3Base;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MultiBlockEntity3x3x3MachineBase extends MultiBlockEntityMachineBase{
    public MultiBlockEntity3x3x3MachineBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x3x3Centered(centerPosition);
    }

    @Override
    public void breakMultiBlocks() {
        if(level == null) return;
        onMasterBreak();
        List<BlockPos> list = getListOfBlockPositions(worldPosition);
        for (BlockPos currentPos : list)
        {
            Block block = level.getBlockState(currentPos).getBlock();
            if (block instanceof MultiBlock3x3x3Base) level.removeBlock(currentPos, false);
        }
    }
}
