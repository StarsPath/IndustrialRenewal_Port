package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class BlockAbstractHorizontalFacingWithActivating extends BlockAbstractHorizontalFacing{

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockAbstractHorizontalFacingWithActivating(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_)
    {
        if (worldIn.isClientSide)
        {
            return ActionResultType.SUCCESS;
        } else
        {
            if (state.getValue(ACTIVE))
            {
                //worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_CLOSE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);

            } else
            {
                //worldIn.playSound(null, pos, SoundsRegistration.BLOCK_CATWALKGATE_OPEN.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);

            }

            state = state.cycle(ACTIVE);
            worldIn.setBlock(pos, state, 2);
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    //    @Override
//    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
//    {
//        return worldIn.getBlockState(pos).get(ACTIVE);
//    }
}
