package com.supertechgroup.core;

import com.supertechgroup.core.proxy.CommonProxy;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorBase;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorCluster;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorPlate;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorVein;
import com.supertechgroup.core.worldgen.ores.Ore;
import com.supertechgroup.core.worldgen.ores.OreBlock;
import com.supertechgroup.core.worldgen.rocks.BlockRock;
import com.supertechgroup.core.worldgen.rocks.RockManager;
import com.supertechgroup.core.worldgen.rocks.StateMapperRock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
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

	public static OreBlock superore;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		superore = new OreBlock();
		event.getRegistry().register(superore);

		// Rocks

		RockManager.addRockTypes(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE),
				"igneous", "andesite", "vanilla", "extrusive", "intermediate");
		RockManager.addTextureOverride(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE),
				new ResourceLocation("minecraft:blocks/andesite"));
		createStoneType("basalt", 5, 100, 2, event, "igneous", "extrusive");
		RockManager.addRockTypes(Blocks.SANDSTONE.getDefaultState(), "sedimentary", "sandstone", "vanilla", "clastic");
		RockManager.addRockTypes(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), "igneous",
				"diorite", "vanilla", "intrusive", "intermediate");
		RockManager.addTextureOverride(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE),
				new ResourceLocation("minecraft:blocks/diorite"));
		RockManager.addRockTypes(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), "igneous",
				"granite", "vanilla", "felsic", "intrusive");
		RockManager.addTextureOverride(
				Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE),
				new ResourceLocation("minecraft:blocks/granite"));
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
	}

	@SideOnly(Side.CLIENT)
	public static void initItemModels() {
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		// TODO Auto-generated method stub

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
		final Block rock;
		// rockCobble = new BlockRock(name + "cobble", true, (float) hardness, (float)
		// blastResistance, toolHardnessLevel,SoundType.STONE);

		ItemBlock itemBlock /*
							 * = (ItemBlock) new
							 * ItemBlock(rockCobble).setRegistryName(rockCobble.getRegistryName())
							 */;
		// ForgeRegistries.ITEMS.register(itemBlock);
		rock = new BlockRock(name, true, (float) hardness, (float) blastResistance, toolHardnessLevel,
				SoundType.STONE) {
			/*
			 * @Override public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos,
			 * IBlockState state, int fortune) { return Arrays.asList(new
			 * ItemStack(Item.getItemFromBlock(rockCobble)));
			 *
			 * }
			 */
		};

		String[] newArray = new String[types.length + 1];
		System.arraycopy(types, 0, newArray, 0, types.length);
		newArray[newArray.length - 1] = name;
		RockManager.addRockTypes(rock.getDefaultState(), newArray);

		/*
		 * rockStairs = new BlockRockStairs(name + "_stairs", rock, (float) hardness,
		 * (float) blastResistance, toolHardnessLevel, SoundType.STONE); rockSlab = new
		 * BlockRockSlab.Half(name + "_slab", (float) hardness, (float) blastResistance,
		 * toolHardnessLevel, SoundType.STONE); rockSlabDouble = new
		 * BlockRockSlab.Double(name + "_slab_double", (float) hardness, (float)
		 * blastResistance, toolHardnessLevel, SoundType.STONE);
		 */

		itemBlock = (ItemBlock) new ItemBlock(rock).setRegistryName(rock.getRegistryName());
		ForgeRegistries.ITEMS.register(itemBlock);

		// GameRegistry.addSmelting(rock, new ItemStack(Blocks.STONE), 0.1F);
		event.getRegistry().registerAll(rock/* , rockCobble , rockStairs, rockSlab, rockSlabDouble */);

		/*
		 * GameRegistry.findRegistry(IRecipe.class).register(new ShapedOreRecipe(new
		 * ResourceLocation("stairs"), new ItemStack(rockStairs, 4), "x  ", "xx ",
		 * "xxx", 'x', rock)); GameRegistry.findRegistry(IRecipe.class).register( new
		 * ShapedOreRecipe(new ResourceLocation("slabs"), new ItemStack(rockSlab, 6),
		 * "xxx", 'x', rock));
		 *
		 * GameRegistry.findRegistry(IRecipe.class).register( new ShapedOreRecipe(new
		 * ResourceLocation("blocks"), new ItemStack(brick, 4), "xx", "xx", 'x', rock));
		 * GameRegistry.findRegistry(IRecipe.class).register(new ShapedOreRecipe(new
		 * ResourceLocation("stairs"), new ItemStack(brickStairs, 4), "x  ", "xx ",
		 * "xxx", 'x', brick)); GameRegistry.findRegistry(IRecipe.class).register( new
		 * ShapedOreRecipe(new ResourceLocation("slabs"), new ItemStack(brickSlab, 6),
		 * "xxx", 'x', brick));
		 *
		 * GameRegistry.findRegistry(IRecipe.class) .register(new ShapedOreRecipe(new
		 * ResourceLocation("blocks"), new ItemStack(smooth, 1), rock, "sand"));
		 * GameRegistry.findRegistry(IRecipe.class).register(new ShapedOreRecipe(new
		 * ResourceLocation("stairs"), new ItemStack(smoothStairs, 4), "x  ", "xx ",
		 * "xxx", 'x', smooth)); GameRegistry.findRegistry(IRecipe.class).register( new
		 * ShapedOreRecipe(new ResourceLocation("slabs"), new ItemStack(smoothSlab, 6),
		 * "xxx", 'x', smooth)); GameRegistry.findRegistry(IRecipe.class).register(new
		 * ShapedOreRecipe(new ResourceLocation("blocks"), new ItemStack(smoothBrick,
		 * 4), "xx", "xx", 'x', smooth));
		 * GameRegistry.findRegistry(IRecipe.class).register(new ShapedOreRecipe(new
		 * ResourceLocation("stairs"), new ItemStack(smoothBrickStairs, 4), "x  ",
		 * "xx ", "xxx", 'x', smoothBrick));
		 * GameRegistry.findRegistry(IRecipe.class).register(new ShapedOreRecipe(new
		 * ResourceLocation("slabs"), new ItemStack(smoothBrickSlab, 6), "xxx", 'x',
		 * smoothBrick));
		 */

		OreDictionary.registerOre("stone", rock);
		// OreDictionary.registerOre("cobblestone", rockCobble);

		final Item item = Item.getItemFromBlock(rock);
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(new ResourceLocation(rock.getRegistryName().getResourceDomain(), "rock"),
						rock.getRegistryName().getResourcePath()));
		ModelLoader.setCustomStateMapper(rock, new StateMapperRock());
	}

	@SubscribeEvent
	public static void registerOres(RegistryEvent.Register<Ore> event) {
		new Ore("Bauxite", 0x7CFC00).registerOre();
		new Ore("Bornite", 0x8B4513).registerOre();
		new Ore("Magnetite", 0x494d51).registerOre();
		new Ore("Limonite", 0xFFF44F).registerOre();
		new Ore("Chalcocite", 0x2F4F4F).registerOre();
		new Ore("Cassiterite", 0x654321).registerOre();
		new Ore("Chromite", 0xC0C0CC).registerOre();
		new Ore("Cinnabar", 0x8b0017).registerOre();
		new Ore("Cobaltite", 0xd2b48c).registerOre();
		new Ore("Galena", 0xbeb2b2).registerOre();
		new Ore("Hematite", 0x101c1f).registerOre();
		new Ore("Ilmenite", 0x323230).registerOre();
		new Ore("Sphalerite", 0x323230).registerOre();
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

		CommonProxy.parsed.add(new WorldGeneratorPlate(
				WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:galena"))),
				"galena", new int[] { 0 }, 10, 10, "extrusive", "metamorphic", "granite", "sandstone"));
/*
		CommonProxy.parsed.add(new WorldGeneratorCluster(
				WorldGeneratorBase
						.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:nativeAluminum"))),
				"nativeAluminum", new int[] { 0 }, 15, 1, 1, 5, "extrusive"));*/
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
				WorldGeneratorBase
						.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:cassiterite"))),
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
	}

	@SubscribeEvent
	public static void registerRegistry(RegistryEvent.NewRegistry event) {
		new RegistryBuilder<Ore>().setType(Ore.class).setName(new ResourceLocation(Reference.MODID, "OreRegistry"))
				.setIDRange(0, 512).create();
		Ore.REGISTRY = GameRegistry.findRegistry(Ore.class);
	}
}
