package com.cassiokf.IndustrialRenewal.blocks;

import com.cassiokf.IndustrialRenewal.blocks.abstracts.Block3x3x3Base;
import com.cassiokf.IndustrialRenewal.item.ItemFireBox;
import com.cassiokf.IndustrialRenewal.item.ItemPowerScrewDrive;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntitySteamBoiler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSteamBoiler extends Block3x3x3Base<TileEntitySteamBoiler> {

    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);

    public BlockSteamBoiler(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        //TODO: add to config
        int waterPtick = 100;
        int steamConversion = 1;

        tooltip.add(new StringTextComponent(
                I18n.get("info.industrialrenewal.requires")
                        + ":"));
        tooltip.add(new StringTextComponent(" -" + I18n.get("info.industrialrenewal.firebox")));
        tooltip.add(new StringTextComponent(
                " -" + Blocks.WATER.getName().getContents()
                        + ": "
                        + waterPtick
                        + " mB/t"));
        int mult = waterPtick * steamConversion;
        tooltip.add(new StringTextComponent(
                I18n.get("info.industrialrenewal.produces")
                        + " "
                        + "Steam"
                        + ": "
                        + mult
                        + " mB/t"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) worldIn.getBlockEntity(pos);
        if (te != null && te.isMaster() && te.getIntType() == 1 && te.getFuelFill() > 0 && rand.nextInt(24) == 0)
        {
            worldIn.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundCategory.BLOCKS, 0.3F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
        }
        //super.animateTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        TileEntitySteamBoiler te = (TileEntitySteamBoiler) world.getBlockEntity(pos);
        if (te != null) te.dropAllItems();
        super.onRemove(state, world, pos, oldState, isMoving);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        TileEntitySteamBoiler tile = (TileEntitySteamBoiler) worldIn.getBlockEntity(pos);
        //Utils.debug("right clicked on tile", tile);
        if(tile == null) return ActionResultType.FAIL;
        tile = tile.getMaster();
//        IItemHandler itemHandler = tile.getFireBoxHandler();
//        if(itemHandler == null) return ActionResultType.FAIL;
        ItemStack heldItem = player.getItemInHand(handIn);

        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemFireBox || heldItem.getItem() instanceof ItemPowerScrewDrive))
        {
            if (heldItem.getItem() instanceof ItemFireBox && tile.getIntType()==0)
            {
                int type = ((ItemFireBox) heldItem.getItem()).type;
                //itemHandler.insertItem(0, new ItemStack(heldItem.getItem(), 1), false);
                tile.setType(type);
                if (!worldIn.isClientSide && !player.isCreative()) heldItem.shrink(1);
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof ItemPowerScrewDrive && tile.getIntType()!=0)
            {
                ItemStack stack = tile.getDrop();
                if (!worldIn.isClientSide && !player.isCreative()) player.addItem(stack);
                tile.setType(0);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
        //return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Nullable
    @Override
    public TileEntitySteamBoiler createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySteamBoiler();
    }
}
