package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.util.CouplingHandler;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.cassiokf.IndustrialRenewal.util.enums.EnumCouplingType;
import com.cassiokf.IndustrialRenewal.util.interfaces.ICoupleCart;
import com.google.common.collect.MapMaker;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import java.util.Map;
import java.util.UUID;

public class ItemCartLinker extends IRBaseItem{

    static final UUID NULL_UUID = new UUID(0, 0);
    private static final Map<PlayerEntity, AbstractMinecartEntity> linkMap = new MapMaker().weakKeys().weakValues().makeMap();

    public ItemCartLinker(String name) {
        super(name, new Item.Properties().stacksTo(1));
    }

    public static void onPlayerUseLinkableItemOnCart(PlayerEntity player, AbstractMinecartEntity cart)
    {
        AbstractMinecartEntity last = linkMap.remove(player);
        if (last != null && last.isAlive())
        {
            if(last.position().distanceTo(cart.position()) > 2f){
                Utils.sendChatMessage(player, "Coupling FAILED, TOO FAR");
                return;
            }

//            Vector3d spawnPos = last.position().add(cart.position().subtract(last.position()).multiply(0.5f,0.5f,0.5f));
//            CouplerEntity couplerEntity = new CouplerEntity(player.level, spawnPos.x, spawnPos.y, spawnPos.z, last, cart);
//            player.level.addFreshEntity(couplerEntity);
            if (CouplingHandler.isConnected(cart, last))
            {
                CouplingHandler.removeConnection(cart, last);
                Utils.sendChatMessage(player, "Carts Decoupled");
                return;
            }
            else
            {
                if (CoupleCarts(last, cart))
                {
                    Utils.sendChatMessage(player, "Carts Coupled");
                    Utils.sendConsoleMessage("player " + player.getDisplayName().getString() + " Connected " + last.getName() + " to " + cart.getName());
                    return;
                }
            }
            Utils.sendChatMessage(player, "Coupling SUCCESS");
        }
        else
        {
            linkMap.put(player, cart);
            Utils.sendChatMessage(player, "Coupling Start");
        }
    }

    public static boolean CoupleCarts(AbstractMinecartEntity cart1, AbstractMinecartEntity cart2)
    {
        if (canCoupleCarts(cart1, cart2))
        {
            setConnection(cart1, cart2);
            setConnection(cart2, cart1);
            return true;
        }
        return false;
    }

    private static void setConnection(AbstractMinecartEntity from, AbstractMinecartEntity to)
    {
        for (EnumCouplingType link : EnumCouplingType.VALUES)
        {
            if (hasFreeConnectionIn(from, link))
            {
//                if (from instanceof EntityTenderBase && to instanceof LocomotiveBase)
//                {
//                    ((LocomotiveBase) to).setTender((EntityTenderBase) from);
//                }
                UUID id = to.getUUID();

                from.getPersistentData().putLong(link.tagMostSigBits, id.getMostSignificantBits());
                from.getPersistentData().putLong(link.tagLeastSigBits, id.getLeastSignificantBits());
//                from.getEntityData().setLong(link.tagMostSigBits, id.getMostSignificantBits());
//                from.getEntityData().setLong(link.tagLeastSigBits, id.getLeastSignificantBits());
                return;
            }
        }
    }

    private static boolean canCoupleCarts(AbstractMinecartEntity cart1, AbstractMinecartEntity cart2)
    {
        if (cart1 == cart2 || (thereIsNoConnectionLeft(cart1) && thereIsNoConnectionLeft(cart2)) || CouplingHandler.isConnected(cart1, cart2))
        {
            return false;
        }
        return cart1.distanceToSqr(cart2) <= getMaxCouplingDistance(cart1, cart2);
    }

    public static boolean thereIsNoConnectionLeft(AbstractMinecartEntity cart)
    {
        return !hasFreeConnectionIn(cart, EnumCouplingType.COUPLING_1)
                && !hasFreeConnectionIn(cart, EnumCouplingType.COUPLING_2);
    }

    public static boolean hasFreeConnectionIn(AbstractMinecartEntity cart, EnumCouplingType type)
    {
        UUID cartUUID = CouplingHandler.getConnection(cart, type);
        return cartUUID.equals(NULL_UUID);
    }

    private static float getMaxCouplingDistance(AbstractMinecartEntity cart1, AbstractMinecartEntity cart2)
    {
        float defaultDistance = 1.6f;
        float dist = 0;
        if (cart1 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart1).getMaxCouplingDistance(cart2);
        else
            dist += defaultDistance;
        if (cart2 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart2).getMaxCouplingDistance(cart1);
        else
            dist += defaultDistance;
        return dist * dist;
    }
}
