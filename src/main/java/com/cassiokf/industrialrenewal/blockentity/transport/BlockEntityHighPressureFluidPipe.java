package com.cassiokf.industrialrenewal.blockentity.transport;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityFluidPipeBase;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHighPressureFluidPipe extends BlockEntityFluidPipeBase<BlockEntityHighPressureFluidPipe> {

    BlockPos intakePos;
    BlockPos turbinePos;
    BlockPos outletPos;
    private int intakeCount = 0;
    private int outletCount = 0;
    private int turbineCount = 0;

    private int tick = 0;

    public BlockEntityHighPressureFluidPipe(BlockPos pos, BlockState state){
        super(ModBlockEntity.HIGH_PRESSURE_PIPE.get(), pos, state, Config.HIGH_PRESSURE_PIPE_TRANSFER_RATE.get());
    }

    @Override
    public void tick() {
        super.tick();
    }

    //    @Override
//    public void tick() {
//        super.tick();
//        if(!level.isClientSide && isMaster()){
//            // 5 seconds per update
//            if(tick >= 20 * 5){
//                tick = 0;
//                validateTileEntities();
//                searchIntake();
//                sync();
//            }
//            tick++;
//        }
//    }

//    @Override
//    public void doTick() {
//        if(intakeCount<2 && turbineCount<2)
//            super.doTick();
//    }

    public float getMultiplier(){
        if(intakePos!=null && turbinePos!=null && (intakeCount == 1) && (turbineCount == 1)){
            int heightDiff = intakePos.getY() - turbinePos.getY();
            heightDiff = Math.min(heightDiff, 16);

            return (heightDiff / 16f) + 1;
        }
        return 0f;
    }

//    public void validateTileEntities(){
//        intakePos = hasIntake()? intakePos : null;
//        turbinePos = hasTurbine()? turbinePos : null;
//        outletPos = hasOutlet()? outletPos : null;
//    }

//    public boolean hasIntake(){
//        return intakePos != null && (level.getBlockEntity(intakePos) instanceof BlockEntityDamIntake);
//    }
//
//    public boolean hasTurbine(){
//        return turbinePos != null && (level.getBlockEntity(turbinePos) instanceof BlockEntityDamTurbine);
//    }
//
//    public boolean hasOutlet(){
////        if(outletPos != null)
////            Utils.debug("has outlet", outletPos, level.getBlockEntity(outletPos) instanceof BlockEntityDamOutlet);
//        return outletPos != null && (level.getBlockEntity(outletPos) instanceof BlockEntityDamOutlet);
//    }

//    public void searchIntake(){
//        intakeCount = 0;
//        outletCount = 0;
//        turbineCount = 0;
//        for(BlockPos pos: getPosSet().keySet()){
//            if(!(level.getBlockEntity(pos) instanceof BlockEntityFluidPipeBase)){
//                if (level.getBlockEntity(pos) instanceof BlockEntityDamIntake) {
//                    if (intakePos == null || !pos.equals(intakePos)) {
//                        intakePos = pos;
//                    }
//                    intakeCount++;
//                }
//                else if(level.getBlockEntity(pos) instanceof BlockEntityDamTurbine){
//                    if(turbinePos == null || !pos.equals(turbinePos)) {
//                        turbinePos = pos;
//                    }
//                    turbineCount++;
//                }
//                else if(level.getBlockEntity(pos) instanceof BlockEntityDamOutlet){
//                    if(outletPos == null || !pos.equals(outletPos)) {
//                        outletPos = pos;
//                    }
//                    outletCount++;
//                }
//            }
//        }
//    }
//
//    @Override
//    public CompoundNBT save(CompoundNBT compound) {
//        if(intakePos != null)
//            compound.putLong("intakePos", intakePos.asLong());
//        if(turbinePos != null)
//            compound.putLong("turbinePos", turbinePos.asLong());
//        if(outletPos != null)
//            compound.putLong("outletPos", outletPos.asLong());
//        return super.save(compound);
//    }
//
//    @Override
//    public void load(BlockState state, CompoundNBT compound) {
//        intakePos = BlockPos.of(compound.getLong("intakePos"));
//        turbinePos = BlockPos.of(compound.getLong("turbinePos"));
//        outletPos = BlockPos.of(compound.getLong("outletPos"));
//        super.load(state, compound);
//    }
}
