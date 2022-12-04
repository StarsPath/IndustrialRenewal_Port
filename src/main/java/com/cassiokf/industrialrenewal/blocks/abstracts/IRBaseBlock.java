package com.cassiokf.industrialrenewal.blocks.abstracts;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IRBaseBlock extends Block {

    public static final VoxelShape NULL_SHAPE = Block.box(0, 0, 0, 0, 0, 0);
    protected static final VoxelShape FULL_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    public IRBaseBlock(){
        super(BlockBehaviour.Properties.of(Material.METAL).strength(2, 15f).sound(SoundType.METAL));
    }

    public IRBaseBlock(Properties p_49795_) {
        super(p_49795_);
    }

    protected BlockState getInitDefaultState()
    {
        BlockState state = this.stateDefinition.any();
        if(state.hasProperty(BlockStateProperties.WATERLOGGED))
            state = state.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE);
        return state;
    }
}
