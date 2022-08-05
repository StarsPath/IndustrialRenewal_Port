package com.cassiokf.IndustrialRenewal.blocks.abstracts;

import com.cassiokf.IndustrialRenewal.tileentity.TileEntityBarrel;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityPortableGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockSaveContent extends HorizontalBlock {

    public BlockSaveContent(Properties props) {
        super(props);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity entity, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(!world.isClientSide()){
            TileEntity te = world.getBlockEntity(pos);
            boolean acceptFluid = FluidUtil.interactWithFluidHandler(entity, hand, world, pos, rayTraceResult.getDirection());
            if(!acceptFluid && te instanceof TileEntityBarrel){
                TileEntityBarrel barrel = (TileEntityBarrel) te;
                String message = String.format("%s: %d/%d mB", new TranslationTextComponent(barrel.getFluid()).getString(), barrel.getFluidAmount(), barrel.getMAX_CAPACITY());
                entity.sendMessage(new StringTextComponent(message), entity.getUUID());
            }
            else if(!acceptFluid && te instanceof TileEntityPortableGenerator){
                TileEntityPortableGenerator portableGenerator = (TileEntityPortableGenerator) te;
                String message = "";
                if(!portableGenerator.isGenerating()){
                    message += "NOT RUNNING: Out of fuel or no signal.\n";
                }
                message += String.format("%s: %d mB, %d FE/t", new TranslationTextComponent(portableGenerator.getTankContent()).getString(), portableGenerator.getTankAmount(), portableGenerator.getGenerateAmount());
                entity.sendMessage(new StringTextComponent(message), entity.getUUID());
            }
            else if (!acceptFluid) doAdditionalFunction(world, pos, state, entity, hand, rayTraceResult.getDirection());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable IBlockReader world, List<ITextComponent> list, ITooltipFlag flag) {
        CompoundNBT nbt = itemStack.getTag();
        if (nbt != null && nbt.contains("FluidName") && nbt.contains("Amount"))
        {
            list.add(new StringTextComponent(nbt.getString("FluidName") + ": " + nbt.getInt("Amount")));
        }
        super.appendHoverText(itemStack, world, list, flag);
    }

    public void doAdditionalFunction(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing)
    {
    }
}
