package com.cassiokf.industrialrenewal.blockentity.abstracts;

import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MultiBlockEntityDummy extends BlockEntity {
    public MultiBlockEntityDummy(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MultiBlockEntityDummy(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DUMMY.get(), pos, state);
    }
}
