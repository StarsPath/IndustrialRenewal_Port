package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTowerBase;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityIndustrialBatteryBank;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockIndustrialBatteryBank extends BlockTowerBase<TileEntityIndustrialBatteryBank> {
    public BlockIndustrialBatteryBank(Properties properties) {
        super(properties);
    }

//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context) {
//        BlockPos pos = context.getClickedPos();
//        World level = context.getLevel();
//        BlockState state = super.getStateForPlacement(context);
//
//        if(level.getBlockState(pos.below(2)).getBlock().is(ModBlocks.INDUSTRIAL_BATTERY_BANK.get()))
//            state.setValue(BASE, false);
//        else state.setValue(BASE, true);
//        if(level.getBlockState(pos.above(2)).getBlock().is(ModBlocks.INDUSTRIAL_BATTERY_BANK.get()))
//            state.setValue(TOP, false);
//        else state.setValue(TOP, true);
//
//        Utils.debug("Base, Top", state.getValue(BASE), state.getValue(TOP));
//        return state;
//    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityIndustrialBatteryBank();
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!world.isClientSide()){
            super.setPlacedBy(world, pos, state, livingEntity, itemStack);
            List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(pos);
            for(BlockPos blockPos : blocks){
                TileEntity te = world.getBlockEntity(blockPos);
                if(te instanceof TileEntityIndustrialBatteryBank && ((TileEntityTowerBase)te).isMaster()){
                    ((TileEntityIndustrialBatteryBank) te).setSelfBooleanProperty();
                    ((TileEntityTowerBase) te).setOtherBooleanProperty(TOP, false, false);
                    ((TileEntityTowerBase) te).setOtherBooleanProperty(BASE, false, true);
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
                if(te instanceof TileEntityIndustrialBatteryBank && ((TileEntityTowerBase)te).isMaster()){
                    ((TileEntityTowerBase) te).setOtherBooleanProperty(TOP, true, false);
                    ((TileEntityTowerBase) te).setOtherBooleanProperty(BASE, true, true);
                }
            }
        }
        super.destroy(world, pos, state);
    }
}
