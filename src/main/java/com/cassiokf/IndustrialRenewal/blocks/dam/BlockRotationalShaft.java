package com.cassiokf.IndustrialRenewal.blocks.dam;

import com.cassiokf.IndustrialRenewal.blocks.IRBaseBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockRotationalShaft extends IRBaseBlock {
    public BlockRotationalShaft() {
        super(AbstractBlock.Properties.of(Material.METAL));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }
}
