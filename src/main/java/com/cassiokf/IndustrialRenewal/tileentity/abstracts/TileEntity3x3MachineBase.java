package com.cassiokf.IndustrialRenewal.tileentity.abstracts;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;

public abstract class TileEntity3x3MachineBase <TE extends TileEntity3x3MachineBase> extends TileEntitySyncable implements ICapabilityProvider {

    private boolean master;
    private boolean breaking;
    private TE masterTE;
    private boolean masterChecked = false;
    private boolean faceChecked = false;
    private int faceIndex;

    public TileEntity3x3MachineBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void onLoad() {

    }

    public TE getMaster() {
        if (masterTE == null || masterTE.isRemoved())
        {
            List<BlockPos> list = Utils.getBlocksIn3x3x3Centered(worldPosition);
            for (BlockPos currentPos : list)
            {
                TileEntity te = level.getBlockEntity(currentPos);
                if (te instanceof TileEntity3x3MachineBase
                        && ((TileEntity3x3MachineBase) te).isMaster()
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

    public List<BlockPos> getListOfBlockPositions(BlockPos centerPosition)
    {
        return Utils.getBlocksIn3x3x3Centered(centerPosition);
    }

    public abstract boolean instanceOf(TileEntity tileEntity);

    public void breakMultiBlocks()
    {
        //Utils.debug("breaking block", isMaster());
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
                if (block instanceof Block3x3x3Base) level.removeBlock(currentPos, false);
            }
        }
    }

    public void onMasterBreak()
    {
    }

    public Direction getMasterFacing()
    {
        if (faceChecked) return Direction.from3DDataValue(faceIndex);

        Direction facing = level.getBlockState(getMaster().worldPosition).getValue(Block3x3x3Base.FACING);
        faceChecked = true;
        faceIndex = facing.get3DDataValue();
        return facing;
    }

    public boolean isMaster()
    {
        if (masterChecked) return this.master;

        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof Block3x3x3Base)) return false;
        master = state.getValue(Block3x3x3Base.MASTER);
        masterChecked = true;
        return master;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putBoolean("master", this.isMaster());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        this.master = compound.getBoolean("master");
        super.load(state, compound);
    }
}
