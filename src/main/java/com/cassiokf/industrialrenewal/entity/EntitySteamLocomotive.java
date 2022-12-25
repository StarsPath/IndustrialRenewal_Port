package com.cassiokf.industrialrenewal.entity;

import com.cassiokf.industrialrenewal.config.Config;
import com.cassiokf.industrialrenewal.init.ModEntity;
import com.cassiokf.industrialrenewal.init.ModFluids;
import com.cassiokf.industrialrenewal.init.ModItems;
import com.cassiokf.industrialrenewal.menus.menu.SteamLocomotiveMenu;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import com.cassiokf.industrialrenewal.util.CustomItemStackHandler;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntitySteamLocomotive extends LocomotiveBase implements MenuProvider
{

    private final CustomItemStackHandler itemStorage = new CustomItemStackHandler(6){
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return FurnaceBlockEntity.isFuel(stack) && !stack.is(Items.LAVA_BUCKET);
//            return true;
//            return stack.getItem().getBurnTime(stack, null) > 0;
        }
    };
    private final LazyOptional<CustomItemStackHandler> itemHandler = LazyOptional.of(()-> itemStorage);


    private final CustomFluidTank fluidWaterTank = new CustomFluidTank(16000);
    private final LazyOptional<CustomFluidTank> waterTankHandler = LazyOptional.of(()->fluidWaterTank);

    private final CustomFluidTank fluidSteamTank = new CustomFluidTank(16000);
    private final LazyOptional<CustomFluidTank> steamTankHandler = LazyOptional.of(()->fluidSteamTank);


    // TODO: add to config
    private final int WATER_CONSUME_PER_TICK = 2; // Amount of water consumed per tick to generate steam

    private final float WATER2STEAM_RATIO = Config.STEAM_BOILER_WATER_STEAM_CONVERSION.get();
    private final int STEAM_PRODUCE_PER_TICK = (int)(WATER_CONSUME_PER_TICK * WATER2STEAM_RATIO); // Amount of steam produced per tick

    private final int STEAM_CONSUME_PER_TICK = 8; // Amount of steam consumed to boost forward

    private int burnTime;


    public EntitySteamLocomotive(EntityType<?> p_38213_, Level p_38214_) {
        super(p_38213_, p_38214_);
    }

    public EntitySteamLocomotive(double p_38208_, double p_38209_, double p_38210_, Level p_38211_) {
        super(ModEntity.STEAM_LOCOMOTIVE.get(), p_38208_, p_38209_, p_38210_, p_38211_);
    }

    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide){
            if(consumeFuel())
                generateSteam();
            if(consumeSteam())
                move();
//            Utils.debug("MOV", getYRot(), this.getDeltaMovement().x, this.getDeltaMovement().y);
        }
    }

    public boolean consumeFuel(){
        if(burnTime <= 0){
            for(int i = 0; i < itemStorage.getSlots(); i++){
                ItemStack stack = itemStorage.getStackInSlot(i);
                if(FurnaceBlockEntity.isFuel(stack)){
                    burnTime += ForgeHooks.getBurnTime(stack, null);
                    stack.shrink(1);
                    break;
                }
            }
        }
        return burnTime > 0;
//        return false;
    }

    public void generateSteam(){
        if(fluidSteamTank.getFluidAmount() <= fluidSteamTank.getCapacity()-STEAM_PRODUCE_PER_TICK &&
            fluidWaterTank.getFluidAmount() > WATER_CONSUME_PER_TICK &&
            burnTime > 0
            )
        {
            FluidStack waterConsumed = fluidWaterTank.drain(WATER_CONSUME_PER_TICK, IFluidHandler.FluidAction.SIMULATE);
            int amountGenerated = fluidSteamTank.fill(new FluidStack(ModFluids.STEAM.get(), (int)(waterConsumed.getAmount() * WATER2STEAM_RATIO)), IFluidHandler.FluidAction.SIMULATE);

            waterConsumed = fluidWaterTank.drain((int)(amountGenerated / WATER2STEAM_RATIO), IFluidHandler.FluidAction.EXECUTE);
            amountGenerated = fluidSteamTank.fill(new FluidStack(ModFluids.STEAM.get(), (int)(waterConsumed.getAmount() * WATER2STEAM_RATIO)), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public boolean consumeSteam(){
        if(fluidSteamTank.getFluidAmount() >= STEAM_CONSUME_PER_TICK){
            fluidSteamTank.drain(STEAM_CONSUME_PER_TICK, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }

    public void move(){
        super.moveForward();
    }


    @Override
    public void destroy(DamageSource p_94095_1_) {
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(ModItems.STEAM_LOCOMOTIVE.get());
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack);
            for(int i = 0; i < itemStorage.getSlots(); i++){
                this.spawnAtLocation(itemStorage.getStackInSlot(i));
            }
        }
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public boolean canCollideWith(Entity p_38168_) {
        return !(p_38168_ instanceof LivingEntity) && super.canCollideWith(p_38168_);
//        return super.canCollideWith(p_38168_);
    }

    @NotNull
    @Override
    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if(!level.isClientSide){
            if(player.isCrouching()){
                return super.interact(player, hand);
            }
            else if(hand == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty() && !player.isCrouching()){
                NetworkHooks.openGui(((ServerPlayer)player), this, new BlockPos(getId(), 0, 0));
            }
            else{
                if(player.getMainHandItem().is(ModItems.SCREW_DRIVE.get()))
                    return InteractionResult.SUCCESS;

                boolean acceptFluid = FluidUtil.interactWithFluidHandler(player, hand, fluidWaterTank);
                if(!acceptFluid){
                    String message = String.format("%s %s: %d/%d mB", "Water Tank", I18n.get(fluidWaterTank.getFluid().getTranslationKey()), fluidWaterTank.getFluidAmount(), fluidWaterTank.getCapacity());
                    player.sendMessage(new TextComponent(message), player.getUUID());
                    message = String.format("%s %s: %d/%d mB", "Steam Tank", I18n.get(fluidSteamTank.getFluid().getTranslationKey()), fluidSteamTank.getFluidAmount(), fluidSteamTank.getCapacity());
                    player.sendMessage(new TextComponent(message), player.getUUID());
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public ItemStackHandler getInventory(){
        return itemStorage;
    }


        @NotNull
    @Override
    public Component getDisplayName() {
        return new TextComponent("Steam Locomotive");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new SteamLocomotiveMenu(id, inv, this);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        fluidWaterTank.writeToNBT(tag);
        fluidSteamTank.writeToNBT(tag);
        tag.put("inv", itemStorage.serializeNBT());
        super.addAdditionalSaveData(tag);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        fluidWaterTank.readFromNBT(tag);
        fluidSteamTank.readFromNBT(tag);
        itemStorage.deserializeNBT(tag.getCompound("inv"));
        super.readAdditionalSaveData(tag);
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        waterTankHandler.invalidate();
        steamTankHandler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return this.getCapability(capability);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return waterTankHandler.cast();
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemHandler.cast();
        return super.getCapability(cap);
    }
}
