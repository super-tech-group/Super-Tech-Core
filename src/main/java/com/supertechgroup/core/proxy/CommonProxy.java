package com.supertechgroup.core.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.supertechgroup.core.Config;
import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.capabilities.team.ITeamCapability;
import com.supertechgroup.core.capabilities.team.TeamCapability;
import com.supertechgroup.core.capabilities.team.TeamCapabilityStorage;
import com.supertechgroup.core.capabilities.teamlist.IListCapability;
import com.supertechgroup.core.capabilities.teamlist.ListCapability;
import com.supertechgroup.core.capabilities.teamlist.ListCapabilityStorage;
import com.supertechgroup.core.items.ItemResearchBook;
import com.supertechgroup.core.items.MaterialItem;
import com.supertechgroup.core.items.MaterialTool;
import com.supertechgroup.core.machinery.basicsmelter.MultiblockBasicSmelter;
import com.supertechgroup.core.machinery.multiblock.MultiblockHandler;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.recipies.ShapelessResearchRecipe;
import com.supertechgroup.core.research.Research;
import com.supertechgroup.core.research.ResearchEvents;
import com.supertechgroup.core.worldgen.WorldGenEvents;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorBase;
import com.supertechgroup.core.worldgen.ores.Ore;
import com.supertechgroup.core.worldgen.ores.OreItem;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class CommonProxy {
	public static Configuration config;
	public static SimpleNetworkWrapper simpleNetworkWrapper;
	public static ArrayList<IBlockState> vanillaReplace = new ArrayList<>();
	public static ArrayList<WorldGeneratorBase> parsed = new ArrayList<>();

	private File configFolder;

	private void addBasicRecipies() {
		// add basic tool recipies

		Material stone = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":stone"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(
						new ShapedOreRecipe(new ResourceLocation("hammers"),
								new ItemStack(stone.getItemHammer(), 1, MaterialTool.HAMMER),
								new Object[] { new String[] { " x ", " sx", "s  " }, 'x',
										new OreIngredient("cobblestone"), 's', new OreIngredient("stickWood") })
												.setRegistryName(Reference.MODID, "hammerStone"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(
						new ShapedOreRecipe(new ResourceLocation("axes"),
								new ItemStack(stone.getItemAxe(), 1, MaterialTool.AXE),
								new Object[] { new String[] { " xx", " sx", " s " }, 'x',
										new OreIngredient("cobblestone"), 's', new OreIngredient("stickWood") })
												.setRegistryName(Reference.MODID, "axeStone"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(
						new ShapedOreRecipe(new ResourceLocation("shovels"),
								new ItemStack(stone.getItemShovel(), 1, MaterialTool.SHOVEL),
								new Object[] { new String[] { " x ", " s ", " s " }, 'x',
										new OreIngredient("cobblestone"), 's', new OreIngredient("stickWood") })
												.setRegistryName(Reference.MODID, "shovelStone"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(
						new ShapedOreRecipe(new ResourceLocation("pickaxes"),
								new ItemStack(stone.getItemPickaxe(), 1, MaterialTool.PICKAXE),
								new Object[] { new String[] { "xxx", " s ", " s " }, 'x',
										new OreIngredient("cobblestone"), 's', new OreIngredient("stickWood") })
												.setRegistryName(Reference.MODID, "pickaxeStone"));

		Material wood = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":wood"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(
						new ShapedOreRecipe(new ResourceLocation("axes"),
								new ItemStack(wood.getItemAxe(), 1, MaterialTool.AXE),
								new Object[] { new String[] { " xx", " sx", " s " }, 'x',
										new OreIngredient("plankWood"), 's', new OreIngredient("stickWood") })
												.setRegistryName(Reference.MODID, "axeWood"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(
						new ShapedOreRecipe(new ResourceLocation("shovels"),
								new ItemStack(wood.getItemShovel(), 1, MaterialTool.SHOVEL),
								new Object[] { new String[] { " x ", " s ", " s " }, 'x',
										new OreIngredient("plankWood"), 's', new OreIngredient("stickWood") })
												.setRegistryName(Reference.MODID, "shovelWood"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(
						new ShapedOreRecipe(new ResourceLocation("pickaxes"),
								new ItemStack(wood.getItemPickaxe(), 1, MaterialTool.PICKAXE),
								new Object[] { new String[] { "xxx", " s ", " s " }, 'x',
										new OreIngredient("plankWood"), 's', new OreIngredient("stickWood") })
												.setRegistryName(Reference.MODID, "pickaxeWood"));

		// basic ingot/nuggets
		Item ironItem = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":iron")).getMaterialItem();
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation("ingots"),
						new ItemStack(ironItem, 1, MaterialItem.INGOT),
						new Object[] { new String[] { "xxx", "xxx", "xxx" }, 'x', new OreIngredient("nuggetIron") })
								.setRegistryName(Reference.MODID, "ironIngot"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(new ResourceLocation("nuggets"),
				new ItemStack(ironItem, 9, MaterialItem.NUGGET), new Object[] { new OreIngredient("ingotIron") })
						.setRegistryName(Reference.MODID, "ironNugget"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation("plates"),
						new ItemStack(ironItem, 1, MaterialItem.PLATE), new Object[] { new OreIngredient("ingotIron"),
								new OreIngredient("ingotIron"), new OreIngredient("toolHammer") })
										.setRegistryName(Reference.MODID, "ironPlate"));

		Item tinItem = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":tin")).getMaterialItem();
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation("ingots"),
						new ItemStack(tinItem, 1, MaterialItem.INGOT),
						new Object[] { new String[] { "xxx", "xxx", "xxx" }, 'x', new OreIngredient("nuggetTin") })
								.setRegistryName(Reference.MODID, "tinIngot"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation("nuggets"),
						new ItemStack(tinItem, 9, MaterialItem.NUGGET), new Object[] { new OreIngredient("ingotTin") })
								.setRegistryName(Reference.MODID, "tinNugget"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation("plates"),
						new ItemStack(tinItem, 1, MaterialItem.PLATE), new Object[] { new OreIngredient("ingotTin"),
								new OreIngredient("ingotTin"), new OreIngredient("toolHammer") })
										.setRegistryName(Reference.MODID, "tinPlate"));

		Item copperItem = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":copper"))
				.getMaterialItem();
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation("ingots"),
						new ItemStack(copperItem, 1, MaterialItem.INGOT),
						new Object[] { new String[] { "xxx", "xxx", "xxx" }, 'x', new OreIngredient("nuggetCopper") })
								.setRegistryName(Reference.MODID, "copperIngot"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(new ResourceLocation("nuggets"),
				new ItemStack(copperItem, 9, MaterialItem.NUGGET), new Object[] { new OreIngredient("ingotCopper") })
						.setRegistryName(Reference.MODID, "copperNugget"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(new ResourceLocation("plates"),
				new ItemStack(copperItem, 1, MaterialItem.PLATE), new Object[] { new OreIngredient("ingotCopper"),
						new OreIngredient("ingotCopper"), new OreIngredient("toolHammer") })
								.setRegistryName(Reference.MODID, "copperPlate"));

		Item zincItem = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":zinc")).getMaterialItem();
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation("ingots"),
						new ItemStack(zincItem, 1, MaterialItem.INGOT),
						new Object[] { new String[] { "xxx", "xxx", "xxx" }, 'x', new OreIngredient("nuggetZinc") })
								.setRegistryName(Reference.MODID, "zincIngot"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(new ResourceLocation("nuggets"),
				new ItemStack(zincItem, 9, MaterialItem.NUGGET), new Object[] { new OreIngredient("ingotZinc") })
						.setRegistryName(Reference.MODID, "zincNugget"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation("plates"),
						new ItemStack(zincItem, 1, MaterialItem.PLATE), new Object[] { new OreIngredient("ingotZinc"),
								new OreIngredient("ingotZinc"), new OreIngredient("toolHammer") })
										.setRegistryName(Reference.MODID, "zincPlate"));

		// basic ore processing
		Ore magnetite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":magnetite"));
		GameRegistry.addSmelting(new ItemStack(magnetite.getItemOre(), 1, OreItem.ORE),
				new ItemStack(ironItem, 3, MaterialItem.NUGGET), 0.3f);
		GameRegistry.addSmelting(new ItemStack(magnetite.getItemOre(), 1, OreItem.CRUSHED),
				new ItemStack(ironItem, 5, MaterialItem.NUGGET), 0.5f);

		Ore limonite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":limonite"));
		GameRegistry.addSmelting(new ItemStack(limonite.getItemOre(), 1, OreItem.ORE),
				new ItemStack(ironItem, 3, MaterialItem.NUGGET), 0.3f);
		GameRegistry.addSmelting(new ItemStack(limonite.getItemOre(), 1, OreItem.CRUSHED),
				new ItemStack(ironItem, 5, MaterialItem.NUGGET), 0.5f);

		Ore hematite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":hematite"));
		GameRegistry.addSmelting(new ItemStack(hematite.getItemOre(), 1, OreItem.ORE),
				new ItemStack(ironItem, 3, MaterialItem.NUGGET), 0.3f);
		GameRegistry.addSmelting(new ItemStack(hematite.getItemOre(), 1, OreItem.CRUSHED),
				new ItemStack(ironItem, 5, MaterialItem.NUGGET), 0.5f);

		Ore cassiterite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":cassiterite"));
		GameRegistry.addSmelting(new ItemStack(cassiterite.getItemOre(), 1, OreItem.ORE),
				new ItemStack(tinItem, 3, MaterialItem.NUGGET), 0.3f);
		GameRegistry.addSmelting(new ItemStack(cassiterite.getItemOre(), 1, OreItem.CRUSHED),
				new ItemStack(tinItem, 5, MaterialItem.NUGGET), 0.5f);

		Ore chalcocite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":chalcocite"));
		GameRegistry.addSmelting(new ItemStack(chalcocite.getItemOre(), 1, OreItem.ORE),
				new ItemStack(copperItem, 3, MaterialItem.NUGGET), 0.3f);
		GameRegistry.addSmelting(new ItemStack(chalcocite.getItemOre(), 1, OreItem.CRUSHED),
				new ItemStack(copperItem, 5, MaterialItem.NUGGET), 0.5f);

		Ore bornite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":bornite"));
		GameRegistry.addSmelting(new ItemStack(bornite.getItemOre(), 1, OreItem.ORE),
				new ItemStack(copperItem, 3, MaterialItem.NUGGET), 0.3f);
		GameRegistry.addSmelting(new ItemStack(bornite.getItemOre(), 1, OreItem.CRUSHED),
				new ItemStack(copperItem, 5, MaterialItem.NUGGET), 0.5f);

		Ore sphalerite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":sphalerite"));
		GameRegistry.addSmelting(new ItemStack(sphalerite.getItemOre(), 1, OreItem.ORE),
				new ItemStack(zincItem, 3, MaterialItem.NUGGET), 0.3f);
		GameRegistry.addSmelting(new ItemStack(sphalerite.getItemOre(), 1, OreItem.CRUSHED),
				new ItemStack(zincItem, 5, MaterialItem.NUGGET), 0.5f);

		// other recipies
		IForgeRegistry<IRecipe> recipeRegistry = GameRegistry.findRegistry(IRecipe.class);
		recipeRegistry.register(
				new ShapedOreRecipe(new ResourceLocation("research"), new ItemStack(ModRegistry.researchStation),
						new Object[] { new String[] { "bxb", "bcb" }, 'x', new ItemStack(Items.WRITABLE_BOOK), 'c',
								new ItemStack(Blocks.CRAFTING_TABLE), 'b', new ItemStack(Items.BOOK) })
										.setRegistryName(Reference.MODID, "basicResearchStation"));
		recipeRegistry.register(new ShapelessOreRecipe(new ResourceLocation("research"),
				ItemResearchBook.getEmptyBookStack(), new Object[] { new ItemStack(Items.WRITABLE_BOOK) })
						.setRegistryName(Reference.MODID, "emptyResearchBook"));

		ShapelessResearchRecipe dirtyBronzeDust = new ShapelessResearchRecipe(new ResourceLocation("crudePowderMixing"),
				new ItemStack(
						Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")).getMaterialItem(),
						5, MaterialItem.DIRTY),
				new Object[] { new OreIngredient("crushedCopperOre"), new OreIngredient("crushedCopperOre"),
						new OreIngredient("crushedCopperOre"), new OreIngredient("crushedCopperOre"),
						new OreIngredient("crushedCopperOre"), new OreIngredient("crushedCopperOre"),
						new OreIngredient("crushedCopperOre"), new OreIngredient("crushedTinOre") });
		dirtyBronzeDust.addResearchUnlock(Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")));
		recipeRegistry.register(dirtyBronzeDust.setRegistryName(Reference.MODID, "oreDirtyBronzeDust"));

	}

	public World getWorld() {
		return getWorld(null);
	}

	public abstract World getWorld(IBlockAccess world);

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(SuperTechCoreMod.instance, new GuiProxy());

		// remove unwanted vanilla items
		ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
		List<Item> disabledItems = Arrays.asList(ModRegistry.disabledVanillaItems);
		for (IRecipe r : Lists.newArrayList(recipeRegistry)) {
			if (disabledItems.contains(r.getRecipeOutput().getItem())) {
				recipeRegistry.remove(r.getRegistryName());
			}
		}

		// setup geology
		vanillaReplace.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE));
		vanillaReplace
				.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE));
		vanillaReplace
				.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE));
		vanillaReplace
				.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE));

		addBasicRecipies();
	}

	public void postInit(FMLPostInitializationEvent event) {
		Config.parseTypes();
		MultiblockHandler.registerMultiblock(new MultiblockBasicSmelter());
	}

	public void preInit(FMLPreInitializationEvent e) {

		PacketHandler.registerMessages(Reference.MODID + "Chan");
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("MBEchannel");
		configFolder = new File(e.getModConfigurationDirectory().toString() + "/" + Reference.MODID + "/");
		config = new Configuration(new File(configFolder.getPath(), "config.cfg"));
		Config.readConfig(configFolder);

		WorldGenEvents wge = new WorldGenEvents();
		MinecraftForge.EVENT_BUS.register(wge);
		MinecraftForge.ORE_GEN_BUS.register(wge);

		ResearchEvents re = new ResearchEvents();
		MinecraftForge.EVENT_BUS.register(re);

		CapabilityManager.INSTANCE.register(ITeamCapability.class, new TeamCapabilityStorage(), TeamCapability.class);
		CapabilityManager.INSTANCE.register(IListCapability.class, new ListCapabilityStorage(), ListCapability.class);
	}

	public abstract void registerItemRenderer(Item item, int i, String name);

	public void registerModels(Material material) {
	}

	public void registerModels(Ore ore) {
	}
}
