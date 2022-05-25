package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntitySyncable;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        //super.setPlacedBy(world, pos, state, livingEntity, itemStack);
        if(!world.isClientSide)
        {
//            boolean base = true;
//            boolean top = true;
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            if (isValidPosition(world, pos)) {
//                if(world.getBlockState(pos.below(3)).getBlock().is(ModBlocks.INDUSTRIAL_BATTERY_BANK.get())) {
//                    if(world.getBlockState(pos.below(2)).getValue(MASTER)) {
//                        //botAlign = true;
//                        base = false;
//                    }
//                }
//                    //state.setValue(BASE, false);
//                if(world.getBlockState(pos.above(3)).getBlock().is(ModBlocks.INDUSTRIAL_BATTERY_BANK.get())) {
//                    if(world.getBlockState(pos.above(4)).getValue(MASTER)) {
//                        //topAlign = true;
//                        top = false;
//                    }
//                }
                world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true).setValue(BASE, true).setValue(TOP, true));

                for (int y = 0; y < 3; y++) {
                    for (int z = -1; z < 2; z++) {
                        for (int x = -1; x < 2; x++) {
                            //Utils.debug("checking", x, y, z, (x != 0 && y != 1 && z != 0 ));
                            BlockPos currentPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                            if (!(x == 0 && y == 1 && z == 0)) {
                                //Utils.debug("placing", currentPos, x, y, z);
                                world.setBlockAndUpdate(currentPos, state.setValue(MASTER, false).setValue(BASE, true).setValue(TOP, true));
                                TileEntity3x3x3MachineBase te = (TileEntity3x3x3MachineBase) world.getBlockEntity(currentPos);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
    }
}
