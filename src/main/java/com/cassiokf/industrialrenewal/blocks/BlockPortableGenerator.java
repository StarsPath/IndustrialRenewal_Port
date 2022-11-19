package com.cassiokf.industrialrenewal.blocks;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityBatteryBank;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityPortableGenerator;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockSaveContent;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class BlockPortableGenerator extends BlockSaveContent implements EntityBlock {

    public BlockPortableGenerator(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public BlockPortableGenerator() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(2f)
                .noOcclusion());
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos1, Block block, BlockPos pos2, boolean flag) {
        BlockEntity te = world.getBlockEntity(pos1);
        //industrialrenewal.LOGGER.info(active);
        if(te instanceof BlockEntityPortableGenerator){
            ((BlockEntityPortableGenerator) te).setCanGenerate(getNeighborSignal(world, pos1));
//            industrialrenewal.LOGGER.info(getNeighborSignal(world, pos1));
        }
        super.neighborChanged(state, world, pos1, block, pos2, flag);
    }

    private boolean getNeighborSignal(Level world, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            if (world.hasSignal(pos.relative(direction), direction)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.PORTABLE_GENERATOR_TILE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityPortableGenerator)blockEntity).tick();
    }
}
