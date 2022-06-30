package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.blocks.Dam.BlockDamIntake;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityDamIntake extends TileEntitySyncable implements ITickableTileEntity {
    public TileEntityDamIntake(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityDamIntake(){
        super(ModTileEntities.DAM_INTAKE.get());
    }

    public CustomFluidTank tank = new CustomFluidTank(50000){
        @Override
        protected void onContentsChanged() {
//            super.onContentsChanged();
            TileEntityDamIntake.this.sync();
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
    private final int MAX_WATER = WIDTH*HEIGHT*DEPTH;
    // 40 buckets per tick; max 160 min 8
    public final int MAX_WATER_PRODUCTION = 40000;
    public int currentProduction = 0;
    public int tick = 0;

    @Override
    public void tick() {
        if(!level.isClientSide){
            BlockPos posBehind = worldPosition.relative(getFacing().getOpposite());
//            BlockState blockBehind = level.getBlockState();
            TileEntity tileBehind = level.getBlockEntity(posBehind);
            if(tileBehind!= null){
                tileBehind.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, getFacing()).ifPresent((tank)->{
                    tank.fill(new FluidStack(Fluids.WATER, currentProduction), IFluidHandler.FluidAction.EXECUTE);
                });
            }

            if(tick >= 20){
                getWaterEfficiency();
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
        return result;
    }

    public float getBiomeEfficiency(){
        Biome.Category category = level.getBiome(worldPosition).getBiomeCategory();
        switch (category){
            case BEACH:
            case RIVER:
            case OCEAN:
                return 2f;
            case MESA:
            case DESERT:
            case SAVANNA:
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
        if(level.isThundering() && level.isRaining())
            return 2;
        else if(level.isRaining())
            return 1.5f;
        else
            return 1;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("production", currentProduction);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        currentProduction = compound.getInt("production");
        super.load(state, compound);
    }

    public Direction getFacing(){
        return level.getBlockState(worldPosition).getValue(BlockDamIntake.FACING);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == getFacing().getOpposite())
            return tankHandler.cast();
        return super.getCapability(cap, side);
    }
}
