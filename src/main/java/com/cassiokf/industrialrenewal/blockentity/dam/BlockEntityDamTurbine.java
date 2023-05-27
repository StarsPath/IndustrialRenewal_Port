package com.cassiokf.industrialrenewal.blockentity.dam;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.blocks.dam.BlockDamGenerator;
import com.cassiokf.industrialrenewal.blocks.dam.BlockDamTurbine;
import com.cassiokf.industrialrenewal.blocks.dam.BlockRotationalShaft;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDamTurbine extends MultiBlockEntity3x3x3MachineBase {

    public static final int MAX_PROCESSING = Config.DAM_TURBINE_WATER_TANK_CAPACITY.get();
    public static final int MAX_EFFICIENCY = Config.DAM_TURBINE_MAX_EFFICIENCY.get();

    public CustomFluidTank inTank = new CustomFluidTank(MAX_PROCESSING){
        @Override
        protected void onContentsChanged() {
            BlockEntityDamTurbine.this.sync();
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int amount = super.fill(resource, action);
            inputRate = amount;
            return amount;
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack stack = super.drain(maxDrain, action);
            outputRate = stack.getAmount();
            return stack;
        }
    };

    public CustomFluidTank dummyTank = new CustomFluidTank(0);

    private float oldRotation;
    private float rotation;
//    private int outLetLimit = Config.DAM_TURBINE_WATER_OUTPUT_RATE.get();
    private int tick = 0;
    private float multiplier = 1f;
    private float rotationMultiplier = 1f;
    private int inputRate = 0;
    private int outputRate = 0;


    public LazyOptional<CustomFluidTank> inTankHandler = LazyOptional.of(()-> inTank);
    public LazyOptional<CustomFluidTank> dummyHandler = LazyOptional.of(()-> dummyTank);


    public BlockEntityDamTurbine(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DAM_TURBINE_TILE.get(), pos, state);
    }


    public void tick() {
        if(level == null) return;
        if(!level.isClientSide){
            if(tick >= 20){
                tick = 0;
                multiplier = getMultiplier();
                rotationMultiplier = getRotationMultiplier();
//                Utils.debug("mult", multiplier, rotationMultiplier);
            }
            tick++;
            //Utils.debug("before", inTank.getFluidAmount());
            if(inputRate > 0 && outputRate > 0)
                doRotation();
            else
                rotationDecay();

            releaseWater();
            passRotation();

            //Utils.debug("after", inTank.getFluidAmount());
        }
    }

    private void passRotation(){
        if(level == null) return;
        BlockPos top = worldPosition.above(2);
        for(int i = 0; i < 16; i++){
            if(level.getBlockState(top).getBlock() instanceof BlockDamGenerator){
                BlockEntity te = level.getBlockEntity(top.above());
                if(te instanceof BlockEntityDamGenerator && ((BlockEntityDamGenerator) te).isMaster()) {
                    BlockEntityDamGenerator generatorTE = (BlockEntityDamGenerator) level.getBlockEntity(top.above());
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
        if(level == null) return;
        if(!inTank.isEmpty()){
            Direction masterFace = getBlockState().getValue(BlockDamTurbine.FACING);
            BlockPos pos = getBlockPos().relative(masterFace.getCounterClockWise(), 2).relative(masterFace.getOpposite()).below();
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if(tileEntity != null){
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, masterFace.getClockWise()).ifPresent(tank -> {
                    if(tank.isFluidValid(0, inTank.getFluid())){
                        int transferAmount = tank.fill(inTank.drain(MAX_PROCESSING, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE);
                        int postFill = tank.fill(inTank.drain(transferAmount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
                    }
                });
            }
        }
    }

    private float getMultiplier(){
        float mult = 1.0f;
//        Direction masterFace = getMasterFacing();
//        BlockEntity tileEntity = level.getBlockEntity(worldPosition.relative(masterFace, 2).relative(masterFace.getClockWise()));
//        if(tileEntity != null){
//            if (tileEntity instanceof BlockEntityHighPressureFluidPipe) {
//                BlockEntityHighPressureFluidPipe pipe = (BlockEntityHighPressureFluidPipe) tileEntity;
//                if(((BlockEntityHighPressureFluidPipe)pipe.getMaster()).hasIntake()) {
//                    mult = ((BlockEntityHighPressureFluidPipe) pipe.getMaster()).getMultiplier();
//                }
//            }
//        }
        return mult;
    }

    private float getRotationMultiplier(){
        float mult = 1.0f;
//        Direction masterFace = getMasterFacing();
//        BlockEntity tileEntity = level.getBlockEntity(worldPosition.relative(masterFace.getCounterClockWise(), 2).relative(masterFace.getOpposite()).below());
//        if(tileEntity != null){
//            if (tileEntity instanceof BlockEntityHighPressureFluidPipe) {
//                BlockEntityHighPressureFluidPipe pipe = (BlockEntityHighPressureFluidPipe) tileEntity;
//                if(((BlockEntityHighPressureFluidPipe)pipe.getMaster()).hasOutlet())
//                    return 1;
//            }
//        }
        return mult;
    }

    private void rotationDecay(){
        rotation *= (1 - 0.10f);
        rotation -= 1;
        rotation = Mth.clamp(rotation, 0, MAX_PROCESSING/40f);
        if (rotation != oldRotation)
        {
            oldRotation = rotation;
            sync();
        }
    }

    private void doRotation()
    {
        float norm = Utils.normalize(inTank.getFluidAmount(), 0, MAX_EFFICIENCY);
        float limit = Utils.normalizeClamped((float)outputRate / (float)inputRate, 0, 1);

        float max = MAX_PROCESSING/40f * norm * limit * rotationMultiplier;

//        Utils.debug("max", max, norm, limit, rotationMultiplier);
        if (rotation < max)
        {
            rotation += (Math.min(norm * 10, max - rotation) * multiplier);
        }
        rotation = Mth.clamp(rotation, 0, max);
        if (rotation != oldRotation)
        {
            oldRotation = rotation;
            sync();
        }
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
        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        inTankHandler.ifPresent(tank->tank.writeToNBT(compoundTag));
        compoundTag.putFloat("rotation", rotation);
        compoundTag.putFloat("multiplier", multiplier);
        compoundTag.putFloat("rotationMultiplier", rotationMultiplier);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        inTankHandler.ifPresent(tank->tank.readFromNBT(compoundTag));
        rotation = compoundTag.getFloat("rotation");
        multiplier = compoundTag.getFloat("multiplier");
        rotationMultiplier = compoundTag.getFloat("rotationMultiplier");
        super.load(compoundTag);
    }

    @Override
    public void onMasterBreak() {

    }

    @Override
    public InteractionResult onUse(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        return null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side, BlockPos pos) {
        if(side == null)
            return super.getCapability(cap, side);
        Direction masterFace = getBlockState().getValue(BlockDamTurbine.FACING);

        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && pos.equals(getBlockPos().relative(masterFace).relative(masterFace.getClockWise())) && side == masterFace)
            return inTankHandler.cast();
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && pos.equals(getBlockPos().relative(masterFace.getCounterClockWise()).relative(masterFace.getOpposite()).below()) && side == masterFace.getCounterClockWise())
            return dummyHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void dropAllItems() {

    }
}
