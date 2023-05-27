package com.cassiokf.industrialrenewal.blocks;

import com.cassiokf.industrialrenewal.blockentity.BlockEntitySteamTurbine;
import com.cassiokf.industrialrenewal.blockentity.abstracts.MultiBlockEntityDummy;
import com.cassiokf.industrialrenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.industrialrenewal.blocks.abstracts.MultiBlock3x3x3Base;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockSteamTurbine extends MultiBlock3x3x3Base implements EntityBlock {
    public BlockSteamTurbine(BlockBehaviour.Properties properties) {
        super(properties);
    }

//    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
//        //TODO: add to config
//        tooltip.add(new StringTextComponent(
//                I18n.get("info.industrialrenewal.requires")
//                        + ": "
//                        + "Steam"
//                        + " "
//                        + 250//(IRConfig.Main.steamTurbineSteamPerTick.get().toString())
//                        + " mB/t"));
//        tooltip.add(new StringTextComponent(
//                I18n.get("info.industrialrenewal.produces")
//                        + ": "
//                        + 512//(IRConfig.Main.steamTurbineEnergyPerTick.get().toString())
//                        + " FE/t"));
//        super.appendHoverText(stack, worldIn, tooltip, flagIn);
//    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if(state.getValue(MASTER)) {
            return ModBlockEntity.STEAM_TURBINE_TILE.get().create(pos, state);
        }
        return new MultiBlockEntityDummy(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState state, BlockEntityType<T> p_153214_) {
        if(state.getValue(MASTER)) {
            return ($0, $1, $2, blockEntity) -> ((BlockEntitySteamTurbine) blockEntity).tick();
        }
        return null;
    }
}
