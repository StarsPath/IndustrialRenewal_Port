package com.cassiokf.IndustrialRenewal.entity;

import com.cassiokf.IndustrialRenewal.init.ModEntity;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityPassengerCar extends RotatableBase{
    public EntityPassengerCar(EntityType<EntityPassengerCar> p_i48538_1_, World p_i48538_2_) {
        super(p_i48538_1_, p_i48538_2_);
    }

    public EntityPassengerCar(World world, double x, double y, double z){
        super(ModEntity.PASSENGER_CAR.get(), world, x, y, z);
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        //return super.interact(player, hand);
        if (player.isCrouching())
        {
            return ActionResultType.FAIL;
        }
        else if (!this.canBeRidden() || this.getPassengers().size()>0)
        {
            return ActionResultType.FAIL;
        }
        else
        {
            if (!this.level.isClientSide)
            {
                player.startRiding(this);
            }

            return ActionResultType.SUCCESS;
        }
    }

    public void destroy(DamageSource p_94095_1_) {
        this.remove();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.passengerCar);
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
        }
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
