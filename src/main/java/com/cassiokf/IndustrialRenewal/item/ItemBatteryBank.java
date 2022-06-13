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

public class ItemBatteryBank extends BlockItem {
    public ItemBatteryBank(Block p_i48527_1_, Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        CompoundNBT nbt = stack.getTagElement("BlockEntityTag");
        CompoundNBT nbt2 = null;
        int amount;

        if(nbt != null)
            nbt2 = nbt.getCompound("energy");
        if(nbt2 != null){
            amount = nbt2.getInt("energy");
            list.add(new StringTextComponent("Energy: " +  amount + "RF"));
        }
        super.appendHoverText(stack, world, list, flag);
    }
}
