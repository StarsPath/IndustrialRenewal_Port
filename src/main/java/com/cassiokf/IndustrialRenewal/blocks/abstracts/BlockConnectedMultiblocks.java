package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityMultiBlocksTube;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockConnectedMultiblocks<TE extends TileEntityMultiBlocksTube> extends BlockAbstractHorizontalFacing
{

    public BlockConnectedMultiblocks(Block.Properties properties)
    {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) worldIn.getBlockEntity(pos);
        if (te != null) te.checkForOutPuts(pos);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack) {
        TileEntityMultiBlocksTube te = (TileEntityMultiBlocksTube) world.getBlockEntity(pos);
        if (te != null)
            te.requestModelRefresh();

        super.setPlacedBy(world, pos, state, player, itemStack);
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public abstract TE createTileEntity(BlockState state, IBlockReader world);


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        //super.createBlockStateDefinition(builder);
    }
}
