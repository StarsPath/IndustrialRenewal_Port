package com.cassiokf.industrialrenewal.blocks.abstracts;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockAbstractNotFullCube extends IRBaseBlock {
    public BlockAbstractNotFullCube(Properties props) {
        super(props.noOcclusion());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_49928_, BlockGetter p_49929_, BlockPos p_49930_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_60472_, BlockGetter p_60473_, BlockPos p_60474_) {
        return 1.0f;
    }
}
