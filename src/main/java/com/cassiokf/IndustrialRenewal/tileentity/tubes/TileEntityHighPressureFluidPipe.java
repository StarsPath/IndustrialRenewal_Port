package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamIntake;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamOutlet;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamTurbine;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class TileEntityHighPressureFluidPipe extends TileEntityFluidPipeBase<TileEntityHighPressureFluidPipe>{

    BlockPos intakePos;
    BlockPos turbinePos;
    BlockPos outletPos;
    private int intakeCount = 0;
    private int outletCount = 0;
    private int turbineCount = 0;

    private int tick = 0;

    public TileEntityHighPressureFluidPipe(TileEntityType<?> tileEntityTypeIn, int maxOutput) {
        super(tileEntityTypeIn, maxOutput);
    }

    public TileEntityHighPressureFluidPipe(){
        super(ModTileEntities.HIGH_PRESSURE_PIPE.get(), Config.HIGH_PRESSURE_PIPE_TRANSFER_RATE.get());
    }

    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide && isMaster()){
            // 5 seconds per update
            if(tick >= 20 * 5){
                tick = 0;
                validateTileEntities();
                searchIntake();
                sync();
            }
            tick++;
        }
    }

    @Override
    public void doTick() {
        if(intakeCount<2 && turbineCount<2)
            super.doTick();
    }

    public float getMultiplier(){
        if(intakePos!=null && turbinePos!=null && (intakeCount == 1) && (turbineCount == 1)){
            int heightDiff = intakePos.getY() - turbinePos.getY();
            heightDiff = Math.min(heightDiff, 16);

            return (heightDiff / 16f) + 1;
        }
        return 0f;
    }

    public void validateTileEntities(){
        intakePos = hasIntake()? intakePos : null;
        turbinePos = hasTurbine()? turbinePos : null;
        outletPos = hasOutlet()? outletPos : null;
    }

    public boolean hasIntake(){
        return intakePos != null && (level.getBlockEntity(intakePos) instanceof TileEntityDamIntake);
    }

    public boolean hasTurbine(){
        return turbinePos != null && (level.getBlockEntity(turbinePos) instanceof TileEntityDamTurbine);
    }

    public boolean hasOutlet(){
//        if(outletPos != null)
//            Utils.debug("has outlet", outletPos, level.getBlockEntity(outletPos) instanceof TileEntityDamOutlet);
        return outletPos != null && (level.getBlockEntity(outletPos) instanceof TileEntityDamOutlet);
    }

    public void searchIntake(){
        intakeCount = 0;
        outletCount = 0;
        turbineCount = 0;
        for(BlockPos pos: getPosSet().keySet()){
            if(!(level.getBlockEntity(pos) instanceof TileEntityFluidPipeBase)){
                if (level.getBlockEntity(pos) instanceof TileEntityDamIntake) {
                    if (intakePos == null || !pos.equals(intakePos)) {
                        intakePos = pos;
                    }
                    intakeCount++;
                }
                else if(level.getBlockEntity(pos) instanceof TileEntityDamTurbine){
                    if(turbinePos == null || !pos.equals(turbinePos)) {
                        turbinePos = pos;
                    }
                    turbineCount++;
                }
                else if(level.getBlockEntity(pos) instanceof TileEntityDamOutlet){
                    if(outletPos == null || !pos.equals(outletPos)) {
                        outletPos = pos;
                    }
                    outletCount++;
                }
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        if(intakePos != null)
            compound.putLong("intakePos", intakePos.asLong());
        if(turbinePos != null)
            compound.putLong("turbinePos", turbinePos.asLong());
        if(outletPos != null)
            compound.putLong("outletPos", outletPos.asLong());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        intakePos = BlockPos.of(compound.getLong("intakePos"));
        turbinePos = BlockPos.of(compound.getLong("turbinePos"));
        outletPos = BlockPos.of(compound.getLong("outletPos"));
        super.load(state, compound);
    }
}
