package com.cassiokf.industrialrenewal.blockentity.dam;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import com.cassiokf.industrialrenewal.blocks.dam.BlockDamIntake;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEntityDamIntake extends BlockEntitySyncable {

    public BlockEntityDamIntake(BlockPos pos, BlockState state){
        super(ModBlockEntity.DAM_INTAKE.get(), pos, state);
    }

    public CustomFluidTank tank = new CustomFluidTank(0){

        @Override
        public boolean canDrain() {
            return false;
        }

        @Override
        public boolean canFill() {
            return false;
        }
    };

    public LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(()->tank);

    private final int WIDTH = 7;
    private final int HEIGHT = 7;
    private final int DEPTH = 3;
    private final int MAX_WATER = WIDTH * HEIGHT * DEPTH;
    // 40 buckets per tick; max 160 min 8
    public final int MAX_WATER_PRODUCTION = Config.DAM_INTAKE_WATER_PRODUCTION.get();
    public int currentProduction = 0;
    public int tick = 0;

    public void tick() {
        if(level == null) return;
        if(!level.isClientSide){
            BlockPos posBehind = worldPosition.relative(getFacing().getOpposite());
//            BlockState blockBehind = level.getBlockState();
            BlockEntity tileBehind = level.getBlockEntity(posBehind);
            if(tileBehind!= null){
                tileBehind.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getFacing()).ifPresent((teTank)->{
//                    Utils.debug("water production", currentProduction);
                    teTank.fill(new FluidStack(Fluids.WATER, currentProduction), IFluidHandler.FluidAction.EXECUTE);
                });
            }

            if(tick >= 20){
                getWaterEfficiency();
                sync();
                //currentProduction = (int)(getWaterEfficiency() * MAX_WATER_PRODUCTION);
            }
            tick++;
        }
    }

    public List<BlockPos> getWaterBlocks(){
        BlockPos center = worldPosition.relative(getFacing(), (int)Math.ceil(WIDTH/2f));
        ArrayList<BlockPos> list = new ArrayList<>();

        for(int x = -WIDTH/2; x <= WIDTH/2; x++){
            for(int z = -HEIGHT/2; z <= HEIGHT/2; z++){
                for(int y = -DEPTH/2; y <= DEPTH/2; y++){
                    list.add(center.mutable().move(x, y, z).immutable());
                }
            }
        }
        return list;
    }

    public float getWaterEfficiency(){
        if(level == null)
            return 0;

        int waterBlockCount = 0;
        List<BlockPos> list = getWaterBlocks();
        for(BlockPos pos : list){
            if(level.getBlockState(pos) == Blocks.WATER.defaultBlockState()){
                waterBlockCount++;
            }
        }
        tick = 0;
        float result = (float)waterBlockCount/MAX_WATER * getBiomeEfficiency() * getWeatherEfficiency();
//        Utils.debug("EFF", waterBlockCount, result);
//        float result = Utils.logisticFunction(1, 12, 0.5f, (float)waterBlockCount/MAX_WATER);
        if(result < 0.20)
            currentProduction = 0;
        else
            currentProduction = (int)(result * MAX_WATER_PRODUCTION);

//        Utils.debug("Production", currentProduction, result);
        return result;
    }

    public float getBiomeEfficiency(){
        if(level == null)
            return 0;
        Biome.BiomeCategory category = Biome.getBiomeCategory(level.getBiome(worldPosition));
        switch (category){
            case BEACH:
            case RIVER:
            case OCEAN:
                return 2f;
            case SWAMP:
                return 1.2f;
            case MESA:
            case DESERT:
            case SAVANNA:
            case UNDERGROUND:
                return 0.2f;
            case ICY:
            case TAIGA:
                return 0.8f;
            case NETHER:
                return 0;
            default:
                return 1;
        }
    }

    public float getWeatherEfficiency(){
        if(level == null)
            return 0;
        if(level.isThundering() && level.isRaining())
            return 2;
        else if(level.isRaining())
            return 1.5f;
        else
            return 1;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("production", currentProduction);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        currentProduction = compoundTag.getInt("production");
        super.load(compoundTag);
    }


    public Direction getFacing(){
        return getBlockState().is(ModBlocks.DAM_INTAKE.get())? getBlockState().getValue(BlockDamIntake.FACING) : Direction.NORTH;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side == null)
            return super.getCapability(cap, side);

        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == getFacing().getOpposite())
            return tankHandler.cast();
        return super.getCapability(cap, side);
    }
}
