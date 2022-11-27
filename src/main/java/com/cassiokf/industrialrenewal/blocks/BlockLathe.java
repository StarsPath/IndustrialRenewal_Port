package com.cassiokf.industrialrenewal.blocks;


import com.cassiokf.industrialrenewal.blockentity.BlockEntityLathe;
import com.cassiokf.industrialrenewal.blockentity.BlockEntitySolarPanel;
import com.cassiokf.industrialrenewal.blocks.abstracts.Block3x2x2Base;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlockLathe extends Block3x2x2Base<BlockEntityLathe> implements EntityBlock {
    public BlockLathe(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if(!worldIn.isClientSide)
        {
            BlockEntityLathe latheMaster = ((BlockEntityLathe) worldIn.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = latheMaster.getBlockPos();

            if(latheMaster != null) {
                NetworkHooks.openGui(((ServerPlayer)player), latheMaster, masterPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.LATHE_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityLathe)blockEntity).tick();
    }
}
