package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.fluids.BlockSteam;
//import com.cassiokf.IndustrialRenewal.fluids.FluidSteam;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, References.MODID);

    public static final RegistryObject<FlowingFluid> STEAM =
            FLUIDS.register("steam_still", ()-> new ForgeFlowingFluid.Source(ModFluids.PROPERTIES));

    public static final RegistryObject<FlowingFluid> STEAM_FLOWING =
            FLUIDS.register("steam_flow", ()-> new ForgeFlowingFluid.Flowing(ModFluids.PROPERTIES));

    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            ()-> STEAM.get(), ()->STEAM_FLOWING.get(), FluidAttributes.builder(
                    new ResourceLocation(References.MODID, "block/steam_still"),
                    new ResourceLocation(References.MODID, "block/steam_flow"))
                    .translationKey("block.industrialrenewal.steam").gaseous()
                    .density(-1000).temperature(380).viscosity(500).color(Color.WHITE.getRGB())
            ).levelDecreasePerBlock(2).slopeFindDistance(4).explosionResistance(100f).tickRate(5)
            .block(()->ModFluids.STEAM_BLOCK.get())
            .bucket(()->ModItems.STEAM_BUCKET.get());

    public static final RegistryObject<FlowingFluidBlock> STEAM_BLOCK = ModBlocks.BLOCKS.register("steam",
            ()-> new FlowingFluidBlock(()->ModFluids.STEAM.get(), AbstractBlock.Properties.of(Material.WATER)
                    .strength(100f).noDrops()){
            });

    public static void init(IEventBus e){
        FLUIDS.register(e);
    }

}
