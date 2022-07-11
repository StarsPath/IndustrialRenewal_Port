package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockFluorescent extends BlockAbstractSixWayConnections {
    public BlockFluorescent()
    {
        super(Block.Properties.of(Material.METAL).lightLevel((blockState)->15), 8, 6);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        super.neighborChanged(state, world, pos, block, neighborPos, flag);
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
            world.setBlock(pos, state, 3);
        }
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        Block nb = neighborState.getBlock();
        if (neighborDirection != Direction.UP)
        {
            return nb instanceof BlockFluorescent;
        }
        return true;
        //return (currentPos.getZ() % 2) == 0;
    }
}
