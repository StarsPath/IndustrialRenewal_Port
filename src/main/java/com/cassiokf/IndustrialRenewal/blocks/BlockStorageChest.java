package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x2Base;
import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import com.cassiokf.IndustrialRenewal.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.core.jmx.Server;

import javax.annotation.Nullable;

public class BlockStorageChest extends Block3x3x2Base<TileEntityStorageChest> {

    public BlockStorageChest(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityStorageChest();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {

        if(!world.isClientSide)
        {
            TileEntityStorageChest storageChestMaster = ((TileEntityStorageChest) world.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = storageChestMaster.getBlockPos();
            INamedContainerProvider containerProvider = createContainerProvider(world, storageChestMaster.getBlockPos());
            //storageChestMaster.openGui(playerEntity, true);

            //Utils.debug("BLOCKS POSES", pos, storageChestMaster.getBlockPos());
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, containerProvider, masterPos);
//                NetworkHooks.openGui((ServerPlayerEntity) playerEntity, storageChestMaster, (PacketBuffer packerBuffer) -> {
//                    packerBuffer.writeBlockPos(storageChestMaster.getBlockPos());
//                });
        }
        return ActionResultType.SUCCESS;
        //return super.use(state, world, pos, playerEntity, hand, rayTraceResult);
    }

    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
        return  new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                TileEntity te = world.getBlockEntity(pos);
                TileEntityStorageChest teMaster = te instanceof TileEntityStorageChest? ((TileEntityStorageChest) te).getMaster() : null;
                return new StorageChestContainer(i, playerInventory, teMaster);
            }
        };
    }
}
