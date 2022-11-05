package com.cassiokf.industrialrenewal.blockentity.transport;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEnergyCableLV extends BlockEntityEnergyCable {

    public BlockEntityEnergyCableLV(BlockPos pos, BlockState state){
        super(ModBlockEntity.ENERGYCABLE_LV_TILE.get(), pos, state);
    }
    @Override
    public int getMaxEnergyToTransport() {
//        return Config.LV_CABLE_TRANSFER_RATE.get();
        return 256;
    }

    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityEnergyCableLV;
    }
}
