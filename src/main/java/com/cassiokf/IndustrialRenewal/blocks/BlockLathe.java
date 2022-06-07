package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x2x2Base;
import com.cassiokf.IndustrialRenewal.containers.container.LatheContainer;
import com.cassiokf.IndustrialRenewal.containers.container.StorageChestContainer;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityLathe;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityStorageChest;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
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

import javax.annotation.Nullable;

public class BlockLathe extends Block3x2x2Base<TileEntityLathe> {
    public BlockLathe(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityLathe();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {

        if(!world.isClientSide)
        {
            TileEntityLathe latheMaster = ((TileEntityLathe) world.getBlockEntity(pos)).getMaster();
            BlockPos masterPos = latheMaster.getBlockPos();
            INamedContainerProvider containerProvider = createContainerProvider(world, latheMaster.getBlockPos());
            //storageChestMaster.openGui(playerEntity, true);

            //Utils.debug("BLOCKS POSES", pos, storageChestMaster.getBlockPos());
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, containerProvider, masterPos);
        }
        return ActionResultType.SUCCESS;
        //return super.use(state, world, pos, playerEntity, hand, rayTraceResult);
    }

    private INamedContainerProvider createContainerProvider(World world, BlockPos pos) {
        return  new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Lathe");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                TileEntity te = world.getBlockEntity(pos);
                TileEntityLathe teMaster = te instanceof TileEntityLathe? ((TileEntityLathe) te).getMaster() : null;
                return new LatheContainer(i, playerInventory, teMaster);
            }
        };
    }
}
