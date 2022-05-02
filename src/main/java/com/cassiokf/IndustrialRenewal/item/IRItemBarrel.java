package com.cassiokf.IndustrialRenewal.item;

import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityBarrel;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class IRItemBarrel extends IRBaseItem{
    public IRItemBarrel(String name) {
        super(name);
    }

    public IRItemBarrel(String name, ItemGroup group){
        super(name, group);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        //Hand hand = context.getHand();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        BlockPos posOffset = pos.offset(facing.getNormal());
        World world = context.getLevel();
        BlockState blockState = world.getBlockState(pos);

        if (world.isEmptyBlock(posOffset) || world.getBlockState(posOffset).getBlock().canBeReplaced(blockState, new BlockItemUseContext(context)))
        {
            world.playSound(playerEntity, pos, new SoundEvent(new ResourceLocation("block.metal.place")), SoundCategory.BLOCKS, 1.0f, 1.2f);

            world.setBlock(posOffset, ModBlocks.BARREL.get().getStateForPlacement(new BlockItemUseContext(context)), Constants.BlockFlags.DEFAULT);
            TileEntity te = world.getBlockEntity(posOffset);
            if (te instanceof TileEntityBarrel && itemStack.getTag() != null && itemStack.getTag().contains("FluidName"))
                ((TileEntityBarrel) te).tank.readFromNBT(itemStack.getTag());
            itemStack.shrink(1);

            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
        //return super.useOn(context);
    }
}
