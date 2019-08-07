package com.supertechgroup.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.supertechgroup.core.agriculture.CottonCropBlock;
import com.supertechgroup.core.agriculture.FertileFarmlandBlock;
import com.supertechgroup.core.agriculture.FertilizerItem;
import com.supertechgroup.core.agriculture.HempCropBlock;
import com.supertechgroup.core.fluids.BlockModFluid;
import com.supertechgroup.core.fluids.ModFluid;
import com.supertechgroup.core.items.ItemConstructor;
import com.supertechgroup.core.items.ItemResearchBook;
import com.supertechgroup.core.items.MaterialTool;
import com.supertechgroup.core.items.SuperTechItem;
import com.supertechgroup.core.machinery.basicsmelter.TileEntityBasicSmelter;
import com.supertechgroup.core.machinery.multiblock.crudeheater.CrudeHeaterBlock;
import com.supertechgroup.core.machinery.multiblock.crudeheater.CrudeHeaterTileEntity;
import com.supertechgroup.core.machinery.multiblock.crudeio.CrudeIOBlock;
import com.supertechgroup.core.machinery.multiblock.crudeio.CrudeIOTileEntity;
import com.supertechgroup.core.machinery.multiblock.crudewall.CrudeWallBlock;
import com.supertechgroup.core.machinery.multiblock.crudewall.CrudeWallTileEntity;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.metallurgy.Material.MaterialBuilder;
import com.supertechgroup.core.proxy.CommonProxy;
import com.supertechgroup.core.recipies.MaterialToolIngredient;
import com.supertechgroup.core.research.Research;
import com.supertechgroup.core.research.ResearchTasks;
import com.supertechgroup.core.research.researchstation.BlockResearchStation;
import com.supertechgroup.core.research.researchstation.TileEntityResearchStation;
import com.supertechgroup.core.worldgen.WorldTypeSuperTech;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorBase;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorCluster;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorFluid;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorKimberlite;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorPlate;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorVein;
import com.supertechgroup.core.worldgen.ores.NativeMetal;
import com.supertechgroup.core.worldgen.ores.Ore;
import com.supertechgroup.core.worldgen.ores.OreBlock;
import com.supertechgroup.core.worldgen.ores.OreItem;
import com.supertechgroup.core.worldgen.rocks.BlockRock;
import com.supertechgroup.core.worldgen.rocks.RockManager;
import com.supertechgroup.core.worldgen.rocks.StateMapperRock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = Reference.MODID)
public class ModRegistry {

	public static final WorldTypeSuperTech worldType = new WorldTypeSuperTech();

