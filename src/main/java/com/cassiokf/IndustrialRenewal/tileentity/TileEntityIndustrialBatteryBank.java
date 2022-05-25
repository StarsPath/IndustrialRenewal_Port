package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityIndustrialBatteryBank extends TileEntityTowerBase<TileEntityIndustrialBatteryBank> implements ITickableTileEntity {
    public TileEntityIndustrialBatteryBank(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityIndustrialBatteryBank(){
        super(ModTileEntities.INDUSTRIAL_BATTERY_TILE.get());
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityIndustrialBatteryBank;
        //return super.instanceOf(tileEntity);
    }

    @Override
    public void tick() {

    }
}
