package com.cassiokf.industrialrenewal.items;


import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModBlocks;
import com.cassiokf.industrialrenewal.util.CouplingHandler;
import com.cassiokf.industrialrenewal.util.Utils;
import com.cassiokf.industrialrenewal.util.enums.EnumCouplingType;
import com.cassiokf.industrialrenewal.util.interfaces.ICoupleCart;
import com.google.common.collect.MapMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


import java.util.Map;
import java.util.UUID;

public class ItemCartLinker extends IRBaseItem{

    static final UUID NULL_UUID = new UUID(0, 0);
    private static final Map<Player, AbstractMinecart> linkMap = new MapMaker().weakKeys().weakValues().makeMap();

    public ItemCartLinker() {
        super(new Item.Properties().tab(IndustrialRenewal.IR_TAB).stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
//        if(state.getBlock().is(ModBlocks.BOOSTER_RAIL.get())){
//            world.setBlock(pos, state.setValue(BlockBoosterRail.FACING, state.getValue(BlockBoosterRail.FACING).getOpposite()), 3);
//            return InteractionResult.SUCCESS;
//        }
        return super.useOn(context);
    }

    public static void onPlayerUseLinkableItemOnCart(Player player, AbstractMinecart cart)
    {
        AbstractMinecart last = linkMap.remove(player);
        if (last != null && last.isAlive())
        {
            if(last.position().distanceTo(cart.position()) > 3f){
                Utils.sendChatMessage(player, "Coupling Failed, TOO FAR");
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
            Utils.sendChatMessage(player, "Coupling Canceled");
        }
        else
        {
            linkMap.put(player, cart);
            Utils.sendChatMessage(player, "Coupling Start");
        }
    }

    public static boolean CoupleCarts(AbstractMinecart cart1, AbstractMinecart cart2)
    {
        if (canCoupleCarts(cart1, cart2))
        {
            setConnection(cart1, cart2);
            setConnection(cart2, cart1);
            return true;
        }
        return false;
    }

    private static void setConnection(AbstractMinecart from, AbstractMinecart to)
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

    private static boolean canCoupleCarts(AbstractMinecart cart1, AbstractMinecart cart2)
    {
        if (cart1 == cart2 || (thereIsNoConnectionLeft(cart1) && thereIsNoConnectionLeft(cart2)) || CouplingHandler.isConnected(cart1, cart2))
        {
            return false;
        }
        return cart1.distanceToSqr(cart2) <= getMaxCouplingDistance(cart1, cart2);
    }

    public static boolean thereIsNoConnectionLeft(AbstractMinecart cart)
    {
        return !hasFreeConnectionIn(cart, EnumCouplingType.COUPLING_1)
                && !hasFreeConnectionIn(cart, EnumCouplingType.COUPLING_2);
    }

    public static boolean hasFreeConnectionIn(AbstractMinecart cart, EnumCouplingType type)
    {
        UUID cartUUID = CouplingHandler.getConnection(cart, type);
        return cartUUID.equals(NULL_UUID);
    }

    private static float getMaxCouplingDistance(AbstractMinecart cart1, AbstractMinecart cart2)
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