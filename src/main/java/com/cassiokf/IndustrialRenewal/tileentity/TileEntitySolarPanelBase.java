package com.cassiokf.IndustrialRenewal.tileentity;

import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TEBase;
import com.cassiokf.IndustrialRenewal.util.CustomEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntitySolarPanelBase extends TEBase implements ITickableTileEntity
{
    //public final VoltsEnergyContainer energyContainer;
    private int tick;
    private final int random;
    private int energyCanGenerate;
//    private RFEnergyStorage energyStorage = createEnergy();
    private final CustomEnergyStorage container;
    private LazyOptional<CustomEnergyStorage> energy;

    public TileEntitySolarPanelBase(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
        random = ThreadLocalRandom.current().nextInt(10);
        container = new CustomEnergyStorage(0, 0, 15){
            @Override
            public boolean canReceive() {
                return false;
            }
        };
        this.energy = LazyOptional.of(()->this.container);
    }

    public TileEntitySolarPanelBase()
    {
        super(ModTileEntities.SOLAR_PANEL_BASE.get());
        random = ThreadLocalRandom.current().nextInt(10);
        container = new CustomEnergyStorage(0, 0, 15){
            @Override
            public boolean canReceive() {
                return false;
            }
        };
        this.energy = LazyOptional.of(()->this.container);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
        setChanged();
    }

    @Override
    public void tick(){
        if(level.isClientSide()){
            return;
        }

        if (tick >= (20 + random))
        {
            tick = 0;
            getEnergyFromSun();
        }
        tick++;
        //if (energyCanGenerate > 0) energyContainer.receiveInternally(energyCanGenerate, false);
//        energyStorage.setEnergy(1000);
        moveEnergyOut(energyCanGenerate, false);
    }

//    private RFEnergyStorage createEnergy(){
//        //return new EnergyStorage(15, 120);
//        return new RFEnergyStorage(1000, 150){
//            @Override
//            protected void onEnergyChanged() {
//                setChanged();
//            }
//        };
//    }

    private void moveEnergyOut(int energy, boolean simulate)
    {
        for(Direction direction : Direction.values()){
            TileEntity te = level.getBlockEntity(worldPosition.relative(direction));
            if(te == null)
                continue;

            te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(iEnergyStorage -> {
                if(iEnergyStorage.canReceive() && iEnergyStorage.getEnergyStored() < iEnergyStorage.getMaxEnergyStored()){
                    iEnergyStorage.receiveEnergy(energy, simulate);
                }
            });
        }
    }

    public static int getGeneration(World world, BlockPos pos)
    {
        int i = world.getLightEmission(pos) - world.getMaxLightLevel();
        float f = world.getSunAngle(1.0F);
        if (i > 0)
        {
            float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
            f = f + (f1 - f) * 0.2F;
            i = Math.round((float) i * MathHelper.cos(f));
        }
        i = MathHelper.clamp(i, 0, 15);
        float normalize = i / 15f;
        if (world.isRaining()) normalize = normalize / 2;
        //return Math.round(normalize * IRConfig.MainConfig.Main.baseSolarPanelMaxGeneration);
        return 15;
    }

    public void getEnergyFromSun()
    {
        energyCanGenerate = getGeneration(this.level, this.worldPosition);
    }

//    @Nullable
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        if (cap == CapabilityEnergy.ENERGY) {
//            return this.energy.cast();
//        }
//        return super.getCapability(cap, side);
//    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return capability == CapabilityEnergy.ENERGY ? this.energy.cast() : super.getCapability(capability, facing);
    }
}
