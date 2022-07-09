package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModItems;
import com.cassiokf.IndustrialRenewal.item.ItemPowerScrewDrive;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityConveyor;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityConveyorHopper;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityConveyorInserter;
import com.cassiokf.IndustrialRenewal.util.Utils;
import com.cassiokf.IndustrialRenewal.util.enums.EnumConveyorType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockConveyor extends BlockAbstractHorizontalFacing {

    public static final IntegerProperty MODE = IntegerProperty.create("mode", 0, 2);
    public static final BooleanProperty FRONT = BooleanProperty.create("front");
    public static final BooleanProperty BACK = BooleanProperty.create("back");

    protected static final VoxelShape BASE_AABB = Block.box(0, 0, 0, 16, 8, 16);
    protected static final VoxelShape NORTH_AABB = Block.box(0, 8, 0, 16, 16, 8);
    protected static final VoxelShape SOUTH_AABB = Block.box(0, 8, 8, 16, 16, 16);
    protected static final VoxelShape WEST_AABB = Block.box(0, 8, 0, 8, 16, 16);
    protected static final VoxelShape EAST_AABB = Block.box(8, 8, 0, 16, 16, 16);
    public final EnumConveyorType type;

    public BlockConveyor(EnumConveyorType type) {
        super(AbstractBlock.Properties.of(Material.METAL));
        this.type = type;
        registerDefaultState(defaultBlockState());
    }

    public static double getMotionX(Direction facing)
    {
        return facing == Direction.EAST ? 0.2 : -0.2;
    }

    public static double getMotionZ(Direction facing)
    {
        return facing == Direction.SOUTH ? 0.2 : -0.2;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MODE, FRONT, BACK);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1.0f;
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean p_220069_6_) {
        if(!world.isClientSide){
            int mode = getMode(world, pos, state);
            world.setBlock(pos, state.setValue(MODE, mode).setValue(FRONT, getFront(world, pos, state, mode)).setValue(BACK, getBack(world, pos, state, mode)), 3);
        }
        super.neighborChanged(state, world, pos, block, neighborPos, p_220069_6_);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!heldItem.isEmpty() && handIn.equals(Hand.MAIN_HAND))
        {
            if (type.equals(EnumConveyorType.NORMAL))
            {
                if (heldItem.getItem().equals(Blocks.HOPPER.asItem()))
                {
                    Direction facing1 = state.getValue(FACING);
                    worldIn.setBlock(pos, ModBlocks.CONVEYOR_HOPPER.get().defaultBlockState().setValue(FACING, facing1).setValue(FRONT, getFront(worldIn, pos, state, 0)).setValue(BACK, getBack(worldIn, pos, state, 0)), 3);
                    worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return ActionResultType.SUCCESS;
                }
                if (heldItem.getItem().equals(Blocks.DISPENSER.asItem()))
                {
                    Direction facing1 = state.getValue(FACING);
                    worldIn.setBlock(pos, ModBlocks.CONVEYOR_INSERTER.get().defaultBlockState().setValue(FACING, facing1).setValue(FRONT, getFront(worldIn, pos, state, 0)).setValue(BACK, getBack(worldIn, pos, state, 0)), 3);
                    worldIn.playSound(null, pos, SoundEvents.METAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    if (!player.isCreative()) heldItem.shrink(1);
                    return ActionResultType.SUCCESS;
                }
            } else if (heldItem.getItem().equals(ModItems.screwDrive.getItem()))
            {
                if (type.equals(EnumConveyorType.HOPPER))
                {
                    Direction facing1 = state.getValue(FACING);
                    worldIn.setBlock(pos, ModBlocks.CONVEYOR.get().defaultBlockState().setValue(FACING, facing1), 3);
//                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    return ActionResultType.SUCCESS;
                }
                if (type.equals(EnumConveyorType.INSERTER))
                {
                    Direction facing1 = state.getValue(FACING);
                    worldIn.setBlock(pos, ModBlocks.CONVEYOR.get().defaultBlockState().setValue(FACING, facing1), 3);
//                    ItemPowerScrewDrive.playDrillSound(worldIn, pos);
                    return ActionResultType.SUCCESS;
                }
            } else if (Block.byItem(heldItem.getItem()) instanceof BlockConveyor)
            {
                Direction face = state.getValue(FACING);
                int mode = state.getValue(MODE);
                if (mode == 2 && worldIn.getBlockState(pos.relative(face).below()).getMaterial().isReplaceable())
                {
                    if (!worldIn.isClientSide)
                    {
                        worldIn.setBlock(pos.relative(face).below(), state, 3);
                        if (!player.isCreative()) heldItem.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                if (worldIn.getBlockState(pos.relative(face)).getMaterial().isReplaceable())
                {
                    if (!worldIn.isClientSide)
                    {
                        worldIn.setBlock(pos.relative(face), state, 3);
                        if (!player.isCreative()) heldItem.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        BlockState stateIn = defaultBlockState();
        int mode = getMode(context.getLevel(), context.getClickedPos(), stateIn);
        boolean front = getFront(context.getLevel(), context.getClickedPos(), stateIn, mode);
        boolean back = getBack(context.getLevel(), context.getClickedPos(), stateIn, mode);

        return stateIn.setValue(FACING, context.getHorizontalDirection())
                .setValue(MODE, mode).setValue(FRONT, front).setValue(BACK, back);
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);

        BlockPos updatePos = pos.above();
        BlockPos updatePos2 = pos.below();
        ((World)world).sendBlockUpdated(updatePos, world.getBlockState(updatePos), world.getBlockState(updatePos), 3);
        ((World)world).sendBlockUpdated(updatePos2, world.getBlockState(updatePos2), world.getBlockState(updatePos2), 3);

        ((World)world).updateNeighborsAt(updatePos, world.getBlockState(updatePos).getBlock());
        ((World)world).updateNeighborsAt(updatePos2, world.getBlockState(updatePos2).getBlock());
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);

        BlockPos updatePos = pos.above();
        BlockPos updatePos2 = pos.below();
        world.sendBlockUpdated(updatePos, world.getBlockState(updatePos), world.getBlockState(updatePos), 3);
        world.sendBlockUpdated(updatePos2, world.getBlockState(updatePos2), world.getBlockState(updatePos2), 3);

        world.updateNeighborsAt(updatePos, world.getBlockState(updatePos).getBlock());
        world.updateNeighborsAt(updatePos2, world.getBlockState(updatePos2).getBlock());
    }

    private int getMode(IBlockReader world, BlockPos pos, BlockState ownState)
    {
        if (type != EnumConveyorType.NORMAL) return 0;
        Direction facing = ownState.getValue(FACING);
        BlockState frontState = world.getBlockState(pos.relative(facing));
        BlockState upState = world.getBlockState(pos.relative(facing).above());
        BlockState directUpState = world.getBlockState(pos.above());
        BlockState downState = world.getBlockState(pos.relative(facing).below());
        BlockState backUpState = world.getBlockState(pos.relative(facing.getOpposite()).above());
        BlockState backState = world.getBlockState(pos.relative(facing.getOpposite()));

        //if (frontState.getBlock() instanceof BlockBulkConveyor && frontState.get(FACING) == facing) return 0;
        if ((upState.getBlock() instanceof BlockConveyor &&
                upState.getValue(FACING).equals(facing)) &&
                !(directUpState.getBlock() instanceof BlockConveyor) &&
                !(frontState.getBlock() instanceof BlockConveyor &&
                        frontState.getValue(FACING).equals(facing)))

            return 1;
        if ((downState.getBlock() instanceof BlockConveyor && downState.getValue(FACING).equals(facing)
                && backUpState.getBlock() instanceof BlockConveyor && backUpState.getValue(FACING).equals(facing))
                || (!(backState.getBlock() instanceof BlockConveyor && backState.getValue(FACING).equals(facing))
                && (backUpState.getBlock() instanceof BlockConveyor && backUpState.getValue(FACING).equals(facing))))
            return 2;
        return 0;
    }

    private boolean getFront(IBlockReader world, BlockPos pos, BlockState ownState, final int mode)
    {
        if (type.equals(EnumConveyorType.INSERTER)) return false;

        Direction facing = ownState.getValue(FACING);
        BlockState frontState = world.getBlockState(pos.relative(facing));
        BlockState downState = world.getBlockState(pos.relative(facing).below());
        BlockState aboveState = world.getBlockState(pos.relative(facing).above());

        return !(frontState.getBlock() instanceof BlockConveyor || downState.getBlock() instanceof BlockConveyor || aboveState.getBlock() instanceof BlockConveyor);
    }

    private boolean getBack(IBlockReader world, BlockPos pos, BlockState ownState, final int mode)
    {
        Direction facing = ownState.getValue(FACING);
        BlockState backState = world.getBlockState(pos.relative(facing.getOpposite()));
        BlockState downState = world.getBlockState(pos.relative(facing.getOpposite()).below());

        if (mode == 0)
            return !(backState.getBlock() instanceof BlockConveyor && backState.getValue(FACING).equals(facing)) && !(downState.getBlock() instanceof BlockConveyor && downState.getValue(FACING).equals(facing));
        if (mode == 1)
            return !(downState.getBlock() instanceof BlockConveyor && downState.getValue(FACING).equals(facing)) && !(backState.getBlock() instanceof BlockConveyor && backState.getValue(FACING).equals(facing));
        if (mode == 2) return false;
        return false;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return getVoxelShape(state);
    }

    private VoxelShape getVoxelShape(BlockState state)
    {
        VoxelShape FINAL_SHAPE = NULL_SHAPE;
        Direction face = state.getValue(FACING);
        int mode = state.getValue(MODE);
        boolean ramp = mode == 1 || mode == 2;
        if (type == EnumConveyorType.NORMAL)
        {
            FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, BASE_AABB);
            if (ramp)
            {
                switch (face)
                {
                    case NORTH:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, NORTH_AABB);
                    case SOUTH:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, SOUTH_AABB);
                    case WEST:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, WEST_AABB);
                    case EAST:
                        FINAL_SHAPE = VoxelShapes.or(FINAL_SHAPE, EAST_AABB);
                }
            }
        } else
        {
            FINAL_SHAPE = FULL_SHAPE;
        }
        return FINAL_SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (type == EnumConveyorType.NORMAL) return new TileEntityConveyor();
        if (type == EnumConveyorType.HOPPER) return new TileEntityConveyorHopper();
        if (type == EnumConveyorType.INSERTER) return new TileEntityConveyorInserter();
//        return new TileEntityBulkConveyorInserter();
        return null;
    }
}
