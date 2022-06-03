package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTowerBase;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityFluidTank;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityIndustrialBatteryBank;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFluidTank extends BlockTowerBase<TileEntityFluidTank> {
    public BlockFluidTank(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!world.isClientSide()){
            super.setPlacedBy(world, pos, state, livingEntity, itemStack);
            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);
            for(BlockPos blockPos : blocks){
                TileEntity te = world.getBlockEntity(blockPos);
                if(te instanceof TileEntityFluidTank && ((TileEntityTowerBase)te).isMaster()){
                    TileEntityFluidTank bankTE = (TileEntityFluidTank) te;

                    bankTE.setSelfBooleanProperty();
                    bankTE.setOtherBooleanProperty(TOP, false, false);
                    bankTE.setOtherBooleanProperty(BASE, false, true);
                    bankTE.getBase().loadTower();
                }
            }
        }
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        if(!world.isClientSide()){
            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);
            for(BlockPos blockPos : blocks){
                TileEntity te = world.getBlockEntity(blockPos);
                if(te instanceof TileEntityFluidTank && ((TileEntityTowerBase)te).isMaster()){
                    TileEntityFluidTank bankTE = (TileEntityFluidTank) te;
                    bankTE.setOtherBooleanProperty(TOP, true, false);
                    bankTE.setOtherBooleanProperty(BASE, true, true);
                    if(!bankTE.isBase()){
                        bankTE.getBase().removeTower(bankTE);
                    }
                    if(bankTE.getAbove() != null){
                        bankTE.getAbove().loadTower();
                    }
                }
            }
        }
        super.destroy(world, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityFluidTank();
    }
}
