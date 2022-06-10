package com.cassiokf.IndustrialRenewal.init;

import com.cassiokf.IndustrialRenewal.References;
import com.cassiokf.IndustrialRenewal.blocks.*;
import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockEnergyCable;
import com.cassiokf.IndustrialRenewal.blocks.pipes.BlockFluidPipe;
import com.cassiokf.IndustrialRenewal.industrialrenewal;
import com.cassiokf.IndustrialRenewal.util.enums.EnumEnergyCableType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, References.MODID);

    public static final IRBaseBlock BLOCKHAZARD = new IRBaseBlock("block_hazard", IRBlockItem::new);
    public static final IRBaseBlock CAUTIONHAZARD = new IRBaseBlock("caution_hazard", IRBlockItem::new);
    public static final IRBaseBlock DEFECTIVEHAZARD = new IRBaseBlock("defective_hazard", IRBlockItem::new);
    public static final IRBaseBlock SAFETYHAZARD = new IRBaseBlock("safety_hazard", IRBlockItem::new);
    public static final IRBaseBlock RADIATIONHAZARD = new IRBaseBlock("radiation_hazard", IRBlockItem::new);
    public static final IRBaseBlock AISLEHAZARD = new IRBaseBlock("aisle_hazard", IRBlockItem::new);
    public static final IRBaseBlock FIREHAZARD = new IRBaseBlock("fire_hazard", IRBlockItem::new);
    public static final IRBaseBlock STEELBLOCK = new IRBaseBlock("block_steel", IRBlockItem::new);
    public static final IRBaseBlock CONCRETE = new IRBaseBlock("concrete", IRBlockItem::new);

    public static final RegistryObject<Block> CONCRETEWALL = registerBlock("concrete_wall",
            () -> new WallBlock(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).strength(2f).noCollission()));

    public static final RegistryObject<BlockSolarPanel> SPANEL = registerBlock("solar_panel",
            BlockSolarPanel::new);

    public static final RegistryObject<BlockBatteryBank> BATTERYBANK = registerBlock("battery_bank",
            () -> new BlockBatteryBank(AbstractBlock.Properties.of(Material.METAL).strength(2f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()), false);

    public static final RegistryObject<BlockBarrel> BARREL = registerBlock("barrel",
            () -> new BlockBarrel(AbstractBlock.Properties.of(Material.METAL).strength(2f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()), false);

    public static final RegistryObject<BlockPortableGenerator> PORTABLE_GENERATOR = registerBlock("portable_generator",
            () -> new BlockPortableGenerator(AbstractBlock.Properties.of(Material.METAL).strength(2f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockTrash> TRASH = registerBlock("trash",
            () -> new BlockTrash(AbstractBlock.Properties.of(Material.METAL).strength(2f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockWindTurbinePillar> TURBINE_PILLAR = registerBlock("small_wind_turbine_pillar",
            () -> new BlockWindTurbinePillar(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockWindTurbineHead> WIND_TURBINE = registerBlock("small_wind_turbine",
            () -> new BlockWindTurbineHead(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_LV = registerBlock("energy_cable_lv",
            () -> new BlockEnergyCable(EnumEnergyCableType.LV, AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_MV = registerBlock("energy_cable_mv",
            () -> new BlockEnergyCable(EnumEnergyCableType.MV, AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockEnergyCable> ENERGYCABLE_HV = registerBlock("energy_cable_hv",
            () -> new BlockEnergyCable(EnumEnergyCableType.HV, AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockFluidPipe> FLUID_PIPE = registerBlock("fluid_pipe",
            ()-> new BlockFluidPipe(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockElectricPump> ELECTRIC_PUMP = registerBlock("electric_pump",
            ()-> new BlockElectricPump(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockSteamBoiler> STEAM_BOILER = registerBlock("steam_boiler",
            ()-> new BlockSteamBoiler(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockSteamTurbine> STEAM_TURBINE = registerBlock("steam_turbine",
            ()-> new BlockSteamTurbine(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockMiner> MINER = registerBlock("mining",
            ()-> new BlockMiner(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));
    

    public static final RegistryObject<BlockLocker> LOCKER = registerBlock("locker",
            ()-> new BlockLocker(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockStorageChest> STORAGE_CHEST = registerBlock("storage_chest",
            ()-> new BlockStorageChest(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockIndustrialBatteryBank> INDUSTRIAL_BATTERY_BANK = registerBlock("ind_battery_bank",
            ()-> new BlockIndustrialBatteryBank(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockFluidTank> FLUID_TANK = registerBlock("fluid_tank",
            ()-> new BlockFluidTank(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<BlockLathe> LATHE = registerBlock("lathe",
            ()-> new BlockLathe(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)
                    .harvestTool(ToolType.PICKAXE).sound(SoundType.METAL).noOcclusion()));



    public static final RegistryObject<BlockPillar> PILLAR = registerBlock("catwalk_pillar",
            BlockPillar::new);

    public static final RegistryObject<BlockPillar> PILLAR_STEEL = registerBlock("catwalk_steel_pillar",
            BlockPillar::new);

    public static final RegistryObject<BlockColumn> COLUMN = registerBlock("catwalk_column",
            BlockColumn::new);

    public static final RegistryObject<BlockColumn> COLUMN_STEEL = registerBlock("catwalk_column_steel",
            BlockColumn::new);

//    public static final BlockChimney blockChimney = new BlockChimney("block_chimney", References.CREATIVE_IR_TAB);
//    public static final BlockFirstAidKit firstAidKit = new BlockFirstAidKit("firstaid_kit", References.CREATIVE_IR_TAB);
//    public static final BlockFireExtinguisher fireExtinguisher = new BlockFireExtinguisher("fire_extinguisher", References.CREATIVE_IR_TAB);
//    public static final BlockLocker locker = new BlockLocker("locker", References.CREATIVE_IR_TAB);
//
//    public static final BlockFluid steamBlock = new BlockFluid("steam", FluidInit.STEAM, References.CREATIVE_IR_TAB);
//    public static final BlockFluidPipe fluidPipe = new BlockFluidPipe("fluid_pipe", References.CREATIVE_IR_TAB);
//    public static final BlockFluidPipeGauge fluidPipeGauge = new BlockFluidPipeGauge("fluid_pipe_gauge", References.CREATIVE_IR_TAB);
//
//    public static final BlockEnergyCable energyCableLV = new BlockEnergyCable(EnumEnergyCableType.LV, "energy_cable_lv", References.CREATIVE_IR_TAB);
//    public static final BlockEnergyCable energyCableMV = new BlockEnergyCable(EnumEnergyCableType.MV, "energy_cable", References.CREATIVE_IR_TAB);
//    public static final BlockEnergyCable energyCableHV = new BlockEnergyCable(EnumEnergyCableType.HV, "energy_cable_hv", References.CREATIVE_IR_TAB);
//
//    public static final BlockEnergyCableGauge energyCableGaugeLV = new BlockEnergyCableGauge(EnumEnergyCableType.LV, "energy_cable_gauge_lv", References.CREATIVE_IR_TAB);
//    public static final BlockEnergyCableGauge energyCableGaugeMV = new BlockEnergyCableGauge(EnumEnergyCableType.MV, "energy_cable_gauge", References.CREATIVE_IR_TAB);
//    public static final BlockEnergyCableGauge energyCableGaugeHV = new BlockEnergyCableGauge(EnumEnergyCableType.HV, "energy_cable_gauge_hv", References.CREATIVE_IR_TAB);
//
//    public static final BlockPillarEnergyCable pillarEnergyCableLV = new BlockPillarEnergyCable(EnumEnergyCableType.LV, "iron_pillar_energy_lv", References.CREATIVE_IR_TAB);
//    public static final BlockPillarEnergyCable pillarEnergyCableMV = new BlockPillarEnergyCable(EnumEnergyCableType.MV, "iron_pillar_energy", References.CREATIVE_IR_TAB);
//    public static final BlockPillarEnergyCable pillarEnergyCableHV = new BlockPillarEnergyCable(EnumEnergyCableType.HV, "iron_pillar_energy_hv", References.CREATIVE_IR_TAB);
//
//    public static final BlockPillarFluidPipe pillarFluidPipe = new BlockPillarFluidPipe("iron_pillar_pipe", References.CREATIVE_IR_TAB);
//    public static final BlockFloorPipe floorPipe = new BlockFloorPipe("floor_pipe", References.CREATIVE_IR_TAB);
//    public static final BlockFloorCable floorCableLV = new BlockFloorCable(EnumEnergyCableType.LV, "floor_cable_lv", References.CREATIVE_IR_TAB);
//    public static final BlockFloorCable floorCableMV = new BlockFloorCable(EnumEnergyCableType.MV, "floor_cable", References.CREATIVE_IR_TAB);
//    public static final BlockFloorCable floorCableHV = new BlockFloorCable(EnumEnergyCableType.HV, "floor_cable_hv", References.CREATIVE_IR_TAB);
//    public static final BlockFloorLamp floorLamp = new BlockFloorLamp("floor_lamp", References.CREATIVE_IR_TAB);
//    public static final BlockHVConnectorBase hvIsolator = new BlockHVConnectorBase("isolator_hv", References.CREATIVE_IR_TAB);
//
//    //Machines
//    public static final BlockLathe latheMachine = new BlockLathe("lathe", References.CREATIVE_IR_TAB);
//
//    public static final BlockAlarm alarm = new BlockAlarm("alarm", References.CREATIVE_IR_TAB);
//    public static final BlockRecordPlayer recordPlayer = new BlockRecordPlayer("record_player", References.CREATIVE_IR_TAB);
//
//    public static final BlockCatWalk catWalk = new BlockCatWalk("catwalk", References.CREATIVE_IR_TAB);
//    public static final BlockCatWalk catWalkSteel = new BlockCatWalk("catwalk_steel", References.CREATIVE_IR_TAB);
//    public static final BlockHandRail handRail = new BlockHandRail("handrail", References.CREATIVE_IR_TAB);
//    public static final BlockHandRail handRailSteel = new BlockHandRail("handrail_steel", References.CREATIVE_IR_TAB);
//    public static final BlockCatwalkStair catwalkStair = new BlockCatwalkStair("catwalk_stair", References.CREATIVE_IR_TAB);
//    public static final BlockCatwalkStair catwalkStairSteel = new BlockCatwalkStair("catwalk_stair_steel", References.CREATIVE_IR_TAB);
//    public static final BlockPillar pillar = new BlockPillar("catwalk_pillar", References.CREATIVE_IR_TAB);
//    public static final BlockPillar steel_pillar = new BlockPillar("catwalk_steel_pillar", References.CREATIVE_IR_TAB);
//    public static final BlockColumn column = new BlockColumn("catwalk_column", References.CREATIVE_IR_TAB);
//    public static final BlockColumn columSteel = new BlockColumn("catwalk_column_steel", References.CREATIVE_IR_TAB);
//    public static final BlockCatwalkLadder iladder = new BlockCatwalkLadder("catwalk_ladder", References.CREATIVE_IR_TAB);
//    public static final BlockCatwalkLadder sladder = new BlockCatwalkLadder("catwalk_ladder_steel", References.CREATIVE_IR_TAB);
//    public static final BlockRoof roof = new BlockRoof("roof", References.CREATIVE_IR_TAB);
//    public static final BlockGutter gutter = new BlockGutter("gutter", References.CREATIVE_IR_TAB);
//    public static final BlockLight light = new BlockLight("light", References.CREATIVE_IR_TAB);
//    public static final BlockFluorescent fluorescent = new BlockFluorescent("fluorescent", References.CREATIVE_IR_TAB);
//    public static final BlockCatwalkGate catwalkGate = new BlockCatwalkGate("catwalk_gate", References.CREATIVE_IR_TAB);
//    public static final BlockCatwalkHatch hatch = new BlockCatwalkHatch("catwalk_hatch", References.CREATIVE_IR_TAB);
//    public static final BlockWindow window = new BlockWindow("window", References.CREATIVE_IR_TAB);
//    public static final BlockPlatform platform = new BlockPlatform("platform", References.CREATIVE_IR_TAB);
//    public static final BlockBrace brace = new BlockBrace("brace", References.CREATIVE_IR_TAB);
//    public static final BlockBrace braceSteel = new BlockBrace("brace_steel", References.CREATIVE_IR_TAB);
//    public static final BlockScaffold scaffold = new BlockScaffold("scaffold", References.CREATIVE_IR_TAB);
//    public static final BlockFrame frame = new BlockFrame("frame", References.CREATIVE_IR_TAB);
//    public static final BlockBunkBed bunkBed = new BlockBunkBed("bunkbed", References.CREATIVE_IR_TAB);
//    public static final BlockBunkerHatch bunkerHatch = new BlockBunkerHatch("bunker_hatch", References.CREATIVE_IR_TAB);
//
//    public static final BlockBarrel barrel = new BlockBarrel("barrel", References.CREATIVE_IR_TAB);
//    public static final BlockTrash trash = new BlockTrash("trash", References.CREATIVE_IR_TAB);
//    public static final BlockGauge gauge = new BlockGauge("fluid_gauge", References.CREATIVE_IR_TAB);
//    public static final BlockEnergyLevel energyLevel = new BlockEnergyLevel("energy_level", References.CREATIVE_IR_TAB);
//
//    public static final BlockElectricFence efence = new BlockElectricFence("electric_fence", References.CREATIVE_IR_TAB);
//    public static final BlockBigFenceColumn bigFenceColumn = new BlockBigFenceColumn("fence_big_column", References.CREATIVE_IR_TAB);
//    public static final BlockElectricBigFenceWire bigFenceWire = new BlockElectricBigFenceWire("fence_big_wire", References.CREATIVE_IR_TAB);
//    public static final BlockBigFenceCorner bigFenceCorner = new BlockBigFenceCorner("fence_big_corner", References.CREATIVE_IR_TAB);
//    public static final BlockBigFenceCornerInner bigFenceInner = new BlockBigFenceCornerInner("fence_big_corner_inner", References.CREATIVE_IR_TAB);
//    public static final BlockBaseWall concreteWall = new BlockBaseWall("wall_concrete", References.CREATIVE_IR_TAB);
//    public static final BlockElectricGate egate = new BlockElectricGate("electric_gate", References.CREATIVE_IR_TAB);
//    public static final BlockRazorWire razorWire = new BlockRazorWire("razor_wire", References.CREATIVE_IR_TAB);
//
//    public static final BlockDamIntake damIntake = new BlockDamIntake("dam_intake", References.CREATIVE_IR_TAB);
//    public static final BlockDamOutFlow damOutFlow = new BlockDamOutFlow("dam_outflow", References.CREATIVE_IR_TAB);
//
//    public static final BlockInfinityGenerator infinityGenerator = new BlockInfinityGenerator("infinity_generator", References.CREATIVE_IR_TAB);
//    public static final BlockSolarPanelFrame fpanel = new BlockSolarPanelFrame("solar_panel_frame", References.CREATIVE_IR_TAB);
//    public static final BlockSmallWindTurbine sWindTurbine = new BlockSmallWindTurbine("small_wind_turbine", References.CREATIVE_IR_TAB);
//    public static final BlockWindTurbinePillar turbinePillar = new BlockWindTurbinePillar("small_wind_turbine_pillar", References.CREATIVE_IR_TAB);
//    public static final BlockCableTray cableTray = new BlockCableTray("cable_tray", References.CREATIVE_IR_TAB);
//    public static final BlockBatteryBank batteryBank = new BlockBatteryBank("battery_bank", References.CREATIVE_IR_TAB);
//
//    public static final BlockSensorRain sensorRain = new BlockSensorRain("sensor_rain", References.CREATIVE_IR_TAB);
//    public static final BlockSignalIndicator signalIndicator = new BlockSignalIndicator("signal_indicator", References.CREATIVE_IR_TAB);
//    public static final BlockTrafficLight trafficLight = new BlockTrafficLight("traffic_light", References.CREATIVE_IR_TAB);
//    public static final BlockFuseBox fuseBox = new BlockFuseBox("fuse_box", References.CREATIVE_IR_TAB);
//    public static final BlockFuseBoxConduitExtension fuseBoxConduitExtension = new BlockFuseBoxConduitExtension("conduit_extension", References.CREATIVE_IR_TAB);
//    public static final BlockFuseBoxConnector fuseBoxConnector = new BlockFuseBoxConnector("conduit_connector", References.CREATIVE_IR_TAB);
//    public static final BlockFlameDetector flameDetector = new BlockFlameDetector("flame_detector", References.CREATIVE_IR_TAB);
//    public static final BlockEntityDetector entityDetector = new BlockEntityDetector("entity_detector", References.CREATIVE_IR_TAB);
//    public static final BlockButtonRed buttonRed = new BlockButtonRed("button_red", References.CREATIVE_IR_TAB);
//
//    public static final BlockNormalRail normalRail = new BlockNormalRail("normal_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockCrossingRail crossingRail = new BlockCrossingRail("crossing_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockDetectorRail detectorRail = new BlockDetectorRail("detector_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockBoosterRail boosterRail = new BlockBoosterRail("booster_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockBufferStopRail bufferStopRail = new BlockBufferStopRail("buffer_stop_rail", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockLoaderRail loaderRail = new BlockLoaderRail("rail_loader", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockRailGate railGate = new BlockRailGate("rail_gate", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockCargoLoader cargoLoader = new BlockCargoLoader("cargo_loader", References.CREATIVE_IRLOCOMOTIVE_TAB);
//    public static final BlockFluidLoader fluidLoader = new BlockFluidLoader("fluid_loader", References.CREATIVE_IRLOCOMOTIVE_TAB);
//
//    public static final BlockValvePipeLarge valveLarge = new BlockValvePipeLarge("valve_pipe_large", References.CREATIVE_IR_TAB);
//    public static final BlockEnergySwitch energySwitch = new BlockEnergySwitch("energy_switch", References.CREATIVE_IR_TAB);
//
//    public static final BlockBulkConveyor conveyorV = new BlockBulkConveyor("conveyor_bulk", References.CREATIVE_IR_TAB, EnumBulkConveyorType.NORMAL);
//    public static final BlockBulkConveyor conveyorVHopper = new BlockBulkConveyor("conveyor_bulk_hopper", References.CREATIVE_IR_TAB, EnumBulkConveyorType.HOPPER);
//    public static final BlockBulkConveyor conveyorVInserter = new BlockBulkConveyor("conveyor_bulk_inserter", References.CREATIVE_IR_TAB, EnumBulkConveyorType.INSERTER);
//
//    public static final BlockSignBase signHV = new BlockSignBase("sign_hv", References.CREATIVE_IR_TAB);
//    public static final BlockSignBase signRA = new BlockSignBase("sign_ra", References.CREATIVE_IR_TAB);
//    public static final BlockSignBase signC = new BlockSignBase("sign_c", References.CREATIVE_IR_TAB);
//
//    public static final BlockEotM baseEotM = new BlockEotM("eotm", References.CREATIVE_IR_TAB);
//    public static final BlockEotM andrbootEotM = new BlockEotM("eotm_andrboot", References.CREATIVE_IR_TAB);
//    public static final BlockEotM ic2exp = new BlockEotM("eotm_ic2exp", References.CREATIVE_IR_TAB);
//    public static final BlockElectricPump electricPump = new BlockElectricPump("electric_pump", References.CREATIVE_IR_TAB);
//
//    public static final BlockPortableGenerator portableGenerator = new BlockPortableGenerator("portable_generator", References.CREATIVE_IR_TAB);
//    public static final BlockSteamBoiler steamBoiler = new BlockSteamBoiler("steam_boiler", References.CREATIVE_IR_TAB);
//    public static final BlockSteamTurbine steamTurbine = new BlockSteamTurbine("steam_turbine", References.CREATIVE_IR_TAB);
//    public static final BlockMining mining = new BlockMining("mining", References.CREATIVE_IR_TAB);
//    public static final BlockTransformerHV transformerHV = new BlockTransformerHV("transformer_hv", References.CREATIVE_IR_TAB);
//    public static final BlockDamTurbine damTurbine = new BlockDamTurbine("dam_turbine", References.CREATIVE_IR_TAB);
//    public static final BlockDamGenerator damGenerator = new BlockDamGenerator("dam_generator", References.CREATIVE_IR_TAB);
//    public static final BlockDamAxis damAxis = new BlockDamAxis("dam_axis", References.CREATIVE_IR_TAB);
//    public static final BlockHighPressurePipe highPressurePipe = new BlockHighPressurePipe("high_pressure_pipe", References.CREATIVE_IR_TAB);
//    public static final BlockFluidTank fluidTank = new BlockFluidTank("fluid_tank", References.CREATIVE_IR_TAB);
//    public static final BlockIndustrialBatteryBank industrialBattery = new BlockIndustrialBatteryBank("ind_battery_bank", References.CREATIVE_IR_TAB);
//    public static final BlockStorage storageRack = new BlockStorage("storage_rack", References.CREATIVE_IR_TAB);
//    public static final BlockStorageChest storageChest = new BlockStorageChest("storage_chest", References.CREATIVE_IR_TAB);
//
//    public static final BlockChunkLoader chunkLoader = new BlockChunkLoader("chunk_loader", References.CREATIVE_IR_TAB);
//
//    public static final BlockOreVein veinHematite = new BlockOreVein("orevein_hematite", "oreIron", References.CREAATIVE_IRWIP_TAB);

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, true);
    }

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean createItem) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        if(createItem)
            registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(industrialrenewal.IR_TAB)));
    }

    public static void register(IForgeRegistry<Block> registry){
        for(Block block : industrialrenewal.registeredIRBlocks)
            registry.register(block);
    }

    public  static void registerInit(IEventBus bus){
        BLOCKS.register(bus);
    }

}