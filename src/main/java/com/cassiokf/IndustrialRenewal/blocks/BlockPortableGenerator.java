package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockSaveContent;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityPortableGenerator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockPortableGenerator extends BlockSaveContent {

    public BlockPortableGenerator(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public BlockPortableGenerator() {
        super(AbstractBlock.Properties.of(Material.METAL).strength(2f)
                .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion());
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context)
                .setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityPortableGenerator(ModTileEntities.PORTABLE_GENERATOR_TILE.get());
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos1, Block block, BlockPos pos2, boolean flag) {
        TileEntity te = world.getBlockEntity(pos1);
        //industrialrenewal.LOGGER.info(active);
        if(te instanceof TileEntityPortableGenerator){
            ((TileEntityPortableGenerator) te).setCanGenerate(getNeighborSignal(world, pos1));
            industrialrenewal.LOGGER.info(getNeighborSignal(world, pos1));
        }
        super.neighborChanged(state, world, pos1, block, pos2, flag);
    }

    private boolean getNeighborSignal(World world, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            if (world.hasSignal(pos.relative(direction), direction)) {
                return true;
            }
        }
        return false;
    }
}
