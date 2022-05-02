package com.cassiokf.IndustrialRenewal.blocks.industrialfloor;

import com.cassiokf.IndustrialRenewal.blocks.IRBaseBlock;
import com.cassiokf.IndustrialRenewal.blocks.IRBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockIndustrialFloor extends IRBaseBlock {

//    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.875D, 0.0D, 1.0D, 1.0D, 1.0D);
//    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
//    protected static final AxisAlignedBB NONE_AABB = new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D);
//    protected static final AxisAlignedBB C_UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
//    protected static final AxisAlignedBB C_DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
//    protected static final AxisAlignedBB C_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
//    protected static final AxisAlignedBB C_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
//    protected static final AxisAlignedBB C_WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
//    protected static final AxisAlignedBB C_EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
//
//    protected static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);


    public BlockIndustrialFloor(String name) {
        super(name, Block.Properties.of(Material.METAL, MaterialColor.METAL).strength(0.8f, 12.0f).sound(SoundType.METAL), IRBlockItem::new);
    }

//    private static boolean isValidConnection(final BlockState neighbourState, final World world, final BlockPos ownPos, final Direction neighbourDirection)
//    {
//        Block nb = neighbourState.getBlock();
//        return nb instanceof BlockIndustrialFloor
//                || nb instanceof BlockFloorPipe
//                || nb instanceof BlockFloorCable
//                || (nb instanceof BlockDoor && neighbourState.getValue(BlockDoor.FACING).equals(neighbourDirection))
//                || (neighbourDirection.equals(Direction.DOWN) && nb instanceof BlockCatwalkLadder)
//                || (neighbourDirection.equals(Direction.UP) && nb instanceof BlockCatwalkHatch)
//                //start check for horizontal Iladder
//                || ((neighbourDirection != Direction.UP && neighbourDirection != EnumFacing.DOWN)
//                && nb instanceof BlockCatwalkLadder && !neighbourState.getValue(BlockCatwalkLadder.ACTIVE))
//                //end
//                ;
//    }

//    public static boolean canConnectTo(final World worldIn, final BlockPos ownPos, final Direction neighbourDirection)
//    {
//        final BlockPos neighbourPos = ownPos.offset(neighbourDirection.getNormal());
//        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
//
//        return !isValidConnection(neighbourState, worldIn, ownPos, neighbourDirection);
//    }
//    @Override
//    public AxisAlignedBB getBoundingBox(BlockState state, World worldIn, BlockPos pos)
//    {
//        if (isConnected(worldIn, pos, state, Direction.UP) && !isConnected(worldIn, pos, state, Direction.DOWN))
//        {
//            return UP_AABB;
//        }
//        if (!isConnected(worldIn, pos, state, Direction.UP) && isConnected(worldIn, pos, state, Direction.DOWN))
//        {
//            return DOWN_AABB;
//        }
//        if (!isConnected(worldIn, pos, state, Direction.UP) && !isConnected(worldIn, pos, state, Direction.DOWN))
//        {
//            return NONE_AABB;
//        }
//        return FULL_BLOCK_AABB;
//    }
//
//    @Override
//    public void addCollisionBoxToList(BlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState)
//    {
//        if (isConnected(worldIn, pos, state, Direction.UP))
//        {
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_UP_AABB);
//        }
//        if (isConnected(worldIn, pos, state, DirectionDirection.DOWN))
//        {
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_DOWN_AABB);
//        }
//        if (isConnected(worldIn, pos, state, Direction.NORTH))
//        {
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_NORTH_AABB);
//        }Direction
//        if (isConnected(worldIn, pos, state, Direction.SOUTH))
//        {
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_SOUTH_AABB);
//        }Direction
//        if (isConnected(worldIn, pos, state, Direction.WEST))
//        {
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_WEST_AABB);
//        }
//        if (isConnected(worldIn, pos, state, Direction.EAST))
//        {
//            addCollisionBoxToList(pos, entityBox, collidingBoxes, C_EAST_AABB);
//        }
//    }

}
