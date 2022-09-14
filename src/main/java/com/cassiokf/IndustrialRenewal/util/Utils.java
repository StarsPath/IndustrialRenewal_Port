package com.cassiokf.IndustrialRenewal.util;

import com.cassiokf.IndustrialRenewal.industrialrenewal;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.text.DecimalFormat;
import java.util.*;

public class Utils {
    public static boolean debugMsg = false;
    private static final Random RANDOM = new Random();
    private static final DecimalFormat form = new DecimalFormat("0.0");
    private static final DecimalFormat preciseForm = new DecimalFormat("0.00");

    public static final Direction[] VALUES = Direction.values();
    public static final Direction[] BY_HORIZONTAL_INDEX = Arrays.stream(VALUES)
            .filter((direction) -> direction.getAxis().isHorizontal())
            .sorted(Comparator.comparingInt(Direction::get2DDataValue))
            .toArray(Direction[]::new);

    public static void debug(String msg, Object ... objects){
        StringBuilder s = new StringBuilder("DEBUG: ");
        s.append(msg).append(" ");
        for(Object obj : objects){
            if(obj == null)
                s.append("EMPTY ");
            else
                s.append(obj.toString()).append(" ");
        }
        industrialrenewal.LOGGER.info(s);
    }

    public static void sendChatMessage(PlayerEntity player, String str)
    {
        if (player == null) Minecraft.getInstance().player.sendMessage(new StringTextComponent(str), Minecraft.getInstance().player.getUUID());
        else player.sendMessage(new StringTextComponent(str), player.getUUID());
    }

    public static void sendConsoleMessage(String str)
    {
        System.out.println(str);
    }


    public static String formatEnergyString(int energy)
    {
        //industrialrenewal.LOGGER.info("formatString "+energy);
        String text = energy + " FE";
        if (energy >= 1000 && energy < 1000000)
            text = form.format((float) energy / 1000) + "K FE";
        else if (energy >= 1000000 && energy < 1000000000)
            text = form.format((float) energy / 1000000) + "M FE";
        else if (energy >= 1000000000)
            text = form.format((float) energy / 1000000000) + "B FE";
        return text;
    }

    public static float normalizeClamped(float value, float min, float max)
    {
        //industrialrenewal.LOGGER.info("normClamp "+ value + " / " + max);
        return MathHelper.clamp((value - min), 0, (max - min)) / (max - min);
    }

    public static float normalize(float value, float min, float max)
    {
        return (value - min) / (max - min);
    }

    public static int directionToInt(Direction d){
        switch (d){
            case DOWN: return 0;
            case UP: return 1;
            case NORTH: return 2;
            case SOUTH: return 3;
            case WEST: return 4;
            case EAST: return 5;
        }
        return 2;
    }

    public static Direction intToDir(int d){
        switch (d){
            case 0: return Direction.DOWN;
            case 1: return Direction.UP;
            case 2: return Direction.NORTH;
            case 3: return Direction.SOUTH;
            case 4: return Direction.WEST;
            case 5: return Direction.EAST;
        }
        return Direction.NORTH;
    }


    public static Direction rotateAround(Direction d, Direction.Axis axis)
    {
        switch(axis)
        {
            case X:
                if(d!=Direction.WEST&&d!=Direction.EAST)
                    return rotateX(d);
                return d;
            case Y:
                if(d!=Direction.UP&&d!=Direction.DOWN)
                    return d.getClockWise();

                return d;
            case Z:
                if(d!=Direction.NORTH&&d!=Direction.SOUTH)
                    return rotateZ(d);

                return d;
            default:
                throw new IllegalStateException("Unable to get CW facing for axis "+axis);
        }
    }

    public static Direction rotateX(Direction d)
    {
        switch(d)
        {
            case NORTH:
                return Direction.DOWN;
            case EAST:
            case WEST:
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of "+d);
            case SOUTH:
                return Direction.UP;
            case UP:
                return Direction.NORTH;
            case DOWN:
                return Direction.SOUTH;
        }
    }

    public static Direction rotateZ(Direction d)
    {
        switch(d)
        {
            case EAST:
                return Direction.DOWN;
            case SOUTH:
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of "+d);
            case WEST:
                return Direction.UP;
            case UP:
                return Direction.EAST;
            case DOWN:
                return Direction.WEST;
        }
    }

    public static float lerp(float a, float b, float f)
    {
        return a + f * (b - a);
    }

    public static double lerp(double a, double b, double f)
    {
        return a + f * (b - a);
    }


