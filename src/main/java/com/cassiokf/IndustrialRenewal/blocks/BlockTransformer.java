package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x2x3Base;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityTransformer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTransformer extends Block3x2x3Base<TileEntityTransformer> {

    public static final IntegerProperty OUTPUT = IntegerProperty.create("output", 1, 2);
    public static final BooleanProperty REDSTONE = BooleanProperty.create("redstone");


    public BlockTransformer(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(OUTPUT, 1).setValue(REDSTONE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OUTPUT, REDSTONE);
    }

    @Override
    public void placeAdditional(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        Direction facing = state.getValue(FACING);
        BlockPos target = pos.relative(facing.getOpposite()).relative(facing.getClockWise());
        BlockState blockState = world.getBlockState(target);
        world.setBlockAndUpdate(target, blockState.setValue(REDSTONE, true));
    }

    public void neighborChanged(BlockState state, World world, BlockPos pos1, Block block, BlockPos pos2, boolean flag) {
        if(state.getValue(REDSTONE)){
            TileEntity te = world.getBlockEntity(pos1);
            if(te instanceof TileEntityTransformer){
                TileEntityTransformer transformerTile = (TileEntityTransformer) te;
                if(transformerTile.masterPos != null && !world.getBlockState(transformerTile.masterPos).getBlock().is(Blocks.AIR)){
                    BlockState masterState = world.getBlockState(transformerTile.masterPos);
                    world.setBlockAndUpdate(transformerTile.masterPos, masterState.setValue(OUTPUT, getNeighborSignal(world, pos1)? 2 : 1));
                }
            }
        }
        super.neighborChanged(state, world, pos1, block, pos2, flag);
    }

    private boolean getNeighborSignal(World world, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            if (world.hasSignal(pos.relative(direction), direction)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityTransformer();
    }
}
