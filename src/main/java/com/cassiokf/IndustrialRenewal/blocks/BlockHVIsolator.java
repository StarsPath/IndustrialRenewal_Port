package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractFacing;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWireIsolator;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHVIsolator extends BlockAbstractFacing {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());

    protected static final VoxelShape NORTH_AABB = Block.box(5, 5, 0, 11, 11, 8);
    protected static final VoxelShape SOUTH_AABB = Block.box(5, 5, 8, 11, 11, 16);
    protected static final VoxelShape EAST_AABB = Block.box(8, 5, 5, 16, 11, 11);
    protected static final VoxelShape WEST_AABB = Block.box(0, 5, 5, 8, 11, 11);
    protected static final VoxelShape UP_AABB = Block.box(5, 8, 5, 11, 16, 11);
    protected static final VoxelShape DOWN_AABB = Block.box(5, 0, 5, 11, 8, 11);

    public BlockHVIsolator(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getVoxelShape(BlockState state) {
        Direction dir = state.getValue(FACING);
        switch (dir)
        {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case DOWN:
                return DOWN_AABB;
            default:
                return UP_AABB;
        }
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide){
            if(handIn == Hand.MAIN_HAND){
                TileEntityWireIsolator tileEntityWireIsolator = (TileEntityWireIsolator)worldIn.getBlockEntity(pos);
                for(BlockPos node : tileEntityWireIsolator.neighbors){
                    Utils.debug("Node", pos, node);
                }
                Utils.debug("ALL");
                for(BlockPos node : tileEntityWireIsolator.allNodes){
                    Utils.debug("Node", pos, node);
                }
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntityWireIsolator createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityWireIsolator();
    }
}
