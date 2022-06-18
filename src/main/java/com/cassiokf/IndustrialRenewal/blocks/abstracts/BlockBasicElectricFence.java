package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockBasicElectricFence extends BlockAbstractSixWayConnections{

    public BlockBasicElectricFence(Block.Properties property, int nodeWidth)
    {
        super(property, nodeWidth, 16);
    }

    public BlockBasicElectricFence(Block.Properties property, int nodeWidth, int nodeHeight)
    {
        super(property, nodeWidth, nodeHeight);
    }

    @Override
    public boolean canConnectTo(IWorld worldIn, BlockPos currentPos, Direction neighborDirection) {
        return false;
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
//        Utils.debug("Entity Inside", entityIn.isColliding(pos, state));
//        if(entityIn.isColliding(pos, state)){
//            Utils.debug("Entity Colliding");
            //TODO: Add to config
            int mode = 1;
            float damage = 1;

            if (mode == 0 && (entityIn instanceof MonsterEntity || entityIn instanceof PlayerEntity))
            {
                float damageR = (entityIn instanceof PlayerEntity) ? 0f : damage;
                DoDamage((World) worldIn, pos, entityIn, damageR);
            } else if (mode == 1 && (entityIn instanceof MonsterEntity || entityIn instanceof PlayerEntity))
            {
                DoDamage((World) worldIn, pos, entityIn, damage);
            } else if (mode == 2)
            {
                DoDamage((World) worldIn, pos, entityIn, 0f);
            } else if (mode == 3)
            {
                DoDamage((World) worldIn, pos, entityIn, damage);
            }
//        }
        super.entityInside(state, worldIn, pos, entityIn);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return super.getCollisionShape(state, worldIn, pos, context);
    }

    private void DoDamage(World world, BlockPos pos, Entity entityIn, float amount)
    {
        entityIn.hurt(DamageSource.LIGHTNING_BOLT, amount);
        //TODO: add to config
//        float knockback = 0.3F;
//        if (knockback > 0)
//            ((LivingEntity) entityIn).knockback(knockback, pos.getX() - entityIn.getX(), pos.getZ() - entityIn.getZ());
//        Utils.debug("knockback", pos.getX() - entityIn.getX(), pos.getZ() - entityIn.getZ());
        Random r = new Random();
        float pitch = r.nextFloat() * (1.1f - 0.9f) + 0.9f;
        //world.playSound(null, pos, SoundsRegistration.EFFECT_SHOCK.get(), SoundCategory.BLOCKS, 0.6F, pitch);
    }
}
