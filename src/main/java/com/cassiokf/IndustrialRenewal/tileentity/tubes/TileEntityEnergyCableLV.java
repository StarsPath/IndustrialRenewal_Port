package com.cassiokf.IndustrialRenewal.tileentity.tubes;

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

    //TODO: add to config
    @Override
    public int getMaxEnergyToTransport() {
        return 256;
    }

    @Override
    public boolean instanceOf(TileEntity te) {
        return te instanceof TileEntityEnergyCableLV;
    }
}
