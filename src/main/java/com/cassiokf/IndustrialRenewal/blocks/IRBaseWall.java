package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractBigFence;
import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractFourConnections;
import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockBasicElectricFence;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.WallBlock;
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
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class IRBaseWall extends BlockAbstractFourConnections {
    public static final BooleanProperty CORE = BooleanProperty.create("core");

    public IRBaseWall()
    {
        super(Block.Properties.of(Material.STONE), 8, 16, 24);
        registerDefaultState(defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CORE);
    }

    private boolean shouldRenderCenter(IBlockReader world, BlockPos ownPos)
    {
        return !((canCenterConnectTo(world, ownPos, Direction.NORTH) && canCenterConnectTo(world, ownPos, Direction.SOUTH) && !canCenterConnectTo(world, ownPos, Direction.EAST) && !canCenterConnectTo(world, ownPos, Direction.WEST))
                || (!canCenterConnectTo(world, ownPos, Direction.NORTH) && !canCenterConnectTo(world, ownPos, Direction.SOUTH) && canCenterConnectTo(world, ownPos, Direction.EAST) && canCenterConnectTo(world, ownPos, Direction.WEST)));
    }

    private boolean canCenterConnectTo(final IBlockReader worldIn, final BlockPos ownPos, final Direction neighborDirection)
    {
        final BlockPos neighborPos = ownPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        return nb instanceof IRBaseBlock || neighborState.isFaceSturdy(worldIn, neighborPos, neighborDirection.getOpposite());
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        return nb instanceof IRBaseWall
                || neighborState.isCollisionShapeFullBlock(worldIn, neighborPos)
                || nb instanceof WallBlock
                || nb instanceof FenceGateBlock
                || nb instanceof BlockBasicElectricFence
//                    && (neighborDirection.getCounterClockWise() == neighborState.getValue(BlockAbstractBigFence.FACING)
//                    || neighborDirection.getClockWise() == neighborState.getValue(BlockAbstractBigFence.FACING)))
                || nb instanceof BlockElectricGate
                || (nb instanceof BlockLight && neighborState.getValue(BlockLight.FACING) == neighborDirection.getOpposite())
//                || nb instanceof BlockSignalIndicator
//                || nb instanceof BlockWindow;
        ;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).setValue(CORE, shouldRenderCenter(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
        for(Direction face : Direction.Plane.HORIZONTAL){
            state = state.setValue(getBooleanProperty(face), canConnectTo(world, pos, face));
        }
        state = state.setValue(CORE, shouldRenderCenter(world, pos));
        world.setBlock(pos, state, Constants.BlockFlags.DEFAULT);
    }

//    @Override
//    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
//        //return getFullState(stateIn, worldIn, currentPos);
//        Utils.debug("updateShape");
//        for(Direction face : Direction.Plane.HORIZONTAL){
//            stateIn = stateIn.setValue(getBooleanProperty(face), canConnectTo(worldIn, currentPos, face));
//        }
//        stateIn = stateIn.setValue(CORE, shouldRenderCenter(worldIn, currentPos));
//        return stateIn;
//        //return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos).setValue(CORE, shouldRenderCenter(worldIn, currentPos));
//    }

    public BooleanProperty getBooleanProperty(Direction face){
        switch (face){
            default:
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST: return EAST;
            case WEST: return WEST;
        }
    }

    @Override
    public boolean fenceCollision() {
        return true;
    }
}
