package com.cassiokf.IndustrialRenewal.fluids;

import com.cassiokf.IndustrialRenewal.init.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSteam extends FlowingFluidBlock {

    public BlockSteam()
    {
        super(() -> ModFluids.STEAM.get(), Block.Properties.of(Material.WATER).strength(100F).noDrops());
    }

    @Override
    public void entityInside(BlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity entityIn) {
        //super.entityInside(p_196262_1_, p_196262_2_, p_196262_3_, p_196262_4_);
        {
            entityIn.hurt(DamageSource.HOT_FLOOR, 0.5F);
        }
    }
}
