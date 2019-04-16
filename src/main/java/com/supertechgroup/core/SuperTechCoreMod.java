package com.supertechgroup.core;

import org.apache.logging.log4j.Logger;

import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.proxy.CommonProxy;
import com.supertechgroup.core.research.teams.InviteToResearchTeamCommand;
import com.supertechgroup.core.research.teams.JoinResearchTeamCommmand;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_MINECRAFT_VERSIONS)
public class SuperTechCoreMod {
	public static Logger logger;
	/**
	 * The proxy to be used. Holds various functions and objects that may need to be
	 * different based on side.
	 */
	@SidedProxy(clientSide = "com.supertechgroup.core.proxy.ClientProxy", serverSide = "com.supertechgroup.core.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		logger.info(Reference.MODNAME + " is loading!");
		proxy.preInit(event);
	}

	public void registerModels(Material mat) {
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new InviteToResearchTeamCommand());
		event.registerServerCommand(new JoinResearchTeamCommmand());
	}
}
