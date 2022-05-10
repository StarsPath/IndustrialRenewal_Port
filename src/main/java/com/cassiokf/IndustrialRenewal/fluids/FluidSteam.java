//package com.cassiokf.IndustrialRenewal.fluids;
//
//import com.cassiokf.IndustrialRenewal.References;
//import com.cassiokf.IndustrialRenewal.init.ModFluids;
//import com.mojang.datafixers.util.Pair;
//import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
//import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
//import net.minecraft.block.BlockState;
//import net.minecraft.fluid.FlowingFluid;
//import net.minecraft.fluid.Fluid;
//import net.minecraft.fluid.FluidState;
//import net.minecraft.item.Item;
//import net.minecraft.state.StateContainer;
//import net.minecraft.tags.ITag;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.IWorldReader;
//import net.minecraft.world.World;
//import net.minecraftforge.fluids.FluidAttributes;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//
//import java.awt.*;
//
//public abstract class FluidSteam extends FlowingFluid {
//
//    @Override
//    public Fluid getFlowing() {
//        return ModFluids.STEAM_FLOWING.get();
//    }
//
//    @Override
//    public Fluid getSource() {
//        return ModFluids.STEAM.get();
//    }
//
//    @Override
//    public Item getBucket() {
//        return ModFluids.STEAM_BUCKET.get();
//    }
//
//    @Override
//    protected boolean canConvertToSource() {
//        return false;
//    }
//
//    @Override
//    protected boolean canBeReplacedWith(FluidState p_215665_1_, IBlockReader p_215665_2_, BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
//        return false;
//    }
//
//    @Override
//    protected void beforeDestroyingBlock(IWorld p_205580_1_, BlockPos p_205580_2_, BlockState p_205580_3_) {
//
//    }
//
//    @Override
//    protected int getDropOff(IWorldReader p_204528_1_) {
//        return 2;
//    }
//
//    @Override
//    protected int getSlopeFindDistance(IWorldReader p_185698_1_) {
//        return 4;
//    }
//
//    @Override
//    public int getTickDelay(IWorldReader p_205569_1_) {
//        return 5;
//    }
//
//    @Override
//    protected float getExplosionResistance() {
//        return 100F;
//    }
//
//    @Override
//    protected FluidAttributes createAttributes() {
//        return FluidAttributes.builder(
//                new ResourceLocation(References.MODID, "block/steam_still"),
//                new ResourceLocation(References.MODID, "block/steam_flow"))
//                .translationKey("block.industrialrenewal.steam").gaseous()
//                .density(-1000).temperature(380).viscosity(500).color(Color.WHITE.getRGB())
//                .build(this);
//    }
//
//    public static class Flowing extends FluidSteam
//    {
//        @Override
//        protected BlockState createLegacyBlock(FluidState state) {
//            return null;
//        }
//
//        @Override
//        public boolean isSource(FluidState state) {
//            return false;
//        }
//
//        @Override
//        public int getAmount(FluidState state) {
//            return state.getValue(FluidSteam.LEVEL);
//        }
//    }
//
//    public static class Source extends FluidSteam
//    {
//        @Override
//        protected BlockState createLegacyBlock(FluidState p_204527_1_) {
//            return null;
//        }
//
//        @Override
//        public boolean isSource(FluidState p_207193_1_) {
//            return true;
//        }
//
//        @Override
//        public int getAmount(FluidState p_207192_1_) {
//            return 8;
//        }
//    }
//}
