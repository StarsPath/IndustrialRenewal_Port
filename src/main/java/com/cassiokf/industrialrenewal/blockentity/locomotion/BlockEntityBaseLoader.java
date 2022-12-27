package com.cassiokf.industrialrenewal.blockentity.locomotion;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntitySyncable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class BlockEntityBaseLoader extends BlockEntitySyncable {

    public Direction blockFacing;
    public waitEnum waitE = waitEnum.NO_ACTIVITY;
    public boolean unload;
    public boolean loading;
    public String cartName = "";
    public int cartActivity;

    public BlockEntityBaseLoader(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public waitEnum getWaitEnum()
    {
        if (waitE == null)
        {
            waitE = waitEnum.WAIT_FULL;
        }
        return waitE;
    }

    public void setWaitEnum(int value)
    {
        waitE = waitEnum.valueOf(value);
    }

    public void setNextWaitEnum()
    {
        int old = getWaitEnum().ordinal();
        waitE = waitEnum.valueOf(old + 1);
        sync();
    }

    public void changeUnload()
    {
        unload = !unload;
        sync();
    }

    public abstract Direction getBlockFacing();

    public boolean isUnload(){
        return unload;
    }

//    public abstract boolean onMinecartPass(AbstractMinecartEntity entityMinecart);


    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        compoundTag.putInt("EnumConfig", this.waitE.intValue);
        compoundTag.putBoolean("unload", unload);
        compoundTag.putBoolean("loading", loading);
        compoundTag.putString("cartname", cartName);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        waitE = waitEnum.valueOf(compoundTag.getInt("EnumConfig"));
        unload = compoundTag.getBoolean("unload");
        loading = compoundTag.getBoolean("loading");
        cartName = compoundTag.getString("cartname");
        super.load(compoundTag);
    }

    public enum waitEnum
    {
        WAIT_FULL(0),
        WAIT_EMPTY(1),
        NO_ACTIVITY(2),
        NEVER(3);

        public int intValue;

        waitEnum(int value)
        {
            intValue = value;
        }

        public static waitEnum valueOf(int waitNo)
        {
            if (waitNo > waitEnum.values().length - 1)
            {
                waitNo = 0;
            }
            for (waitEnum l : waitEnum.values())
            {
                if (l.intValue == waitNo) return l;
            }
            throw new IllegalArgumentException("waitEnum not found");
        }

        public static waitEnum cycle(waitEnum e){
            return valueOf((e.intValue+1) % waitEnum.values().length);
        }
    }
}
