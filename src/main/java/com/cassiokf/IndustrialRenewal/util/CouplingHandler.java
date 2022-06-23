package com.cassiokf.IndustrialRenewal.util;

import com.cassiokf.IndustrialRenewal.util.enums.EnumCouplingType;
import com.cassiokf.IndustrialRenewal.util.interfaces.ICoupleCart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

public class CouplingHandler {
    public static @Nullable
    AbstractMinecartEntity getCartFromUUID(@Nullable World world, @Nullable UUID id)
    {
        if (world == null || id == null)
            return null;
        if (world instanceof ServerWorld)
        {
            Entity entity = ((ServerWorld) world).getEntity(id);
            if (entity instanceof AbstractMinecartEntity && entity.isAlive())
            {
                return (AbstractMinecartEntity) entity;
            }
        }
        else
        {
//            for (Entity entity : world.getEntities())
//            {
//                if (entity instanceof EntityMinecart && entity.isEntityAlive() && entity.getPersistentID().equals(id))
//                    return (EntityMinecart) entity;
//            }
        }
        return null;
    }

    public static void onMinecartTick(AbstractMinecartEntity cart)
    {
        boolean connectedA = updateCartVelocity(cart, EnumCouplingType.COUPLING_1);
        boolean connectedB = updateCartVelocity(cart, EnumCouplingType.COUPLING_2);

        if (connectedA || connectedB)
        {
            applyDrag(cart);
        }
    }

    private static void applyDrag(AbstractMinecartEntity cart)
    {
        cart.setDeltaMovement(cart.getDeltaMovement().x * 0.99, cart.getDeltaMovement().y, cart.getDeltaMovement().z * 0.99);
    }

    private static boolean updateCartVelocity(AbstractMinecartEntity cart1, EnumCouplingType ConnectionType)
    {
        AbstractMinecartEntity cart2 = getCartFromUUID(cart1.level, getConnection(cart1, ConnectionType));
        if (cart2 != null && cart1 != cart2)
        {
            double dist = cart1.distanceTo(cart2);
            if (dist > 10F)
            {
                removeConnection(cart1, cart2);
                return false;
            }

//            if (cart1 instanceof EntityTenderBase && cart2 instanceof LocomotiveBase && ((LocomotiveBase) cart2).tender != cart1)
//            {
//                ((LocomotiveBase) cart2).setTender((EntityTenderBase) cart1);
//            }
//            else if (cart2 instanceof EntityTenderBase && cart1 instanceof LocomotiveBase && ((LocomotiveBase) cart1).tender != cart2)
//            {
//                ((LocomotiveBase) cart1).setTender((EntityTenderBase) cart2);
//            }

            Vector2f cart1Pos = new Vector2f((float)cart1.xo, (float)cart1.zo);
            Vector2f cart2Pos = new Vector2f((float)cart2.xo, (float)cart2.zo);
            Vector2f normalized = new Vector2f(cart2Pos.x - cart1Pos.x, cart2Pos.y - cart1Pos.y);
            normalized = normalize(normalized);

            // Spring force
            applySpringForce(cart1, cart2, dist, normalized);

            // Damping force
            applyDampingForce(cart1, cart2, normalized);

            return true;
        }
        return false;
    }

    public static Vector2f normalize(Vector2f toNorm)
    {
        float x = toNorm.x;
        float y = toNorm.y;
        double sqrt = Math.sqrt(x * x + y * y);
        if (sqrt != 0)
        {
            return new Vector2f((float)(x / sqrt), (float)(y / sqrt));
        }
        return toNorm;
    }

    private static void applySpringForce(AbstractMinecartEntity cart1, AbstractMinecartEntity cart2, double distance, Vector2f normalized)
    {
        double stretch = 1.5F * (distance - getDistanceBetween(cart1, cart2));

        double springX = stretch * normalized.x;
        double springZ = stretch * normalized.y;

        springX = limitForce(springX);
        springZ = limitForce(springZ);

//        cart1.motionX += springX;
//        cart1.motionZ += springZ;
//        cart2.motionX -= springX;
//        cart2.motionZ -= springZ;
        cart1.setDeltaMovement(cart1.getDeltaMovement().x + springX, cart1.getDeltaMovement().y, cart1.getDeltaMovement().z + springZ);
        cart2.setDeltaMovement(cart2.getDeltaMovement().x - springX, cart2.getDeltaMovement().y, cart2.getDeltaMovement().z - springZ);
    }

