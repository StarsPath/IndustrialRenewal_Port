package com.cassiokf.industrialrenewal.blockentity.locomotion;

import com.cassiokf.industrialrenewal.blocks.locomotion.BlockCargoLoader;
import com.cassiokf.industrialrenewal.blocks.locomotion.BlockFluidLoader;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.menus.menu.FluidLoaderMenu;
import com.cassiokf.industrialrenewal.util.CustomFluidTank;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockEntityFluidLoader extends BlockEntityBaseLoader implements MenuProvider {

    public CustomFluidTank tank = new CustomFluidTank(16000){

        @Override
        public boolean canFill()
        {
            return true;
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            BlockEntityFluidLoader.this.sync();
        }
    };
    public LazyOptional<CustomFluidTank> tankHandler = LazyOptional.of(()->tank);

    private final int maxFlowPerTick = 200;
    private boolean checked = false;
    private boolean master;
    private float ySlide = 0;

    private int cartFluidAmount;
    private int cartFluidCapacity;
    private int noActivity = 0;

    public BlockEntityFluidLoader(BlockPos pos, BlockState state) {
        super(ModBlockEntity.FLUID_LOADER.get(), pos, state);
    }

//    public BlockEntityFluidLoader(TileEntityType<?> tileEntityTypeIn) {
//        super(tileEntityTypeIn);
//    }
//
//    public BlockEntityFluidLoader() {
//        super(ModTileEntities.FLUID_LOADER.get());
//    }

    public void tick() {
        if (!level.isClientSide && isMaster())
        {
            if (cartActivity > 0)
            {
                cartActivity--;
                sync();
            }

            BlockPos loaderPosition = worldPosition.relative(getBlockFacing());
            FluidTank tank = tankHandler.orElse(null);
            if(tank == null)
                return;

            IFluidHandler containerTank = getTankAt(level, loaderPosition.getX(), loaderPosition.getY(), loaderPosition.getZ());

            if(isUnload()) { // from cart to cargoLoader
                if(containerTank!=null){
                    cartFluidAmount = containerTank.getFluidInTank(0).getAmount();
                    cartFluidCapacity = containerTank.getTankCapacity(0);
                    cartActivity = 10;
                    loading = true;
                    Utils.moveFluidToTank(containerTank, tank);
                }
                else{
                    loading = false;
                }
            }
            else if (!isUnload()) { // from cargoLoader to cart
                if(containerTank!=null){
                    cartFluidAmount = containerTank.getFluidInTank(0).getAmount();
                    cartFluidCapacity = containerTank.getTankCapacity(0);
                    cartActivity = 10;
                    loading = true;
                    Utils.moveFluidToTank(tank, containerTank);
                }
                else{
                    loading = false;
                }
            }

            if(containerTank == null){
                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, true), 3);
            }
            else
                switch (waitE){
                    case WAIT_FULL:
                        if(containerTank!=null) {
                            boolean setBool = containerTank.getFluidInTank(0).getAmount()>= containerTank.getTankCapacity(0);
                            if(level.getBlockState(worldPosition).getValue(BlockStateProperties.POWERED) != setBool)
                                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, setBool), 3);
                        }
                        else{
                            if(level.getBlockState(worldPosition).getValue(BlockStateProperties.POWERED))
                                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, false), 3);
                        }
                        break;
                    case WAIT_EMPTY:
                        if(containerTank!=null) {
                            boolean setBool = containerTank.getFluidInTank(0).getAmount()<=0;
                            if(level.getBlockState(worldPosition).getValue(BlockStateProperties.POWERED) != setBool)
                                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, setBool), 3);
                        }
                        else{
                            if(level.getBlockState(worldPosition).getValue(BlockStateProperties.POWERED))
                                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, false), 3);
                        }
                        break;
                    case NO_ACTIVITY: {
                        if(!level.getBlockState(worldPosition).getValue(BlockStateProperties.POWERED))
                            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, true), 3);
                        break;
                    }
                    case NEVER: {
                        if(level.getBlockState(worldPosition).getValue(BlockStateProperties.POWERED))
                            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BlockStateProperties.POWERED, false), 3);
                        break;
                    }
                }
        }
        else{
            if (loading)
            {
                ySlide = Utils.lerp(ySlide, 0.5f, 0.08f);
            }
            else
            {
                ySlide = Utils.lerp(ySlide, 0, 0.04f);
            }
        }
    }

    public IFluidHandler getTankAt(Level world, double x, double y, double z){
        //IFluidHandler inventory = null;
        IFluidHandler handler = null;

        List<Entity> list = world.getEntities((Entity)null, new AABB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelector.ENTITY_STILL_ALIVE);
        if (!list.isEmpty()) {
            handler = list.get(world.random.nextInt(list.size())).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);;
        }
        return handler;
    }

    @Override
    public Direction getBlockFacing() {
        if (blockFacing == null) blockFacing = level.getBlockState(worldPosition).getValue(BlockFluidLoader.FACING);
        return blockFacing;
    }

    public BlockEntityFluidLoader getMaster(){
        if(isMaster())
            return this;
        else if(level.getBlockEntity(worldPosition.below())instanceof BlockEntityFluidLoader)
            return (BlockEntityFluidLoader) level.getBlockEntity(worldPosition.below());
        return null;
    }

    public boolean isMaster()
    {
        if (!checked)
        {
            master = level.getBlockState(worldPosition).getValue(BlockCargoLoader.MASTER);
            checked = true;
        }
        return master;
    }

    public String getTankText()
    {
        if (tank.getFluid() == null) return I18n.get("gui.industrialrenewal.fluid.empty");
        return I18n.get(this.tank.getFluid().getTranslationKey());
    }

    public String getCartName()
    {
        if (cartActivity <= 0) return "No Cart";
        return cartName;
    }

    public float getSlide()
    {
        return ySlide;
    }

    public float getCartFluidAngle()
    {
        if (cartActivity <= 0) return 0;
        float currentAmount = cartFluidAmount;
        float totalCapacity = cartFluidCapacity;
        return Utils.normalizeClamped(currentAmount, 0, totalCapacity) * 180f;
    }

    public float getTankFluidAngle()
    {
        float currentAmount = tank.getFluidAmount();
        float totalCapacity = tank.getCapacity();
        return Utils.normalizeClamped(currentAmount, 0, totalCapacity) * 180f;
    }

    @Override
    public boolean isUnload()
    {
        return unload;
    }

    public LazyOptional<CustomFluidTank> getFluidHandler(){
        return tankHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        tankHandler.ifPresent(tank->tank.writeToNBT(compoundTag));
        compoundTag.putInt("capacity", cartFluidCapacity);
        compoundTag.putInt("cartAmount", cartFluidAmount);
        compoundTag.putInt("activity", cartActivity);
        compoundTag.putBoolean("loading", loading);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        tankHandler.ifPresent(tank->tank.readFromNBT(compoundTag));
        cartFluidCapacity = compoundTag.getInt("capacity");
        cartFluidAmount = compoundTag.getInt("cartAmount");
        cartActivity = compoundTag.getInt("activity");
        loading = compoundTag.getBoolean("loading");
        super.load(compoundTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return (isMaster() && side == getBlockFacing().getOpposite() && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                ? tankHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Fluid Loader");
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new FluidLoaderMenu(id, inv, this);
    }
}
