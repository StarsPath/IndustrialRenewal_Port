package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockConnectedMultiblocks;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityWindTurbinePillar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWindTurbinePillar extends BlockConnectedMultiblocks<TileEntityWindTurbinePillar> {
    public static final BooleanProperty BASE = BooleanProperty.create("base");

    public BlockWindTurbinePillar(Properties props) {
        super(props);
        registerDefaultState(defaultBlockState().setValue(BASE, false));
    }

    public BlockWindTurbinePillar() {
        super(Properties.of(Material.METAL).strength(0.8f).noOcclusion());
        registerDefaultState(defaultBlockState().setValue(BASE, false));
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        Item playerItem = player.getMainHandItem().getItem();
        Block clickedBlock = state.getBlock();
        //industrialrenewal.LOGGER.info("TRYING TO PLACE PILLAR ON TOP " + playerItem + Item.byBlock(ModBlocks.TURBINE_PILLAR.get()));
        if (playerItem.equals(Item.byBlock(ModBlocks.TURBINE_PILLAR.get())) && clickedBlock.equals(ModBlocks.TURBINE_PILLAR.get()))
        {
            //industrialrenewal.LOGGER.info("READING PILLAR HEIGHT");
            int n = 1;
            while (world.getBlockState(pos.above(n)).getBlock() instanceof BlockWindTurbinePillar)
            {
                n++;
            }

            if (world.getBlockState(pos.above(n)).getMaterial().isReplaceable())
            {
                //industrialrenewal.LOGGER.info("PLACED");
                world.setBlock(pos.above(n), Block.byItem(playerItem).defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(BASE, false), 3);
                if (!player.isCreative())
                {
                    player.getMainHandItem().shrink(1);
                    //player.inventory.clearMatchingItems(playerItem, 0, 1, null);
                }
                return ActionResultType.SUCCESS;
            }
            //industrialrenewal.LOGGER.info("PLACEMENT FAILED");
        }
        return ActionResultType.FAIL;
        //return super.use(state, world, pos, player, hand, hit);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, BASE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World level = context.getLevel();
        //Utils.debug("Block Below", level.getBlockState(pos).getBlock(), level.getBlockState(pos).getBlock().is(ModBlocks.TURBINE_PILLAR.get()));
        if(level.getBlockState(pos.below()).getBlock().is(ModBlocks.TURBINE_PILLAR.get()))
            return super.getStateForPlacement(context).setValue(BASE, false);
        return super.getStateForPlacement(context).setValue(BASE, true);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0f;
        //return super.getShadeBrightness(p_220080_1_, p_220080_2_, p_220080_3_);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean flag) {
        if(!worldIn.isClientSide){
            if(worldIn.getBlockEntity(pos.below()) instanceof TileEntityWindTurbinePillar){
                //worldIn.getBlockState(pos).setValue(BASE, false);
                worldIn.setBlock(pos, state.setValue(BASE, false), 3);
            }
            else{
                //worldIn.getBlockState(pos).setValue(BASE, true);
                worldIn.setBlock(pos, state.setValue(BASE, true), 3);
            }
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, flag);
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
        //return super.hasTileEntity(state);
    }

    @Nullable
    @Override
    public TileEntityWindTurbinePillar createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityWindTurbinePillar();
    }
}
