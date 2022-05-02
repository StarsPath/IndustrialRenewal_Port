package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityBatteryBank;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBatteryBank extends BlockAbstractHorizontalFacing {

    public BlockBatteryBank() {
        super(Properties.of(Material.METAL));
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public BlockBatteryBank(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isClientSide())
            return super.use(state, worldIn, pos, player, handIn, hit);

        if(handIn == Hand.MAIN_HAND){
            Direction facehit = hit.getDirection();
//            BooleanProperty blockfacehit = OUTPUT[Utils.directionToInt(facehit)];
//            boolean currentValue = state.getValue(blockfacehit);
//            worldIn.setBlock(pos, state.setValue(blockfacehit, !currentValue), 3);
            TileEntityBatteryBank batteryBank = (TileEntityBatteryBank)worldIn.getBlockEntity(pos);
            batteryBank.toggleFacing(facehit);
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

//    @Nullable
//    @Override
//    public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        return new Direction[0];
//    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> list, ITooltipFlag tooltipFlag) {
        super.appendHoverText(stack, world, list, tooltipFlag);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityBatteryBank();
    }
}
