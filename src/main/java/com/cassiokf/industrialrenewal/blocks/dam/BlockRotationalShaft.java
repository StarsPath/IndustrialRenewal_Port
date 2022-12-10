package com.cassiokf.industrialrenewal.blocks.dam;

import com.cassiokf.industrialrenewal.blocks.abstracts.IRBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockRotationalShaft extends IRBaseBlock {
    public BlockRotationalShaft() {
        super(BlockBehaviour.Properties.of(Material.METAL).noOcclusion());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, BlockGetter p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }
}
