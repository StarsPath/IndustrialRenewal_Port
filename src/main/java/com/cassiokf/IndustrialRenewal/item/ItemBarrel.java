package com.cassiokf.IndustrialRenewal.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBarrel extends BlockItem {
    public ItemBarrel(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        CompoundNBT nbt = stack.getTagElement("BlockEntityTag");
        CompoundNBT nbt2 = null;
        String fluidName;
        int amount;

        if(nbt != null)
            nbt2 = nbt.getCompound("fluid");
        if(nbt2 != null){
            fluidName = nbt2.getString("FluidName");
            amount = nbt2.getInt("Amount");
            list.add(new StringTextComponent("Fluid: " + fluidName + " " + amount + "mb"));
        }
        super.appendHoverText(stack, world, list, flag);
    }
}
