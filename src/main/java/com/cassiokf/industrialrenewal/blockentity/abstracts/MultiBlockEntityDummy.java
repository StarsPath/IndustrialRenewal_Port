package com.cassiokf.industrialrenewal.blockentity.abstracts;

import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiBlockEntityDummy extends BlockEntitySyncable {
    public MultiBlockEntityDummy(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BlockPos masterPos;

    public MultiBlockEntityDummy(BlockPos pos, BlockState state) {
        super(ModBlockEntity.DUMMY.get(), pos, state);
    }

    public InteractionResult onUse(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult){
        if(!level.isClientSide){
            BlockEntity be = level.getBlockEntity(masterPos);
            if(be instanceof  MultiBlockEntityMachineBase master){
                master.onUse(state, worldIn, masterPos, player, handIn, hitResult);
            }
//            BlockState blockState = level.getBlockState(masterPos);
//            Block block = blockState.getBlock();
//            return block.use(blockState, worldIn, masterPos, player, handIn, hitResult);
        }
        return InteractionResult.PASS;
    }

    public void onDestroy(){
        if(!level.isClientSide && masterPos != null){
            BlockEntity be = level.getBlockEntity(masterPos);
            if(be instanceof MultiBlockEntityMachineBase master){
                master.breakMultiBlocks();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        if(masterPos != null)
            compoundTag.putLong("masterPos" ,masterPos.asLong());
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        if(compoundTag.getLong("masterPos") != 0L)
            masterPos = BlockPos.of(compoundTag.getLong("masterPos"));
        super.load(compoundTag);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return super.getCapability(cap);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
//        Utils.debug("dummy getCapability");
        if(masterPos != null && level != null){
            BlockEntity blockEntity = level.getBlockEntity(masterPos);
            if(blockEntity instanceof MultiBlockEntityMachineBase multiBlockEntityMachineBase){
//                Utils.debug("getting master capability");
                return multiBlockEntityMachineBase.getCapability(cap, side, worldPosition);
            }
        }
        return super.getCapability(cap, side);
    }
}
