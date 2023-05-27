package com.cassiokf.industrialrenewal.blocks;

import com.cassiokf.industrialrenewal.blockentity.BlockEntitySteamBoiler;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityDummy;
import com.cassiokf.industrialrenewal.blocks.abstracts.MultiBlock3x3x3Base;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class BlockSteamBoiler extends MultiBlock3x3x3Base implements EntityBlock {

    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);

    public BlockSteamBoiler(BlockBehaviour.Properties properties) {
        super(properties);
    }

//    @Override
//    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
//        BlockEntitySteamBoiler te = (BlockEntitySteamBoiler) worldIn.getBlockEntity(pos);
//        if (te != null && te.isMaster() && te.getIntType() == 1 && te.getFuelFill() > 0 && rand.nextInt(24) == 0)
//        {
//            worldIn.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.3F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
//        }
//        //super.animateTick(stateIn, worldIn, pos, rand);
//    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(state.getValue(MASTER)) {
            return ModBlockEntity.STEAM_BOILER_TILE.get().create(pos, state);
        }
        return new MultiBlockEntityDummy(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState state, BlockEntityType<T> p_153214_) {
        if(state.getValue(MASTER)) {
            return ($0, $1, $2, blockEntity) -> ((BlockEntitySteamBoiler) blockEntity).tick();
        }
        return null;
    }
}
