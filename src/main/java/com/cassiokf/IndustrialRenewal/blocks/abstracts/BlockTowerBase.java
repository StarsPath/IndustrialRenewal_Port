package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockTowerBase <TE extends TileEntity3x3x3MachineBase> extends Block3x3x3Base<TE> {

    public static final BooleanProperty BASE = BooleanProperty.create("base");
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public BlockTowerBase(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BASE, false).setValue(TOP, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BASE, TOP);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if(!world.isClientSide)
        {
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            Direction facing = state.getValue(FACING);
            if (isValidPosition(world, pos, facing)) {
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true).setValue(BASE, true).setValue(TOP, true));

                for (int y = 0; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                            if (!(x == 0 && y == 1 && z == 0)) {
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false).setValue(BASE, true).setValue(TOP, true));
                                TileEntity3x3x3MachineBase te = (TileEntity3x3x3MachineBase) world.getBlockEntity(currentPos);
                            }
                        }
                    }
                }
            }
        }
    }
}