    private static void applyDampingForce(AbstractMinecartEntity cart1, AbstractMinecartEntity cart2, Vector2f norm)
    {
        Vector2f cart1Vel = new Vector2f((float)cart1.getDeltaMovement().x, (float)cart1.getDeltaMovement().z);
        Vector2f cart2Vel = new Vector2f((float)cart2.getDeltaMovement().x, (float)cart2.getDeltaMovement().z);

        double normal = 0.3F * (((cart2Vel.x - cart1Vel.x) * norm.x) + ((cart2Vel.y - cart1Vel.y) * norm.y));

        double dampX = normal * norm.x;
        double dampZ = normal * norm.y;

        dampX = limitForce(dampX);
        dampZ = limitForce(dampZ);

//        cart1.motionX += dampX;
//        cart1.motionZ += dampZ;
//        cart2.motionX -= dampX;
//        cart2.motionZ -= dampZ;
        cart1.setDeltaMovement(cart1.getDeltaMovement().x + dampX, cart1.getDeltaMovement().y, cart1.getDeltaMovement().z + dampZ);
        cart2.setDeltaMovement(cart2.getDeltaMovement().x - dampX, cart2.getDeltaMovement().y, cart2.getDeltaMovement().z - dampZ);
    }

    private static double limitForce(double force)
    {
        return Math.copySign(Math.min(Math.abs(force), 6F), force);
    }

    private static float getDistanceBetween(AbstractMinecartEntity cart1, AbstractMinecartEntity cart2)
    {
        float dist = 0;
        if (cart1 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart1).getFixedDistance(cart2);
        else
            dist += 0.78f;
        if (cart2 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart2).getFixedDistance(cart1);
        else
            dist += 0.78f;
        return dist;
    }

    public static UUID getConnection(AbstractMinecartEntity cart, EnumCouplingType ConnectionType)
    {
        long high = cart.getPersistentData().getLong(ConnectionType.tagMostSigBits);
        long low = cart.getPersistentData().getLong(ConnectionType.tagLeastSigBits);
//        long high = cart.getEntityData().getLong(ConnectionType.tagMostSigBits);
//        long low = cart.getEntityData().getLong(ConnectionType.tagLeastSigBits);
        return new UUID(high, low);
    }

    public static boolean isConnected(AbstractMinecartEntity cart1, AbstractMinecartEntity cart2)
    {
        if (cart1 == cart2) return false;

        UUID id1 = cart1.getUUID();
        UUID id2 = cart2.getUUID();
        boolean cart1Connected = id2.equals(getConnection(cart1, EnumCouplingType.COUPLING_1))
                || id2.equals(getConnection(cart1, EnumCouplingType.COUPLING_2));
        boolean cart2Connected = id1.equals(getConnection(cart2, EnumCouplingType.COUPLING_1))
                || id1.equals(getConnection(cart2, EnumCouplingType.COUPLING_2));

        return cart1Connected || cart2Connected;
    }

    public static void removeConnection(AbstractMinecartEntity one, AbstractMinecartEntity two)
    {
        EnumCouplingType firstConnection = getConnectionType(one, two);
        EnumCouplingType secondConnection = getConnectionType(two, one);

        if (firstConnection != null)
        {
            removeConnection(one, firstConnection);
        }
        if (secondConnection != null)
        {
            removeConnection(two, secondConnection);
        }
    }

    private static EnumCouplingType getConnectionType(AbstractMinecartEntity from, AbstractMinecartEntity to)
    {
        UUID id = to.getUUID();
        if (id.equals(getConnection(from, EnumCouplingType.COUPLING_1)))
            return EnumCouplingType.COUPLING_1;
        else if (id.equals(getConnection(from, EnumCouplingType.COUPLING_2)))
            return EnumCouplingType.COUPLING_2;
        return null;
    }

    private static void removeConnection(AbstractMinecartEntity cart, EnumCouplingType ConnectionType)
    {
//        cart.removeTag(ConnectionType.tagMostSigBits);
//        cart.removeTag(ConnectionType.tagLeastSigBits);
//        cart.getEntityData().removeTag(ConnectionType.tagMostSigBits);
//        cart.getEntityData().removeTag(ConnectionType.tagLeastSigBits);
        cart.getPersistentData().remove(ConnectionType.tagMostSigBits);
        cart.getPersistentData().remove(ConnectionType.tagLeastSigBits);
    }
}
