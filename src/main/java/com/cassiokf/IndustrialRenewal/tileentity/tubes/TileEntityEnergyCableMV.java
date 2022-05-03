package com.cassiokf.IndustrialRenewal.tileentity.tubes;

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

    //TODO: add to config
    @Override
    public int getMaxEnergyToTransport() {
        return 1024;
    }

    @Override
    public boolean instanceOf(TileEntity te) {
        return te instanceof TileEntityEnergyCableMV;
    }
}
