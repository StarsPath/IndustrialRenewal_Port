package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTileEntity;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityLocker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLocker extends BlockTileEntity<TileEntityLocker> {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public BlockLocker(Properties props) {
        super(props);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, OPEN, DOWN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context)
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(OPEN, false)
                .setValue(DOWN, connectDown(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if (worldIn.isClientSide)
        {
            return ActionResultType.SUCCESS;
        }
        if (player.isCrouching())
        {
            worldIn.setBlockAndUpdate(pos, state.cycle(OPEN));
            return ActionResultType.SUCCESS;
        }
        LockableLootTileEntity ilockablecontainer = getContainer(worldIn, pos);
        if (ilockablecontainer != null)
        {
            //TODO Open/Close sound like rust one
            player.openMenu(ilockablecontainer);
            player.awardStat(Stats.OPEN_CHEST);
        }
        return ActionResultType.SUCCESS;
        //return super.use(p_225533_1_, p_225533_2_, p_225533_3_, p_225533_4_, p_225533_5_, p_225533_6_);
    }

    @Nullable
    @Override
    public TileEntityLocker createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityLocker();
    }

    @Nullable
    public LockableLootTileEntity getContainer(World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getBlockEntity(pos);

        if (!(tileentity instanceof TileEntityLocker))
        {
            return null;
        }
        return (TileEntityLocker) tileentity;
    }

    private boolean connectDown(IBlockReader world, BlockPos pos)
    {
        BlockState downState = world.getBlockState(pos.below());
        return downState.getBlock() instanceof BlockLocker;
    }
}
