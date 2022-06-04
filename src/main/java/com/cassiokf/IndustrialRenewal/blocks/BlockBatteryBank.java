package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.item.IRItemDrill;
import com.cassiokf.IndustrialRenewal.item.ItemPowerScrewDrive;
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

    public static BooleanProperty NORTH_OUTPUT = BooleanProperty.create("north_out");
    public static BooleanProperty SOUTH_OUTPUT = BooleanProperty.create("south_out");
    public static BooleanProperty EAST_OUTPUT = BooleanProperty.create("east_out");
    public static BooleanProperty WEST_OUTPUT = BooleanProperty.create("west_out");
    public static BooleanProperty UP_OUTPUT = BooleanProperty.create("up_out");
    public static BooleanProperty DOWN_OUTPUT = BooleanProperty.create("down_out");

    public BlockBatteryBank() {
        super(Properties.of(Material.METAL));
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH)
                .setValue(NORTH_OUTPUT, false)
                .setValue(SOUTH_OUTPUT, false)
                .setValue(EAST_OUTPUT, false)
                .setValue(WEST_OUTPUT, false)
                .setValue(UP_OUTPUT, false)
                .setValue(DOWN_OUTPUT, false));
    }

    public BlockBatteryBank(Properties props) {
        super(props);
        registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH)
                .setValue(NORTH_OUTPUT, false)
                .setValue(SOUTH_OUTPUT, false)
                .setValue(EAST_OUTPUT, false)
                .setValue(WEST_OUTPUT, false)
                .setValue(UP_OUTPUT, false)
                .setValue(DOWN_OUTPUT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH_OUTPUT, SOUTH_OUTPUT, EAST_OUTPUT, WEST_OUTPUT, UP_OUTPUT, DOWN_OUTPUT);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(worldIn.isClientSide())
            return super.use(state, worldIn, pos, player, handIn, hit);

        ItemStack heldItem = player.getItemInHand(handIn);
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemPowerScrewDrive){
            Direction facehit = hit.getDirection();
            TileEntityBatteryBank batteryBank = (TileEntityBatteryBank)worldIn.getBlockEntity(pos);
            batteryBank.toggleFacing(facehit);
            Utils.debug("hit with screwdriver", facehit);
            worldIn.setBlockAndUpdate(pos, state.cycle(toggleOutput(facehit)));
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    public BooleanProperty toggleOutput(Direction facing){
        switch (facing){
            case NORTH: return NORTH_OUTPUT;
            case SOUTH: return SOUTH_OUTPUT;
            case EAST: return EAST_OUTPUT;
            case WEST: return WEST_OUTPUT;
            case UP: return UP_OUTPUT;
            case DOWN: return DOWN_OUTPUT;
        }
        return NORTH_OUTPUT;
    }

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
