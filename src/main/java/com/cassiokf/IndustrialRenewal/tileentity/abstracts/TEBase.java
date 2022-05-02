package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class TEBase extends TileEntity {

    public static final String[] EMPTY_ARRAY = new String[0];
    public static final Random rand = new Random();

    public TEBase(TileEntityType<?> tileEntityType) {
        //super();
        super(tileEntityType);
    }

    public boolean hasCapability(final Capability<?> capability, @Nullable final Direction facing)
    {
        return getCapability(capability, facing) != null;
    }


}
