package com.cassiokf.industrialrenewal.blocks.decor;

import com.cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacingWithActivating;
import com.cassiokf.industrialrenewal.init.ModSound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class BlockCatwalkHatch extends BlockAbstractHorizontalFacingWithActivating {

    protected static final VoxelShape RDOWN_AABB = Block.box(0, 0, 0, 16, 3, 16);

    protected static final VoxelShape OPEN_NORTH_AABB = Block.box(0, 0, 0, 16, 16, 3);
    protected static final VoxelShape OPEN_SOUTH_AABB = Block.box(0, 0, 13, 16, 16, 16);
    protected static final VoxelShape OPEN_WEST_AABB = Block.box(0, 0, 0, 3, 16, 16);
    protected static final VoxelShape OPEN_EAST_AABB = Block.box(13, 0, 0, 16, 16, 16);

    public BlockCatwalkHatch(Properties properties) {
        super(properties);
    }

    public BlockCatwalkHatch()
    {
        super(Block.Properties.of(Material.METAL).strength(1f));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return state.getValue(ACTIVE);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return level.getBlockState(pos).getValue(ACTIVE);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
    {
        return getVoxelShape(state);
    }

    public VoxelShape getVoxelShape(BlockState state){
        if (state.getValue(ACTIVE))
        {
            Direction direction = state.getValue(FACING);
            switch (direction)
            {
                default:
                case NORTH:
                    return OPEN_NORTH_AABB;
                case SOUTH:
                    return OPEN_SOUTH_AABB;
                case WEST:
                    return OPEN_WEST_AABB;
                case EAST:
                    return OPEN_EAST_AABB;
            }
        } else
        {
            return RDOWN_AABB;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide){
            worldIn.playSound(null, pos, ModSound.GATE_SOUND.get(), SoundSource.BLOCKS, 0.5f, 1.0f);
        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }
}
