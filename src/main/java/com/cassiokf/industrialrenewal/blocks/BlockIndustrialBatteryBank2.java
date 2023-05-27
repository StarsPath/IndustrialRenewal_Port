package com.cassiokf.industrialrenewal.blocks;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityIndustrialBatteryBank2;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityDummy;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityTower;
import com.cassiokf.industrialrenewal.blocks.abstracts.MultiBlock3x3x3Base;
import com.cassiokf.industrialrenewal.blocks.abstracts.MultiBlockTower;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockIndustrialBatteryBank2 extends MultiBlockTower<BlockEntityIndustrialBatteryBank2> implements EntityBlock {
    public BlockIndustrialBatteryBank2(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide){
            BlockEntity be = worldIn.getBlockEntity(pos);
            if(be instanceof MultiBlockEntityDummy dummy){
                return dummy.onUse(state, worldIn, pos, player, handIn, hitResult);
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hitResult);
    }

    @Override
    public boolean hasBase(Level world, BlockPos pos, BlockState state) {
        BlockState stateBelow = world.getBlockState(pos.below(3));
        return !stateBelow.is(ModBlocks.INDUSTRIAL_BATTERY_BANK.get()) || !stateBelow.getValue(MASTER);
    }

    @Override
    public boolean hasTop(Level world, BlockPos pos, BlockState state) {
        BlockState stateAbove= world.getBlockState(pos.above(3));
        return !stateAbove.is(ModBlocks.INDUSTRIAL_BATTERY_BANK.get()) || !stateAbove.getValue(MASTER);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(state.getValue(MASTER)) {
            return ModBlockEntity.TEST_TILE.get().create(pos, state);
        }
//        return null;
        return new MultiBlockEntityDummy(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState state, BlockEntityType<T> p_153214_) {
        if(state.getValue(MASTER)) {
            return ($0, $1, $2, blockEntity) -> ((BlockEntityIndustrialBatteryBank2) blockEntity).tick();
        }
        return null;
    }
}
