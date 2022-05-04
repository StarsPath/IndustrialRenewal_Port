package com.cassiokf.IndustrialRenewal.blocks.pipes;

import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.tileentity.abstracts.TETubeBase;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityEnergyCable;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityEnergyCableHV;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityEnergyCableLV;
import com.cassiokf.IndustrialRenewal.tileentity.tubes.TileEntityEnergyCableMV;
import com.cassiokf.IndustrialRenewal.util.enums.EnumCableIn;
import com.cassiokf.IndustrialRenewal.util.enums.EnumEnergyCableType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEnergyCable extends BlockPipeBase<TileEntityEnergyCable>{

    public EnumEnergyCableType type;

    public BlockEnergyCable(EnumEnergyCableType type, Properties props){
        super(props, 4, 4);
        registerDefaultState(this.defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
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

//    public Block getBlockFromType()
//    {
//        switch (type)
//        {
//            default:
//            case LV:
//                return ModBlocks.ENERGYCABLE_LV.get();
//            case MV:
//                return ModBlocks.ENERGYCABLE_MV.get();
//            case HV:
//                return ModBlocks.ENERGYCABLE_HV.get();
//        }
//    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
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
        tooltip.add(new StringTextComponent(amount + " FE/t"));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.isClientSide){
            for(Direction direction : Direction.values()){
                if(canConnectTo(worldIn, pos, direction)){
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), true), Constants.BlockFlags.DEFAULT);
                }
                else{
                    worldIn.setBlock(pos, worldIn.getBlockState(pos).setValue(directionToBooleanProp(direction), false), Constants.BlockFlags.DEFAULT);
                }
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getState(context.getLevel(), context.getClickedPos(), defaultBlockState());
    }

    @Override
    public boolean canConnectTo(IBlockReader world, BlockPos pos, Direction facing) {
        TileEntity te = world.getBlockEntity(pos.relative(facing));
        return (te != null && te.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).isPresent());
    }

    public BlockState getState(World world, BlockPos pos, BlockState oldState){
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
    public TileEntityEnergyCable createTileEntity(BlockState state, IBlockReader world) {
        switch (type)
        {
            default:
            case LV:
                return new TileEntityEnergyCableLV();
            case MV:
                return new TileEntityEnergyCableMV();
            case HV:
                return new TileEntityEnergyCableHV();
        }
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }


}
