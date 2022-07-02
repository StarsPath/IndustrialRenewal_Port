package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamIntake;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class TileEntityHighPressureFluidPipe extends TileEntityFluidPipeBase<TileEntityHighPressureFluidPipe>{

    BlockPos intakePos;
    BlockPos turbinePos;
    BlockPos outletPos;
    private boolean duplicateIntake = false;
    private boolean duplicateTurbine = false;
    private boolean duplicateOutlet = false;

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
            }
            tick++;
        }
    }

    public void searchIntake(){
        for(BlockPos pos: getPosSet().keySet()){
            if(!(level.getBlockEntity(pos) instanceof TileEntityFluidPipeBase)){
                if (level.getBlockEntity(pos) instanceof TileEntityDamIntake) {
                    if (intakePos == null || !(level.getBlockEntity(intakePos) instanceof TileEntityDamIntake))
                        intakePos = pos;
                    else {
                        duplicateIntake = true;
                    }
                }
//                else if(level.getBlockEntity(pos) instanceof TileEntityDamTurbine){
//                    if(turbinePos == null || !(level.getBlockEntity(intakePos) instanceof TileEntityDamTurbine))
//                        turbinePos = pos;
//                    else
//                        duplicateTurbine = true;
//                }
//                else if(level.getBlockEntity(pos) instanceof TileEntityDamOutlet){
//                    if(outletPos == null || !(level.getBlockEntity(outletPos) instanceof TileEntityDamTurbine))
//                        outletPos = pos;
//                    else
//                        duplicateOutlet = true;
//                }}
            }
        }
        duplicateIntake = false;
        duplicateTurbine = false;
        duplicateOutlet = false;
    }
}
