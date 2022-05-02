package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public abstract class TileEntitySaveContent extends TileEntitySyncable {
    public TileEntitySaveContent(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }
    public abstract FluidTank getTank();
}
