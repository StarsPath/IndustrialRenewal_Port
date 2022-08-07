package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.config.Config;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamIntake;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamTurbine;
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
                searchIntake();
            }
            tick++;
        }
    }

    @Override
    public void doTick() {
        if(intakeCount<2 && turbineCount<2 && outletCount<2)
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
            }
        }
    }
}
