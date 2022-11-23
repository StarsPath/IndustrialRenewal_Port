package com.cassiokf.industrialrenewal.init;

import com.cassiokf.industrialrenewal.IndustrialRenewal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class ModFluids {

    public static final TagKey<Fluid> STEAM_TAG = FluidTags.create(new ResourceLocation("forge:steam"));

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, IndustrialRenewal.MODID);

    public static final RegistryObject<FlowingFluid> STEAM =
            FLUIDS.register("steam_still", ()-> new ForgeFlowingFluid.Source(ModFluids.PROPERTIES));

    public static final RegistryObject<FlowingFluid> STEAM_FLOWING =
            FLUIDS.register("steam_flow", ()-> new ForgeFlowingFluid.Flowing(ModFluids.PROPERTIES));

    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            ()-> STEAM.get(), ()->STEAM_FLOWING.get(), FluidAttributes.builder(
                    new ResourceLocation(IndustrialRenewal.MODID, "block/steam_still"),
                    new ResourceLocation(IndustrialRenewal.MODID, "block/steam_flow"))
                    .translationKey("block.industrialrenewal.steam").gaseous()
                    .density(-1000).temperature(380).viscosity(500).color(Color.WHITE.getRGB())
            ).levelDecreasePerBlock(2).slopeFindDistance(4).explosionResistance(100f).tickRate(5)
            .block(()->ModFluids.STEAM_BLOCK.get())
            .bucket(()->ModItems.STEAM_BUCKET.get());

    public static final RegistryObject<LiquidBlock> STEAM_BLOCK = ModBlocks.BLOCKS.register("steam",
            ()-> new LiquidBlock(()->ModFluids.STEAM.get(), BlockBehaviour.Properties.of(Material.WATER)
                    .strength(100f).noDrops()){
            });

    public static void init(IEventBus e){
        FLUIDS.register(e);
    }

}
