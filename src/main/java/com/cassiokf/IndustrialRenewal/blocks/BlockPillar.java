package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockPillar extends BlockAbstractSixWayConnections {


    public BlockPillar()
    {
        super(Block.Properties.of(Material.METAL), 8, 16);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            return false;
//            return nb instanceof LeverBlock
//                    || (nb instanceof BlockHVIsolator && neighborState.get(BlockHVIsolator.FACING) == neighborDirection.getOpposite())
//                    || nb instanceof RedstoneTorchBlock
//                    || nb instanceof TripWireHookBlock
//                    || nb instanceof BlockColumn
//                    || (nb instanceof BlockCableTray && neighborState.get(BlockCableTray.BASE).equals(EnumBaseDirection.byIndex(neighborDirection.getOpposite().getIndex())))
//                    || nb instanceof LadderBlock
//                    || (nb instanceof BlockLight && neighborState.get(BlockLight.FACING) == neighborDirection.getOpposite())
//                    || nb instanceof BlockRoof
//                    || (nb instanceof BlockBrace && Objects.equals(neighborState.get(BlockBrace.FACING).getName(), neighborDirection.getOpposite().getName()))
//                    || (nb instanceof BlockBrace && Objects.equals(neighborState.get(BlockBrace.FACING).getName(), "down_" + neighborDirection.getName()))
//                    || (nb instanceof BlockAlarm && neighborState.get(BlockAlarm.FACING) == neighborDirection)
//                    || (nb instanceof BlockSignBase && neighborState.get(BlockSignBase.ONWALL) && Objects.equals(neighborState.get(BlockSignBase.FACING).getName(), neighborDirection.getOpposite().getName()))
//                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:connector")
//                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_decoration2")
//                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:wooden_device1")
//                    || Objects.requireNonNull(nb.getRegistryName()).toString().matches("immersiveengineering:metal_device1")
//                    //start Industrial floor side connection
//                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
//                    || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable;
            //end
        }
        if (neighborDirection == Direction.DOWN)
        {
            return Block.canSupportRigidBlock(worldIn, neighborPos);
        }
        return Block.canSupportRigidBlock(worldIn, neighborPos);
        //return false;
//        return neighborState.isSolid() || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorLamp
//                || nb instanceof BlockFloorPipe || nb instanceof BlockFloorCable || nb instanceof BlockCatWalk;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        //Utils.debug("neighbor changed", state, world, pos, block, neighbor, flag);
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlockAndUpdate(pos, state);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }
}
