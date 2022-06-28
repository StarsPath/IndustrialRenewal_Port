package com.cassiokf.IndustrialRenewal.blocks.locomotion;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.BlockAbstractHorizontalFacing;
import com.cassiokf.IndustrialRenewal.containers.container.CargoLoaderContainer;
import com.cassiokf.IndustrialRenewal.containers.container.LatheContainer;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityLathe;
import com.cassiokf.IndustrialRenewal.tileentity.locomotion.TileEntityCargoLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class BlockCargoLoader extends BlockAbstractHorizontalFacing {

    public static final BooleanProperty MASTER = BooleanProperty.create("master");
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BlockCargoLoader(Properties properties) {
        super(properties);
    }

    public BlockCargoLoader() {
        super(Block.Properties.of(Material.METAL));
        registerDefaultState(defaultBlockState().setValue(MASTER, false).setValue(POWERED, false));
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
        return state.getValue(POWERED)? 15 : 0;
    }

    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {

        if(!world.isClientSide)
        {
            TileEntityCargoLoader loaderMaster = ((TileEntityCargoLoader) world.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = loaderMaster.getBlockPos();
            INamedContainerProvider containerProvider = createContainerProvider(world, loaderMaster.getBlockPos());
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, containerProvider, masterPos);
        }
        return ActionResultType.SUCCESS;
        //return super.use(state, world, pos, playerEntity, hand, rayTraceResult);
    }

    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
        return  new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Cargo Loader");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                TileEntity te = world.getBlockEntity(pos);
                TileEntityCargoLoader teMaster = te instanceof TileEntityCargoLoader? ((TileEntityCargoLoader) te).getMaster() : null;
                return new CargoLoaderContainer(i, playerInventory, teMaster);
            }
        };
    }

    public static BlockPos getMasterPos(IWorld world, BlockPos pos, Direction facing)
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
    public void destroy(IWorld world, BlockPos pos, BlockState state) {
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
        popResource((World)world, pos, new ItemStack(this.asItem()));
        super.destroy(world, pos, state);
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if(canPlaceBlockAt(context.getLevel(), context.getClickedPos(), context.getHorizontalDirection()))
            return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(MASTER, false);
        return null;
        //return super.getStateForPlacement(context);
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MASTER, POWERED);
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        Set<BlockPos> list = getBlocks(pos.above(), state.getValue(FACING));
        for(BlockPos pos1 : list){
            world.setBlockAndUpdate(pos1, state);
        }
        world.setBlockAndUpdate(pos.above(), state.setValue(MASTER, true));
        super.setPlacedBy(world, pos, state, livingEntity, itemStack);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos, Direction facing)
    {
        Set<BlockPos> list = getBlocks(pos.above(), facing);
        for(BlockPos blockPos : list){
            if(!worldIn.getBlockState(blockPos).getMaterial().isReplaceable())
                return false;
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityCargoLoader();
    }
}
