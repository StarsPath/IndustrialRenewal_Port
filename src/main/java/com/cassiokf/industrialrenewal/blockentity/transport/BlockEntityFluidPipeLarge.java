package com.cassiokf.industrialrenewal.blockentity.transport;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityFluidPipeBase;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityFluidPipeLarge extends BlockEntityFluidPipeBase<BlockEntityFluidPipeLarge> {

    public BlockEntityFluidPipeLarge(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUIDPIPE_LARGE_TILE.get(), pos, state, 20000);
    }

    public void tick(){
        super.tick();
    }

//    public TileEntityFluidPipe(TileEntityType<?> tileEntityTypeIn) {
//        super(tileEntityTypeIn);
//    }

}
