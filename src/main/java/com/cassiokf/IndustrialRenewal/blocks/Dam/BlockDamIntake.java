package com.cassiokf.IndustrialRenewal.blocks.Dam;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityDamIntake;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDamIntake extends BlockAbstractHorizontalFacing {
    public BlockDamIntake() {
        super(AbstractBlock.Properties.of(Material.STONE));
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if(!worldIn.isClientSide){
            if(player.getMainHandItem().isEmpty()){
                TileEntity te = worldIn.getBlockEntity(pos);
                if(te != null && te instanceof TileEntityDamIntake){
                    float efficiency = ((TileEntityDamIntake) te).getWaterEfficiency();
                    Utils.sendChatMessage(player, String.format("Efficiency: %.2f%% %.2fB/t", efficiency*100, (float)((TileEntityDamIntake) te).currentProduction/1000f));
                }
            }
        }
        return ActionResultType.PASS;
//        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityDamIntake();
    }
}
