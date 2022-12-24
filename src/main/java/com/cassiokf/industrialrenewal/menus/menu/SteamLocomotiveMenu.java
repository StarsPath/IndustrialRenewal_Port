package com.cassiokf.industrialrenewal.menus.menu;

import com.cassiokf.industrialrenewal.entity.EntitySteamLocomotive;
import com.cassiokf.industrialrenewal.init.ModMenus;
import com.cassiokf.industrialrenewal.menus.MenuBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.SlotItemHandler;

public class SteamLocomotiveMenu extends MenuBase {
    private EntitySteamLocomotive steamLocomotive;

    private final int[] xPos = {62, 80, 98, 62, 80, 98};
    private final int[] yPos = {21, 21, 21, 39, 39, 39};

    public SteamLocomotiveMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getEntity(extraData.readBlockPos().getX()));
    }

    public SteamLocomotiveMenu(int pContainerId, Inventory inv, Entity entity) {
        super(ModMenus.STEAM_LOCOMOTIVE_MENU.get(), pContainerId);
        if(entity instanceof EntitySteamLocomotive)
            this.steamLocomotive = (EntitySteamLocomotive)entity;
        else
            throw new IllegalStateException("No SteamLocomotiveEntity Found");

        for(int i = 0; i < xPos.length; i++){
            this.addSlot(new SlotItemHandler(this.steamLocomotive.getInventory(), i, xPos[i], yPos[i]){
                @Override
                public void setChanged() {
//                    entity.setChanged();
                    super.setChanged();
                }
            });
        }

        drawPlayerInv(inv);
    }

//    protected SteamLocomotiveMenu(@Nullable MenuType<?> p_38851_, int p_38852_) {
//        super(p_38851_, p_38852_);
//    }


    public Entity getSteamLocomotive() {
        return steamLocomotive;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceTo(steamLocomotive) < 5.0f;
//        return stillValid(ContainerLevelAccess.create(entity.getLevel(), entity.getOnPos()),
//                player, ModBlocks.STORAGE_CHEST.get());
    }
}
