package com.cassiokf.industrialrenewal.blocks.abstracts;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class IRBaseBlock extends Block {
    public IRBaseBlock(){
        super(BlockBehaviour.Properties.of(Material.METAL).strength(2, 15f).sound(SoundType.METAL));
    }

    public IRBaseBlock(Properties p_49795_) {
        super(p_49795_);
    }
}