	public static final Item[] disabledVanillaItems = new Item[] { Items.GOLD_NUGGET, Items.IRON_NUGGET,
			Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_SWORD,
			Items.STONE_AXE, Items.STONE_HOE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_SWORD,
			Items.IRON_AXE, Items.IRON_HOE, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_SWORD, Items.GOLDEN_AXE,
			Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD, Items.DIAMOND_AXE,
			Items.DIAMOND_HOE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_SWORD, Items.GOLD_INGOT,
			Items.IRON_INGOT, Item.getItemFromBlock(Blocks.COAL_ORE), Item.getItemFromBlock(Blocks.DIAMOND_ORE),
			Item.getItemFromBlock(Blocks.EMERALD_ORE), Item.getItemFromBlock(Blocks.GOLD_ORE),
			Item.getItemFromBlock(Blocks.IRON_ORE), Item.getItemFromBlock(Blocks.LAPIS_ORE),
			Item.getItemFromBlock(Blocks.LIT_REDSTONE_ORE), Item.getItemFromBlock(Blocks.QUARTZ_ORE),
			Item.getItemFromBlock(Blocks.REDSTONE_ORE), Item.getItemFromBlock(Blocks.COAL_BLOCK),
			Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), Item.getItemFromBlock(Blocks.EMERALD_BLOCK),
			Item.getItemFromBlock(Blocks.GOLD_BLOCK), Item.getItemFromBlock(Blocks.IRON_BLOCK),
			Item.getItemFromBlock(Blocks.LAPIS_BLOCK), Item.getItemFromBlock(Blocks.QUARTZ_BLOCK) };

	public static OreBlock superore;
	public static BlockResearchStation researchStation;

	public static SuperTechItem itemTech;
	public static ItemResearchBook itemResearchBook;
	public static ItemConstructor itemConstructor;
	public static CrudeIOBlock crudeIOBlock;
	public static CrudeWallBlock crudeWallBlock;
	public static CrudeHeaterBlock crudeHeaterBlock;

	public static CottonCropBlock cottonCrop;
	public static HempCropBlock hempCrop;

	public static ArrayList<ModFluid> fluids = new ArrayList<>();

	public static Item itemCotton;
	public static Item itemHempSeed;

	public static Block fertileBlock;
	public static FertilizerItem itemFertilizer;

	private static void createRegisterFluid(String name, int density, boolean gaseous, int luminosity, int viscosity,
			int temperature, boolean hasBlock) {
		ModFluid fluid = (ModFluid) new ModFluid(name,
				new ResourceLocation(Reference.MODID, "fluids/" + name + "_still"),
				new ResourceLocation(Reference.MODID, "fluids/" + name + "_flow")).setHasBlock(hasBlock)
						.setMaterial(net.minecraft.block.material.Material.WATER).setDensity(density)
						.setGaseous(gaseous).setLuminosity(luminosity).setViscosity(viscosity)
						.setTemperature(temperature);
		FluidRegistry.registerFluid(fluid);
		fluids.add(fluid);
	}

	/**
	 *
	 * @param type              Igneous, sedimentary, or metamorphic
	 * @param name              id-name of the block
	 * @param hardness          How hard (time duration) the block is to pick. For
	 *                          reference, dirt is 0.5, stone is 1.5, ores are 3,
	 *                          and obsidian is 50
	 * @param blastResistance   how resistant the block is to explosions. For
	 *                          reference, dirt is 0, stone is 10, and blast-proof
	 *                          materials are 2000
	 * @param toolHardnessLevel 0 for wood tools, 1 for stone, 2 for iron, 3 for
	 *                          diamond
	 */
	private static void createStoneType(String name, double hardness, double blastResistance, int toolHardnessLevel,
			RegistryEvent.Register<Block> event, String... types) {
		final Block rock, rockCobble;

		rockCobble = new BlockRock(name + "cobble", true, (float) hardness, (float) blastResistance, toolHardnessLevel,
				SoundType.STONE);

		ForgeRegistries.ITEMS.register(new ItemBlock(rockCobble).setRegistryName(rockCobble.getRegistryName()));

		rock = new BlockRock(name, true, (float) hardness, (float) blastResistance, toolHardnessLevel,
				SoundType.STONE) {

			@Override
			public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
				return Arrays.asList(new ItemStack(Item.getItemFromBlock(rockCobble)));

			}

		};

		String[] newArray = new String[types.length + 1];
		System.arraycopy(types, 0, newArray, 0, types.length);
		newArray[newArray.length - 1] = name;
		RockManager.addRockTypes(rock.getDefaultState(), newArray);

		ItemBlock itemBlock = (ItemBlock) new ItemBlock(rock).setRegistryName(rock.getRegistryName());
		ForgeRegistries.ITEMS.register(itemBlock);

		GameRegistry.addSmelting(rockCobble, new ItemStack(rock), 0.0f);
		event.getRegistry().registerAll(rock, rockCobble);

		OreDictionary.registerOre("stone", rock);
		OreDictionary.registerOre("cobblestone", rockCobble);

		final Item item = Item.getItemFromBlock(rock);
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(new ResourceLocation(rock.getRegistryName().getResourceDomain(), "rock"),
						rock.getRegistryName().getResourcePath()));
		final Item cobble = Item.getItemFromBlock(rockCobble);
		ModelLoader.setCustomModelResourceLocation(cobble, 0,
				new ModelResourceLocation(
						new ResourceLocation(rockCobble.getRegistryName().getResourceDomain(), "rock"),
						rockCobble.getRegistryName().getResourcePath()));
		ModelLoader.setCustomStateMapper(rock, new StateMapperRock());
		ModelLoader.setCustomStateMapper(rockCobble, new StateMapperRock());
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		itemTech.registerModels();
		itemResearchBook.registerModels();
		itemConstructor.registerModels();
		researchStation.registerModels();
		crudeIOBlock.registerModels();
		crudeWallBlock.registerModels();
		crudeHeaterBlock.registerModels();
		itemFertilizer.registerItemModel();
		((FertileFarmlandBlock) fertileBlock).registerModels();

		for (ModFluid fluid : fluids) {
			if (fluid.hasBlock()) {
				((BlockModFluid) fluid.getBlock()).registerModels();

			}
		}

		ModelLoader.setCustomModelResourceLocation(itemCotton, 0,
				new ModelResourceLocation("supertechcore:cotton", "inventory"));
		ModelLoader.setCustomModelResourceLocation(itemHempSeed, 0,
				new ModelResourceLocation("supertechcore:seed_hemp", "inventory"));

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		crudeIOBlock = new CrudeIOBlock();
		event.getRegistry().register(crudeIOBlock);
		ForgeRegistries.ITEMS.register(new ItemBlock(crudeIOBlock).setRegistryName(crudeIOBlock.getRegistryName()));
		GameRegistry.registerTileEntity(CrudeIOTileEntity.class, crudeIOBlock.getRegistryName());

		crudeHeaterBlock = new CrudeHeaterBlock();
		event.getRegistry().register(crudeHeaterBlock);
		ForgeRegistries.ITEMS
				.register(new ItemBlock(crudeHeaterBlock).setRegistryName(crudeHeaterBlock.getRegistryName()));
		GameRegistry.registerTileEntity(CrudeHeaterTileEntity.class, crudeHeaterBlock.getRegistryName());

		crudeWallBlock = new CrudeWallBlock();
		event.getRegistry().register(crudeWallBlock);
		ForgeRegistries.ITEMS.register(new ItemBlock(crudeWallBlock).setRegistryName(crudeWallBlock.getRegistryName()));
		GameRegistry.registerTileEntity(CrudeWallTileEntity.class, crudeWallBlock.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityBasicSmelter.class,
				new ResourceLocation(Reference.MODID, "basicSmelter"));

		superore = new OreBlock();
		event.getRegistry().register(superore);

		researchStation = new BlockResearchStation();
		ForgeRegistries.ITEMS
				.register(new ItemBlock(researchStation).setRegistryName(researchStation.getRegistryName()));
		event.getRegistry().register(researchStation);
		GameRegistry.registerTileEntity(TileEntityResearchStation.class, researchStation.getRegistryName());

		cottonCrop = new CottonCropBlock();
		event.getRegistry().register(cottonCrop);

		hempCrop = new HempCropBlock();
		event.getRegistry().register(hempCrop);

		fertileBlock = new FertileFarmlandBlock();
		ForgeRegistries.ITEMS.register(new ItemBlock(fertileBlock).setRegistryName(fertileBlock.getRegistryName()));
		event.getRegistry().register(fertileBlock);

		// Rocks

		RockManager.addRockTypes(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE),
				"igneous", "andesite", "vanilla", "extrusive", "intermediate");
		RockManager.addTextureOverride(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE),
				new ResourceLocation("minecraft:blocks/stone_andesite"));
		createStoneType("basalt", 5, 100, 2, event, "igneous", "extrusive");
		RockManager.addRockTypes(
				Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH),
				"sedimentary", "sandstone", "vanilla", "clastic");
		RockManager.addTextureOverride(
				Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH),
				new ResourceLocation("minecraft:blocks/sandstone_smooth"));
		RockManager.addRockTypes(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), "igneous",
				"diorite", "vanilla", "intrusive", "intermediate");
		RockManager.addTextureOverride(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE),
				new ResourceLocation("minecraft:blocks/stone_diorite"));
		RockManager.addRockTypes(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), "igneous",
				"granite", "vanilla", "felsic", "intrusive");
		RockManager.addTextureOverride(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE),
				new ResourceLocation("minecraft:blocks/stone_granite"));
		createStoneType("rhyolite", 1.5, 10, 0, event, "igneous", "felsic", "extrusive");
		createStoneType("gabbro", 1.5, 10, 0, event, "igneous", "mafic", "intrusive");
		createStoneType("scoria", 1.5, 10, 0, event, "igneous", "mafic", "extrusive");
		createStoneType("pegmatite", 1.5, 10, 0, event, "igneous", "felsic", "intrusive");

		// RockManager.addRockTypes(Blocks.GRAVEL.getDefaultState(), "sedimentary");
		createStoneType("shale", 1.5, 10, 0, event, "sedimentary", "clastic");
		createStoneType("chert", 1.5, 10, 0, event, "sedimentary");
		createStoneType("conglomerate", 1.5, 10, 0, event, "sedimentary", "clastic");
		createStoneType("dolomite", 3, 15, 1, event, "sedimentary");
		createStoneType("limestone", 1.5, 10, 0, event, "sedimentary");
		createStoneType("chalk", 1.5, 10, 0, event, "sedimentary");

		createStoneType("schist", 3, 15, 1, event, "metamorphic");
		createStoneType("gneiss", 3, 15, 1, event, "metamorphic");
		createStoneType("marble", 1.5, 10, 0, event, "sedimentary");
		createStoneType("phyllite", 1.5, 10, 0, event, "metamorphic");
		createStoneType("amphibolite", 3, 15, 1, event, "metamorphic");
		createStoneType("slate", 1.5, 10, 0, event, "metamorphic");

		createStoneType("kimberlite", 2.0, 14, 3, event, "gabbro");

		OreDictionary.registerOre("fluxStone",
				event.getRegistry().getValue(new ResourceLocation(Reference.MODID, "limestonecobble")));
		OreDictionary.registerOre("fluxStone",
				event.getRegistry().getValue(new ResourceLocation(Reference.MODID, "dolomitecobble")));

		// fluids
		for (ModFluid fluid : fluids) {
			if (fluid.hasBlock()) {
				BlockModFluid bmf = new BlockModFluid(fluid, net.minecraft.block.material.Material.WATER);

				event.getRegistry().register(bmf);

				ForgeRegistries.ITEMS.register(new ItemBlock(bmf).setRegistryName(bmf.getRegistryName()));
			}
		}
	}

	public static void registerFluids() {
		createRegisterFluid("oil", 880, false, 0, 1200, 300, true);
	}

	public static void registerItemModels() {
		// TODO Auto-generated method stub

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		itemConstructor = new ItemConstructor();

		itemTech = new SuperTechItem();
		itemResearchBook = new ItemResearchBook();
		itemCotton = new ItemSeeds(cottonCrop, Blocks.FARMLAND).setUnlocalizedName("cotton")
				.setRegistryName(Reference.MODID, "cotton");
		itemHempSeed = new ItemSeeds(hempCrop, Blocks.FARMLAND).setUnlocalizedName("hemp_seed")
				.setRegistryName(Reference.MODID, "hempSeed");
		itemFertilizer = new FertilizerItem();

		event.getRegistry().registerAll(itemTech, itemResearchBook, itemConstructor, itemCotton, itemHempSeed,
				itemFertilizer);

		itemTech.setupDictionary();

	}

	@SubscribeEvent
	public static void registerMaterials(RegistryEvent.Register<Material> event) {
		new MaterialBuilder("Antimony").setColor(0xfada5e).setDensity(6.697).setThermalExpansion(11)
				.setSpecificHeat(25.23).setThermalConductivity(24.4).setElectricalResistance(417).setYoungsModulus(55)
				.setShearModulus(20).setBulkModulus(42).setHarvestLevel(3).build().registerMaterial();
		new MaterialBuilder("Bismuth").setColor(0xed7d92).setDensity(9.78).setThermalExpansion(13.4)
				.setSpecificHeat(25.52).setThermalConductivity(7.97).setElectricalResistance(1290).setYoungsModulus(32)
				.setShearModulus(12).setBulkModulus(31).setHarvestLevel(0).build().registerMaterial();
		new MaterialBuilder("Cadmium").setColor(0xed872d).setDensity(8.65).setThermalExpansion(32.8)
				.setThermalConductivity(96.6).setElectricalResistance(72.7).setYoungsModulus(50).setShearModulus(19)
				.setSpecificHeat(26.02).setBulkModulus(42).setHarvestLevel(0).build().registerMaterial();

		new MaterialBuilder("Mercury").setColor(0x751f27).setHarvestLevel(0).setDensity(13.546).setSpecificHeat(27.98)
				.setElectricalResistance(961).setThermalExpansion(60.4).setShearModulus(0).setThermalConductivity(8.3)
				.setBulkModulus(0).setYoungsModulus(0).build().registerMaterial();
		Material copper = new MaterialBuilder("Copper").setColor(0xb4713d).setToolLevel(2).setSpecificHeat(24.44)
				.setDensity(8.96).setThermalExpansion(16.5).setThermalConductivity(401).setElectricalResistance(16.78)
				.setYoungsModulus(119).setShearModulus(48).setBulkModulus(140).setHarvestLevel(1).build();
		copper.registerMaterial();
		new NativeMetal(copper).registerOre();
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:nativecopper"))),
				"nativeCopper", new int[] { 0 }, 10, 1, 5, 2, "sedimentary"));
		new MaterialBuilder("Zinc").setColor(0xbac4cb).setDensity(7.14).setThermalExpansion(30.2).setSpecificHeat(25.47)
				.setThermalConductivity(116).setElectricalResistance(59.0).setYoungsModulus(108).setShearModulus(43)
				.setBulkModulus(70).setHarvestLevel(1).build().registerMaterial();

		new MaterialBuilder("Coal").setColor(0x060607).setHarvestLevel(1).setDensity(2.08).setSpecificHeat(1380)
				.setCustomDrops(new ItemStack(Items.COAL)).setElectricalResistance(30000).setThermalExpansion(6)
				.setShearModulus(6).setThermalConductivity(293).setBulkModulus(12).setYoungsModulus(14).build()
				.registerMaterial();

		Material iron = new MaterialBuilder("Iron").setColor(0xd3ad90).setToolLevel(3).setHarvestLevel(2)
				.setSpecificHeat(25.1).setThermalExpansion(11.8).setThermalConductivity(80.4)
				.setElectricalResistance(96.1).setYoungsModulus(211).setShearModulus(82).setBulkModulus(170)
				.setDensity(7.874).build();
		iron.registerMaterial();
		new MaterialBuilder("Chromium").setColor(0x18391e).setDensity(7.19).setThermalExpansion(4.9)
				.setThermalConductivity(93.9).setElectricalResistance(125).setYoungsModulus(279).setShearModulus(115)
				.setSpecificHeat(23.35).setBulkModulus(160).setHarvestLevel(2).build().registerMaterial();
		new MaterialBuilder("Aluminum").setColor(0xe0d9cd).setDensity(2.7).setThermalExpansion(23.1)
				.setThermalConductivity(237).setElectricalResistance(28.2).setYoungsModulus(70).setShearModulus(26)
				.setSpecificHeat(24.2).setBulkModulus(79).setHarvestLevel(2).build().registerMaterial();

		Material silver = new MaterialBuilder("Silver").setColor(0xb5b5bd).setDensity(10.49).setThermalExpansion(18.9)
				.setThermalConductivity(429).setElectricalResistance(15.87).setYoungsModulus(83).setSpecificHeat(25.35)
				.setShearModulus(30).setBulkModulus(100).setHarvestLevel(2).build();
		silver.registerMaterial();
		new NativeMetal(silver).registerOre();
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:nativesilver"))),
				"nativeSilver", new int[] { 0 }, 10, 1, 2, 2, "sedimentary"));
		new MaterialBuilder("Tellurium").setColor(0xb5b5bd).setDensity(6.24).setThermalExpansion(18)
				.setThermalConductivity(2.5).setElectricalResistance(10000).setYoungsModulus(43).setShearModulus(16)
				.setSpecificHeat(25.73).setBulkModulus(65).setHarvestLevel(2).build().registerMaterial();

		new MaterialBuilder("Lapis").setColor(0x000094).setHarvestLevel(2).setDensity(2.8)
				.setCustomDrops(new ItemStack(Items.DYE, 5, 4)).setElectricalResistance(999999).setThermalExpansion(6)
				.setShearModulus(200).setThermalConductivity(293).setBulkModulus(170).setYoungsModulus(137).build()
				.registerMaterial();

		Material tin = new MaterialBuilder("Tin").setColor(0x726a78).setDensity(7.265).setThermalExpansion(22)
				.setThermalConductivity(66.8).setElectricalResistance(115).setYoungsModulus(50).setSpecificHeat(27.112)
				.setShearModulus(18).setBulkModulus(58).setHarvestLevel(3).build();
		tin.registerMaterial();
		Material gold = new MaterialBuilder("Gold").setColor(0xccccc33).setDensity(19.3).setThermalExpansion(14.2)
				.setThermalConductivity(318).setElectricalResistance(22.14).setYoungsModulus(79).setSpecificHeat(25.418)
				.setShearModulus(27).setBulkModulus(180).setHarvestLevel(3).build();
		gold.registerMaterial();
		new NativeMetal(gold).registerOre();
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:nativegold"))),
				"nativeGold", new int[] { 0 }, 10, 1, 2, 2, "sedimentary"));
		Material lead = new MaterialBuilder("Lead").setColor(0x474c4d).setDensity(11.34).setThermalExpansion(28.9)
				.setThermalConductivity(35.3).setElectricalResistance(208).setYoungsModulus(16).setSpecificHeat(26.665)
				.setShearModulus(6).setBulkModulus(46).setHarvestLevel(3).build();
		lead.registerMaterial();
		new MaterialBuilder("Palladium").setColor(0xced0dd).setHarvestLevel(4).setDensity(12.023).setSpecificHeat(25.98)
				.setThermalExpansion(11.8).setThermalConductivity(71.8).setElectricalResistance(105.4)
				.setYoungsModulus(121).setShearModulus(44).setBulkModulus(180).build().registerMaterial();
		new MaterialBuilder("Redstone").setColor(0xd43c2c).setDensity(8.96).setThermalExpansion(16.5)
				.setThermalConductivity(401).setElectricalResistance(16.78).setYoungsModulus(119).setShearModulus(48)
				.setBulkModulus(140).setHarvestLevel(2).setCustomDrops(new ItemStack(Items.REDSTONE, 4, 0)).build()
				.registerMaterial();
		// TODO make sulfur drop sulfur
		new MaterialBuilder("Sulfur").setColor(0xedff21).setHarvestLevel(3).setSpecificHeat(22.75)
				.setCustomDrops(new ItemStack(Items.GUNPOWDER)).setDensity(20.7).setElectricalResistance(999999)
				.setThermalExpansion(0).setShearModulus(0).setThermalConductivity(.205).setBulkModulus(0)
				.setYoungsModulus(0).build().registerMaterial();
		Material nickel = new MaterialBuilder("Nickel").setColor(0xccd3d8).setDensity(8.908).setThermalExpansion(13.4)
				.setSpecificHeat(26.07).setThermalConductivity(90.9).setElectricalResistance(69.3).setYoungsModulus(200)
				.setShearModulus(76).setBulkModulus(180).setHarvestLevel(3).build();
		nickel.registerMaterial();
		new MaterialBuilder("Osmium").setColor(0x9090a3).setDensity(22.59).setThermalExpansion(5.1)
				.setSpecificHeat(24.7).setThermalConductivity(87.6).setElectricalResistance(81.2).setShearModulus(222)
				.setBulkModulus(462).setYoungsModulus(565).setHarvestLevel(3).build().registerMaterial();
		new MaterialBuilder("Diamond").setColor(0xb9f2ff).setHarvestLevel(3)
				.setCustomDrops(new ItemStack(Items.DIAMOND)).setDensity(3.52).setElectricalResistance(999999)
				.setThermalExpansion(1).setShearModulus(455).setThermalConductivity(1800).setBulkModulus(530)
				.setYoungsModulus(1100).build().registerMaterial();
		new MaterialBuilder("Emerald").setColor(0x50c878).setHarvestLevel(4)
				.setCustomDrops(new ItemStack(Items.EMERALD)).setDensity(2.52).setElectricalResistance(999999)
				.setThermalExpansion(1).setShearModulus(150).setThermalConductivity(1800).setBulkModulus(1)
				.setYoungsModulus(287).build().registerMaterial();

		new MaterialBuilder("Manganese").setColor(0x242d36).setDensity(7.21).setThermalExpansion(21.7)
				.setThermalConductivity(7.81).setElectricalResistance(1440).setYoungsModulus(198).setBulkModulus(120)
				.setShearModulus(74).setHarvestLevel(4).build().registerMaterial();

		new MaterialBuilder("Uranium").setColor(0x329832).setDensity(19.1).setThermalExpansion(13.9)
				.setThermalConductivity(27.5).setElectricalResistance(280).setYoungsModulus(208).setShearModulus(111)
				.setBulkModulus(100).setHarvestLevel(6).build().registerMaterial();
		new MaterialBuilder("Platinum").setColor(0xb8b7b2).setDensity(21.45).setThermalExpansion(8.8)
				.setElectricalResistance(105).setThermalConductivity(71.6).setYoungsModulus(168).setShearModulus(61)
				.setBulkModulus(230).setHarvestLevel(3).build().registerMaterial();
		new MaterialBuilder("Iridium").setColor(0xe0e2dd).setDensity(22.56).setThermalExpansion(6.4)
				.setThermalConductivity(147).setElectricalResistance(47.1).setYoungsModulus(528).setShearModulus(210)
				.setBulkModulus(320).setHarvestLevel(7).build().registerMaterial();
		new MaterialBuilder("Titanium").setColor(0x323230).setDensity(4.506).setThermalExpansion(8.6)
				.setThermalConductivity(21.9).setElectricalResistance(420).setYoungsModulus(116).setShearModulus(44)
				.setBulkModulus(110).setHarvestLevel(7).build().registerMaterial();
		new MaterialBuilder("Stone").setColor(0x8b8d7a).setToolLevel(1).setHarvestLevel(0).setDensity(1.6)
				.setElectricalResistance(999999).setThermalExpansion(2.7).setShearModulus(30)
				.setThermalConductivity(12.83).setBulkModulus(60).setYoungsModulus(47).build().registerMaterial();
		new MaterialBuilder("Netherrack").setColor(0x800000).setToolLevel(1).setHarvestLevel(0).setDensity(1.24)
				.setElectricalResistance(999999).setThermalExpansion(2.7).setShearModulus(30)
				.setThermalConductivity(14.87).setBulkModulus(54).setYoungsModulus(31).build().registerMaterial();
		new MaterialBuilder("Wood").setColor(0x4f2412).setHarvestLevel(0).setDensity(.75).setElectricalResistance(10000)
				.setThermalExpansion(30).setToolLevel(0).setShearModulus(13).setThermalConductivity(0.15)
				.setBulkModulus(17).setYoungsModulus(11).build().registerMaterial();
		new MaterialBuilder("Electrum").setColor(0x928729).setHarvestLevel(3).setDensity(14.2)
				.setElectricalResistance(24.67).setThermalExpansion(20).setShearModulus(35).setThermalConductivity(400)
				.setYoungsModulus(80).setBulkModulus(140).build().registerMaterial();
		new MaterialBuilder("Brass").setColor(0xe4ad5b).setHarvestLevel(2).setDensity(8.78)
				.setElectricalResistance(59.92).setThermalExpansion(20.5).setShearModulus(40)
				.setElectricalResistance(109).setYoungsModulus(112).setBulkModulus(108).build().registerMaterial();
		new MaterialBuilder("Constantan").setColor(0xe0a050).setHarvestLevel(3).setDensity(8.885)
				.setElectricalResistance(490).setThermalExpansion(14.9).setShearModulus(62).setThermalConductivity(21.2)
				.setYoungsModulus(162).setBulkModulus(130).build().registerMaterial();
		new MaterialBuilder("Bronze").setColor(0xe69e2f).setHarvestLevel(4).setToolLevel(4).setDensity(8.73)
				.setElectricalResistance(111.86).setThermalExpansion(18.2).setShearModulus(44)
				.setThermalConductivity(26).setYoungsModulus(107).setBulkModulus(112).build().registerMaterial();
		new MaterialBuilder("Steel").setColor(0xdfdfdf).setToolLevel(5).setHarvestLevel(5).setDensity(7.9)
				.setElectricalResistance(169).setThermalExpansion(72).setShearModulus(79).setThermalConductivity(50.2)
				.setBulkModulus(139).setYoungsModulus(200).build().registerMaterial();
		new MaterialBuilder("Obsidian").setColor(0x3d354b).setHarvestLevel(3).setDensity(2.4)
				.setElectricalResistance(999999).setThermalExpansion(2.9).setShearModulus(12)
				.setThermalConductivity(0.8).setBulkModulus(40).setYoungsModulus(73)
				.setCustomDrops(new ItemStack(Blocks.OBSIDIAN)).build().registerMaterial();

		new MaterialBuilder("Mithril").setColor(0xaebbdb).setHarvestLevel(5).setDensity(3.0)
				.setElectricalResistance(14.3).setThermalExpansion(20).setShearModulus(200).setThermalConductivity(521)
				.setBulkModulus(400).setYoungsModulus(387).build().registerMaterial();

		new MaterialBuilder("Adamantine").setColor(0xb30000).setHarvestLevel(8).setDensity(24)
				.setElectricalResistance(400).setThermalExpansion(1).setShearModulus(1000).setThermalConductivity(50)
				.setBulkModulus(1000).setYoungsModulus(1000).build().registerMaterial();
		new MaterialBuilder("AluBrass").setColor(0xcaa585).setHarvestLevel(1).setDensity(7.78)
				.setElectricalResistance(115).setThermalExpansion(17.8).setShearModulus(30).setThermalConductivity(65)
				.setYoungsModulus(120).setBulkModulus(70).build().registerMaterial();
		new MaterialBuilder("Invar").setColor(0xd0c0b3).setHarvestLevel(3).setDensity(8.1).setElectricalResistance(80)
				.setThermalExpansion(7.3).setShearModulus(326).setThermalConductivity(13).setBulkModulus(109)
				.setYoungsModulus(141).build().registerMaterial();
		new MaterialBuilder("Nichrome").setColor(0x858f80).setHarvestLevel(3).setDensity(8.4)
				.setElectricalResistance(125).setThermalExpansion(14).setShearModulus(100).setThermalConductivity(11.3)
				.setBulkModulus(160).setYoungsModulus(200).build().registerMaterial();
		new MaterialBuilder("Quartz").setColor(0xdddddd).setHarvestLevel(2).setDensity(2.65)
				.setElectricalResistance(10000).setThermalConductivity(1.3).setThermalExpansion(12.3)
				.setShearModulus(55).setBulkModulus(2).setCustomDrops(new ItemStack(Items.QUARTZ)).build()
				.registerMaterial();
		new MaterialBuilder("StainlessSteel").setColor(0xe0dfdb).setHarvestLevel(2).setDensity(8)
				.setElectricalResistance(7200).setThermalExpansion(17.3).setShearModulus(86)
				.setThermalConductivity(16.2).setYoungsModulus(198).setBulkModulus(143).build().registerMaterial();
		Material thorium = new MaterialBuilder("Thorium").setBulkModulus(54).setColor(0xD3D3D3).setDensity(11.7)
				.setElectricalResistance(157).setHarvestLevel(3).setShearModulus(31).setThermalConductivity(54)
				.setThermalExpansion(11).setYoungsModulus(79).build();
		thorium.registerMaterial();
		Material lithium = new MaterialBuilder("Lithium").setBulkModulus(11).setColor(0xD3D3C0).setDensity(0.534)
				.setElectricalResistance(92.8).setHarvestLevel(1).setShearModulus(4).setThermalConductivity(84.8)
				.setThermalExpansion(46).setYoungsModulus(5).build();
		lithium.registerMaterial();
		Material boron = new MaterialBuilder("Boron").setColor(0xBC8F8).setDensity(2.37)
				.setElectricalResistance(1000000).setHarvestLevel(0).setThermalConductivity(27.4).setThermalExpansion(6)
				.build();
		boron.registerMaterial();
		Material magnesium = new MaterialBuilder("Magnesium").setBulkModulus(35).setColor(0xd3d3e5).setDensity(1.738)
				.setElectricalResistance(43.9).setHarvestLevel(1).setShearModulus(17).setThermalConductivity(156)
				.setThermalExpansion(24.8).setYoungsModulus(45).build();
		magnesium.registerMaterial();
	}

	@SubscribeEvent
	public static void registerOres(RegistryEvent.Register<Ore> event) {
		// actually register the ores
		new Ore("Bauxite", 0x7CFC00).registerOre();// aluminium
		Ore bornite = new Ore("Bornite", 0x8B4513).registerOre();// copper, iron, sulfur
		new Ore("Magnetite", 0x494d51).registerOre();// iron ore
		new Ore("Limonite", 0xFFF44F).registerOre();// iron ore
		Ore chalcocite = new Ore("Chalcocite", 0x2F4F4F).registerOre();// copper, sulfur
		Ore cassiterite = new Ore("Cassiterite", 0x654321).registerOre();// tin
		new Ore("Chromite", 0xC0C0CC).registerOre();// iron, chromium. potential for magnesium
		new Ore("Cinnabar", 0x8b0017).registerOre();// mercury, sulfur
		new Ore("Cobaltite", 0xd2b48c).registerOre();// cobalt, arsenic, sulfur; small percentage of iron and nickel
		Ore galena = new Ore("Galena", 0xbeb2b2).registerOre();// silver/lead ore
		new Ore("Hematite", 0x101c1f).registerOre();// iron ore
		new Ore("Ilmenite", 0x323230).registerOre();// iron, titanium
		Ore sphalerite = new Ore("Sphalerite", 0x323230).registerOre();// zinc, sulfur, iron.
		new Ore("Coal", 0x060607) {
			@Override
			public ItemStack getDrops(byte base) {
				return new ItemStack(Items.COAL);
			}
		}.registerOre();
		new Ore("Emerald", 0x50c878) {
			@Override
			public ItemStack getDrops(byte base) {
				return new ItemStack(Items.EMERALD);
			}
		}.registerOre();
		new Ore("Lapis", 0x000094) {
			@Override
			public ItemStack getDrops(byte base) {
				return new ItemStack(Items.DYE, (int) (Math.random() * 4 + 2), 4);
			}
		}.registerOre();
		new Ore("Redstone", 0xa00010) {
			@Override
			public ItemStack getDrops(byte base) {
				return new ItemStack(Items.REDSTONE, (int) (Math.random() * 4 + 2));
			}
		}.registerOre();

		new Ore("Quartz", 0xdddddd) {
			@Override
			public ItemStack getDrops(byte base) {
				return new ItemStack(Items.QUARTZ);
			}
		}.registerOre();
		new Ore("Diamond", 0xb9f2ff) {
			@Override
			public ItemStack getDrops(byte base) {
				return new ItemStack(Items.DIAMOND);
			}
		}.registerOre();

		// advanced ore dictionary stuff
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "crushedTinOre",
				new ItemStack(cassiterite.getItemOre(), 1, OreItem.CRUSHED));
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "crushedVopprtOre",
				new ItemStack(bornite.getItemOre(), 1, OreItem.CRUSHED));
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "crushedCopperOre",
				new ItemStack(chalcocite.getItemOre(), 1, OreItem.CRUSHED));
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "crushedZincOre",
				new ItemStack(sphalerite.getItemOre(), 1, OreItem.CRUSHED));
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "crushedLeadOre",
				new ItemStack(galena.getItemOre(), 1, OreItem.CRUSHED));

		OreDictionary.registerOre("crushedTinOre", new ItemStack(cassiterite.getItemOre(), 1, OreItem.CRUSHED));
		OreDictionary.registerOre("crushedCopperOre", new ItemStack(bornite.getItemOre(), 1, OreItem.CRUSHED));
		OreDictionary.registerOre("crushedCopperOre", new ItemStack(chalcocite.getItemOre(), 1, OreItem.CRUSHED));
		OreDictionary.registerOre("crushedZincOre", new ItemStack(sphalerite.getItemOre(), 1, OreItem.CRUSHED));
		OreDictionary.registerOre("crushedLeadOre", new ItemStack(galena.getItemOre(), 1, OreItem.CRUSHED));

		// setup ore veins
		CommonProxy.parsed.add(new WorldGeneratorPlate(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:galena"))),
				"galena", new int[] { 0 }, 10, 10, "extrusive", "metamorphic", "granite", "sandstone"));
		CommonProxy.parsed
				.add(new WorldGeneratorCluster(
						WorldGeneratorBase.singleOre(
								Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:bauxite"))),
						"bauxite", new int[] { 0 }, 20, 1, 1, 5, "limestone", "dolomite", "granite", "gneiss", "basalt",
						"shale"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:bornite"))),
				"bornite", new int[] { 0 }, 20, 1, 2, 5, "mafic", "pegmatite", "shale"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:chalcocite"))),
				"chalcocite", new int[] { 0 }, 20, 1, 3, 5, "sedimentary", ""));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:cassiterite"))),
				"cassiterite", new int[] { 0 }, 10, 1, 3, 5, "sedimentary"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:chromite"))),
				"chromite", new int[] { 0 }, 10, 1, 4, 5, "intrusive", "metamorphic"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:magnetite"))),
				"magnetite", new int[] { 0 }, 10, 1, 6, 5, "sedimentary"));
		CommonProxy.parsed.add(new WorldGeneratorVein(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:cinnabar"))),
				"cinnabar", new int[] { 0 }, 2, 1, 1, 2, "extrusive", "shale"));
		CommonProxy.parsed.add(new WorldGeneratorVein(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:limonite"))),
				"limonite", new int[] { 0 }, 2, 1, 2, 8, "sedimentary"));
		CommonProxy.parsed.add(new WorldGeneratorVein(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:cobaltite"))),
				"cobaltite", new int[] { 0 }, 2, 1, 1, 2, "igneous", "metamorphic"));
		CommonProxy.parsed.add(new WorldGeneratorVein(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:hematite"))),
				"hematite", new int[] { 0 }, 2, 1, 2, 4, "extrusive", "sedimentary"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:ilmenite"))),
				"ilmenite", new int[] { 0 }, 10, 1, 8, 5, "gabbro"));
		CommonProxy.parsed.add(new WorldGeneratorVein(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:sphalerite"))),
				"sphalerite", new int[] { 0 }, 2, 1, 1, 3, "metamorphic", "dolomite"));
		CommonProxy.parsed.add(new WorldGeneratorVein(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:coal"))),
				"lignite", new int[] { 0 }, 2, 3, 1, 8, "sedimentary"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:coal"))),
				"bitumin", new int[] { 0 }, 10, 1, 15, 2, "sedimentary"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:redstone"))),
				"redstone", new int[] { 0 }, 10, 1, 7, 2, "igneous"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:emerald"))),
				"emerald", new int[] { 0 }, 5, 1, 7, 2, "granite", "schist", "marble"));
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:lapis"))),
				"lapis", new int[] { 0 }, 5, 1, 7, 2, "intrusive", "marble"));

		CommonProxy.parsed.add(new WorldGeneratorFluid("oil", new int[] { 0 }, 5, 12, new String[] { "metamorphic" },
				FluidRegistry.getFluid("oil")));

		CommonProxy.parsed.add(new WorldGeneratorKimberlite("kimberlite", new int[] { 0 }, 20));
	}

	@SubscribeEvent
	public static void registerRegistry(RegistryEvent.NewRegistry event) {
		new RegistryBuilder<Ore>().setType(Ore.class).setName(new ResourceLocation(Reference.MODID, "OreRegistry"))
				.setIDRange(0, 512).create();
		Ore.REGISTRY = GameRegistry.findRegistry(Ore.class);

		new RegistryBuilder<Material>().setType(Material.class)
				.setName(new ResourceLocation(Reference.MODID, "MaterialRegistry")).setIDRange(0, 512).create();
		Material.REGISTRY = GameRegistry.findRegistry(Material.class);

		new RegistryBuilder<Research>().setType(Research.class)
				.setName(new ResourceLocation(Reference.MODID, "ResearchRegistry")).setIDRange(0, 2048).create();
		Research.REGISTRY = GameRegistry.findRegistry(Research.class);
	}

	@SubscribeEvent
	public static void registerResearch(RegistryEvent.Register<Research> event) {
		registerResearchTasks();
		Research bronze = new Research("bronze");
		bronze.addTask(new ResourceLocation(Reference.RESEARCH_CRAFTING, "crushedTinOre"), 5);
		bronze.addTask(new ResourceLocation(Reference.RESEARCH_CRAFTING, "crushedCopperOre"), 5);
		bronze.registerResearch();

		Research brass = new Research("brass");
		brass.addTask(new ResourceLocation(Reference.RESEARCH_CRAFTING, "crushedZincOre"), 5);
		brass.addTask(new ResourceLocation(Reference.RESEARCH_CRAFTING, "crushedCopperOre"), 5);
		brass.registerResearch();

		Research metalTools = new Research("metalTools");
		metalTools.addTask(new ResourceLocation(Reference.RESEARCH_CRAFTING, "toolMaking"), 5);
		metalTools.registerResearch();
	}

	private static void registerResearchTasks() {

		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "toolMaking", new MaterialToolIngredient(MaterialTool.AXE));
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "toolMaking",
				new MaterialToolIngredient(MaterialTool.HAMMER));
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "toolMaking",
				new MaterialToolIngredient(MaterialTool.SHOVEL));
		ResearchTasks.addTask(Reference.RESEARCH_CRAFTING, "toolMaking",
				new MaterialToolIngredient(MaterialTool.PICKAXE));

		ResearchTasks.addTask(Reference.RESEARCH_VANILLA_FURNACE, "potato", new ItemStack(Items.BAKED_POTATO));
	}
}
