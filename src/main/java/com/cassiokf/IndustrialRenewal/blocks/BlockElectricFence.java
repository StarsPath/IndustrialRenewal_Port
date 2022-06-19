package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockBasicElectricFence;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class BlockElectricFence extends BlockBasicElectricFence {

    public BlockElectricFence()
    {
        super(Block.Properties.of(Material.METAL), 2);
    }

    @Override
    @Deprecated
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        if (isConnected(state, UP))
        {
            return 7;
        } else
        {
            return 0;
        }
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection)
    {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        if (neighborDirection == Direction.DOWN)
        {
            return false;
        }
        if (neighborDirection == Direction.UP)
        {
            return false;
//            int z = Math.abs(currentPos.getZ());
//            int x = Math.abs(currentPos.getX());
//            int w = x - z;
//            return nb.isAir(neighborState, worldIn, neighborPos) && (Math.abs(w) % 3 == 0);
        }
        return nb instanceof BlockElectricFence
                || nb instanceof BlockElectricGate
                || neighborState.isFaceSturdy(worldIn, currentPos.relative(neighborDirection), neighborDirection)
                || nb instanceof BlockBasicElectricFence
                || nb instanceof IRBaseWall
                ;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        for (Direction face : Direction.Plane.HORIZONTAL) {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlock(pos, state, Constants.BlockFlags.DEFAULT);
        super.neighborChanged(state, world, pos, block, neighborPos, flag);
    }

    @Override
    public boolean fenceCollision()
    {
        return true;
    }
}
