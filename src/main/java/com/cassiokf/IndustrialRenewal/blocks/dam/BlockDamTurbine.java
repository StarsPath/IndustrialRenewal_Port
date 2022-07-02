package com.cassiokf.IndustrialRenewal.blocks.dam;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamIntake;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamTurbine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDamTurbine extends Block3x3x3Base<TileEntityDamTurbine> {
    public static BooleanProperty NO_COLLISION = BooleanProperty.create("no_collision");

    public BlockDamTurbine(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(NO_COLLISION, false));
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
        if(!world.isClientSide){
            BlockPos center = pos.above(2);
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    if(i != 0 || j != 0) {
                        BlockPos target = new BlockPos(center.getX() + i, center.getY(), center.getZ() + j);
                        world.setBlock(target, state.setValue(NO_COLLISION, true), 3);
                    }
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NO_COLLISION);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
        if(state.getValue(NO_COLLISION))
            return NULL_SHAPE;
        return super.getCollisionShape(state, p_220071_2_, p_220071_3_, p_220071_4_);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        if(state.getValue(NO_COLLISION))
            return NULL_SHAPE;
        return super.getShape(state, p_220053_2_, p_220053_3_, p_220053_4_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityDamTurbine();
    }
}
