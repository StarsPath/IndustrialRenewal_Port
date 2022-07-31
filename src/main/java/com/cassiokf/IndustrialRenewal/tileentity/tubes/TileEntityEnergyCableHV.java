package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.config.Config;
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

    @Override
    public int getMaxEnergyToTransport() {
        return Config.HV_CABLE_TRANSFER_RATE.get();
    }

    @Override
    public boolean instanceOf(TileEntity te) {
        return te instanceof TileEntityEnergyCableHV;
    }
}