    public static void dropInventoryItems(World worldIn, BlockPos pos, ItemStackHandler inventory)
    {
        if(inventory == null)
            return;
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }

    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler inventory)
    {
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, pos, itemstack);
            }
        }
    }

    public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stack)
    {
        if (worldIn.isClientSide) return;
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (!stack.isEmpty())
        {
            ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + (double) f, pos.getY() + (double) f1, pos.getZ() + (double) f2, stack.split(RANDOM.nextInt(21) + 10));
//            entityitem.motionX = RANDOM.nextGaussian() * 0.05000000074505806D;
//            entityitem.motionY = RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
//            entityitem.motionZ = RANDOM.nextGaussian() * 0.05000000074505806D;
            entityitem.setDeltaMovement(RANDOM.nextGaussian() * 0.05000000074505806D, RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D, RANDOM.nextGaussian() * 0.05000000074505806D);
            worldIn.addFreshEntity(entityitem);
            //worldIn.spawnEntity(entityitem);
        }
    }

    public static double getDistanceSq(BlockPos pos, double x, double y, double z) {
        double d0 = (double)pos.getX() + 0.5D - x;
        double d1 = (double)pos.getY() + 0.5D - y;
        double d2 = (double)pos.getZ() + 0.5D - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public static IFluidHandler wrapFluidBlock(BlockState state, World world, BlockPos pos)
    {
        return new BlockWrapper(state, world, pos);
    }

    public static List<BlockPos> getBlocksIn3x3x3Centered(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int y = -1; y < 2; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    list.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                }
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x2x3Centered(BlockPos pos)
    {
        List<BlockPos> list = new ArrayList<BlockPos>();
        for (int y = -1; y <1; y++)
        {
            for (int z = -1; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    list.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                }
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x3x2Centered(BlockPos pos, Direction facing)
    {
        List<BlockPos> list = new ArrayList<>();
        boolean isSided = facing == Direction.EAST || facing == Direction.WEST;
        boolean invert = facing == Direction.NORTH || facing == Direction.WEST;
        for (int y = -1; y < 2; y++)
        {
            for (int z = 0; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    int finalX = (isSided ? z : x);
                    int finalZ = (isSided ? x : z);
                    list.add(new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ)));
                }
            }
        }
        return list;
    }

    public static List<BlockPos> getBlocksIn3x2x2Centered(BlockPos pos, Direction facing)
    {
        List<BlockPos> list = new ArrayList<>();
        boolean isSided = facing == Direction.EAST || facing == Direction.WEST;
        boolean invert = facing == Direction.NORTH || facing == Direction.WEST;
        for (int y = 0; y < 2; y++)
        {
            for (int z = 0; z < 2; z++)
            {
                for (int x = -1; x < 2; x++)
                {
                    int finalX = (isSided ? z : x);
                    int finalZ = (isSided ? x : z);
                    list.add(new BlockPos(pos.getX() + (invert ? -finalX : finalX), pos.getY() + y, pos.getZ() + (invert ? -finalZ : finalZ)));
                }
            }
        }
        return list;
    }

    public static float getConvertedTemperature(float temp)
    {
        //TODO: Add to config
        //0 C. 1 F. 2 K
        int scaleSetting = 0;
        switch (scaleSetting)
        {
            default:
            case 0:
                return temp;
            case 1:
                return (float) (temp * 1.8 + 32);
            case 2:
                return (float) (temp + 273.15);
        }
    }

    public static String getTemperatureUnit()
    {
        String st = "";

        //TODO: Add to config
        //0 C. 1 F. 2 K
        int scaleSetting = 0;
        switch (scaleSetting)
        {
            default:
                return "??";
            case 0:
                //st = " " + I18n.get("render.industrialrenewal.c");
                return "C";
            case 1:
                //st = " " + I18n.get("render.industrialrenewal.f");
                return "F";
            case 2:
                //st = " " + I18n.get("render.industrialrenewal.k");
                return "K";
        }
    }

    public static boolean isInventoryFull(IInventory inv){
        for(int i = 0; i < inv.getContainerSize(); i++){
            if(inv.getItem(i).isEmpty())
                return false;
        }
        return true;
    }

    public static boolean moveFluidToTank(FluidTank from, IFluidHandler to){
        to.fill(from.drain(to.fill(from.drain(200, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        return false;
    }

    public static boolean moveFluidToTank(IFluidHandler from, FluidTank to){
        to.fill(from.drain(to.fill(from.drain(200, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        return false;
    }


    public static boolean moveItemToInventory(IItemHandler from, int slot, IItemHandler to)
    {
        boolean movement = false;
        for (int j = 0; j < to.getSlots(); j++)
        {
            ItemStack stack = from.getStackInSlot(slot);
            if (!stack.isEmpty() && to.isItemValid(j, stack))
            {
                ItemStack left = to.insertItem(j, stack, false);
                if (!ItemStack.isSame(stack, left))
                {
                    int toExtract = stack.getCount() - left.getCount();
                    from.extractItem(slot, toExtract, false);
                    movement = true;
                }
            }
        }
        return movement;
    }

    public static boolean moveItemsBetweenInventories(IInventory from, IItemHandler to){

        if(to==null || from == null)
            return false;
        for(int i = 0; i < from.getContainerSize(); i++){
            if(from.getItem(i).isEmpty()) {
                continue;
            }
            ItemStack remainder = null;
            remainder = from.getItem(i).copy();
            remainder.setCount(1);
            for(int j = 0; j < to.getSlots(); j++){
                remainder = to.insertItem(j, remainder, false);
                if(remainder.isEmpty()) {
                    from.getItem(i).shrink(1);
                    from.setChanged();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean moveItemsBetweenInventories(IItemHandler from, IInventory to)
    {
        if(to==null || from==null)
            return false;
        for(int i = 0; i < from.getSlots(); i++){
            ItemStack stack = from.extractItem(i, 1, true);
            if(stack.isEmpty())
                continue;
            for(int j = 0; j < to.getContainerSize(); j++){
                if(to.getItem(j).isEmpty()){
                    to.setItem(j, from.extractItem(i, 1, false));
                    to.setChanged();
                    return true;
                }
                else if(ItemStack.tagMatches(to.getItem(j), stack) && to.getItem(j).sameItem(stack) && to.getItem(j).isStackable() && to.getItem(j).getCount() < to.getItem(j).getMaxStackSize()){
                    from.extractItem(i, 1, false);
                    to.getItem(j).grow(1);
                    to.setChanged();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean moveItemsBetweenInventories(IItemHandler from, IItemHandler to)
    {
        return moveItemsBetweenInventories(from, to, true, Integer.MAX_VALUE);
    }

    public static boolean moveItemsBetweenInventories(IItemHandler from, IItemHandler to, int maxStackCount)
    {
        return moveItemsBetweenInventories(from, to, true, maxStackCount);
    }

    private static boolean moveItemsBetweenInventories(IItemHandler from, IItemHandler to, boolean stackPerTick, int maxStackCount)
    {
        boolean movement = false;
        for (int i = 0; i < from.getSlots(); i++)
        {
            boolean needBreak = false;
            ItemStack stack = from.extractItem(i, maxStackCount, true);
            for (int j = 0; j < to.getSlots(); j++)
            {
                if (!stack.isEmpty() && to.isItemValid(j, stack))
                {
                    ItemStack left = to.insertItem(j, stack, false);
                    if (!ItemStack.isSame(stack, left))
                    {
                        int toExtract = stack.getCount() - left.getCount();
                        from.extractItem(i, toExtract, false);
                        movement = true;
                        if (stackPerTick)
                        {
                            needBreak = true;
                            break;
                        }
                    }
                }
            }
            if (stackPerTick && needBreak) break;
        }
        return movement;
    }

    public static String formatPreciseEnergyString(int energy)
    {
        String text = energy + " FE";
        if (energy >= 1000 && energy < 1000000)
            text = preciseForm.format((float) energy / 1000) + "K FE";
        else if (energy >= 1000000 && energy < 1000000000)
            text = preciseForm.format((float) energy / 1000000) + "M FE";
        else if (energy >= 1000000000)
            text = preciseForm.format((float) energy / 1000000000) + "B FE";
        return text;
    }

    public static float getInvNorm(IItemHandler inventory)
    {
        if(inventory == null)
            return 0;
        float items = 0;
        for (int i = 0; i < inventory.getSlots(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);
            if (!itemstack.isEmpty())
            {
                items = items + 1;
            }
        }
        return items / (float) inventory.getSlots();
    }

    public static boolean IsInventoryEmpty(IItemHandler handler)
    {
        for (int i = 0; i < handler.getSlots(); i++)
        {
            if (!handler.getStackInSlot(i).isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    public static boolean IsInventoryFull(IItemHandler handler)
    {
        int slotsFull = 0;
        for (int i = 0; i < handler.getSlots(); i++)
        {
            if (!handler.getStackInSlot(i).isEmpty())
            {
                slotsFull++;
            }
        }
        return slotsFull == handler.getSlots();
    }

    public static float logisticFunction(float L, float k, float xo, float x){
        return (float)(L/(1+Math.exp(-k * (x-xo))));
    }

    public static Vector3d midPoint(Vector3d start, Vector3d end, double yOffset){
        double x = (start.x + end.x)/2;
        double y = (start.y + end.y)/2;
        double z = (start.z + end.z)/2;
//        double x = Math.sqrt(Math.pow(start.x, 2) + Math.pow(end.x, 2));
//        double y = Math.sqrt(Math.pow(start.y, 2) + Math.pow(end.y, 2));
//        double z = Math.sqrt(Math.pow(start.z, 2) + Math.pow(end.z, 2));
        return new Vector3d(x, y + yOffset, z);
    }

    public static Vector3d midPoint(Vector3d start, Vector3d end){
        return  midPoint(start, end, 0);
    }

    public static Vector3d lerp(Vector3d start, Vector3d end, double amount){
        amount = MathHelper.clamp(amount, 0, 1);
        double x = lerp(start.x, end.x, amount);
        double y = lerp(start.y, end.y, amount);
        double z = lerp(start.z, end.z, amount);
        return new Vector3d(x, y, z);
    }
}
