package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCableMV extends TileEntityEnergyCable{
    public TileEntityEnergyCableMV(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public TileEntityEnergyCableMV(){
        super(ModTileEntities.ENERGYCABLE_MV_TILE.get());
    }

    @Override
    public int getMaxEnergyToTransport() {
        return Config.MV_CABLE_TRANSFER_RATE.get();
    }

    @Override
    public boolean instanceOf(TileEntity te) {
        return te instanceof TileEntityEnergyCableMV;
    }
}
