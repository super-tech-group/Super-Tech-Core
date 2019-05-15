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
import com.supertechgroup.core.items.SuperTechItem;
import com.supertechgroup.core.machinery.basicsmelter.MultiblockBasicSmelter;
import com.supertechgroup.core.machinery.multiblock.MultiblockHandler;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.recipe.BasicSmelterRecipe;
import com.supertechgroup.core.recipe.ShapedResearchRecipe;
import com.supertechgroup.core.recipe.ShapelessResearchRecipe;
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
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation(Reference.MODID, "flux"),
						new ItemStack(ModRegistry.itemTech, 4, SuperTechItem.FLUX),
						new Object[] { new OreIngredient("fluxStone"), new OreIngredient("toolHammer") })
								.setRegistryName(Reference.MODID, "fluxdust"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MODID, "constructor"),
						new ItemStack(ModRegistry.itemConstructor),
						new Object[] { new String[] { " x ", "xsx", " s " }, 's', new ItemStack(Items.STICK), 'x',
								new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FIRE_BRICK) })
										.setRegistryName(Reference.MODID, "constructor"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation(Reference.MODID, "fireclay"),
						new ItemStack(ModRegistry.itemTech, 4, SuperTechItem.FIRE_CLAY),
						new Object[] { new OreIngredient("sand"), new ItemStack(Items.CLAY_BALL),
								new ItemStack(Items.CLAY_BALL), new ItemStack(Items.CLAY_BALL) })
										.setRegistryName(Reference.MODID, "fireclay"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MODID, "crude_heater"),
						new ItemStack(ModRegistry.crudeHeaterBlock),
						new Object[] { new String[] { "xxx", "xfx", "xxx" }, 'x',
								new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FIRE_BRICK), 'f',
								new ItemStack(Blocks.FURNACE) }).setRegistryName(Reference.MODID, "crude_heater"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MODID, "crude_io"),
						new ItemStack(ModRegistry.crudeIOBlock),
						new Object[] { new String[] { "x x", "xxx", "x x" }, 'x',
								new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FIRE_BRICK) })
										.setRegistryName(Reference.MODID, "crude_io"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation(Reference.MODID, "crude_wall"),
						new ItemStack(ModRegistry.crudeWallBlock),
						new Object[] { new String[] { "xxx", "x x", "xxx" }, 'x',
								new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FIRE_BRICK) })
										.setRegistryName(Reference.MODID, "crude_wall"));

		GameRegistry.addSmelting(new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FIRE_CLAY),
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FIRE_BRICK), 0.5f);

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

		Material bronze = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":bronze"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("axes"),
						new ItemStack(bronze.getItemAxe(), 1, MaterialTool.AXE),
						new Object[] { new String[] { " xx", " sx", " s " }, 'x', new OreIngredient("ingotBronze"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "axeBronze"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("shovels"),
						new ItemStack(bronze.getItemShovel(), 1, MaterialTool.SHOVEL),
						new Object[] { new String[] { " x ", " s ", " s " }, 'x', new OreIngredient("ingotBronze"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "shovelBronze"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("pickaxes"),
						new ItemStack(bronze.getItemPickaxe(), 1, MaterialTool.PICKAXE),
						new Object[] { new String[] { "xxx", " s ", " s " }, 'x', new OreIngredient("ingotBronze"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "pickaxeBronze"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("hammers"),
						new ItemStack(bronze.getItemHammer(), 1, MaterialTool.HAMMER),
						new Object[] { new String[] { " x ", " sx", "s  " }, 'x', new OreIngredient("ingotBronze"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "hammerBronze"));

		Material brass = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":brass"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("axes"),
						new ItemStack(brass.getItemAxe(), 1, MaterialTool.AXE),
						new Object[] { new String[] { " xx", " sx", " s " }, 'x', new OreIngredient("ingotBrass"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "axeBrass"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("shovels"),
						new ItemStack(brass.getItemShovel(), 1, MaterialTool.SHOVEL),
						new Object[] { new String[] { " x ", " s ", " s " }, 'x', new OreIngredient("ingotBrass"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "shovelBrass"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("pickaxes"),
						new ItemStack(brass.getItemPickaxe(), 1, MaterialTool.PICKAXE),
						new Object[] { new String[] { "xxx", " s ", " s " }, 'x', new OreIngredient("ingotBrass"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "pickaxeBrass"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("hammers"),
						new ItemStack(brass.getItemHammer(), 1, MaterialTool.HAMMER),
						new Object[] { new String[] { " x ", " sx", "s  " }, 'x', new OreIngredient("ingotBrass"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")),
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "hammerBrass"));

		Material copper = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":copper"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("axes"),
						new ItemStack(copper.getItemAxe(), 1, MaterialTool.AXE),
						new Object[] { new String[] { " xx", " sx", " s " }, 'x', new OreIngredient("ingotCopper"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "axeCopper"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("shovels"),
						new ItemStack(copper.getItemShovel(), 1, MaterialTool.SHOVEL),
						new Object[] { new String[] { " x ", " s ", " s " }, 'x', new OreIngredient("ingotCopper"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "shovelCopper"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("pickaxes"),
						new ItemStack(copper.getItemPickaxe(), 1, MaterialTool.PICKAXE),
						new Object[] { new String[] { "xxx", " s ", " s " }, 'x', new OreIngredient("ingotCopper"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "pickaxeCopper"));
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("hammers"),
						new ItemStack(copper.getItemHammer(), 1, MaterialTool.HAMMER),
						new Object[] { new String[] { " x ", " sx", "s  " }, 'x', new OreIngredient("ingotCopper"), 's',
								new OreIngredient("stickWood") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "metalTools")))
								.setRegistryName(Reference.MODID, "hammerCopper"));

		// basic ingot/nuggets
		Item bronzeItem = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":bronze"))
				.getMaterialItem();
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("ingots"),
						new ItemStack(bronzeItem, 1, MaterialItem.INGOT),
						new Object[] { new String[] { "xxx", "xxx", "xxx" }, 'x', new OreIngredient("nuggetBronze") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")))
								.setRegistryName(Reference.MODID, "bronzeIngot"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessResearchRecipe(new ResourceLocation("nuggets"),
				new ItemStack(bronzeItem, 9, MaterialItem.NUGGET), new Object[] { new OreIngredient("ingotBronze") },
				Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")))
						.setRegistryName(Reference.MODID, "bronzeNugget"));

		Item brassItem = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":brass")).getMaterialItem();
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedResearchRecipe(new ResourceLocation("ingots"),
						new ItemStack(brassItem, 1, MaterialItem.INGOT),
						new Object[] { new String[] { "xxx", "xxx", "xxx" }, 'x', new OreIngredient("nuggetBrass") },
						Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")))
								.setRegistryName(Reference.MODID, "brassIngot"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessResearchRecipe(new ResourceLocation("nuggets"),
				new ItemStack(brassItem, 9, MaterialItem.NUGGET), new Object[] { new OreIngredient("ingotBrass") },
				Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")))
						.setRegistryName(Reference.MODID, "brassNugget"));

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

		Item leadItem = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":lead"))
				.getMaterialItem();
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapedOreRecipe(new ResourceLocation("ingots"),
						new ItemStack(leadItem, 1, MaterialItem.INGOT),
						new Object[] { new String[] { "xxx", "xxx", "xxx" }, 'x', new OreIngredient("nuggetLead") })
								.setRegistryName(Reference.MODID, "leadIngot"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(new ResourceLocation("nuggets"),
				new ItemStack(leadItem, 9, MaterialItem.NUGGET), new Object[] { new OreIngredient("ingotLead") })
						.setRegistryName(Reference.MODID, "leadNugget"));
		GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(new ResourceLocation("plates"),
				new ItemStack(leadItem, 1, MaterialItem.PLATE), new Object[] { new OreIngredient("ingotLead"),
						new OreIngredient("ingotLead"), new OreIngredient("toolHammer") })
								.setRegistryName(Reference.MODID, "leadPlate"));
		
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
		BasicSmelterRecipe magnetiteSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(magnetite.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(ironItem, 5, MaterialItem.NUGGET), 1811, 27.75, 572.22);
		BasicSmelterRecipe.registerRecipe(magnetiteSmelt, new ResourceLocation(Reference.MODID, "crushed_magnetite"));

		Ore limonite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":limonite"));
		BasicSmelterRecipe limoniteSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(limonite.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(ironItem, 5, MaterialItem.NUGGET), 1811, 17.77, 388.88);
		BasicSmelterRecipe.registerRecipe(limoniteSmelt, new ResourceLocation(Reference.MODID, "crushed_limonite"));

		Ore hematite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":hematite"));
		BasicSmelterRecipe hematiteSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(hematite.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(ironItem, 5, MaterialItem.NUGGET), 1811, 22.83, 583.88);
		BasicSmelterRecipe.registerRecipe(hematiteSmelt, new ResourceLocation(Reference.MODID, "crushed_hematite"));

		Ore cassiterite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":cassiterite"));
		BasicSmelterRecipe tinSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(cassiterite.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(tinItem, 5, MaterialItem.NUGGET), 505.1, 34.95, 295.26);
		BasicSmelterRecipe.registerRecipe(tinSmelt, new ResourceLocation(Reference.MODID, "crushed_cassiterite"));

		Ore chalcocite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":chalcocite"));
		BasicSmelterRecipe chalcociteSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(chalcocite.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(copperItem, 5, MaterialItem.NUGGET), 1358, 48.37, 644.44);
		BasicSmelterRecipe.registerRecipe(chalcociteSmelt, new ResourceLocation(Reference.MODID, "crushed_chalcocite"));

		Ore bornite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":bornite"));
		BasicSmelterRecipe borniteSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(bornite.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(copperItem, 5, MaterialItem.NUGGET), 1358, 43.29, 565.55);
		BasicSmelterRecipe.registerRecipe(borniteSmelt, new ResourceLocation(Reference.MODID, "crushed_bornite"));

		Ore sphalerite = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":sphalerite"));
		BasicSmelterRecipe sphaleriteSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(sphalerite.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(zincItem, 5, MaterialItem.NUGGET), 692.7, 15.02, 445.55);
		BasicSmelterRecipe.registerRecipe(sphaleriteSmelt, new ResourceLocation(Reference.MODID, "crushed_sphalerite"));

		Ore galena = Ore.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":galena"));
		BasicSmelterRecipe galenaSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(galena.getItemOre(), 1, OreItem.CRUSHED) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(leadItem, 5, MaterialItem.NUGGET), 1423, 23.12, 345.55);
		BasicSmelterRecipe.registerRecipe(galenaSmelt, new ResourceLocation(Reference.MODID, "crushed_galena"));
		
		BasicSmelterRecipe bronzeSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze"))
								.getMaterialItem(), 1, MaterialItem.DIRTY) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(
						Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "bronze")).getMaterialItem(),
						5, MaterialItem.NUGGET),
				1186.15, 30, 435.62);
		BasicSmelterRecipe.registerRecipe(bronzeSmelt, new ResourceLocation(Reference.MODID, "poor_bronze"));

		BasicSmelterRecipe brassSmelt = new BasicSmelterRecipe(
				new ItemStack[] { new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.FLUX),
						new ItemStack(Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass"))
								.getMaterialItem(), 1, MaterialItem.DIRTY) },
				new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG),
				new ItemStack(
						Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")).getMaterialItem(), 5,
						MaterialItem.NUGGET),
				1186.15, 30, 425.62);
		BasicSmelterRecipe.registerRecipe(brassSmelt, new ResourceLocation(Reference.MODID, "poor_brass"));

		// other recipies
		IForgeRegistry<IRecipe> recipeRegistry = GameRegistry.findRegistry(IRecipe.class);
		recipeRegistry
				.register(
						new ShapedOreRecipe(new ResourceLocation("buckets"), new ItemStack(Items.BUCKET),
								new Object[] { new String[] { "bxb", " b " }, 'x', new OreIngredient("toolHammer"), 'b',
										new OreIngredient("ingotCopper") }).setRegistryName(Reference.MODID,
												"copperBucket"));
		recipeRegistry.register(
				new ShapedOreRecipe(new ResourceLocation("research"), new ItemStack(ModRegistry.researchStation),
						new Object[] { new String[] { "bxb", "bcb" }, 'x', new ItemStack(Items.WRITABLE_BOOK), 'c',
								new ItemStack(Blocks.CRAFTING_TABLE), 'b', new ItemStack(Items.BOOK) })
										.setRegistryName(Reference.MODID, "basicResearchStation"));
		recipeRegistry.register(new ShapelessOreRecipe(new ResourceLocation("research"),
				ItemResearchBook.getEmptyBookStack(), new Object[] { new ItemStack(Items.WRITABLE_BOOK) })
						.setRegistryName(Reference.MODID, "emptyResearchBook"));
		recipeRegistry
				.register(
						new ShapelessOreRecipe(new ResourceLocation("paper"),
								new ItemStack(ModRegistry.itemTech, 2, SuperTechItem.WOOD_PULP),
								new Object[] { new OreIngredient("logWood"), new OreIngredient("toolHammer"),
										new ItemStack(Items.WATER_BUCKET) }).setRegistryName(Reference.MODID,
												"woodenPaper"));
		GameRegistry.addSmelting(new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.WOOD_PULP),
				new ItemStack(Items.PAPER, 2), 0.15f);

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

		ShapelessResearchRecipe dirtyBrassDust = new ShapelessResearchRecipe(new ResourceLocation("crudePowderMixing"),
				new ItemStack(
						Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")).getMaterialItem(), 3,
						MaterialItem.DIRTY),
				new Object[] { new OreIngredient("crushedCopperOre"), new OreIngredient("crushedCopperOre"),
						new OreIngredient("crushedCopperOre"), new OreIngredient("crushedCopperOre"),
						new OreIngredient("crushedZincOre") });
		dirtyBrassDust.addResearchUnlock(Research.REGISTRY.getValue(new ResourceLocation(Reference.MODID, "brass")));
		recipeRegistry.register(dirtyBrassDust.setRegistryName(Reference.MODID, "oreDirtyBrassDust"));

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

		ModRegistry.registerFluids();
	}

	public abstract void registerItemRenderer(Item item, int i, String name);

	public void registerModels(Material material) {
	}

	public void registerModels(Ore ore) {
	}
}
