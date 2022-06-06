package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x2x2MachineBase;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityLathe extends TileEntity3x2x2MachineBase<TileEntityLathe> implements ITickableTileEntity {
    public TileEntityLathe(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityLathe(){
        super(ModTileEntities.LATHE_TILE.get());
    }

    @Override
    public void tick() {

    }
}
