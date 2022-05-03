package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCableHV extends TileEntityEnergyCable{
    public TileEntityEnergyCableHV(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public TileEntityEnergyCableHV(){
        super(ModTileEntities.ENERGYCABLE_HV_TILE.get());
    }

    //TODO: add to config
    @Override
    public int getMaxEnergyToTransport() {
        return 4096;
    }

    @Override
    public boolean instanceOf(TileEntity te) {
        return te instanceof TileEntityEnergyCableHV;
    }
}
