package com.cassiokf.industrialrenewal.blocks.transport;

import com.cassiokf.industrialrenewal.blockentity.abstracts.BlockEntityEnergyCable;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockPipeBase;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.enums.EnumCableIn;
import com.cassiokf.industrialrenewal.util.enums.EnumEnergyCableType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEnergyCable extends BlockPipeBase<BlockEntityEnergyCable> implements EntityBlock {

    public EnumEnergyCableType type;

    public BlockEnergyCable(EnumEnergyCableType type){
        super(BlockBehaviour.Properties.of(Material.METAL).strength(0.8f)
                .sound(SoundType.METAL).noOcclusion(), 4, 4);
        this.type = type;
    }

    public static EnumCableIn convertFromType(EnumEnergyCableType type)
    {
        switch (type)
        {
            default:
            case LV:
                return EnumCableIn.LV;
            case MV:
                return EnumCableIn.MV;
            case HV:
                return EnumCableIn.HV;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
        int amount;
        switch (type)
        {
            default:
            case LV:
                amount = 256;
                break;
            case MV:
                amount = 1024;
                break;
            case HV:
                amount = 4096;
                break;
        }
        tooltip.add(new TextComponent(amount + " FE/t"));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                if(canConnectTo(worldIn, pos, direction)){
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), true), 3);
                }
                else{
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), false), 3);
                }
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getState(context.getLevel(), context.getClickedPos(), defaultBlockState());

    }

    @Override
    public boolean canConnectTo(BlockGetter world, BlockPos pos, Direction facing) {
        BlockEntity te = world.getBlockEntity(pos.relative(facing));
        return (te != null && te.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).isPresent());
    }

    public BlockState getState(Level world, BlockPos pos, BlockState oldState){
        return oldState
                .setValue(UP, canConnectTo(world, pos, Direction.UP))
                .setValue(DOWN, canConnectTo(world, pos, Direction.DOWN))
                .setValue(NORTH, canConnectTo(world, pos, Direction.NORTH))
                .setValue(SOUTH, canConnectTo(world, pos, Direction.SOUTH))
                .setValue(EAST, canConnectTo(world, pos, Direction.EAST))
                .setValue(WEST, canConnectTo(world, pos, Direction.WEST));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        switch (type){
            case LV : return ModBlockEntity.ENERGYCABLE_LV_TILE.get().create(pos, state);
            case MV : return ModBlockEntity.ENERGYCABLE_MV_TILE.get().create(pos, state);
            case HV : return ModBlockEntity.ENERGYCABLE_HV_TILE.get().create(pos, state);
        }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return level.isClientSide? null : ($0, $1, $2, blockEntity) -> ((BlockEntityEnergyCable)blockEntity).tick();
    }
}
