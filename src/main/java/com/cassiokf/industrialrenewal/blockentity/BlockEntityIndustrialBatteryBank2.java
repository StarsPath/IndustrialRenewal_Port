package com.cassiokf.industrialrenewal.blockentity;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityTowerBase;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntity3x3x3MachineBase;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityMachineBase;
import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.CustomEnergyStorage;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class BlockEntityIndustrialBatteryBank2 extends MultiBlockEntity3x3x3MachineBase {

    public BlockEntityIndustrialBatteryBank2(BlockPos pos, BlockState state){
        super(ModBlockEntity.TEST_TILE.get(), pos, state);
    }

    public void tick(){
        if(!level.isClientSide){
//            Utils.debug("TE Ticking");
        }
    }

    @Override
    public void onMasterBreak() {

    }

    @Override
    public InteractionResult onUse(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side, BlockPos worldPosition) {
        return null;
    }

    @Override
    public void dropAllItems() {

    }
}
