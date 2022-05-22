package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityElectricPump;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPowerScrewDrive extends IRBaseItem{
    public ItemPowerScrewDrive(String name) {
        super(name);
    }
    public ItemPowerScrewDrive(String name, Properties props) {
        super(name, props.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //tooltip.add(new TranslationTextComponent("Energy: " + this.container.getEnergyStored() + " / " + this.container.getMaxEnergyStored()));
        super.appendHoverText(stack, world, tooltip, flagIn);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        World world = context.getLevel();
        TileEntity te = world.getBlockEntity(pos);

//        Utils.debug("pos, facing ", pos, facing);
//        Utils.debug("te ", te);

        return super.useOn(context);
    }
}
