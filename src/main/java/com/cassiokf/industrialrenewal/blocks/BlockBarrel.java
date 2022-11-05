package com.cassiokf.industrialrenewal.blocks;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityBarrel;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockSaveContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBarrel extends BlockSaveContent implements EntityBlock {

    public static final BooleanProperty FRAME = BooleanProperty.create("frame");

//    public BlockBarrel(Properties props) {
//        super(props);
//        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FRAME, false));
//    }
    public BlockBarrel() {
        super(Properties.of(Material.METAL).noCollission().strength(2f));
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(FRAME, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(FRAME, context.getPlayer().isCrouching());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRAME, FACING);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityBarrel(pos, state);
    }

//    @Nullable
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
//        return new TileEntityBarrel(ModTileEntities.BARREL_TILE.get());
//    }
}
