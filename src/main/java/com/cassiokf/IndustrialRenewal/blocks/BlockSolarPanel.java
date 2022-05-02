package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTileEntity;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntitySolarPanelBase;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BlockSolarPanel extends Block{

    protected static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);;

    private static final Optional<VoxelShape> SHAPE = Stream.of(
            Block.box(0.5, 0, 0.5, 15.5, 1, 15.5),
            Block.box(0, 0, 0, 16, 1.2, 0.5),
            Block.box(0, 0, 15.5, 16, 1.2, 16),
            Block.box(0, 0, 0.5, 0.5, 1.2, 15.5),
            Block.box(15.5, 0, 0.5, 16, 1.2, 15.5)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR));

    public BlockSolarPanel() {
        super(Properties.of(Material.GLASS).strength(2f).noOcclusion());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        super.appendHoverText(stack, world, list, tooltipFlag);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntitySolarPanelBase();
        //return ModTileEntities.SOLAR_PANEL_BASE.get().create();
    }

    @Deprecated
    public boolean isOpaqueCube(final BlockState state) {
        return false;
    }

    @Deprecated
    public boolean isFullCube(final BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        //BLOCK_AABB
        //return super.getShape(state, world, pos, context);
        return SHAPE.orElse(VoxelShapes.block());
    }
}
