package com.cassiokf.IndustrialRenewal.blocks.locomotion;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.containers.container.FluidLoaderContainer;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityFluidLoader;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockFluidLoader extends BlockAbstractHorizontalFacing {
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
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(!world.isClientSide){
            TileEntityFluidLoader loaderMaster = ((TileEntityFluidLoader) world.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = loaderMaster.getBlockPos();

            boolean acceptFluid = FluidUtil.interactWithFluidHandler(playerEntity, hand, loaderMaster.getFluidHandler().orElse(null));
            Utils.debug("accept Fluid", acceptFluid);
            if(acceptFluid)
                return ActionResultType.SUCCESS;

            INamedContainerProvider containerProvider = createContainerProvider(world, loaderMaster.getBlockPos());
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, containerProvider, masterPos);
        }
        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
        return  new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Fluid Loader");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                TileEntity te = world.getBlockEntity(pos);
                TileEntityFluidLoader teMaster = te instanceof TileEntityFluidLoader? ((TileEntityFluidLoader) te).getMaster() : null;
                return new FluidLoaderContainer(i, playerInventory, teMaster);
            }
        };
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(world.getBlockState(pos.above()).getMaterial().isReplaceable())
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection())
                    .setValue(MASTER, false).setValue(POWERED, false);
            //return super.getStateForPlacement(context);
        return null;
    }

    @Override
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
        if(state.getValue(MASTER))
            world.removeBlock(pos.above(), true);
        else
            world.removeBlock(pos.below(), true);
        popResource((World)world, pos, new ItemStack(this.asItem()));
        super.destroy(world, pos, state);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        world.setBlock(pos.above(), state, 3);
        world.setBlock(pos, state.setValue(MASTER, true), 3);
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
        return state.getValue(POWERED)? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER, POWERED);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
        return 1f;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityFluidLoader();
    }
}
