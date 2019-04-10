package com.supertechgroup.core;

import java.io.File;
import java.util.HashMap;

import com.supertechgroup.core.proxy.CommonProxy;
import com.supertechgroup.core.util.Helpers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import scala.actors.threadpool.Arrays;

public class Config {

	private static final String WORLD_GEN_VANILLA = "assets/" + Reference.MODID + "/other/ores.json";

	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_DIMENSIONS = "dimensions";

	// This values below you can access elsewhere in your mod:
	public static HashMap<IBlockState, ResourceLocation> stone;
	public static HashMap<IBlockState, ResourceLocation> nether;
	public static HashMap<IBlockState, ResourceLocation> end;
	public static boolean removeVanilla;
	public static boolean debug;
	public static String extraDrop;
	public static boolean oreOnly;

	private static String[] types;

	private static String[] netherTypes;

	private static String[] endTypes;

	private static void initGeneralConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
		removeVanilla = cfg.getBoolean("removeVanilla", CATEGORY_GENERAL, true,
				"If vanilla generation should be removed");
		debug = cfg.getBoolean("debug", CATEGORY_GENERAL, false, "If debug info should be printed to the log");
		types = cfg.getStringList("stone types", CATEGORY_GENERAL, new String[] {
				"minecraft:stone,minecraft:blocks/stone", "minecraft:stone:1,minecraft:blocks/stone_granite",
				"minecraft:stone:3,minecraft:blocks/stone_diorite", "minecraft:stone:5,minecraft:blocks/stone_andesite",
				Reference.MODID + ":slate," + Reference.MODID + ":blocks/slate",
				Reference.MODID + ":gneiss," + Reference.MODID + ":blocks/gneiss",
				Reference.MODID + ":schist," + Reference.MODID + ":blocks/schist",
				Reference.MODID + ":phyllite," + Reference.MODID + ":blocks/phyllite",
				Reference.MODID + ":amphibolite," + Reference.MODID + ":blocks/amphibolite",
				Reference.MODID + ":shale," + Reference.MODID + ":blocks/shale",
				Reference.MODID + ":marble," + Reference.MODID + ":blocks/marble",
				Reference.MODID + ":basalt," + Reference.MODID + ":blocks/basalt",
				Reference.MODID + ":chert," + Reference.MODID + ":blocks/chert",
				Reference.MODID + ":conglomerate," + Reference.MODID + ":blocks/conglomerate",
				Reference.MODID + ":dolomite," + Reference.MODID + ":blocks/dolomite",
				Reference.MODID + ":gabbro," + Reference.MODID + ":blocks/gabbro",
				Reference.MODID + ":limestone," + Reference.MODID + ":blocks/limestone",
				Reference.MODID + ":pegmatite," + Reference.MODID + ":blocks/pegmatite",
				Reference.MODID + ":rhyolite," + Reference.MODID + ":blocks/rhyolite",
				Reference.MODID + ":scoria," + Reference.MODID + ":blocks/scoria" },
				"possible types of block to replace for stone veins");

		netherTypes = cfg.getStringList("nether types", CATEGORY_GENERAL,
				new String[] { "minecraft:netherrack,minecraft:blocks/netherrack" },
				"possible types of block to replace for nether veins");

		endTypes = cfg.getStringList("end types", CATEGORY_GENERAL,
				new String[] { "minecraft:end_stone,minecraft:blocks/end_stone" },
				"possible types of block to replace for ender veins");

	}

	private static void initVanillaOres(File configFolder) {
		try {
			File vanillaGen = new File(configFolder, "ores.json");
			if (vanillaGen.createNewFile()) {
				Helpers.copyFileUsingStream(WORLD_GEN_VANILLA, vanillaGen);
			} else if (!vanillaGen.exists()) {
				throw new Error("Unable to create vanilla generation json.");
			}
		} catch (Throwable t) {
		}
	}

	public static void parseTypes() {
		stone = new HashMap<IBlockState, ResourceLocation>();
		for (String type : types) {// Adds the read in stone types to the
			// 'stone' block type. used for generation
			String[] parts = type.split(",");
			String[] split = parts[0].split(":");
			if (split.length == 3) {// This one is called for items such as
									// "minecraft:stone:4", which is a specific
									// type of stone
				stone.put(
						Block.getBlockFromName(split[0] + ":" + split[1]).getStateFromMeta(Integer.parseInt(split[2])),
						new ResourceLocation(parts[1]));
			} else {// This one is called for items without metadata, such as
					// "minecraft:dirt"
				System.err.println("parts: " + Arrays.toString(parts));
				stone.put(Block.getBlockFromName(parts[0]).getDefaultState(), new ResourceLocation(parts[1]));
			}
		}
		nether = new HashMap<IBlockState, ResourceLocation>();
		for (String type : netherTypes) {// Adds the read in nether types to the
			// 'nether' block type. used for generation
			String[] parts = type.split(",");
			String[] split = parts[0].split(":");
			if (split.length == 3) {
				nether.put(
						Block.getBlockFromName(split[0] + ":" + split[1]).getStateFromMeta(Integer.parseInt(split[2])),
						new ResourceLocation(parts[1]));
			} else {
				nether.put(Block.getBlockFromName(parts[0]).getDefaultState(), new ResourceLocation(parts[1]));
			}
		}
		end = new HashMap<IBlockState, ResourceLocation>();
		for (String type : endTypes) {// Adds the read in nether types to the
			// 'nether' block type. used for generation
			String[] parts = type.split(",");
			String[] split = parts[0].split(":");
			if (split.length == 3) {
				end.put(Block.getBlockFromName(split[0] + ":" + split[1]).getStateFromMeta(Integer.parseInt(split[2])),
						new ResourceLocation(parts[1]));
			} else {
				end.put(Block.getBlockFromName(parts[0]).getDefaultState(), new ResourceLocation(parts[1]));
			}
		}
	}

	// Call this from CommonProxy.preInit(). It will create our config if it
	// doesn't exist yet and read the values if it does exist.
	public static void readConfig(File configFolder) {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
			initVanillaOres(configFolder);
		} catch (Exception e1) {
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}
}