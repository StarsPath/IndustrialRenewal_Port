package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntitySteamTurbine;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockSteamTurbine extends Block3x3x3Base<TileEntitySteamTurbine> {
    public BlockSteamTurbine(Properties properties) {
        super(properties);
    }

    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //TODO: add to config
        tooltip.add(new StringTextComponent(
                I18n.get("info.industrialrenewal.requires")
                        + ": "
                        + "Steam"
                        + " "
                        + 250//(IRConfig.Main.steamTurbineSteamPerTick.get().toString())
                        + " mB/t"));
        tooltip.add(new StringTextComponent(
                I18n.get("info.industrialrenewal.produces")
                        + ": "
                        + 512//(IRConfig.Main.steamTurbineEnergyPerTick.get().toString())
                        + " FE/t"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public TileEntitySteamTurbine createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySteamTurbine();
    }
}
