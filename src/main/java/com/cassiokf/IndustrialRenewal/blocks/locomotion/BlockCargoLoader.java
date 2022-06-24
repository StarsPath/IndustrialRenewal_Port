package com.cassiokf.IndustrialRenewal.blocks.locomotion;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockCargoLoader extends BlockAbstractHorizontalFacing {

    public static final BooleanProperty MASTER = BooleanProperty.create("master");

    public BlockCargoLoader(Properties properties) {
        super(properties);
    }

    public BlockCargoLoader() {
        super(Block.Properties.of(Material.METAL));
    }

    public static BlockPos getMasterPos(IWorld world, BlockPos pos, Direction facing)
    {
        for (int y = -2; y < 3; y++)
        {
            BlockPos newPos = pos.above(y);
            BlockPos newPosFront = pos.relative(facing).above(y);
            BlockPos newPosBack = pos.relative(facing.getOpposite()).above(y);
            if (world.getBlockState(newPos).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPos).getValue(MASTER)) return newPos;
            }
            if (world.getBlockState(newPosFront).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosFront).getValue(MASTER)) return newPosFront;
            }
            if (world.getBlockState(newPosBack).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosBack).getValue(MASTER)) return newPosBack;
            }
        }
        return null;
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        if (state.getValue(MASTER))
        {
            for (BlockPos pos1 : getBlocks(pos, facing))
            {
                world.removeBlock(pos1, true);
            }
        }
        else
        {
            BlockPos masterPos = getMasterPos(world, pos, facing);
            if (masterPos != null)
            {
                world.removeBlock(masterPos, true);
                for (BlockPos pos1 : getBlocks(masterPos, facing))
                {
                    if (pos1 != pos)
                        world.removeBlock(pos1, true);
                }
            }
        }
        super.destroy(world, pos, state);
    }

    private Set<BlockPos> getBlocks(BlockPos posMaster, Direction facing)
    {
        Set<BlockPos> positions = new HashSet<>();
        positions.add(posMaster);
        positions.add(posMaster.below());
        positions.add(posMaster.below().relative(facing));
        positions.add(posMaster.above());
        positions.add(posMaster.above().relative(facing));
        return positions;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if(canPlaceBlockAt(context.getLevel(), context.getClickedPos(), context.getHorizontalDirection()))
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(MASTER, false);
        return null;
        //return super.getStateForPlacement(context);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        Set<BlockPos> list = getBlocks(pos.above(), state.getValue(FACING));
        for(BlockPos pos1 : list){
            world.setBlockAndUpdate(pos1, state);
        }
        world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos, Direction facing)
    {
        Set<BlockPos> list = getBlocks(pos.above(), facing);
        for(BlockPos blockPos : list){
            if(!worldIn.getBlockState(blockPos).getMaterial().isReplaceable())
                return false;
        }
        return true;
    }
}
