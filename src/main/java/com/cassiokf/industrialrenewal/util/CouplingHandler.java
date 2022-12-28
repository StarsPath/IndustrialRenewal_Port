package com.cassiokf.industrialrenewal.util;

import com.cassiokf.industrialrenewal.util.enums.EnumCouplingType;
import com.cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;

import javax.annotation.Nullable;
import java.util.UUID;

public class CouplingHandler {
    public static @Nullable
    AbstractMinecart getCartFromUUID(@Nullable Level world, @Nullable UUID id)
    {
        if (world == null || id == null)
            return null;
        if (world instanceof ServerLevel)
        {
            Entity entity = ((ServerLevel) world).getEntity(id);
            if (entity instanceof AbstractMinecart && entity.isAlive())
            {
                return (AbstractMinecart) entity;
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

    public static void onMinecartTick(AbstractMinecart cart)
    {
        boolean connectedA = updateCartVelocity(cart, EnumCouplingType.COUPLING_1);
        boolean connectedB = updateCartVelocity(cart, EnumCouplingType.COUPLING_2);

        if (connectedA || connectedB)
        {
            applyDrag(cart);
        }
    }

    private static void applyDrag(AbstractMinecart cart)
    {
        cart.setDeltaMovement(cart.getDeltaMovement().x * 0.99, cart.getDeltaMovement().y, cart.getDeltaMovement().z * 0.99);
    }

    private static boolean updateCartVelocity(AbstractMinecart cart1, EnumCouplingType ConnectionType)
    {
        AbstractMinecart cart2 = getCartFromUUID(cart1.level, getConnection(cart1, ConnectionType));
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

            Vec2 cart1Pos = new Vec2((float)cart1.xo, (float)cart1.zo);
            Vec2 cart2Pos = new Vec2((float)cart2.xo, (float)cart2.zo);
            Vec2 normalized = new Vec2(cart2Pos.x - cart1Pos.x, cart2Pos.y - cart1Pos.y);
            normalized = normalize(normalized);

            // Spring force
            applySpringForce(cart1, cart2, dist, normalized);

            // Damping force
            applyDampingForce(cart1, cart2, normalized);

            return true;
        }
        return false;
    }

    public static Vec2 normalize(Vec2 toNorm)
    {
        float x = toNorm.x;
        float y = toNorm.y;
        double sqrt = Math.sqrt(x * x + y * y);
        if (sqrt != 0)
        {
            return new Vec2((float)(x / sqrt), (float)(y / sqrt));
        }
        return toNorm;
    }

    private static void applySpringForce(AbstractMinecart cart1, AbstractMinecart cart2, double distance, Vec2 normalized)
    {
        double stretch = 1.0F * (distance - getDistanceBetween(cart1, cart2));

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

    private static void applyDampingForce(AbstractMinecart cart1, AbstractMinecart cart2, Vec2 norm)
    {
        Vec2 cart1Vel = new Vec2((float)cart1.getDeltaMovement().x, (float)cart1.getDeltaMovement().z);
        Vec2 cart2Vel = new Vec2((float)cart2.getDeltaMovement().x, (float)cart2.getDeltaMovement().z);

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

    private static float getDistanceBetween(AbstractMinecart cart1, AbstractMinecart cart2)
    {
        float dist = 0;
        if (cart1 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart1).getFixedDistance(cart2);
        else
            dist += 0.85f;
        if (cart2 instanceof ICoupleCart)
            dist += ((ICoupleCart) cart2).getFixedDistance(cart1);
        else
            dist += 0.85f;
        return dist;
    }

    public static UUID getConnection(AbstractMinecart cart, EnumCouplingType ConnectionType)
    {
        long high = cart.getPersistentData().getLong(ConnectionType.tagMostSigBits);
        long low = cart.getPersistentData().getLong(ConnectionType.tagLeastSigBits);
//        long high = cart.getEntityData().getLong(ConnectionType.tagMostSigBits);
//        long low = cart.getEntityData().getLong(ConnectionType.tagLeastSigBits);
        return new UUID(high, low);
    }

    public static boolean isConnected(AbstractMinecart cart1, AbstractMinecart cart2)
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

    public static void removeConnection(AbstractMinecart one, AbstractMinecart two)
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

    private static EnumCouplingType getConnectionType(AbstractMinecart from, AbstractMinecart to)
    {
        UUID id = to.getUUID();
        if (id.equals(getConnection(from, EnumCouplingType.COUPLING_1)))
            return EnumCouplingType.COUPLING_1;
        else if (id.equals(getConnection(from, EnumCouplingType.COUPLING_2)))
            return EnumCouplingType.COUPLING_2;
        return null;
    }

    private static void removeConnection(AbstractMinecart cart, EnumCouplingType ConnectionType)
    {
//        cart.removeTag(ConnectionType.tagMostSigBits);
//        cart.removeTag(ConnectionType.tagLeastSigBits);
//        cart.getEntityData().removeTag(ConnectionType.tagMostSigBits);
//        cart.getEntityData().removeTag(ConnectionType.tagLeastSigBits);
        cart.getPersistentData().remove(ConnectionType.tagMostSigBits);
        cart.getPersistentData().remove(ConnectionType.tagLeastSigBits);
    }
}
