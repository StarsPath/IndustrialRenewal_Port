package com.cassiokf.industrialrenewal.blocks.locomotion;

import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityCargoLoader;
import com.cassiokf.industrialrenewal.blockentity.locomotion.BlockEntityFluidLoader;
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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockFluidLoader extends BlockAbstractHorizontalFacing implements EntityBlock {
    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockFluidLoader(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(MASTER, false).setValue(POWERED, false));
    }

    public BlockFluidLoader() {
        super(Block.Properties.of(Material.METAL));
        registerDefaultState(defaultBlockState().setValue(MASTER, false).setValue(POWERED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult rayTraceResult) {
        if(!world.isClientSide){
            BlockEntityFluidLoader loaderMaster = ((BlockEntityFluidLoader) world.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = loaderMaster.getBlockPos();

            IFluidHandler fluidHandler = loaderMaster.getFluidHandler().orElse(null);
            if(fluidHandler != null) {
                boolean acceptFluid = FluidUtil.interactWithFluidHandler(playerEntity, hand, fluidHandler);
//            Utils.debug("accept Fluid", acceptFluid);
                if (acceptFluid)
                    return InteractionResult.SUCCESS;
            }
            if(loaderMaster != null){
                NetworkHooks.openGui((ServerPlayer) playerEntity, loaderMaster, masterPos);
            }
            else{
                throw new IllegalStateException("Our Container provider is missing!");
            }
//            INamedContainerProvider containerProvider = createContainerProvider(world, loaderMaster.getBlockPos());
        }
        return InteractionResult.SUCCESS;
    }

//    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
//        return  new INamedContainerProvider() {
//            @Override
//            public ITextComponent getDisplayName() {
//                return new TranslationTextComponent("Fluid Loader");
//            }
//
//            @Nullable
//            @Override
//            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
//                TileEntity te = world.getBlockEntity(pos);
//                TileEntityFluidLoader teMaster = te instanceof TileEntityFluidLoader? ((TileEntityFluidLoader) te).getMaster() : null;
//                return new FluidLoaderContainer(i, playerInventory, teMaster);
//            }
//        };
//    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(world.getBlockState(pos.above()).getMaterial().isReplaceable())
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection())
                    .setValue(MASTER, false).setValue(POWERED, false);
            //return super.getStateForPlacement(context);
        return null;
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        if(state.getValue(MASTER))
            world.removeBlock(pos.above(), true);
        else
            world.removeBlock(pos.below(), true);
        popResource((Level) world, pos, new ItemStack(this.asItem()));
        super.destroy(world, pos, state);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        world.setBlock(pos.above(), state, 3);
        world.setBlock(pos, state.setValue(MASTER, true), 3);
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER, POWERED);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, BlockGetter p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.FLUID_LOADER.get().create(pos, state);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return ($0, $1, $2, blockEntity) -> ((BlockEntityFluidLoader)blockEntity).tick();
    }
}
