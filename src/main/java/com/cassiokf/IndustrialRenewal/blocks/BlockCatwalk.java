package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockEnergyCable;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.item.ItemBlockCatwalk;
import com.cassiokf.IndustrialRenewal.item.ItemBlockCatwalkStair;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCatwalk extends BlockAbstractSixWayConnections {

    protected static final VoxelShape BASE_AABB = Block.box(0, 0, 0, 16, 2, 16);

    protected static final VoxelShape RNORTH_AABB = Block.box(0, 0, 0, 16, 16, 0.5);
    protected static final VoxelShape RSOUTH_AABB = Block.box(0, 0, 15.5, 16, 16, 16);
    protected static final VoxelShape RWEST_AABB = Block.box(0, 0, 0, 0.5, 16, 16);
    protected static final VoxelShape REAST_AABB = Block.box(15.5, 0, 0, 16, 16, 16);

    protected static final VoxelShape NORTH_AABB = Block.box(0, 0, 0, 16, 24, 0.5);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 0, 15.5, 16, 24, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 0, 0, 0.5, 24, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15.5, 0, 0, 16, 24, 16);

    public BlockCatwalk()
    {
        super(Block.Properties.of(Material.METAL).speedFactor(1.2F), 16, 2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = defaultBlockState();
        for (Direction direction : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(direction), canConnectTo(context.getLevel(), context.getClickedPos(), direction));
        }
        return state;
    }

    public BlockState getStateForPlacement(World level, BlockPos pos) {
        BlockState state = defaultBlockState();
        for (Direction direction : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(direction), canConnectTo(level, pos, direction));
        }
        return state;
    }

    @Override
    public boolean canBeReplaced(BlockState p_196253_1_, BlockItemUseContext context) {
        if(!context.getPlayer().isCrouching())
            return context.getItemInHand().getItem() instanceof ItemBlockCatwalk || context.getItemInHand().getItem() instanceof ItemBlockCatwalkStair;
        //context.getItemInHand().getItem() == this.asItem();// || Block.byItem(context.getItemInHand().getItem()) instanceof BlockCatwalkStair;
        return super.canBeReplaced(p_196253_1_, context);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(!worldIn.isClientSide){
            if (handIn == Hand.MAIN_HAND) {
                Item playerItem = player.getMainHandItem().getItem();
                if (playerItem.equals(ModItems.screwDrive)) {
                    Vector3d hitQuad = hit.getLocation().subtract(Vector3d.atCenterOf(pos));
                    if (hit.getDirection() == Direction.UP)
                        state = state.cycle(quadToDir(hitQuad));
                    else
                        state = state.cycle(getBooleanProperty(hit.getDirection()));

                    worldIn.setBlock(pos, state, 2);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    public BooleanProperty getBooleanProperty(Direction face){
        switch (face){
            default:
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case UP: return UP;
            case DOWN: return DOWN;
        }
    }

    public BooleanProperty quadToDir(Vector3d vector3d){
        if(vector3d.z > vector3d.x && vector3d.z > -vector3d.x)
            return SOUTH;
        if(vector3d.z < vector3d.x && vector3d.z < -vector3d.x)
            return NORTH;
        if(vector3d.z > vector3d.x && vector3d.z < -vector3d.x)
            return WEST;
        if(vector3d.z < vector3d.x && vector3d.z > -vector3d.x)
            return EAST;
        return NORTH;
    }

    protected boolean isValidConnection(final BlockState neighborState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection)
    {
        BlockState downstate = world.getBlockState(ownPos.relative(neighborDirection).below());
        Block nb = neighborState.getBlock();

        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            return nb instanceof BlockCatwalk
                    || nb instanceof DoorBlock
                    || nb instanceof BlockElectricGate
                    || (nb instanceof StairsBlock && (neighborState.getValue(StairsBlock.FACING) == neighborDirection || neighborState.getValue(StairsBlock.FACING) == neighborDirection.getOpposite()))
                    || (downstate.getBlock() instanceof StairsBlock && downstate.getValue(StairsBlock.FACING) == neighborDirection.getOpposite())
                    || (nb instanceof BlockCatwalkHatch && neighborState.getValue(BlockCatwalkHatch.FACING) == neighborDirection)
                    || (nb instanceof BlockCatwalkGate && neighborState.getValue(BlockCatwalkGate.FACING) == neighborDirection.getOpposite())
                    || (nb instanceof BlockCatwalkStair && neighborState.getValue(BlockCatwalkStair.FACING) == neighborDirection)
                    || (downstate.getBlock() instanceof BlockCatwalkStair && downstate.getValue(BlockCatwalkStair.FACING) == neighborDirection.getOpposite())
                    || (downstate.getBlock() instanceof BlockCatwalkLadder && downstate.getValue(BlockCatwalkLadder.FACING) == neighborDirection.getOpposite())
                    || (nb instanceof BlockCatwalkLadder && neighborState.getValue(BlockCatwalkLadder.FACING) == neighborDirection && !neighborState.getValue(BlockCatwalkLadder.ACTIVE))
            ;
        }
        if (neighborDirection == Direction.DOWN)
        {
            return nb instanceof BlockCatwalkLadder
                    || nb instanceof LadderBlock
//                    || nb instanceof BlockIndustrialFloor || nb instanceof BlockFloorCable || nb instanceof BlockFloorPipe
                    || nb instanceof BlockCatwalk;
        }
        return !(neighborState.getBlock() instanceof BlockEnergyCable);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);

        return !isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    public final boolean isConnected(final BlockState state, final Direction facing)
    {
        return state.getValue(getPropertyBasedOnDirection(facing));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        VoxelShape SHAPE = NULL_SHAPE;
        SHAPE = VoxelShapes.or(SHAPE, BASE_AABB);
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        VoxelShape SHAPE = NULL_SHAPE;
        if (isConnected(state, Direction.DOWN))
        {
            SHAPE = VoxelShapes.or(SHAPE, BASE_AABB);
        }
        if (isConnected(state, Direction.NORTH))
        {
            SHAPE = VoxelShapes.or(SHAPE, NORTH_AABB);
        }
        if (isConnected(state, Direction.SOUTH))
        {
            SHAPE = VoxelShapes.or(SHAPE, SOUTH_AABB);
        }
        if (isConnected(state, Direction.WEST))
        {
            SHAPE = VoxelShapes.or(SHAPE, WEST_AABB);
        }
        if (isConnected(state, Direction.EAST))
        {
            SHAPE = VoxelShapes.or(SHAPE, EAST_AABB);
        }
        return SHAPE;
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        for (Direction face : Direction.values())
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
        }
        world.setBlock(pos, state, 2);
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }
}
