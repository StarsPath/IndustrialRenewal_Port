package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x2MachineBase;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Block3x3x2Base <TE extends TileEntity3x3x2MachineBase> extends Block3x3x3Base<TE>{
    public Block3x3x2Base(Properties properties) {
        super(properties);
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
                            //Utils.debug("checking", x, y, z, (x != 0 && y != 1 && z != 0 ));
                            if (!(x == 0 && y == 1 && z == 0)) {
                                BlockPos currentPos = new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ));
                                //Utils.debug("placing", currentPos, x, y, z);
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false));
                                TileEntity3x3x2MachineBase te = (TileEntity3x3x2MachineBase) world.getBlockEntity(currentPos);
                                te.getMaster();
                                //Utils.debug("Master pos", te.getMaster().getBlockPos());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        //if (state.getBlock() == newState.getBlock()) return;
        if(!world.isClientSide())
        {
            List<BlockPos> blocks = Utils.getBlocksIn3x3x2Centered(pos, state.getValue(FACING));

            if(state.getValue(MASTER)){
                //Utils.debug("block destroyed is master");
                for (BlockPos blockPos : blocks){
                    world.removeBlock(blockPos, false);
                }
                return;
            }
            //Utils.debug("break bock at pos", pos);

            for(BlockPos blockPos : blocks){
                TileEntity te = world.getBlockEntity(blockPos);
                //
                if(te != null){
                    //Utils.debug("break test", blockPos, te instanceof TileEntity3x3x2MachineBase, ((TileEntity3x3x2MachineBase)te).isMaster());
                    if(te instanceof TileEntity3x3x2MachineBase && ((TileEntity3x3x2MachineBase)te).isMaster()){
                        //Utils.debug("isMaster at", blockPos, te);
                        ((TileEntity3x3x2MachineBase)te).breakMultiBlocks();
                    }
                }
            }
        }
        super.destroy(world, pos, state);
    }

    public boolean isValidPosition(World worldIn, BlockPos pos, Direction facing)
    {
        //PlayerEntity player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        PlayerEntity player = worldIn.getNearestPlayer(EntityPredicate.DEFAULT, pos.getX(), pos.getY(), pos.getZ());

        boolean isSided = facing == Direction.EAST || facing == Direction.WEST;
        boolean invert = facing == Direction.NORTH || facing == Direction.WEST;

        if (player == null) return false;
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
                    if (!currentState.getMaterial().isReplaceable()) return false;
                }
            }
        }
        //Utils.debug("valid for placement");
        return true;
    }
}
