package com.cassiokf.IndustrialRenewal.tileentity.tubes;

import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TETubeBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public abstract class TileEntityMultiBlocksTube<TE extends TileEntityMultiBlocksTube> extends TETubeBase implements ITickableTileEntity
{
    public int outPut;
    public int oldOutPut = -1;
    int outPutCount;
    int oldOutPutCount = -1;
    private TE master;
    private boolean isMaster;
    public boolean firstTick;
    private Map<BlockPos, Direction> posSet = new ConcurrentHashMap<>();

    public TileEntityMultiBlocksTube(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick()
    {
        if (!firstTick && this.hasLevel())
        {
            firstTick = true;
            initializeMultiblockIfNecessary(true);
            onFirstLoad();
        }
        if (this.hasLevel() && !isRemoved()) doTick();
    }

    public void onFirstLoad()
    {
    }

    public void doTick()
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
            List<TileEntityMultiBlocksTube> connectedCables = new CopyOnWriteArrayList<>();
            Stack<TileEntityMultiBlocksTube> traversingCables = new Stack<>();
            TE master = (TE) this;
            traversingCables.add(this);
            while (!traversingCables.isEmpty())
            {
                TileEntityMultiBlocksTube storage = traversingCables.pop();
                if (storage.isMaster())
                {
                    master = (TE) storage;
                }
                connectedCables.add(storage);
                for (Direction d : getFacesToCheck())
                {
                    TileEntity te = level.getBlockEntity(storage.getBlockPos().relative(d));
                    if (instanceOf(te) && !connectedCables.contains(te))
                    {
                        traversingCables.add((TE) te);
                    }
                }
            }
            master.getPosSet().clear();
            if (canBeMaster(master))
            {
                for (TileEntityMultiBlocksTube storage : connectedCables)
                {
                    if (!canBeMaster(storage)) continue;
                    storage.setMaster((TE) master);
                    storage.checkForOutPuts(storage.getBlockPos());
                    storage.setChanged();
                }
            } else
            {
                for (TileEntityMultiBlocksTube storage : connectedCables)
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

    private boolean canBeMaster(TileEntity te)
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

    public abstract boolean instanceOf(TileEntity te);

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
        requestModelRefresh();
    }

    public Map<BlockPos, Direction> getPosSet()
    {
        return posSet;
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();

        if (master != null)
        {
            master.setMaster(null);
            if (master != null) master.getMaster();
            else if (!isMaster) getMaster();
        }

        for (Direction d : Direction.values())
        {
            TileEntity te = level.getBlockEntity(getBlockPos().relative(d));
            if (instanceOf(te))
            {
                ((TileEntityMultiBlocksTube) te).master = null;

//                if (te instanceof TileEntityCableTray)
//                    ((TileEntityCableTray) te).refreshConnections();
//                else
                    ((TileEntityMultiBlocksTube) te).initializeMultiblockIfNecessary();
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
    public void load(BlockState state, CompoundNBT compound)
    {
        isMaster = compound.getBoolean("isMaster");
        outPut = compound.getInt("out");
        //industrialrenewal.LOGGER.info("load out: " + outPut + worldPosition);
        outPutCount = compound.getInt("count");

//        BlockPos[] blockPos = Arrays.stream(compound.getLongArray("posKey")).mapToObj(longPos -> BlockPos.of(longPos)).toArray(BlockPos[]::new);
//        Direction[] faces = Arrays.stream(compound.getIntArray("posValue")).mapToObj(intFace -> Direction.from3DDataValue(intFace)).toArray(Direction[]::new);
//        if(isMaster) {
//            for (int i = 0; i < blockPos.length; i++) {
//                industrialrenewal.LOGGER.info("LAODING... : " + blockPos[i] + faces[i]);
//                posSet.put(blockPos[i], faces[i]);
//            }
//            industrialrenewal.LOGGER.info("load posSet: " + posSet + worldPosition);
//        }

        super.load(state, compound);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound)
    {
//        industrialrenewal.LOGGER.info("save out: " + outPut + worldPosition);
//        industrialrenewal.LOGGER.info("save posSet: " + posSet);
        compound.putBoolean("isMaster", isMaster);
        compound.putInt("out", outPut);
        compound.putInt("count", outPutCount);

//        compound.putLongArray("posKey", posSet.keySet().stream().map(blockPos->blockPos.asLong()).collect(Collectors.toList()));
//        compound.putIntArray("posValue", posSet.values().stream().map(face->face.get3DDataValue()).collect(Collectors.toList()));
        return super.save(compound);
    }

//    @Override
//    public CompoundNBT getUpdateTag() {
//        return this.save(super.getUpdateTag());
//    }
//
//    @Override
//    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
//        this.load(state, tag);
//    }
//
//    @Nullable
//    @Override
//    public SUpdateTileEntityPacket getUpdatePacket() {
//        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
//    }
//
//    @Override
//    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
//        handleUpdateTag(level.getBlockState(pkt.getPos()), pkt.getTag());
//    }
}
