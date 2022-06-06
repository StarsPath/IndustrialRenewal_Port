package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x2Base;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class TileEntity3x3x2MachineBase<TE extends TileEntity3x3x2MachineBase> extends TileEntity3x3x3MachineBase<TE> {
    public TileEntity3x3x2MachineBase(TileEntityType tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    public BlockPos masterPos = worldPosition;

    @Override
    public TE getMaster() {
        //return super.getMaster();
        TileEntity te = level.getBlockEntity(masterPos);
        Utils.debug("master pos", worldPosition, masterPos, te);
        if(te instanceof TileEntity3x3x2MachineBase
                && ((TileEntity3x3x2MachineBase) te).isMaster()
                && instanceOf(te))
        {
            masterTE = (TE) te;
            return masterTE;
        }
        return null;
    }

    @Override
    public void setRemoved() {
        TileEntity3x3x2MachineBase te = (TileEntity3x3x2MachineBase) level.getBlockEntity(masterPos);
        if(te != null)
            te.breakMultiBlocks();
        super.setRemoved();
    }

    @Override
    public Direction getMasterFacing()
    {
        if (faceChecked) return Direction.from3DDataValue(faceIndex);

        Direction facing = level.getBlockState(getMaster().worldPosition).getValue(Block3x3x2Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x3x2Centered(centerPosition, getMasterFacing());
    }

    @Override
    public boolean instanceOf(TileEntity tileEntity) {
        return tileEntity instanceof TileEntity3x3x2MachineBase;
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
                if (block instanceof Block3x3x2Base) level.removeBlock(currentPos, false);
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putLong("masterPos", masterPos.asLong());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        masterPos = BlockPos.of(compound.getLong("masterPos"));
        super.load(state, compound);
    }
}
