package com.cassiokf.IndustrialRenewal.blocks.pipes;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityFluidValve;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityFluidPipe;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockFluidValve extends BlockPipeSwitchBase{

    public BlockFluidValve(Properties property) {
        super(property);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityFluidValve();
    }
}
