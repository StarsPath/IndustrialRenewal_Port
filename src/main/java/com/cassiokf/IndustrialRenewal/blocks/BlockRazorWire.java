package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class BlockRazorWire extends BlockAbstractHorizontalFacing {

    public static final BooleanProperty FRAME = BooleanProperty.create("frame");

    public BlockRazorWire()
    {
        super(Block.Properties.of(Material.METAL));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRAME);
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entityIn) {
        float damage = 1f;
        if (entityIn instanceof LivingEntity)
        {
            entityIn.makeStuckInBlock(state, new Vector3d(0.25D, 0.05D, 0.25D));
            entityIn.hurt(DamageSource.GENERIC, damage);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return NULL_SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState p_196271_3_, IWorld worldIn, BlockPos currentPos, BlockPos p_196271_6_) {
        Direction face = stateIn.getValue(FACING);
        if (facing == face.getClockWise())
            stateIn = stateIn.setValue(FRAME, canConnect(worldIn, currentPos, stateIn));
        worldIn.setBlock(currentPos, stateIn, Constants.BlockFlags.DEFAULT);
        return stateIn;
    }

//    @Override
//    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean flag) {
//        Direction face = state.getValue(FACING);
//        if (facing == face.getClockWise()) return state.setValue(FRAME, canConnect(world, pos, state));
//        world.setBlock(pos, state, Constants.BlockFlags.DEFAULT);
//        super.neighborChanged(state, world, pos, block, neighborPos, flag);
//    }

    private boolean canConnect(IBlockReader world, BlockPos pos, BlockState state)
    {
        Direction facing = state.getValue(FACING);
        return !(world.getBlockState(pos.relative(facing.getClockWise())).getBlock() instanceof BlockRazorWire);
    }
}
