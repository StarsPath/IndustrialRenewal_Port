package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTowerBase;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityIndustrialBatteryBank;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntityTowerBase;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockIndustrialBatteryBank extends BlockTowerBase<TileEntityIndustrialBatteryBank> {
    public BlockIndustrialBatteryBank(Properties properties) {
        super(properties);
    }

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
                    TileEntityIndustrialBatteryBank bankTE = (TileEntityIndustrialBatteryBank) te;

                    bankTE.setSelfBooleanProperty();
                    bankTE.setOtherBooleanProperty(TOP, false, false);
                    bankTE.setOtherBooleanProperty(BASE, false, true);
                    //bankTE.getBase().addToTower(bankTE, bankTE.getAbove()!=null? bankTE.getAbove().getMaster().tower : null);
                    bankTE.getBase().loadTower();
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
                    popResource((World) world, te.getBlockPos(), new ItemStack(ModItems.battery_lithium, ((TileEntityIndustrialBatteryBank)te).getBatteries()));
                    TileEntityIndustrialBatteryBank bankTE = (TileEntityIndustrialBatteryBank) te;
                    bankTE.setOtherBooleanProperty(TOP, true, false);
                    bankTE.setOtherBooleanProperty(BASE, true, true);
                    if(!bankTE.isBase()){
                        bankTE.getBase().removeTower(bankTE);
                    }
                    if(bankTE.getAbove() != null){
                        //bankTE.getAbove().tower = new ArrayList<>();
                        bankTE.getAbove().loadTower();
                    }
                }
            }
        }
        super.destroy(world, pos, state);
    }


    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult context) {
        if(!world.isClientSide){
            //Utils.debug("USE ON BATTERYBANK BLOCK");
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem().equals(ModItems.battery_lithium)) {
                TileEntity te = world.getBlockEntity(pos);
                //Utils.debug("PLACING",te instanceof TileEntityIndustrialBatteryBank);
                if (te instanceof TileEntityIndustrialBatteryBank) {
                    if (((TileEntityIndustrialBatteryBank) te).getMaster().placeBattery(player, stack))
                        return ActionResultType.PASS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, context);
    }
}
