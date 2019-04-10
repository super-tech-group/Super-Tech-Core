package com.supertechgroup.core.proxy;

import java.io.File;
import java.util.ArrayList;

import com.supertechgroup.core.Config;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.worldgen.WorldGenEvents;
import com.supertechgroup.core.worldgen.generators.WorldGeneratorBase;
import com.supertechgroup.core.worldgen.ores.Ore;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public abstract class CommonProxy {
	public static Configuration config;
	private File configFolder;
	public static SimpleNetworkWrapper simpleNetworkWrapper;
	public static ArrayList<IBlockState> vanillaReplace = new ArrayList<IBlockState>();

	public static ArrayList<WorldGeneratorBase> parsed = new ArrayList<WorldGeneratorBase>();

	public abstract Side getSide();

	public World getWorld() {
		return getWorld(null);
	}

	public abstract World getWorld(IBlockAccess world);

	public void preInit(FMLPreInitializationEvent e) {

		PacketHandler.registerMessages(Reference.MODID + "Chan");
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("MBEchannel");
		configFolder = new File(e.getModConfigurationDirectory().toString() + "/" + Reference.MODID + "/");
		config = new Configuration(new File(configFolder.getPath(), "config.cfg"));
		Config.readConfig(configFolder);

		WorldGenEvents wge = new WorldGenEvents();
		MinecraftForge.EVENT_BUS.register(wge);
		MinecraftForge.ORE_GEN_BUS.register(wge);
	}

	public void init(FMLInitializationEvent event) {
		vanillaReplace.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE));
		vanillaReplace
				.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE));
		vanillaReplace
				.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE));
		vanillaReplace
				.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE));
	}

	public void postInit(FMLPostInitializationEvent event) {
		Config.parseTypes();

	}

	public void registerModels(Ore ore) {
	}

	public abstract void registerItemRenderer(Item item, int i, String name);
}
