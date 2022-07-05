package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamIntake;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamTurbine;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityHighPressureFluidPipe extends TileEntityFluidPipeBase<TileEntityHighPressureFluidPipe>{

    BlockPos intakePos;
    BlockPos turbinePos;
    BlockPos outletPos;
    private int intakeCount = 0;
    private int outletCount = 0;
    private int turbineCount = 0;
//    private boolean duplicateIntake = false;
//    private boolean duplicateTurbine = false;
//    private boolean duplicateOutlet = false;

    private int tick = 0;

    public TileEntityHighPressureFluidPipe(TileEntityType<?> tileEntityTypeIn, int maxOutput) {
        super(tileEntityTypeIn, maxOutput);
    }

    public TileEntityHighPressureFluidPipe(){
        super(ModTileEntities.HIGH_PRESSURE_PIPE.get(), 200000);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide && isMaster()){
            // 5 seconds per update
            if(tick >= 20 * 5){
                tick = 0;
//                if(intakePos == null || !(level.getBlockEntity(intakePos) instanceof TileEntityDamIntake) || duplicateIntake)
//                    searchIntake();
                searchIntake();
                Utils.debug("Pipe Master", intakePos, turbinePos, outletPos, intakeCount, turbineCount, outletCount);
//                Utils.debug("multiplier", getMultiplier());
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
//        Utils.debug("getMult called", isMaster());
//        Utils.debug("Pipe Master", intakePos, turbinePos, outletPos, intakeCount, turbineCount, outletCount);
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
//                else if(level.getBlockEntity(pos) instanceof TileEntityDamOutlet){
//                    if(outletPos == null || !(level.getBlockEntity(outletPos) instanceof TileEntityDamTurbine))
//                        outletPos = pos;
//                    else
//                        duplicateOutlet = true;
//                }}
            }
        }
//        duplicateIntake = false;
//        duplicateTurbine = false;
//        duplicateOutlet = false;
    }
}
