package com.cassiokf.IndustrialRenewal.proxy;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.init.ModBlocks;
import com.cassiokf.IndustrialRenewal.init.ModTileEntities;
import com.cassiokf.IndustrialRenewal.tesr.TESRBatteryBank;
import com.cassiokf.IndustrialRenewal.tileentity.TileEntityBatteryBank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy{
    @Override
    public void Init()
    {
        super.Init();
        //MinecraftForge.EVENT_BUS.register(IRSoundHandler.class);
    }

    @Override
    public void preInit()
    {
        //IRConfig.clientPreInit();
//        RenderHandler.registerEntitiesRender();
//        RenderHandler.registerCustomMeshesAndStates();
    }

    public void registerItemModel(Item item, String path, String renderCase)
    {
        Minecraft.getInstance().getItemRenderer().getItemModelShaper().register(item, new ModelResourceLocation(path, renderCase));
    }

    public void registerRenderers(){
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.BATTERY_BANK_TILE.get(), TESRBatteryBank::new);
    }

}
