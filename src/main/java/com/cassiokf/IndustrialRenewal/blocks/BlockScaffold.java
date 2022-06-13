package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractSixWayConnections;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockScaffold extends BlockAbstractSixWayConnections {

    protected static final VoxelShape CBASE_AABB = Block.box(1, 0, 1, 15, 16, 15);

    public BlockScaffold() {
        super(Block.Properties.of(Material.METAL), 16, 16);
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
        World worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState downState = worldIn.getBlockState(pos.below());
//        if(downState.isFaceSturdy(worldIn, pos.below(), Direction.UP)
//                || downState.getBlock() instanceof BlockScaffold)
        return super.getStateForPlacement(context);
    }

    protected boolean isValidConnection(final BlockState neighborState, final IBlockReader world, final BlockPos ownPos, final Direction neighborDirection)
    {
        Block nb = neighborState.getBlock();
        Block nbd = world.getBlockState(ownPos.relative(neighborDirection).below()).getBlock();
        BlockState upState = world.getBlockState(ownPos.above());
        if (neighborDirection == Direction.DOWN)
        {
            return neighborState.isFaceSturdy(world, ownPos.below(), Direction.UP);
        }
        if (neighborDirection != Direction.UP)
        {
            return !(upState.getBlock() instanceof BlockScaffold)
                    && !(upState.isFaceSturdy(world, ownPos.above(), Direction.DOWN))
                    && !(nb instanceof BlockScaffold)
                    && !(nbd instanceof BlockScaffold);
        }
        return !(neighborState.isFaceSturdy(world, ownPos.above(), Direction.DOWN) || nb instanceof BlockScaffold);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = worldIn.getBlockState(neighborPos);
        return isValidConnection(neighborState, worldIn, currentPos, neighborDirection);
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }

    /*
    The Following code, ln 91 - 118, is adapted from
    Immersive Engineering 1.16.5
    /common/blocks/IEBaseBlock.java ln 334 - 361
     */
    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        super.entityInside(state, worldIn, pos, entityIn);
        if(entityIn instanceof LivingEntity&&isLadder(state, worldIn, pos, (LivingEntity)entityIn))
            applyLadderLogic(entityIn);
            //entityIn.setDeltaMovement(handleOnClimbable((LivingEntity)entityIn, entityIn.getDeltaMovement()));
        Utils.debug("INSIDE", entityIn.getDeltaMovement());
    }

    public static void applyLadderLogic(Entity entityIn)
    {
        if(entityIn instanceof LivingEntity&&!((LivingEntity)entityIn).onClimbable())
        {
            Vector3d motion = entityIn.getDeltaMovement();
            float maxMotion = 0.15F;
            motion = new Vector3d(
                    MathHelper.clamp(motion.x, -maxMotion, maxMotion),
                    //Math.max(motion.y, -maxMotion),
                    MathHelper.clamp(motion.y, -maxMotion, maxMotion),
                    MathHelper.clamp(motion.z, -maxMotion, maxMotion)
            );

            entityIn.fallDistance = 0.0F;

            if(motion.y < 0 && entityIn instanceof PlayerEntity && entityIn.isCrouching()) {
                motion = new Vector3d(motion.x, 0, motion.z);
                //Utils.debug("CROUCHING", motion);
            }
            else if(entityIn.horizontalCollision) {
                motion = new Vector3d(motion.x, 0.2, motion.z);
                //Utils.debug("SLIDING", motion);
            }
            //Utils.debug("FINAL", motion);
            entityIn.setDeltaMovement(motion);
            Utils.debug("RESULT", entityIn.getDeltaMovement());
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return CBASE_AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return CBASE_AABB;
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
