package com.cassiokf.industrialrenewal.blocks.transport;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityEnergySwitch;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityFluidValve;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockPipeSwitchBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockFluidValve extends BlockPipeSwitchBase implements EntityBlock {
    public BlockFluidValve(Properties property) {
        super(property);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityFluidValve(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityFluidValve)blockEntity).tick();
    }
}
