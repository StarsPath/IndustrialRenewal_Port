package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.blocks.IRBaseBlock;
import com.cassiokf.IndustrialRenewal.blocks.IRBlockItem;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockTileEntity<TE extends TileEntity> extends IRBaseBlock
{

    public BlockTileEntity(String name) {
        super(name, IRBlockItem::new);
    }

    public BlockTileEntity(Properties props) {
        super(props);
    }

    public BlockTileEntity(String name, Properties props) {
        super(name, props, IRBlockItem::new);
    }

    public TE getTileEntity(World world, BlockPos pos) {
        return (TE) world.getBlockEntity(pos);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(BlockState state, IBlockReader world);

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }
}