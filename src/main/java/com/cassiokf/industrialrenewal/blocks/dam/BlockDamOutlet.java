package com.cassiokf.industrialrenewal.blocks.dam;


import com.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamIntake;
import com.cassiokf.industrialrenewal.blockentity.dam.BlockEntityDamOutlet;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class BlockDamOutlet extends BlockAbstractHorizontalFacing implements EntityBlock {
    public BlockDamOutlet()  {
        super(BlockBehaviour.Properties.of(Material.STONE));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.DAM_OUTLET.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityDamOutlet)blockEntity).tick();
    }
}
