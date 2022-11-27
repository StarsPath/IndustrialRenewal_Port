package com.cassiokf.industrialrenewal.blocks;

import com.cassiokf.industrialrenewal.blockentity.BlockEntityStorageChest;
import com.cassiokf.industrialrenewal.blocks.abstracts.Block3x3x2Base;
import com.cassiokf.industrialrenewal.init.ModBlockEntity;
import com.cassiokf.industrialrenewal.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlockStorageChest extends Block3x3x2Base<BlockEntityStorageChest> implements EntityBlock {

    public BlockStorageChest(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hitResult) {
        if (!worldIn.isClientSide()) {
            Utils.debug("", worldIn.getBlockEntity(pos));
            BlockEntityStorageChest storageChestMaster = ((BlockEntityStorageChest) worldIn.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = storageChestMaster.getBlockPos();

            if(storageChestMaster != null) {
                NetworkHooks.openGui(((ServerPlayer)player), storageChestMaster, masterPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(worldIn.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntity.STORAGE_CHEST_TILE.get().create(pos, state);
    }

//    @Override
//    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
//
//        if(!world.isClientSide)
//        {
//            TileEntityStorageChest storageChestMaster = ((TileEntityStorageChest) world.getBlockEntity(pos)).getMaster();
//            BlockPos masterPos = storageChestMaster.getBlockPos();
//            INamedContainerProvider containerProvider = createContainerProvider(world, storageChestMaster.getBlockPos());
//            //storageChestMaster.openGui(playerEntity, true);
//
//            //Utils.debug("BLOCKS POSES", pos, storageChestMaster.getBlockPos());
//            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, containerProvider, masterPos);
//        }
//        return ActionResultType.SUCCESS;
//        //return super.use(state, world, pos, playerEntity, hand, rayTraceResult);
//    }

//    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
//        return  new INamedContainerProvider() {
//            @Override
//            public ITextComponent getDisplayName() {
//                return new TranslationTextComponent("");
//            }
//
//            @Nullable
//            @Override
//            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
//                TileEntity te = world.getBlockEntity(pos);
//                TileEntityStorageChest teMaster = te instanceof TileEntityStorageChest? ((TileEntityStorageChest) te).getMaster() : null;
//                return new StorageChestContainer(i, playerInventory, teMaster);
//            }
//        };
//    }
}
