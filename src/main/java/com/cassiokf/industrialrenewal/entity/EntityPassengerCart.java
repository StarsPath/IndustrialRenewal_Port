package com.cassiokf.industrialrenewal.entity;

import com.cassiokf.industrialrenewal.init.ModEntity;
import com.cassiokf.industrialrenewal.init.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class EntityPassengerCart extends RotatableBase {
    public EntityPassengerCart(EntityType<?> p_38087_, Level p_38088_) {
        super(p_38087_, p_38088_);
    }

    public EntityPassengerCart(Level p_38091_, double p_38092_, double p_38093_, double p_38094_) {
        super(ModEntity.PASSENGER_CART.get(), p_38091_, p_38092_, p_38093_, p_38094_);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.PASSENGER_CART_MK1.get());
    }


    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        //return super.interact(player, hand);
        if (player.isCrouching())
        {
            return InteractionResult.FAIL;
        }
        else if (!this.canBeRidden() || this.getPassengers().size()>0)
        {
            return InteractionResult.FAIL;
        }
        else
        {
            if (!this.level.isClientSide)
            {
                player.startRiding(this);
            }

            return InteractionResult.SUCCESS;
        }
    }

    public void destroy(DamageSource p_94095_1_) {
        this.remove(RemovalReason.KILLED);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.PASSENGER_CART_MK1.get());
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
