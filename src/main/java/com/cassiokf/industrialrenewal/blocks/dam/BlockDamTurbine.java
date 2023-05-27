package com.cassiokf.industrialrenewal.blocks.dam;



import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityDummy;
import com.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamOutlet;
import com.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamTurbine;
import com.cassiokf.industrialrenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.industrialrenewal.blocks.abstracts.MultiBlock3x3x3Base;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockDamTurbine extends MultiBlock3x3x3Base implements EntityBlock {
    public static BooleanProperty NO_COLLISION = BooleanProperty.create("no_collision");

    public BlockDamTurbine(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(NO_COLLISION, false));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity livingEntity, ItemStack itemStack) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NO_COLLISION);
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        if(state.getValue(NO_COLLISION))
            return NULL_SHAPE;
        return super.getCollisionShape(state, p_220071_2_, p_220071_3_, p_220071_4_);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        if(state.getValue(NO_COLLISION))
            return NULL_SHAPE;
        return super.getShape(state, p_220053_2_, p_220053_3_, p_220053_4_);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(state.getValue(MASTER)) {
            return ModBlockEntity.DAM_TURBINE_TILE.get().create(pos, state);
        }
        return new MultiBlockEntityDummy(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState state, BlockEntityType<T> p_153214_) {
        if(state.getValue(MASTER)) {
            return ($0, $1, $2, blockEntity) -> ((BlockEntityDamTurbine) blockEntity).tick();
        }
        return null;
    }
}
