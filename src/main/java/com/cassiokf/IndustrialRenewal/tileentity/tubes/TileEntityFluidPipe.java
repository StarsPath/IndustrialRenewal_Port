package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class TileEntityFluidPipe extends TileEntityFluidPipeBase{

    public TileEntityFluidPipe() {
        super(ModTileEntities.FLUIDPIPE_TILE.get());
    }

    public TileEntityFluidPipe(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

}
