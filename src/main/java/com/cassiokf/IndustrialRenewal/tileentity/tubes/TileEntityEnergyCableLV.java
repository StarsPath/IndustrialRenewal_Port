package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityEnergyCableLV extends TileEntityEnergyCable{
    public TileEntityEnergyCableLV(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public TileEntityEnergyCableLV(){
        super(ModTileEntities.ENERGYCABLE_LV_TILE.get());
    }

    @Override
    public int getMaxEnergyToTransport() {
        return Config.LV_CABLE_TRANSFER_RATE.get();
    }

    @Override
    public boolean instanceOf(TileEntity te) {
        return te instanceof TileEntityEnergyCableLV;
    }
}
