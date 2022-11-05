package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityBarrel extends BlockEntitySyncable {
    private final int MAX_CAPACITY = 64000;

    public final FluidTank tank = new FluidTank(MAX_CAPACITY){
        @Override
        public void onContentsChanged()
        {
            BlockEntityBarrel.this.sync();
        }
    };
    protected LazyOptional<FluidTank> handler;

    public BlockEntityBarrel(BlockPos pos, BlockState state){
        super(ModBlockEntity.BARREL_TILE.get(), pos, state);
        this.handler = LazyOptional.of(()->this.tank);
    }

    public String getFluid(){
        return this.tank.getFluid().getTranslationKey();
    }

    public int getFluidAmount(){
        return this.tank.getFluidAmount();
    }

    public int getMAX_CAPACITY(){
        return MAX_CAPACITY;
    }

//    public String getChatQuantity()
//    {
//        if (this.tank.getFluidAmount() > 0)
//            return String.format("%s: %d/%d mB", I18n.get(this.tank.getFluid().getTranslationKey()), this.tank.getFluidAmount(), MAX_CAPACITY);
//        return "Empty";
//    }

    @Override
    public void setRemoved() {
        super.setRemoved();
//        setChanged();
//        handler.invalidate();
    }

    @Override
    public void saveAdditional (CompoundTag compoundNBT) {
        CompoundTag nbt = new CompoundTag();
        tank.writeToNBT(nbt);
        compoundNBT.put("fluid", nbt);
        super.saveAdditional(compoundNBT);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        CompoundTag nbt = compoundNBT.getCompound("fluid");
        tank.readFromNBT(nbt);

        super.load(compoundNBT);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side == null)
            return super.getCapability(cap, side);

        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return this.handler.cast();
        }

        return super.getCapability(cap, side);
    }
}
