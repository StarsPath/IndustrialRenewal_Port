package com.cassiokf.IndustrialRenewal.entity;

import com.cassiokf.IndustrialRenewal.init.ModEntity;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class EntityCargoContainer extends ContainerMinecartEntity {
    public EntityCargoContainer(EntityType<EntityCargoContainer> entityType, World world) {
        super(entityType, world);
    }

    public EntityCargoContainer(World world, double x, double y, double z){
        super(ModEntity.CARGO_CONTAINER.get(), x, y, z, world);
    }

    @Override
    public Type getMinecartType() {
        return Type.CHEST;
    }

    @Override
    protected Container createMenu(int p_213968_1_, PlayerInventory p_213968_2_) {
        return new ChestContainer(ContainerType.GENERIC_9x4, p_213968_1_, p_213968_2_, this,4);
    }

    @Override
    public int getContainerSize() {
        return 36;
    }

    public void destroy(DamageSource p_94095_1_) {
        this.remove();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.cargoContainer);
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //    @Nullable
//    @Override
//    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
//        return ChestContainer.threeRows(p_createMenu_1_, p_createMenu_2_);
//    }
//
//    @Override
//    public ActionResultType interact(PlayerEntity p_184230_1_, Hand p_184230_2_) {
//        ActionResultType ret = super.interact(p_184230_1_, p_184230_2_);
//        if (ret.consumesAction()) return ret;
//        p_184230_1_.openMenu(this);
//        return ActionResultType.SUCCESS;
//    }
}
