package com.cassiokf.IndustrialRenewal.tileentity.locomotion;

import com.cassiokf.IndustrialRenewal.blocks.locomotion.BlockCargoLoader;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFluidLoader extends TileEntityBaseLoader implements ITickableTileEntity {

    public CustomFluidTank tank = new CustomFluidTank(16000){

        @Override
        public boolean canFill()
        {
            return !TileEntityFluidLoader.this.isUnload();
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            TileEntityFluidLoader.this.sync();
        }
    };
    public LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(()->tank);

    private final int maxFlowPerTick = 200;
    private boolean checked = false;
    private boolean master;
    private float ySlide = 0;

    private int cartFluidAmount;
    private int cartFluidCapacity;
    private int noActivity = 0;

    public TileEntityFluidLoader(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityFluidLoader() {
        super(ModTileEntities.FLUID_LOADER.get());
    }


    @Override
    public void tick() {

    }

    @Override
    public Direction getBlockFacing() {
        if (blockFacing == null) blockFacing = level.getBlockState(worldPosition).getValue(BlockCargoLoader.FACING);
        return blockFacing;
    }

    public TileEntityFluidLoader getMaster(){
        if(isMaster())
            return this;
        else if(level.getBlockEntity(worldPosition.below())instanceof TileEntityFluidLoader)
            return (TileEntityFluidLoader) level.getBlockEntity(worldPosition.below());
        return null;
    }

    public boolean isMaster()
    {
        if (!checked)
        {
            master = level.getBlockState(worldPosition).getValue(BlockCargoLoader.MASTER);
            checked = true;
        }
        return master;
    }

    public String getTankText()
    {
        if (tank.getFluid() == null) return I18n.get("gui.industrialrenewal.fluid.empty");
        return I18n.get("render.industrialrenewal.fluid") + ": " + tank.getFluid().getDisplayName();
    }

    public String getCartName()
    {
        if (cartActivity <= 0) return "No Cart";
        return cartName;
    }

    public float getSlide()
    {
        return ySlide;
    }

    public float getCartFluidAngle()
    {
        if (cartActivity <= 0) return 0;
        float currentAmount = cartFluidAmount;
        float totalCapacity = cartFluidCapacity;
        return Utils.normalizeClamped(currentAmount, 0, totalCapacity) * 180f;
    }

    public float getTankFluidAngle()
    {
        float currentAmount = tank.getFluidAmount();
        float totalCapacity = tank.getCapacity();
        return Utils.normalizeClamped(currentAmount, 0, totalCapacity) * 180f;
    }

    @Override
    public boolean isUnload()
    {
        return unload;
    }

    public LazyOptional<CustomFluidTank> getFluidHandler(){
        return tankHandler;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        tankHandler.ifPresent(tank->tank.writeToNBT(compound));
        compound.putInt("capacity", cartFluidCapacity);
        compound.putInt("cartAmount", cartFluidAmount);
        compound.putInt("activity", cartActivity);
        compound.putBoolean("loading", loading);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        tankHandler.ifPresent(tank->tank.readFromNBT(compound));
        cartFluidCapacity = compound.getInt("capacity");
        cartFluidAmount = compound.getInt("cartAmount");
        cartActivity = compound.getInt("activity");
        loading = compound.getBoolean("loading");
        super.load(state, compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return (isMaster() && side == getBlockFacing().getOpposite() && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                ? tankHandler.cast() : super.getCapability(cap, side);
    }
}
