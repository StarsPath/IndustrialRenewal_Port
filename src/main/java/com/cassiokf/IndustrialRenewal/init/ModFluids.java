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
//    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);
//    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, References.MODID);
//    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, References.MODID);
//    public static final RegistryObject<FlowingFluid> STEAM = FLUIDS.register("steam", FluidSteam.Source::new);
//    public static final RegistryObject<FlowingFluid> STEAM_FLOWING = FLUIDS.register("flowing_steam", FluidSteam.Flowing::new);
//    public static final RegistryObject<BlockSteam> STEAM_BLOCK = BLOCKS.register("steam_block", BlockSteam::new);
//    public static final RegistryObject<Item> STEAM_BUCKET = ITEMS.register("steam_bucket", () ->
//            new BucketItem(() -> ModFluids.STEAM.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(ItemGroup.TAB_MATERIALS)));
//
//    public static void init(IEventBus e)
//    {
//        FLUIDS.register(e);
//        BLOCKS.register(e);
//        ITEMS.register(e);
//    }
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
//                @Override
//                public void entityInside(BlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity entityIn) {
//                    //super.entityInside(p_196262_1_, p_196262_2_, p_196262_3_, p_196262_4_);
//                    {
//                        entityIn.hurt(DamageSource.HOT_FLOOR, 0.5F);
//                    }
//                }
            });

    public static void init(IEventBus e){
        FLUIDS.register(e);
    }

}
