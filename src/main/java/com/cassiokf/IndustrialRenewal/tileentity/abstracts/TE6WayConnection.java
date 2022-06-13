package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntityType;

public abstract class TE6WayConnection extends TileEntitySyncable
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public TE6WayConnection(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }
}
