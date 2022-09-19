package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x2x2MachineBase;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x2x3MachineBase;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Block3x2x3Base<TE extends TileEntity3x2x3MachineBase> extends Block3x3x3Base<TE>{
    public Block3x2x3Base(Properties properties) {
        super(properties.strength(10f, 10f));
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        //world.setBlockAndUpdate(pos.relative(state.getValue(FACING)).above(), state.setValue(MASTER, true));
        if(!world.isClientSide)
        {
            Direction facing = state.getValue(FACING);
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (isValidPosition(world, pos, facing)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
                for (int y = 0; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            //Utils.debug("checking", x, y, z, (x != 0 && y != 1 && z != 0 ));
                            if (!(x == 0 && y == 1 && z == 0)) {
                                BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                                //Utils.debug("placing", currentPos, x, y, z);
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false));
                                TileEntity3x2x3MachineBase te = (TileEntity3x2x3MachineBase) world.getBlockEntity(currentPos);
                                te.masterPos = pos.above();
                                //te.getMaster();
                                //Utils.debug("Master pos", te.getMaster().getBlockPos());
                            }
                        }
                    }
                }
                placeAdditional(world, pos, state, livingEntity, itemStack);
            }
        }
    }

    public void placeAdditional(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack){

    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        //if (state.getBlock() == newState.getBlock()) return;
        if(!world.isClientSide()) {
            List<BlockPos> blocks = Utils.getBlocksIn3x2x3Centered(pos);

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
        PlayerEntity player = worldIn.getNearestPlayer(EntityPredicate.DEFAULT, pos.getX(), pos.getY(), pos.getZ());
        if (player == null) return false;
        for (int y = 0; y < 2; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    BlockPos currentPos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.getMaterial().isReplaceable())
                        return false;
                }
            }
        }
        //Utils.debug("valid for placement");
        return true;
    }
}
