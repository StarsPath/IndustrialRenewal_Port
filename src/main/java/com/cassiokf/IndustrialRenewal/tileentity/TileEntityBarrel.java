package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySaveContent;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityBarrel extends TileEntitySaveContent {
    private final int MAX_CAPACITY = 64000;

    public final FluidTank tank = new FluidTank(MAX_CAPACITY){
        @Override
        public void onContentsChanged()
        {
            TileEntityBarrel.this.sync();
        }
    };
    protected LazyOptional<FluidTank> handler;

    public TileEntityBarrel(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.handler = LazyOptional.of(()->this.tank);
    }

    public TileEntityBarrel() {
        super(ModTileEntities.BARREL_TILE.get());
        this.handler = LazyOptional.of(()->this.tank);
    }

    public String getChatQuantity()
    {
        if (this.tank.getFluidAmount() > 0)
            return String.format("%s: %d/%d mB", I18n.get(this.tank.getFluid().getTranslationKey()), this.tank.getFluidAmount(), MAX_CAPACITY);
        return "Empty";
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        setChanged();
        handler.invalidate();
    }

    @Override
    public FluidTank getTank() {
        return null;
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        CompoundNBT nbt = new CompoundNBT();
        tank.writeToNBT(nbt);
        compoundNBT.put("fluid", nbt);
        return super.save(compoundNBT);
    }

    @Override
    public void load(BlockState state, CompoundNBT compoundNBT) {
        CompoundNBT nbt = compoundNBT.getCompound("fluid");
        tank.readFromNBT(nbt);

        super.load(state, compoundNBT);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return this.handler.cast();
        }

        return super.getCapability(cap, side);
    }
}
