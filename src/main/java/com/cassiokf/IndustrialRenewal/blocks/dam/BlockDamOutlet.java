package com.cassiokf.IndustrialRenewal.blocks.dam;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamOutlet;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockDamOutlet extends BlockAbstractHorizontalFacing {
    public BlockDamOutlet()  {
        super(AbstractBlock.Properties.of(Material.STONE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityDamOutlet();
    }
}
