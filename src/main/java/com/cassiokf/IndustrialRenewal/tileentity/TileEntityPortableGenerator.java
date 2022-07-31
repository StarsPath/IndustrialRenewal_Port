package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.handlers.FluidGenerator;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySaveContent;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class TileEntityPortableGenerator extends TileEntitySaveContent implements ITickableTileEntity {

    private final FluidGenerator generator = new FluidGenerator(this);
    private boolean soundStarted = false;
    private final float volume = 0.5f;

    public TileEntityPortableGenerator(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public TileEntityPortableGenerator() {
        super(ModTileEntities.PORTABLE_GENERATOR_TILE.get());
    }

    @Override
    public FluidTank getTank() {
        return generator.tank;
    }

    @Override
    public void tick() {
        if (!level.isClientSide())
        {
            generator.onTick();
            passEnergy();
        }
        else
        {
            handleSound();
        }
    }

    private void passEnergy()
    {
        if (generator.energyStorage.getEnergyStored() >= 0)
        {
            TileEntity te = level.getBlockEntity(worldPosition.relative(getBlockFacing()));
            if (te != null)
            {
                te.getCapability(CapabilityEnergy.ENERGY, getBlockFacing()).ifPresent(cap->{
                    if(cap.canReceive()){
                        int amount = cap.receiveEnergy(generator.energyStorage.extractEnergy(128, true), true);
                        Utils.debug("amount", amount);
                        generator.energyStorage.extractEnergy(cap.receiveEnergy(amount, false), false);
                    }
                });
            }
        }
    }
    private void handleSound()
    {

    }

    public String getChatQuantity(){
        String message = "";
        if(!generator.isGenerating()){
            message += "NOT RUNNING: Out of fuel or no signal.\n";
        }

        message += String.format("%s: %d mB, %d FE/t", I18n.get(generator.getTankContent()), generator.getTankAmount(), generator.getGenerateAmount());
        return message;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        setChanged();
        generator.setRemoved();
    }

    public void changeGenerator()
    {
        generator.changeCanGenerate();
    }

    public boolean isWorking()
    {
        return generator.isGenerating();
    }

    public Direction getBlockFacing()
    {
        return level.getBlockState(worldPosition).getValue(HorizontalBlock.FACING);
    }

    public float getTankFill()
    {
        return Utils.normalizeClamped(getTank().getFluidAmount(), 0, getTank().getCapacity()) * 180f;
    }

    public String getTankText()
    {
        return getTank().getFluidAmount() > 0 ? I18n.get(getTank().getFluid().getTranslationKey()) : "Empty";
    }

    public float getEnergyFill()
    {
        return Utils.normalizeClamped(generator.isGenerating() ? FluidGenerator.energyPerTick : 0, 0, 128) * 90;
    }

    public String getEnergyText()
    {
        return Utils.formatEnergyString(generator.isGenerating() ? FluidGenerator.energyPerTick : 0) + "/t";
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing)
    {
        if (capability == CapabilityEnergy.ENERGY && facing == getBlockFacing())
        {
            return generator.energyHandler.cast();
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return generator.tankHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    public void setCanGenerate(boolean value) {
        generator.setCanGenerate(value);
    }


    @Override
    public CompoundNBT save(CompoundNBT compound) {
        generator.saveGenerator(compound);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        generator.loadGenerator(compound);
        super.load(state, compound);
    }
}
