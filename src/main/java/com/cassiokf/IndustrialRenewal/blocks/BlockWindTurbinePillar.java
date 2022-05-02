package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockConnectedMultiblocks;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
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
        //boolean isBase = false;
//        if(super.getStateForPlacement(context).hasProperty(FACING)){
//            return super.getStateForPlacement(context)
//                    .setValue(FACING, context.getHorizontalDirection())
//                    .setValue(BASE, true);
//        }
        return super.getStateForPlacement(context).setValue(BASE, true);
    }

//    private boolean canConnectTo(final World worldIn, final BlockPos ownPos, final Direction neighbourDirection)
//    {
//        final BlockPos neighbourPos = ownPos.relative(neighbourDirection);
//        final BlockState neighbourState = worldIn.getBlockState(neighbourPos);
//
//        if (neighbourDirection == Direction.DOWN)
//        {
//            return !(neighbourState.getBlock() instanceof BlockWindTurbinePillar);
//        }
//        TileEntity te = worldIn.getBlockEntity(ownPos.relative(neighbourDirection));
//        return te != null && te.getCapability(CapabilityEnergy.ENERGY, neighbourDirection.getOpposite()).isPresent();
//    }

//    @Override
//    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
//        return BlockRenderType.MODEL;
//        //return super.getRenderShape(p_149645_1_);
//    }

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
//
//    @Override
//    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
//        if(!world.isClientSide()){
//            if(world.getBlockEntity(pos.below()) instanceof TileEntityWindTurbinePillar){
//                world.getBlockState(pos).setValue(BASE, false);
//            }
//            else{
//                world.getBlockState(pos).setValue(BASE, true);
//            }
//        }
//    }

//    @Override
//    public void onRemove(BlockState blockState, World world, BlockPos pos, BlockState state, boolean flag) {
//        //worldIn.sendBlockUpdated(pos, state, state, 2);
//        //industrialrenewal.LOGGER.info("ONREMOVE");
//        if(!world.isClientSide){
//            if(world.getBlockState(pos.above()).getBlock() instanceof BlockWindTurbinePillar){
//                if(!world.getBlockState(pos.above()).getValue(BASE)){
//                    world.setBlock(pos.above(), world.getBlockState(pos.above()).setValue(BASE, true), 3);
//                }
//            }
//        }
//        super.onRemove(blockState, world, pos, state, flag);
//    }
//
//    @Override
//    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack) {
//        if(!world.isClientSide){
//            if(world.getBlockState(pos.above()).getBlock() instanceof BlockWindTurbinePillar){
//                if(world.getBlockState(pos.above()).getValue(BASE)){
//                    world.setBlock(pos.above(), world.getBlockState(pos.above()).setValue(BASE, false), 3);
//                }
//            }
//        }
//        super.setPlacedBy(world, pos, state, player, itemStack);
//    }

//    @Override
//    public void onPlace(BlockState blockState, World worldIn, BlockPos pos, BlockState state, boolean flag) {
//        if(!worldIn.isClientSide){
//            if(worldIn.getBlockState(pos.above()).getBlock() instanceof BlockWindTurbinePillar){
//                worldIn.getBlockState(pos.above()).setValue(BASE, false);
//                industrialrenewal.LOGGER.info("SETTING BLOCK ABOVE NOT BASE");
//            }
//        }
//
//        super.onPlace(blockState, worldIn, pos, state, flag);
//    }


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
