package com.cassiokf.industrialrenewal.blocks.abstracts;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntity3x3x3MachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public abstract class MultiBlockTower<TE extends MultiBlockEntity3x3x3MachineBase> extends MultiBlock3x3x3Base{

    public static final BooleanProperty BASE = BooleanProperty.create("base");
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public MultiBlockTower(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BASE, false).setValue(TOP, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BASE, TOP);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
        if(state.getValue(MASTER)){
            BlockState newState = state;
            if(hasBase(world, pos, state)) {
                newState.setValue(BASE, true);
            }
            else {
                updateBelowChunk();
            }
            if(hasTop(world, pos, state)) {
                newState.setValue(TOP, true);
            }
            else {
                updateAboveChunk();
            }
            world.setBlockAndUpdate(pos, newState);


        }
//        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }

    public abstract boolean hasBase(Level world, BlockPos pos, BlockState state);

    public abstract boolean hasTop(Level world, BlockPos pos, BlockState state);

    public void updateAboveChunk(){

    }

    public void updateBelowChunk(){

    }
}
