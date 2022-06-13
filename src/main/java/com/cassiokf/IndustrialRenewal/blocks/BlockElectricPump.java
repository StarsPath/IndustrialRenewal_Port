package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTileEntity;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityElectricPump;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, entity, itemStack);
        if (state.getValue(INDEX) == 0)
            world.setBlockAndUpdate(pos.relative(state.getValue(FACING)), state.setValue(INDEX, 1));
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
        popResource((World) world, pos, new ItemStack(this.asItem()));
        super.destroy(world, pos, state);
    }

    private boolean IsPump(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() instanceof BlockElectricPump;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, INDEX);
    }

    @Nullable
    @Override
    public TileEntityElectricPump createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityElectricPump();
    }
}
