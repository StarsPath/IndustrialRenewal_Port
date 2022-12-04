package com.cassiokf.industrialrenewal.blocks.decor;

import com.cassiokf.industrialrenewal.blocks.abstracts.BlockBasicElectricFence;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockElectricFence extends BlockBasicElectricFence {

    public BlockElectricFence()
    {
        super(Block.Properties.of(Material.METAL), 2);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos)
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
    public boolean canConnectTo(Level worldIn, BlockPos currentPos, Direction neighborDirection)
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
//                || nb instanceof IRBaseWall
                ;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        for (Direction face : Direction.Plane.HORIZONTAL) {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlock(pos, state, 3);
        super.neighborChanged(state, world, pos, block, neighborPos, flag);
    }

    @Override
    public boolean fenceCollision()
    {
        return true;
    }
}
