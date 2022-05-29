package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTowerBase;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityIndustrialBatteryBank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityTowerBase<TE extends TileEntityTowerBase> extends TileEntity3x3x3MachineBase<TE>{

    public TileEntityTowerBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntityTowerBase;
    }

    public void setSelfBooleanProperty(){
        List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(getMaster().worldPosition);
        if(isAligned(true)){
            for(BlockPos blockPos : blocks){
                TileEntity te = level.getBlockEntity(blockPos);
                BlockState blockState = level.getBlockState(blockPos);
                if(te instanceof TileEntityTowerBase){
                    level.setBlockAndUpdate(blockPos, blockState.setValue(BlockTowerBase.TOP, false));
                    //Utils.debug("Updated BlockState", property, level.getBlockState(blockPos).getValue(property));
                }
            }
        }
        if(isAligned(false)){
            for(BlockPos blockPos : blocks){
                TileEntity te = level.getBlockEntity(blockPos);
                BlockState blockState = level.getBlockState(blockPos);
                if(te instanceof TileEntityTowerBase){
                    level.setBlockAndUpdate(blockPos, blockState.setValue(BlockTowerBase.BASE, false));
                    //Utils.debug("Updated BlockState", property, level.getBlockState(blockPos).getValue(property));
                }
            }
        }
    }

    public void setSelfBooleanProperty(BooleanProperty property, boolean bool){
        List<BlockPos> blocks = Utils.getBlocksIn3x3x3Centered(getMaster().worldPosition);
        for(BlockPos blockPos : blocks){
            TileEntity te = level.getBlockEntity(blockPos);
            BlockState blockState = level.getBlockState(blockPos);
            if(te instanceof TileEntityTowerBase){
                level.setBlockAndUpdate(blockPos, blockState.setValue(property, bool));
                //Utils.debug("Updated BlockState", property, level.getBlockState(blockPos).getValue(property));
            }
        }
    }

    public boolean topAligned(){
        TE master = getMaster();
        BlockPos relativePos = master.getBlockPos().above(3);
        TileEntity relativeMaster = level.getBlockEntity(relativePos);

        if(master.instanceOf(relativeMaster) && ((TileEntityTowerBase<?>) relativeMaster).isMaster() &&
                ((TileEntityTowerBase<?>) relativeMaster).getMasterFacing() == master.getMasterFacing()) {
            return true;
        }
        return false;
    }

    public boolean botAligned(){
        TE master = getMaster();
        BlockPos relativePos = master.getBlockPos().below(3);
        TileEntity relativeMaster = level.getBlockEntity(relativePos);

        if(master.instanceOf(relativeMaster) && ((TileEntityTowerBase<?>) relativeMaster).isMaster() &&
                ((TileEntityTowerBase<?>) relativeMaster).getMasterFacing() == master.getMasterFacing()) {
            return true;
        }
        return false;
    }

    public boolean isAligned(boolean above){
        return above? topAligned() : botAligned();
    }

    public void setOtherBooleanProperty(BooleanProperty property, boolean bool, boolean above){
        if(isAligned(above)){
            TE master = getMaster();
            BlockPos relativePos = above? master.getBlockPos().above(3) : master.getBlockPos().below(3);
            TileEntity relativeMaster = level.getBlockEntity(relativePos);
            ((TileEntityTowerBase<?>) relativeMaster).setSelfBooleanProperty(property, bool);
        }
    }

    public boolean isBase(){
        return level.getBlockState(getMaster().worldPosition).getValue(BlockTowerBase.BASE);
    }
}
