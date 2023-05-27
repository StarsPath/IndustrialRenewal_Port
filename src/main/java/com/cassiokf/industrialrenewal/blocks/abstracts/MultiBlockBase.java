package com.cassiokf.industrialrenewal.blocks.abstracts;

import com.cassiokf.industrialrenewal.blockentity.BlockEntitySteamBoiler;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityDummy;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class MultiBlockBase extends BlockAbstractHorizontalFacing{

    public static final BooleanProperty MASTER = BooleanProperty.create("master");


    public MultiBlockBase(Properties properties) {
        super(properties.strength(10f, 10f));
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(MASTER, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, BlockGetter p_220080_2_, BlockPos p_220080_3_) {
        return 1.0f;
        //return super.getShadeBrightness(p_220080_1_, p_220080_2_, p_220080_3_);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide){
            BlockEntity be = worldIn.getBlockEntity(pos);
            if(be instanceof MultiBlockEntityDummy dummy){
                return dummy.onUse(state, worldIn, pos, player, handIn, hitResult);
            }
            else if(be instanceof MultiBlockEntityMachineBase master){
                return master.onUse(state, worldIn, pos, player, handIn, hitResult);
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    @Override
    public void onRemove(BlockState preState, Level world, BlockPos pos, BlockState newState, boolean flag) {
        if(!world.isClientSide() && !newState.is(preState.getBlock())){
            BlockEntity be = world.getBlockEntity(pos);
            if(be instanceof MultiBlockEntityDummy dummy){
                dummy.onDestroy();
            }
            else if(be instanceof MultiBlockEntityMachineBase master && preState.getValue(MASTER)){
                master.breakMultiBlocks();
                master.dropAllItems();
                popResource(world, pos, new ItemStack(this.asItem()));
            }
        }
        super.onRemove(preState, world, pos, newState, flag);
    }
}
