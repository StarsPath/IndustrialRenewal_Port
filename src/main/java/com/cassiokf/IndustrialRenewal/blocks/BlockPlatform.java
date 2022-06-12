package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.item.ItemBlockCatwalk;
import com.cassiokf.IndustrialRenewal.item.ItemBlockCatwalkStair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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

public class BlockPlatform extends BlockAbstractSixWayConnections {

    protected static final VoxelShape BASE_AABB = Block.box(0, 0.0D, 0, 16, 16, 16);
    protected static final VoxelShape NORTH_AABB = Block.box(0, 16, 0, 16, 32, 0.5D);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 16, 15.5D, 16, 32, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 16, 0.0D, 0.03125D, 32, 16);
    protected static final VoxelShape EAST_AABB = Block.box(15.5D, 16, 0, 16, 32, 16);

    public BlockPlatform()
    {
        super(Block.Properties.of(Material.METAL).noOcclusion(), 16, 16);
    }

    @Override
    public boolean canBeReplaced(BlockState p_196253_1_, BlockItemUseContext context) {
        if(!context.getPlayer().isCrouching())
            return context.getItemInHand().getItem() == this.asItem();
        return super.canBeReplaced(p_196253_1_, context);
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

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        BlockPos neighborPos = currentPos.relative(neighborDirection);
        BlockState neighborState = worldIn.getBlockState(neighborPos);
        Block nb = neighborState.getBlock();
        BlockState ub = worldIn.getBlockState(currentPos.above());
        BlockState nub = worldIn.getBlockState(neighborPos.above());
        if (neighborDirection != Direction.UP && neighborDirection != Direction.DOWN)
        {
            return nb instanceof BlockPlatform
                    || neighborState.isFaceSturdy(worldIn, neighborPos, neighborDirection.getOpposite())
                    || nb instanceof RailBlock
                    || (nb instanceof BlockCatwalkStair && neighborState.getValue(BlockCatwalkStair.FACING) == neighborDirection.getOpposite())
                    || (ub.getBlock() instanceof BlockCatwalkGate && neighborDirection == worldIn.getBlockState(currentPos.above()).getValue(BlockCatwalkGate.FACING))
                    || (nub.getBlock() instanceof BlockCatwalkStair && worldIn.getBlockState(neighborPos.above()).getValue(BlockCatwalkStair.FACING) == neighborDirection);
        }
        if (neighborDirection == Direction.DOWN)
        {
            return Block.canSupportRigidBlock(worldIn, neighborPos)//neighborState.isSolid()
                    //|| nb instanceof BlockBrace
                    || nb instanceof BlockPlatform
                    || nb instanceof BlockPillar
                    || nb instanceof BlockColumn;
        }
        return neighborState.isFaceSturdy(worldIn, neighborPos, neighborDirection.getOpposite())
                || nb instanceof BlockPlatform
                || nb instanceof BlockPillar;
    }
    @Override
    public boolean collisionExtendsVertically(BlockState state, IBlockReader world, BlockPos pos, Entity collidingEntity)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        VoxelShape finalShape = BASE_AABB;
        if (!isConnected(state, UP))
        {
            if (!isConnected(state, NORTH))
            {
                finalShape = VoxelShapes.or(finalShape, NORTH_AABB);
            }
            if (!isConnected(state, SOUTH))
            {
                finalShape = VoxelShapes.or(finalShape, SOUTH_AABB);
            }
            if (!isConnected(state, WEST))
            {
                finalShape = VoxelShapes.or(finalShape, WEST_AABB);
            }
            if (!isConnected(state, EAST))
            {
                finalShape = VoxelShapes.or(finalShape, EAST_AABB);
            }
        }
        return finalShape;
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
