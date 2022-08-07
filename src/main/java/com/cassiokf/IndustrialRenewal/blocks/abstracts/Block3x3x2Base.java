package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x2MachineBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Block3x3x2Base <TE extends TileEntity3x3x2MachineBase> extends Block3x3x3Base<TE>{
    public Block3x3x2Base(Properties properties) {
        super(properties.strength(10f, 10f));
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        //world.setBlockAndUpdate(pos.relative(state.getValue(FACING)).above(), state.setValue(MASTER, true));
        if(!world.isClientSide)
        {
            Direction facing = state.getValue(FACING);
            boolean isSided = facing == Direction.EAST || facing == Direction.WEST;
            boolean invert = facing == Direction.NORTH || facing == Direction.WEST;
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (isValidPosition(world, pos, facing)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
                for (int y = 0; y < 3; y++) {
                    for (int z = 0; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            int finalX = (isSided ? z : x);
                            int finalZ = (isSided ? x : z);
                            BlockPos currentPos = new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ));
                            if (!(x == 0 && y == 1 && z == 0)) {
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false));
                            }
                            TileEntity3x3x2MachineBase te = (TileEntity3x3x2MachineBase) world.getBlockEntity(currentPos);
                            te.masterPos =  new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        if(!world.isClientSide()) {
            List<BlockPos> blocks = Utils.getBlocksIn3x3x2Centered(pos, state.getValue(FACING));

            if (state.getValue(MASTER)) {
                for (BlockPos blockPos : blocks) {
                    world.removeBlock(blockPos, false);
                }
            }

            popResource((World) world, pos, new ItemStack(this.asItem()));
        }
    }

    @Override
    public boolean isValidPosition(World worldIn, BlockPos pos, Direction facing)
    {
        boolean isSided = facing == Direction.EAST || facing == Direction.WEST;
        boolean invert = facing == Direction.NORTH || facing == Direction.WEST;

        for (int y = 0; y < 3; y++)
        {
            for (int z = 0; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    int finalX = (isSided ? z : x);
                    int finalZ = (isSided ? x : z);
                    BlockPos currentPos = new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ));
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.getMaterial().isReplaceable())
                        return false;
                }
            }
        }
        return true;
    }
}
