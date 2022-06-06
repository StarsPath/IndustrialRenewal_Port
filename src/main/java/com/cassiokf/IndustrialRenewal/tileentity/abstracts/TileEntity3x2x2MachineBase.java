package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x2x2Base;
import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x2Base;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntity3x2x2MachineBase <TE extends TileEntity3x2x2MachineBase> extends TileEntity3x3x3MachineBase<TE>{
    public TileEntity3x2x2MachineBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public TE getMaster() {
        //return super.getMaster();
        if (masterTE == null || masterTE.isRemoved())
        {
            List<BlockPos> list = Utils.getBlocksIn3x2x2Centered(worldPosition, level.getBlockState(worldPosition).getValue(Block3x2x2Base.FACING));
            for (BlockPos currentPos : list)
            {
                TileEntity te = level.getBlockEntity(currentPos);
                if (te instanceof TileEntity3x3x2MachineBase
                        && ((TileEntity3x3x2MachineBase) te).isMaster()
                        && instanceOf(te))
                {
                    masterTE = (TE) te;
                    return masterTE;
                }
            }
            return null;
        }
        return masterTE;
    }

    @Override
    public Direction getMasterFacing()
    {
        if (faceChecked) return Direction.from3DDataValue(faceIndex);

        Direction facing = level.getBlockState(getMaster().worldPosition).getValue(Block3x2x2Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x2x2Centered(centerPosition, getMasterFacing());
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntity3x2x2MachineBase;
    }

    @Override
    public void breakMultiBlocks() {
        if (!this.isMaster())
        {
            if (getMaster() != null)
            {
                getMaster().breakMultiBlocks();
            }
            return;
        }
        if (!breaking)
        {
            breaking = true;
            onMasterBreak();
            List<BlockPos> list = getListOfBlockPositions(worldPosition);
            for (BlockPos currentPos : list)
            {
                Block block = level.getBlockState(currentPos).getBlock();
                if (block instanceof Block3x2x2Base) level.removeBlock(currentPos, false);
            }
        }
    }
}
