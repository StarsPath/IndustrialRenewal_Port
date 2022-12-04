package com.cassiokf.industrialrenewal.blockentity.abstracts;

import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BlockEntityMultiBlocksTube<TE extends BlockEntityMultiBlocksTube> extends BETubeBase
{
    public int outPut;
    public int oldOutPut = -1;
    int outPutCount;
    int oldOutPutCount = -1;
    private TE master;
    private boolean isMaster;
    public boolean firstTick;
    private Map<BlockPos, Direction> posSet = new ConcurrentHashMap<>();

    public BlockEntityMultiBlocksTube(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state)
    {
        super(tileEntityTypeIn, pos, state);
    }

    public void tick()
    {
        if (!firstTick && this.hasLevel())
        {
            firstTick = true;
            initializeMultiblockIfNecessary(true);
            onFirstLoad();
        }
//        if (this.hasLevel() && !isRemoved()) doTick();
    }

    public void onFirstLoad()
    {
    }

    public int getOutPut()
    {
        return outPut;
    }

    public int getOutPutCount()
    {
        return outPutCount;
    }

    private void initializeMultiblockIfNecessary(){
        initializeMultiblockIfNecessary(false);
    }

    private void initializeMultiblockIfNecessary(boolean forceInit)
    {
        if ((isMasterInvalid() && !this.isRemoved()) || forceInit)
        {
            //industrialrenewal.LOGGER.info("TRYING TO INIT MULTIBLOCK, Forced: "+forceInit);
            if (isTray()) return;
            List<BlockEntityMultiBlocksTube> connectedCables = new CopyOnWriteArrayList<>();
            Stack<BlockEntityMultiBlocksTube> traversingCables = new Stack<>();
            TE master = (TE) this;
            traversingCables.add(this);
            while (!traversingCables.isEmpty())
            {
                BlockEntityMultiBlocksTube storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = (TE) storage;
                }
                connectedCables.add(storage);
                for (Direction d : getFacesToCheck())
                {
                    BlockEntity te = level.getBlockEntity(storage.getBlockPos().relative(d));
                    if (instanceOf(te) && !connectedCables.contains(te))
                    {
                        traversingCables.add((TE) te);
                    }
                }
            }
            master.getPosSet().clear();
            if (canBeMaster(master))
            {
                for (BlockEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.setMaster((TE) master);
                    storage.checkForOutPuts(storage.getBlockPos());
                    storage.setChanged();
                }
            } else
            {
                for (BlockEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.getPosSet().clear();
                    storage.setMaster(null);
                }
            }
            //Utils.debug("", connectedCables);
            setChanged();
        }
    }

    public void requestModelRefresh()
    {
        this.requestModelDataUpdate();
    }

    public boolean isTray()
    {
        return false;
    }

    private boolean canBeMaster(BlockEntity te)
    {
        return te != null;// && !(te instanceof TileEntityCableTray);
    }

    public boolean isMasterInvalid()
    {
        return master == null || master.isRemoved();
    }

    public Direction[] getFacesToCheck()
    {
        return Direction.values();
    }

    public abstract boolean instanceOf(BlockEntity te);

    public abstract void checkForOutPuts(BlockPos bPos);

    public boolean isMaster()
    {
        return isMaster;
    }

    public TE getMaster()
    {
        initializeMultiblockIfNecessary();
        return master;
    }

    public void setMaster(TE master)
    {
        this.master = master;
        isMaster = master == this;
        if (!isMaster) posSet.clear();
//        requestModelRefresh();
    }

    public Map<BlockPos, Direction> getPosSet()
    {
        return posSet;
    }

    @Override
    public void setRemoved()
    {
//        Utils.debug("SET REMOVED");
        super.setRemoved();
        if (master != null)
        {
            master.setMaster(null);
        }
    }

    public void breakBlock(){
//        Utils.debug("BREAK BLOCK");
        super.setRemoved();
        if (master != null) master.getMaster();
        else if (!isMaster) getMaster();
        for (Direction d : Direction.values())
        {
            BlockEntity te = level.getBlockEntity(getBlockPos().relative(d));
//            Utils.debug("", te instanceof BlockEntityMultiBlocksTube);
            if (te instanceof BlockEntityMultiBlocksTube)
            {
                ((BlockEntityMultiBlocksTube) te).master = null;
                ((BlockEntityMultiBlocksTube) te).initializeMultiblockIfNecessary();

            }
        }
    }

    public void addMachine(BlockPos pos, Direction face)
    {
        posSet.put(pos, face);
    }

    public void removeMachine(BlockPos ownPos, BlockPos machinePos)
    {
        posSet.remove(machinePos);
    }

    @Override
    public void load(CompoundTag compound)
    {
        isMaster = compound.getBoolean("isMaster");
        outPut = compound.getInt("out");
        outPutCount = compound.getInt("count");
        super.load(compound);
    }


    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.putBoolean("isMaster", isMaster);
        compound.putInt("out", outPut);
        compound.putInt("count", outPutCount);
        super.saveAdditional(compound);
    }
}
