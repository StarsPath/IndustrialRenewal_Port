package com.cassiokf.IndustrialRenewal.blocks.pipes;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityEnergySwitch;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityFluidValve;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockEnergySwitch extends BlockPipeSwitchBase{
    public BlockEnergySwitch(Properties property) {
        super(property);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityEnergySwitch();
    }
}
