package com.cassiokf.industrialrenewal.blockentity.transport;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityEnergyCableHV extends BlockEntityEnergyCable {

    public BlockEntityEnergyCableHV(BlockPos pos, BlockState state){
        super(ModBlockEntity.ENERGYCABLE_HV_TILE.get(), pos, state);
    }

    @Override
    public int getMaxEnergyToTransport() {
        return Config.HV_CABLE_TRANSFER_RATE.get();
    }

    @Override
    public boolean instanceOf(BlockEntity te) {
        return te instanceof BlockEntityEnergyCableHV;
    }
}
