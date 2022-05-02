package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.data.ModelProperty;

public abstract class TETubeBase extends TE6WayConnection
{
    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public static final BooleanProperty CUP = BooleanProperty.create("cup");
    public static final BooleanProperty CDOWN = BooleanProperty.create("cdown");
    public static final BooleanProperty CNORTH = BooleanProperty.create("cnorth");
    public static final BooleanProperty CSOUTH = BooleanProperty.create("csouth");
    public static final BooleanProperty CEAST = BooleanProperty.create("ceast");
    public static final BooleanProperty CWEST = BooleanProperty.create("cwest");
    //For PillarPipe
    public static final BooleanProperty WUP = BooleanProperty.create("wup");
    public static final BooleanProperty WDOWN = BooleanProperty.create("wdown");
    public static final BooleanProperty WNORTH = BooleanProperty.create("wnorth");
    public static final BooleanProperty WSOUTH = BooleanProperty.create("wsouth");
    public static final BooleanProperty WEAST = BooleanProperty.create("weast");
    public static final BooleanProperty WWEST = BooleanProperty.create("wwest");

    public TETubeBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }
}
