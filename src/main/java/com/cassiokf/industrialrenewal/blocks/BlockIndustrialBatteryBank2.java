package com.cassiokf.industrialrenewal.blocks;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityIndustrialBatteryBank2;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityDummy;
import com.cassiokf.industrialrenewal.blocks.abstracts.MultiBlock3x3x3Base;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockIndustrialBatteryBank2 extends MultiBlock3x3x3Base implements EntityBlock {
    public BlockIndustrialBatteryBank2(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(state.getValue(MASTER)) {
            return ModBlockEntity.TEST_TILE.get().create(pos, state);
        }
        return new MultiBlockEntityDummy(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState state, BlockEntityType<T> p_153214_) {
        if(state.getValue(MASTER)) {
            return ($0, $1, $2, blockEntity) -> ((BlockEntityIndustrialBatteryBank2) blockEntity).tick();
        }
        return null;
//        return EntityBlock.super.getTicker(p_153212_, state, p_153214_);
    }
}
