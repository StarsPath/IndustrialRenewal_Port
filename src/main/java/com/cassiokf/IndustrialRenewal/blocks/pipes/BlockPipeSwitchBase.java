package com.cassiokf.IndustrialRenewal.blocks.pipes;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockBase;
import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockTileEntity;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.item.ItemPowerScrewDrive;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockPipeSwitchBase extends BlockBase {
    public static BooleanProperty ON_OFF = BooleanProperty.create("on_off");
    public static DirectionProperty FACING = BlockStateProperties.FACING;
    public static IntegerProperty HANDLE_FACING = IntegerProperty.create("handle_facing", 0, 3);

    public BlockPipeSwitchBase(Block.Properties property) {
        super(property);
        registerDefaultState(defaultBlockState().setValue(ON_OFF, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ON_OFF, FACING, HANDLE_FACING);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if(worldIn.isClientSide())
            return super.use(state, worldIn, pos, player, handIn, p_225533_6_);

        else{
            if(handIn == Hand.MAIN_HAND && player.getMainHandItem().isEmpty()){
                worldIn.setBlockAndUpdate(pos, state.setValue(ON_OFF, !state.getValue(ON_OFF)));
                return ActionResultType.PASS;
            }
//            if(handIn == Hand.MAIN_HAND && player.getMainHandItem().getItem() instanceof ItemPowerScrewDrive && player.isCrouching()){
//                state = state.cycle(FACING);
//                worldIn.setBlockAndUpdate(pos, state);
//                return ActionResultType.PASS;
//            }
            if(handIn == Hand.MAIN_HAND && player.getMainHandItem().getItem() instanceof ItemPowerScrewDrive){
                state = state.cycle(HANDLE_FACING);
                worldIn.setBlockAndUpdate(pos, state);
                return ActionResultType.PASS;
            }
        }
        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context)
                .setValue(FACING, context.getNearestLookingDirection())
                .setValue(ON_OFF, false);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }
}
