package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTileEntity;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityElectricPump;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockElectricPump extends BlockTileEntity<TileEntityElectricPump> {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty INDEX = IntegerProperty.create("index", 0, 1);

    public BlockElectricPump(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(INDEX, 1));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(world.getBlockState(pos.relative(context.getHorizontalDirection())).getMaterial().isReplaceable())
            return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection())
                    .setValue(INDEX, 0);
        return null;
    }

//    @Override
//    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit) {
////        if(world.getBlockState(pos.relative(state.getValue(FACING))).getMaterial().isReplaceable())
//            return super.use(state, world, pos, playerEntity, hand, hit);
//
//    }


//    @Override
//    public void onPlace(BlockState state, World world, BlockPos pos, BlockState state2, boolean flag) {
//        if(world.getBlockState(pos.relative(state2.getValue(FACING))).getMaterial().isReplaceable())
//            super.onPlace(state, world, pos, state2, flag);
//    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, entity, itemStack);
        if (state.getValue(INDEX) == 0)
            world.setBlockAndUpdate(pos.relative(state.getValue(FACING)), state.setValue(INDEX, 1));
        //world.blockUpdated(pos, Blocks.AIR);
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        switch (state.getValue(INDEX))
        {
            case 0:
                if (IsPump(world, pos.relative(state.getValue(FACING))))
                    world.removeBlock(pos.relative(state.getValue(FACING)), false);
                break;
            case 1:
                if (IsPump(world, pos.relative(state.getValue(FACING).getOpposite())))
                    world.removeBlock(pos.relative(state.getValue(FACING).getOpposite()), false);
                break;
        }
        super.destroy(world, pos, state);
    }

//    private boolean IsPump(World world, BlockPos pos)
//    {
//        return world.getBlockState(pos).getBlock() instanceof BlockElectricPump;
//    }
    private boolean IsPump(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricPump;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, INDEX);
    }

//    @Nullable
//    @Override
//    public TileEntityElectricPump createTileEntity(World world, BlockState state) {
//        Utils.debug("creating tile entity");
//        return new TileEntityElectricPump();
//    }

    @Nullable
    @Override
    public TileEntityElectricPump createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityElectricPump();
    }
}
