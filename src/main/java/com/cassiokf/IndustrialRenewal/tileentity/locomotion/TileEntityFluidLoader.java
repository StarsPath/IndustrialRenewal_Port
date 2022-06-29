package com.cassiokf.IndustrialRenewal.tileentity.locomotion;

import com.cassiokf.IndustrialRenewal.blocks.locomotion.BlockCargoLoader;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFluidLoader extends TileEntityBaseLoader implements ITickableTileEntity {

    public CustomFluidTank tank = new CustomFluidTank(16000){

        @Override
        public boolean canFill()
        {
            return true;
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            TileEntityFluidLoader.this.sync();
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

    public TileEntityFluidLoader(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public TileEntityFluidLoader() {
        super(ModTileEntities.FLUID_LOADER.get());
    }


    @Override
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

    public IFluidHandler getTankAt(World world, double x, double y, double z){
        //IFluidHandler inventory = null;
        IFluidHandler handler = null;

        List<Entity> list = world.getEntities((Entity)null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.ENTITY_STILL_ALIVE);
        if (!list.isEmpty()) {
            handler = list.get(world.random.nextInt(list.size())).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);;
        }
        return handler;
    }

    @Override
    public Direction getBlockFacing() {
        if (blockFacing == null) blockFacing = level.getBlockState(worldPosition).getValue(BlockCargoLoader.FACING);
        return blockFacing;
    }

    public TileEntityFluidLoader getMaster(){
        if(isMaster())
            return this;
        else if(level.getBlockEntity(worldPosition.below())instanceof TileEntityFluidLoader)
            return (TileEntityFluidLoader) level.getBlockEntity(worldPosition.below());
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
    public CompoundNBT save(CompoundNBT compound) {
        tankHandler.ifPresent(tank->tank.writeToNBT(compound));
        compound.putInt("capacity", cartFluidCapacity);
        compound.putInt("cartAmount", cartFluidAmount);
        compound.putInt("activity", cartActivity);
        compound.putBoolean("loading", loading);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        tankHandler.ifPresent(tank->tank.readFromNBT(compound));
        cartFluidCapacity = compound.getInt("capacity");
        cartFluidAmount = compound.getInt("cartAmount");
        cartActivity = compound.getInt("activity");
        loading = compound.getBoolean("loading");
        super.load(state, compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return (isMaster() && side == getBlockFacing().getOpposite() && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                ? tankHandler.cast() : super.getCapability(cap, side);
    }
}
