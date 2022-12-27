package com.cassiokf.industrialrenewal.blocks.locomotion;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbineHead;
import com.cassiokf.industrialrenewal.blockentity.BlockEntityWindTurbinePillar;
import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityCargoLoader;
import com.cassiokf.industrialrenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockCargoLoader extends BlockAbstractHorizontalFacing implements EntityBlock {

    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public static final BooleanProperty HAND = BooleanProperty.create("hand");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    protected static final VoxelShape TOP_AABB = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public BlockCargoLoader(Properties properties) {
        super(properties);
    }

    public BlockCargoLoader() {
        super(Block.Properties.of(Material.METAL).noOcclusion());
        registerDefaultState(defaultBlockState().setValue(MASTER, false).setValue(POWERED, false).setValue(HAND, false));
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
        return state.getValue(POWERED)? 15 : 0;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult rayTraceResult) {

        if(!world.isClientSide)
        {
            BlockEntityCargoLoader loaderMaster = ((BlockEntityCargoLoader) world.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = loaderMaster.getBlockPos();
//            INamedContainerProvider containerProvider = createContainerProvider(world, loaderMaster.getBlockPos());
            if(loaderMaster != null) {
                NetworkHooks.openGui(((ServerPlayer)playerEntity), loaderMaster, masterPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
        //return super.use(state, world, pos, playerEntity, hand, rayTraceResult);
    }

//    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
//        return  new INamedContainerProvider() {
//            @Override
//            public ITextComponent getDisplayName() {
//                return new TranslationTextComponent("Cargo Loader");
//            }
//
//            @Nullable
//            @Override
//            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
//                TileEntity te = world.getBlockEntity(pos);
//                TileEntityCargoLoader teMaster = te instanceof TileEntityCargoLoader? ((TileEntityCargoLoader) te).getMaster() : null;
//                return new CargoLoaderContainer(i, playerInventory, teMaster);
//            }
//        };
//    }

    public static BlockPos getMasterPos(LevelAccessor world, BlockPos pos, Direction facing)
    {
        for (int y = -2; y < 3; y++)
        {
            BlockPos newPos = pos.above(y);
            BlockPos newPosFront = pos.relative(facing).above(y);
            BlockPos newPosBack = pos.relative(facing.getOpposite()).above(y);
            if (world.getBlockState(newPos).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPos).getValue(MASTER)) return newPos;
            }
            if (world.getBlockState(newPosFront).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosFront).getValue(MASTER)) return newPosFront;
            }
            if (world.getBlockState(newPosBack).getBlock() instanceof BlockCargoLoader)
            {
                if (world.getBlockState(newPosBack).getValue(MASTER)) return newPosBack;
            }
        }
        return null;
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        if (state.getValue(MASTER))
        {
            for (BlockPos pos1 : getBlocks(pos, facing))
            {
                world.removeBlock(pos1, true);
            }
        }
        else
        {
            BlockPos masterPos = getMasterPos(world, pos, facing);
            if (masterPos != null)
            {
                world.removeBlock(masterPos, true);
                for (BlockPos pos1 : getBlocks(masterPos, facing))
                {
                    if (pos1 != pos)
                        world.removeBlock(pos1, true);
                }
            }
        }
        popResource((Level) world, pos, new ItemStack(this.asItem()));
        super.destroy(world, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BlockEntityCargoLoader) {
                ((BlockEntityCargoLoader)blockentity).dropContents();
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, flag);
        }
    }

    private Set<BlockPos> getBlocks(BlockPos posMaster, Direction facing)
    {
        Set<BlockPos> positions = new HashSet<>();
        positions.add(posMaster);
        positions.add(posMaster.below());
        positions.add(posMaster.below().relative(facing));
        positions.add(posMaster.above());
        positions.add(posMaster.above().relative(facing));
        return positions;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if(state.getValue(HAND))
            return TOP_AABB;
        return super.getShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if(state.getValue(HAND))
            return TOP_AABB;
        return super.getCollisionShape(state, world, pos, context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if(canPlaceBlockAt(context.getLevel(), context.getClickedPos(), context.getHorizontalDirection()))
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(MASTER, false);
        return null;
        //return super.getStateForPlacement(context);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, BlockGetter p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER, POWERED, HAND);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        Set<BlockPos> list = getBlocks(pos.above(), state.getValue(FACING));
        for(BlockPos pos1 : list){
            world.setBlockAndUpdate(pos1, state);
        }
        world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
        world.setBlockAndUpdate(pos.above(2).relative(state.getValue(FACING)), state.setValue(HAND, true));
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }

    public boolean canPlaceBlockAt(Level worldIn, BlockPos pos, Direction facing)
    {
        Set<BlockPos> list = getBlocks(pos.above(), facing);
        for(BlockPos blockPos : list){
            if(!worldIn.getBlockState(blockPos).getMaterial().isReplaceable())
                return false;
        }
        return true;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.CARGO_LOADER.get().create(pos, state);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityCargoLoader)blockEntity).tick();
    }

//    @Override
//    public boolean hasTileEntity(BlockState state) {
//        return true;
//    }
//
//    @Nullable
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
//        return new TileEntityCargoLoader();
//    }
}
