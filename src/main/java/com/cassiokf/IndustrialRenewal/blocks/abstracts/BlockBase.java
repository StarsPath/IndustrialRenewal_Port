package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.VoxelShape;


public abstract class BlockBase extends Block
{
    public static final VoxelShape NULL_SHAPE = Block.box(0, 0, 0, 0, 0, 0);
    protected static final VoxelShape FULL_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public BlockBase(Properties properties)
    {
        super(properties.strength(2f, 10f));
    }
}