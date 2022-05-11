package com.cassiokf.IndustrialRenewal.data.client;

import com.cassiokf.IndustrialRenewal.References;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper){
        super(generator, References.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("block_hazard", modLoc("block/block_hazard"));
        withExistingParent("caution_hazard", modLoc("block/caution_hazard"));
        withExistingParent("defective_hazard", modLoc("block/defective_hazard"));
        withExistingParent("safety_hazard", modLoc("block/safety_hazard"));
        withExistingParent("radiation_hazard", modLoc("block/radiation_hazard"));
        withExistingParent("aisle_hazard", modLoc("block/aisle_hazard"));
        withExistingParent("fire_hazard", modLoc("block/fire_hazard"));
        withExistingParent("block_steel", modLoc("block/block_steel"));
        withExistingParent("concrete", modLoc("block/concrete"));

        withExistingParent("concrete_wall", modLoc("block/concrete_wall_inventory"));

        withExistingParent("solar_panel", modLoc("block/solar_panel"));
        withExistingParent("battery_bank", modLoc("block/battery_bank"));
        withExistingParent("barrel", modLoc("block/fluid_barrel"));
        withExistingParent("portable_generator", modLoc("block/portable_generator"));
        withExistingParent("trash", modLoc("block/trash"));

        withExistingParent("small_wind_turbine_pillar", modLoc("block/small_wind_turbine_pillar"));
        withExistingParent("small_wind_turbine", modLoc("block/small_wind_turbine"));

        withExistingParent("steam_boiler", modLoc("block/steam_boiler"));
        withExistingParent("steam_turbine", modLoc("block/steam_turbine"));

        //withExistingParent("energy_cable_lv", modLoc("block/pipe_energy_lv/cable_inventory"));

//        withExistingParent("pointer", modLoc("item/pointer"));
//        withExistingParent("limiter", modLoc("item/limiter"));
//        withExistingParent("pointer_long", modLoc("item/pointer_long"));
//        withExistingParent("fire", modLoc("item/fire"));
//        withExistingParent("bar_level", modLoc("item/bar_level"));
//        withExistingParent("fluid_loader_arm", modLoc("item/fluid_loader_arm"));
//        withExistingParent("tambor", modLoc("item/tambor"));
//        withExistingParent("cutter", modLoc("item/cutter"));
//        withExistingParent("indicator_on", modLoc("item/indicator_on"));
//        withExistingParent("indicator_off", modLoc("item/indicator_off"));
//        withExistingParent("switch_on", modLoc("item/switch_on"));
//        withExistingParent("switch_off", modLoc("item/switch_off"));
//        withExistingParent("push_button", modLoc("item/push_button"));
//        withExistingParent("label_5", modLoc("item/label_5"));
//        withExistingParent("disc_r", modLoc("item/disc_r"));


        //withExistingParent("", modLoc("block/caution_hazard"));

//
//        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
    }
}
