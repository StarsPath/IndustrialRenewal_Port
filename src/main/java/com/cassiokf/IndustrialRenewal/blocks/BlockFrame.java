package com.cassiokf.IndustrialRenewal.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockFrame extends IRBaseBlock{
    public BlockFrame()
    {
        super(Block.Properties.of(Material.METAL).noOcclusion());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }
}
