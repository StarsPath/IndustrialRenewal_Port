package com.cassiokf.industrialrenewal.blocks.decor;

import com.cassiokf.industrialrenewal.blocks.abstracts.IRBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockFrame extends IRBaseBlock {
    public BlockFrame()
    {
        super(Block.Properties.of(Material.METAL).noOcclusion().strength(1f));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }
}
