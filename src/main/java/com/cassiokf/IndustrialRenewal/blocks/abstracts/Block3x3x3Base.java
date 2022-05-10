package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public abstract class Block3x3x3Base <TE extends TileEntity3x3MachineBase> extends BlockAbstractHorizontalFacing{

    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public Block3x3x3Base(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(MASTER, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(MASTER, false);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        //world.setBlockAndUpdate(pos.relative(state.getValue(FACING)).above(), state.setValue(MASTER, true));
        if(!world.isClientSide)
        {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (isValidPosition(world, pos)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
                for (int y = 0; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            //Utils.debug("checking", x, y, z, (x != 0 && y != 1 && z != 0 ));
                            if (!(x == 0 && y == 1 && z == 0)) {
                                BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                                //Utils.debug("placing", currentPos, x, y, z);
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false));
                                TileEntity3x3MachineBase te = (TileEntity3x3MachineBase) world.getBlockEntity(currentPos);
                                te.getMaster();
                                //Utils.debug("Master pos", te.getMaster().getBlockPos());
                            }
                        }
                    }
                }
            }
        }
    }

//    @Override
//    public void onPlace(BlockState state, World world, BlockPos pos, BlockState blockState, boolean flag) {
//        if(isValidPosition(world, pos)){
//            if (state.getValue(MASTER))
//            {
//                for (int y = -1; y < 2; y++)
//                {
//                    for (int z = -1; z < 2; z++)
//                    {
//                        for (int x = -1; x < 2; x++)
//                        {
//                            BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
//                            if (y != 0 || z != 0 || x != 0) {
//                                Utils.debug("placing", currentPos);
//                                world.setBlock(currentPos, state.setValue(MASTER, false), Constants.BlockFlags.DEFAULT);
//                            }
//                        }
//                    }
//                }
//                super.onPlace(state, world, pos, blockState, flag);
//            }
//        }
//    }

    //    @Override
//    public void playerDestroy(World world, PlayerEntity playerEntity, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack itemStack) {
//        super.playerDestroy(world, playerEntity, pos, state, tileEntity, itemStack);
//    }

//    @Override
//    public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
//        super.onPlace(p_220082_1_, p_220082_2_, p_220082_3_, p_220082_4_, p_220082_5_);
//    }


//    @Override
//    public void playerWillDestroy(World p_176208_1_, BlockPos p_176208_2_, BlockState p_176208_3_, PlayerEntity p_176208_4_) {
//        super.playerWillDestroy(p_176208_1_, p_176208_2_, p_176208_3_, p_176208_4_);
//    }

//    @Override
//    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
//        Utils.debug("onRemove is called", pos);
//        if(!world.isClientSide())
//        {
//            TileEntity3x3MachineBase te = (TileEntity3x3MachineBase) world.getBlockEntity(pos);
//            //Utils.debug("break bock at pos", pos, te);
//            if (te != null) {
//                te.breakMultiBlocks();
//            }
//        }
//        super.onRemove(state, world, pos, oldState, isMoving);
//    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        //if (state.getBlock() == newState.getBlock()) return;
        if(!world.isClientSide())
        {
            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);

            for(BlockPos blockPos : blocks){
                TileEntity te = world.getBlockEntity(blockPos);
                //Utils.debug("break bock at pos", pos, te);
                if(te != null){
                    if(te instanceof TileEntity3x3MachineBase && ((TileEntity3x3MachineBase)te).isMaster()){
                        ((TileEntity3x3MachineBase)te).breakMultiBlocks();
                    }
                }
            }
        }
        super.destroy(world, pos, state);
    }

    public boolean isValidPosition(World worldIn, BlockPos pos)
    {
        //PlayerEntity player = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10D, false);
        PlayerEntity player = worldIn.getNearestPlayer(EntityPredicate.DEFAULT, pos.getX(), pos.getY(), pos.getZ());
        if (player == null) return false;
        for (int y = 0; y < 3; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    Direction facing = player.getDirection();
                    //BlockPos currentPos = new BlockPos(pos.relative(facing, z).relative(facing.getClockWise(), x).relative(Direction.UP, y));
                    BlockPos currentPos = new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z);
                    //Utils.debug("clicked on, check for pos", pos, currentPos);
                    BlockState currentState = worldIn.getBlockState(currentPos);
                    if (!currentState.getMaterial().isReplaceable()) {
                        //Utils.debug("invalid for placement");
                        return false;
                    }
                }
            }
        }
        //Utils.debug("valid for placement");
        return true;
    }
}
