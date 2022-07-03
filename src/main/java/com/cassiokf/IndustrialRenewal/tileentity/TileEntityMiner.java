package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.item.IRItemDrill;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TileEntity3x3x3MachineBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import com.cassiokf.IndustrialRenewal.util.CustomFluidTank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileEntityMiner extends TileEntity3x3x3MachineBase<TileEntityMiner> implements ITickableTileEntity {

    public CustomFluidTank waterTank = new CustomFluidTank(32000)
    {
        @Override
        public boolean isFluidValid(FluidStack stack)
        {
            return stack != null && stack.getFluid().equals(Fluids.WATER);
        }

        @Override
        public void onContentsChanged()
        {
            TileEntityMiner.this.sync();
            //Utils.debug("water changed", waterTank.getFluidAmount());
        }
    };

    public LazyOptional<IItemHandler> drillInv = LazyOptional.of(this::createHandler);
    public LazyOptional<IItemHandler> internalInv = LazyOptional.of(this::createInternalHandler);
    private LazyOptional<IEnergyStorage> energyStorage = LazyOptional.of(this::createEnergy);
    private int maxHeat = 18000;
    private int drillHeat;
    private int oldDrillHeat;

    // TODO: add to config
    private int waterPerTick = 10;
    private int energyPerTick = 500;
    private int deepEnergyPerTick = 1000;
    private static final int cooldown = 120;
    private static final int damageAmount = 1;


    private int currentTick = 0;
    private boolean depleted = false;
    private boolean isDeepMine = false;
    private boolean running;
    private boolean oldRunning;

    //private Set<BlockPos> oresPos;
    private final Stack<OreMining> ores = new Stack<>();
    private final NonNullList<ItemStack> tempStack = NonNullList.create();
    private ItemStack vein = ItemStack.EMPTY;
    private int size;

    //Client only
    private float rotation;
    private float ySlide = 0;
    private boolean revert;

    //Server
    private int particleTick;
    public boolean firstLoad = false;

    public TileEntityMiner()
    {
        super(ModTileEntities.MINER_TILE.get());
    }

    public TileEntityMiner(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    private IEnergyStorage createEnergy()
    {
        return new CustomEnergyStorage(100000, 10240, 0)
        {
            @Override
            public void onEnergyChange()
            {
                TileEntityMiner.this.sync();
            }
        };
    }

    private IItemHandler createHandler()
    {
        return new ItemStackHandler(1)
        {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                if (stack.isEmpty()) return false;
                return stack.getItem() instanceof IRItemDrill;
            }

            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityMiner.this.sync();
            }
        };
    }

    private IItemHandler createInternalHandler()
    {
        return new ItemStackHandler(1)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                TileEntityMiner.this.sync();
            }
        };
    }

    @Override
    public void tick() {
        if (this.isMaster() && hasLevel())
        {
            running = canRun();
            doAnimation();
            if (!level.isClientSide)
            {
                //Utils.debug("energy, water", energyStorage.orElse(null).getEnergyStored(), energyStorage.orElse(null).getMaxEnergyStored(), waterTank.getFluidAmount());
                outputOrSpawn();
                boolean canCheck = canCheckOre();
                if (canCheck && getOreSize() == 0) getOres();
                if (canCheck && canRun())
                {
                    if (getOreSize() > 0) running = true;

                    if (running)
                    {
                        consumeEnergy();
                        if (drillHeat < (waterTank.getFluidAmount() >= waterPerTick ? 9400 : 17300)) drillHeat += 20;
                        mineOre();
                        size = getOreSize();
                        //Utils.debug("MINING...", size);
                    } else
                    {
                        size = 0;
                        drillHeat -= 30;
                        currentTick = 0;
                    }
                } else
                {
                    size = getOreSize();
                    drillHeat -= 30;
                    running = false;
                    currentTick = 0;
                }

                drillHeat = MathHelper.clamp(drillHeat, 3200, maxHeat);
                if (running != oldRunning || drillHeat != oldDrillHeat)
                {
                    oldRunning = running;
                    oldDrillHeat = drillHeat;
                    sync();
                }
            }
            doAnimation();
            //updateSound();
        }
    }

    private int getOreSize()
    {
        return isDeepMine() ? vein.getCount() : ores.size();
    }
    private boolean isDeepMine()
    {
        return isDeepMine;
    }

    public void checkDeepMine()
    {
        isDeepMine = drillInv.orElse(null).getStackInSlot(0).getItem() == ModItems.drillDeep;
        depleted = false;
        sync();
    }

    private int getFortune()
    {
        return drillInv.orElse(null).getStackInSlot(0).getItem().equals(ModItems.drillDiamond) ? 2 : 1;
    }

    private int getMaxCooldown()
    {
        int t = waterTank.getFluidAmount() >= waterPerTick ? cooldown : cooldown * 2;
        return isDeepMine() ? t * 2 : t;
    }

    private void mineOre()
    {
        //Utils.debug("Called mineOre");
        if (currentTick >= getMaxCooldown())
        {
            currentTick = 0;
            if (isDeepMine())
            {
                ItemStack s = vein.copy();
                s.setCount(1);
                tempStack.add(s);
                vein.shrink(1);
            } else
            {
                if (ores.isEmpty()) return;
                OreMining ore = ores.pop();
                if (level.getBlockState(ore.pos).getBlock() != ore.state.getBlock()) return;
                currentTick = 0;
                int fortune = getFortune();
                Block block = ore.state.getBlock();
                List<ItemStack> drops = block.getDrops(ore.state, level.getServer().getLevel(this.level.dimension()), ore.pos, this, null, getDrill());
                tempStack.addAll(drops);
                level.setBlock(ore.pos, Blocks.COBBLESTONE.defaultBlockState(), Constants.BlockFlags.DEFAULT);
                Utils.debug("Ore mined, set block cobble");
            }
            ItemStack s = tempStack.get(0);
            if (!s.isEmpty())
            {
                internalInv.orElse(null).insertItem(0, s, false);
                tempStack.remove(s);
            }
            damageDrill();
        } else currentTick++;
    }

    private void damageDrill()
    {
        int damage = drillHeat <= 13000 ? damageAmount : damageAmount * 4;
        ItemStack stack = drillInv.orElse(null).getStackInSlot(0);
        if (stack.hurt(damage, level.random, null))
        {
            stack.shrink(stack.getCount());
        }
    }

    private void consumeEnergy()
    {
        energyStorage.orElse(null).extractEnergy(isDeepMine() ? deepEnergyPerTick : energyPerTick, false);
        waterTank.drain(waterPerTick, IFluidHandler.FluidAction.EXECUTE);
    }

    private boolean canCheckOre()
    {
        return running
                && energyStorage.orElse(null).getEnergyStored() >= (isDeepMine() ? deepEnergyPerTick : energyPerTick)
                && !drillInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    private void outputOrSpawn()
    {
        if (!tempStack.isEmpty())
        {
            ItemStack s = tempStack.get(0);
            if (!s.isEmpty())
            {
                internalInv.orElse(null).insertItem(0, s, false);
                tempStack.remove(s);
            }
            return;
        }
        if (internalInv.orElse(null).getStackInSlot(0).isEmpty()) return;

        BlockPos outPos = worldPosition.relative(getMasterFacing(), 2).below();
        TileEntity te = level.getBlockEntity(outPos);
        if (te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getMasterFacing().getOpposite()).orElse(null) != null)
        {
            IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, getMasterFacing().getOpposite()).orElse(null);
            if (handler != null)
            {
                Utils.moveItemToInventory(internalInv.orElse(null), 0, handler);
            }
        } else
        {
            BlockState state = level.getBlockState(outPos);
            if (state.getBlock().isAir(state, level, outPos))
            {
                Utils.spawnItemStack(level, outPos, internalInv.orElse(null).getStackInSlot(0));
            }
        }
    }

    private void getOres()
    {
        tempStack.clear();
        //Utils.debug("call getOres");
        if (isDeepMine())
        {
            //vein = OreGeneration.getChunkVein(level, worldPosition);
            //size = vein.getCount();
            depleted = size == 0;
            sync();
            return;
        }
        IChunk chunk = level.getChunk(worldPosition);
        int a = chunk.getPos().x * 16;
        int b = chunk.getPos().z * 16;
        for (double y = 1; y <= worldPosition.getY() - 2; y++)
        {
            for (double x = 0; x <= 15; x++)
            {
                for (double z = 0; z <= 15; z++)
                {
                    BlockPos actualPosition = new BlockPos(a + x, y, b + z);
                    BlockState state = chunk.getBlockState(actualPosition);
//                    Block block = state.getBlock();
//                    if(block.getName().getContents().toLowerCase(Locale.ROOT).contains("ore")){
//
//                    }
                    if(state.is(Tags.Blocks.ORES)){
                        ores.add(new OreMining(state, actualPosition));
                        //tempStack.add(new ItemStack(state.getBlock().asItem()));
                        //Utils.debug("adding ore to list", state.getBlock(), actualPosition);
                    }
//                    if (OreGeneration.MINERABLE_ORES.contains(Item.byBlock(state.getBlock())))
//                    {
//                        ores.add(new OreMining(state, actualPosition));
//                    }
                }
            }
        }
        size = ores.size();
        depleted = (size == 0);
        sync();
    }

    private void doAnimation()
    {
        if (!level.isClientSide)
        {
            if (running)
            {
                if (waterTank.getFluidAmount() > 0 && particleTick >= 10)
                {
                    particleTick = 0;
                    spawnFluidParticle(worldPosition.getX() + 0.5f, worldPosition.getY() - 1f, worldPosition.getZ() + 0.5f, Blocks.STONE);
                    spawnFluidParticle(worldPosition.getX() + 0.5f, worldPosition.getY() - 1f, worldPosition.getZ() + 0.5f, waterTank.getFluid().getFluid().defaultFluidState().createLegacyBlock().getBlock());
                }
                particleTick++;
            }
        } else
        {
            if (running)
            {
                rotation += 20f;
                if (rotation >= 360) rotation = 0;

                ySlide = Utils.lerp(ySlide, revert ? 0 : -1, 0.01f);
                if (ySlide >= -0.01 || ySlide <= -0.9) revert = !revert;
            } else
            {
                rotation = 0f;
                ySlide = Utils.lerp(ySlide, -1, 0.05f);
            }
        }
    }


    @Override
    public boolean instanceOf(TileEntity tileEntity)
    {
        return tileEntity instanceof TileEntityMiner;
    }

    private void spawnFluidParticle(double x, double y, double z, Block block)
    {
        float f = (float) MathHelper.ceil(1.0F);
        double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
        int i = (int) (150.0D * d0);
        //((ServerWorld) world).addParticle(IParticleData., x, y, z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, Block.getStateId(block.getDefaultState()));
    }

    private boolean canRun()
    {
        Direction facing = getMasterFacing();
        BlockPos posPort = worldPosition.relative(facing.getCounterClockWise()).relative(facing.getOpposite()).below();
        //Utils.debug("can run port pos", posPort, level.hasSignal(posPort, facing.getOpposite()));
        return (level.hasSignal(posPort, facing.getOpposite()))
                && energyStorage.orElse(null).getEnergyStored() >= energyPerTick
                && !drillInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    public boolean isRunning()
    {
        return running;
    }

    public void dropAllItems()
    {
        Utils.dropInventoryItems(level, worldPosition, drillInv.orElse(null));
    }

    public String getWaterText(int line)
    {
        //if (line == 1) return I18n.get("render.industrialrenewal.fluid") + ":";
        if (line == 1) return "Coolant: ";
        return Blocks.WATER.getName().getString();
    }

    public String getEnergyText(int line)
    {
        //if (line == 1) return I18n.get("render.industrialrenewal.energy") + ":";
        if (line == 1) return "Energy: ";
        return energyStorage.orElse(null).getEnergyStored() + " FE";
    }


    public String getHeatText()
    {
        //String name = I18n.get("render.industrialrenewal.drillheat") + ": ";
        String name = "Drill Heat: ";
        return name + (int) Utils.getConvertedTemperature(drillHeat / 100F) + Utils.getTemperatureUnit();
    }


    public float getWaterFill() //0 ~ 180
    {
        //Utils.debug("currentAmount", getMaster().waterTank, waterTank);
        float currentAmount = waterTank.getFluidAmount() / 1000F;
        float totalCapacity = waterTank.getCapacity() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public float getEnergyFill() //0 ~ 180
    {
        float currentAmount = energyStorage.orElse(null).getEnergyStored() / 1000F;
        float totalCapacity = energyStorage.orElse(null).getMaxEnergyStored() / 1000F;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount;
    }

    public float getHeatFill() //0 ~ 180
    {
        float currentAmount = drillHeat;
        float totalCapacity = maxHeat;
        currentAmount = currentAmount / totalCapacity;
        return currentAmount * 180f;
    }

    public String[] getScreenTexts()
    {
        //if (energyContainer.getEnergyStored() <= 0) return EMPTY_ARRAY;
        List<String> texts = new ArrayList<>();
        texts.add("Mining Drill Status: " + (running ? "Running" : TextFormatting.RED + " Stoped"));
        texts.add("Mining Drill Mode: " + TextFormatting.BLUE + (isDeepMine ? "Deep Mine" : "Surface Mine"));
        texts.add("Vein Size: " + (depleted ? "Depleted" :size));
        //Utils.debug("displayed size", ores.size(), isMaster(), worldPosition);
        texts.add("Consumption: " + (isDeepMine() ? deepEnergyPerTick : energyPerTick) + " FE/t");
        texts.add(getHeatText());
        int maxD = getDrill().getMaxDamage();
        texts.add("Drill condition: " + (maxD - getDrill().getDamageValue()) + "/" + maxD);

        String[] itemsArray = new String[texts.size()];
        itemsArray = texts.toArray(itemsArray);
        return itemsArray;
    }

    public boolean hasDrill()
    {
        return !drillInv.orElse(null).getStackInSlot(0).isEmpty();
    }

    public ItemStack getDrill()
    {
        return drillInv.orElse(null).getStackInSlot(0);
    }

    public float getRotation()
    {
        return -rotation;
    }

    public float getSlide()
    {
        return ySlide;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        TileEntityMiner masterTE = this.getMaster();
        if (masterTE == null) return super.getCapability(capability, facing);
        Direction face = masterTE.getMasterFacing();

        if (facing == face && this.worldPosition.equals(masterTE.getBlockPos().below().relative(face).relative(face.getClockWise())) && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> masterTE.waterTank).cast();
        if (facing == Direction.UP && this.worldPosition.equals(masterTE.getBlockPos().relative(face.getOpposite()).above()) && capability == CapabilityEnergy.ENERGY)
            return masterTE.energyStorage.cast();
        return super.getCapability(capability, facing);
    }

    public IItemHandler getDrillHandler()
    {
        return getMaster().drillInv.orElse(null);
    }

    public class OreMining
    {
        public final BlockState state;
        public final BlockPos pos;

        OreMining(BlockState state, BlockPos pos)
        {
            this.state = state;
            this.pos = pos;
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        CompoundNBT waterTag = new CompoundNBT();
        waterTank.writeToNBT(waterTag);
        compound.put("water", waterTag);
        drillInv.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("inv", tag);
        });
        energyStorage.ifPresent(h ->
        {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            compound.put("energy", tag);
        });
        compound.putInt("heat", drillHeat);
        compound.putInt("size", size);
        compound.putBoolean("running", running);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        CompoundNBT waterTag = compound.getCompound("water");
        this.waterTank.readFromNBT(waterTag);
        CompoundNBT invTag = compound.getCompound("inv");
        drillInv.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        energyStorage.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compound.getCompound("energy")));
        this.drillHeat = compound.getInt("heat");
        this.size = compound.getInt("size");
        this.running = compound.getBoolean("running");
        super.load(state, compound);
    }
}
