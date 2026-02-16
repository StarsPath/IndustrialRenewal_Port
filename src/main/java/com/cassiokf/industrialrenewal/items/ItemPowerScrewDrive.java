package com.cassiokf.industrialrenewal.items;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import com.cassiokf.industrialrenewal.init.ModSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Properties;

public class ItemPowerScrewDrive extends IRBaseItem{

    public ItemPowerScrewDrive() {
        super(new Properties().stacksTo(1).tab(IndustrialRenewal.IR_TAB));
    }

    public ItemPowerScrewDrive(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if(level.isClientSide){
            level.playSound(null, pos, ModSound.SCREW_SOUND.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        return super.useOn(context);
    }
}
