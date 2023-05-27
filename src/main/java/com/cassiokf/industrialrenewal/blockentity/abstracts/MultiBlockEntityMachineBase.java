package com.cassiokf.industrialrenewal.blockentity.abstracts;

import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class MultiBlockEntityMachineBase extends BlockEntitySyncable{
    public MultiBlockEntityMachineBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract void onMasterBreak();

    public abstract void breakMultiBlocks();

    public abstract InteractionResult onUse(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult);

    public abstract <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side, BlockPos worldPosition);

    public abstract void dropAllItems();
}
