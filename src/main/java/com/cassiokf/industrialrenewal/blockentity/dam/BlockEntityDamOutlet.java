package com.cassiokf.industrialrenewal.blockentity.dam;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import com.cassiokf.industrialrenewal.blocks.dam.BlockDamOutlet;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityDamOutlet extends BlockEntitySyncable {
    public static final int MAX_PROCESSING = Config.DAM_OUTLET_WATER_CONSUMPTION.get();
    public static final int AMOUNT_PER_BLOCK = 40000;

    public CustomFluidTank tank = new CustomFluidTank(MAX_PROCESSING){
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            BlockEntityDamOutlet.this.sync();
        }
    };
    //public LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(()->tank);

    public int currentProcessing = 0;
    private int tick = 20;
//    private ArrayList<BlockPos> aPoses = new ArrayList<>();
//    private ArrayList<BlockPos> bPoses = new ArrayList<>();
//    private boolean firstLoad = true;


    public BlockEntityDamOutlet(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DAM_OUTLET.get(), pos, state);
    }

    public void tick() {
        if(!level.isClientSide){
//            if(firstLoad){
//                aPoses = getAvailableSpaces();
//                bPoses = checkAvailableSpace();
//                firstLoad = false;
//            }
            if (tick >= 20) {
                tick = 0;
//                bPoses = checkAvailableSpace();
                releaseWater();
            }
            tank.drain(currentProcessing, IFluidHandler.FluidAction.EXECUTE);
            tick++;
        }
    }

    public void releaseWater(){
        Direction facing = level.getBlockState(worldPosition).getValue(BlockDamOutlet.FACING);
        if(level.getBlockState(worldPosition.relative(facing)).getMaterial().isReplaceable()){
            currentProcessing = MAX_PROCESSING;
            if(tank.getFluidAmount() > 0)
                level.setBlock(worldPosition.relative(facing), Blocks.WATER.defaultBlockState(), 3);
            else
                level.setBlock(worldPosition.relative(facing), Blocks.AIR.defaultBlockState(), 3);
        }
        else
            currentProcessing = 0;
    }

//    public void releaseWater(){
//        int power = tank.getFluidAmount() / AMOUNT_PER_BLOCK;
////        Utils.debug("list size", bPoses.size(), power, tank.getFluidAmount());
//        if(bPoses.size() > power){
//            for (int i = 0; i < power; i++) {
//                if(level.getBlockState(bPoses.get(i)).getMaterial().isReplaceable())
//                    level.setBlock(bPoses.get(i), Blocks.WATER.defaultBlockState(), 3);
//            }
//            for (int j = power; j < bPoses.size(); j++) {
//                if(level.getBlockState(bPoses.get(j)) == Blocks.WATER.defaultBlockState())
////                    if(level.getBlockState(bPoses.get(j)).getMaterial().isReplaceable())
//                    level.setBlock(bPoses.get(j), Blocks.AIR.defaultBlockState(), 3);
//            }
//        }
//        currentProcessing = AMOUNT_PER_BLOCK * bPoses.size();
//    }
//
//    public ArrayList<BlockPos> getAvailableSpaces(){
//        Direction facing = level.getBlockState(worldPosition).getValue(BlockDamOutlet.FACING);
//        BlockPos checkPos = worldPosition.relative(facing);
//        ArrayList<BlockPos> availableSpace = new ArrayList<>();
//        for(int i = 0; i <= MAX_PROCESSING/AMOUNT_PER_BLOCK; i++){
//            if(level.getBlockState(checkPos).getMaterial().isReplaceable()){
//                availableSpace.add(checkPos);
//                checkPos = checkPos.relative(facing);
//            }
//        }
//        return availableSpace;
//    }
//
//    public ArrayList<BlockPos> checkAvailableSpace(){
//        ArrayList<BlockPos> availableSpace = new ArrayList<>();
//        for(BlockPos pos: aPoses){
//            if(level.getBlockState(pos).getMaterial().isReplaceable()){
//                availableSpace.add(pos);
//            }
//            else
//                break;
//        }
//        return availableSpace;
//    }


    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        tank.writeToNBT(compoundTag);
        compoundTag.putInt("current", currentProcessing);
        super.saveAdditional(compoundTag);
    }


    @Override
    public void load(CompoundTag compoundTag) {
        currentProcessing = compoundTag.getInt("current");
        tank.readFromNBT(compoundTag);
        super.load(compoundTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction facing = level.getBlockState(worldPosition).getValue(BlockDamOutlet.FACING);
        if(side == null)
            return super.getCapability(cap, side);
        
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side.equals(facing.getOpposite()))
            return LazyOptional.of(()->tank).cast();
        return super.getCapability(cap, side);
    }
}
