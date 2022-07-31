package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.dam.BlockDamGenerator;
import com.cassiokf.IndustrialRenewal.blocks.dam.BlockRotationalShaft;
import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityHighPressureFluidPipe;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityDamTurbine extends TileEntity3x3x3MachineBase<TileEntityDamTurbine> implements ITickableTileEntity {

    public static final int MAX_PROCESSING = Config.DAM_TURBINE_WATER_TANK_CAPACITY.get();

    public CustomFluidTank inTank = new CustomFluidTank(MAX_PROCESSING){
        @Override
        protected void onContentsChanged() {
            hasFlow = true;
            TileEntityDamTurbine.this.sync();
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int amount = super.fill(resource, action);
            return amount;
        }
    };

    private boolean hasFlow = false;
    private float oldRotation;
    private float rotation;
    private int outLetLimit = Config.DAM_TURBINE_WATER_OUTPUT_RATE.get();
    private int tick = 0;
    private float multiplier = 1f;

    public LazyOptional<CustomFluidTank> inTankHandler = LazyOptional.of(()-> inTank);

    public TileEntityDamTurbine(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityDamTurbine() {
        super(ModTileEntities.DAM_TURBINE_TILE.get());
    }


    @Override
    public void tick() {
        if(!level.isClientSide && isMaster()){
            if(tick >= 20){
                tick = 0;
                multiplier = getMultiplier();
            }
            tick++;
//            Utils.debug("multiplier", rotation, inTank.getFluidAmount());
            doRotation();
            releaseWater();
            passRotation();

        }
    }

    private void passRotation(){
        BlockPos top = worldPosition.above(2);
        for(int i = 0; i < 16; i++){
            if(level.getBlockState(top).getBlock() instanceof BlockDamGenerator){
                TileEntity te = level.getBlockEntity(top.above());
                if(te instanceof TileEntityDamGenerator && ((TileEntityDamGenerator) te).isMaster()) {
                    TileEntityDamGenerator generatorTE = (TileEntityDamGenerator) level.getBlockEntity(top.above());
                    generatorTE.updateRotation((int)rotation);
                }
                break;
            }
            else if(level.getBlockState(top).getBlock() instanceof BlockRotationalShaft){
                top = top.above();
            }
            else
                break;
        }
    }

    private void releaseWater(){
        if(!inTank.isEmpty()){
            Direction masterFace = getMasterFacing();
            BlockPos pos = getMaster().getBlockPos().relative(masterFace.getCounterClockWise(), 2).relative(masterFace.getOpposite()).below();
            TileEntity tileEntity = level.getBlockEntity(pos);
            if(tileEntity != null){
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, masterFace.getClockWise()).ifPresent(tank -> {
                    if(tank.isFluidValid(0, inTank.getFluid())){
                        FluidStack fluidStack = inTank.drain(MAX_PROCESSING, IFluidHandler.FluidAction.SIMULATE);
                        int transferAmount = Math.min(fluidStack.getAmount(), tank.getTankCapacity(0) - tank.getFluidInTank(0).getAmount());

                        //int amount = tank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);

                        int postFill = tank.fill(inTank.drain(transferAmount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                        outLetLimit = transferAmount;
//                        Utils.debug("outLimit", fluidStack.getAmount(), tank.getTankCapacity(0) - tank.getFluidInTank(0).getAmount(), tank.getTanks());
                    }
                });
            }
            else
                outLetLimit = 0;
        }
    }

    private float getMultiplier(){
        float mult = 0f;
        Direction masterFace = getMasterFacing();
        TileEntity tileEntity = level.getBlockEntity(worldPosition.relative(masterFace, 2).relative(masterFace.getClockWise()));
        if(tileEntity != null){
            if (tileEntity instanceof TileEntityHighPressureFluidPipe) {
                TileEntityHighPressureFluidPipe pipe = (TileEntityHighPressureFluidPipe) tileEntity;
                mult = ((TileEntityHighPressureFluidPipe)pipe.getMaster()).getMultiplier();
//                multiplier = ((TileEntityHighPressureFluidPipe) tileEntity).getMaster().getMultiplier();
            } else {
                mult = 0.5f;
            }
        }
        return mult;
    }

    private void doRotation()
    {
        float norm = Utils.normalizeClamped(inTank.getFluidAmount(), 0, MAX_PROCESSING/40f);
        hasFlow = inTank.getFluidAmount() > 0;
        if (hasFlow && outLetLimit >0)
        {
//            Utils.debug("hasFlow");
            float max = Math.min(outLetLimit/40f, (MAX_PROCESSING/40f * norm));
            if (rotation < max)
            {
                rotation += (Math.min(norm * 10, max - rotation) * multiplier);
            }
            else if (rotation > max)
            {
                rotation *= (1 - 0.02f);
                rotation -=1;
            }
        }
        else if (rotation > 0)
        {
//            Utils.debug("No flow");
            rotation *= (1 - 0.10f);
            rotation -= 1;
        }
        rotation = MathHelper.clamp(rotation, 0, MAX_PROCESSING/40f);
        if (rotation != oldRotation)
        {
            oldRotation = rotation;
            sync();
        }
        hasFlow = false;
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityDamTurbine;
    }

    public String getRotationText()
    {
        return "Rot: " + (int) rotation + " rpm";
    }

    public float getRotationFill()
    {
        return getNormalizedRotation() * 180;
    }

    private float getNormalizedRotation()
    {
        return Utils.normalizeClamped(rotation, 0, MAX_PROCESSING/40f);
    }

//    @Override
//    public float getPitch()
//    {
//        return getNormalizedRotation() * 0.7f;
//    }

//    @Override
//    public float getVolume()
//    {
//        return volume;
//    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        TileEntityDamTurbine master = getMaster();
        if(master == null)
            return super.getCapability(cap, side);
        Direction masterFace = getMasterFacing();
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && worldPosition.equals(master.getBlockPos().relative(masterFace).relative(masterFace.getClockWise())) && side == masterFace)
            return getMaster().inTankHandler.cast();
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && worldPosition.equals(master.getBlockPos().relative(masterFace.getCounterClockWise()).relative(masterFace.getOpposite()).below()) && side == masterFace.getCounterClockWise())
            return getMaster().inTankHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        inTankHandler.ifPresent(tank->tank.writeToNBT(compound));
        compound.putFloat("rotation", rotation);
        compound.putInt("outlet", outLetLimit);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        inTankHandler.ifPresent(tank->tank.readFromNBT(compound));
        rotation = compound.getFloat("rotation");
        outLetLimit = compound.getInt("outlet");
        super.load(state, compound);
    }
}
