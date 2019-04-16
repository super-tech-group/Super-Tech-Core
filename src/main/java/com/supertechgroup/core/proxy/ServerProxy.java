package com.supertechgroup.core.proxy;

import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Proxy for PHYSICAL SERVERS ONLY. Singleplayer worlds only use ClientProxy
 *
 * @author oa10712
 *
 */
public class ServerProxy extends CommonProxy {
	MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

	@Override
	public World getWorld(IBlockAccess world) {
		if (world instanceof World) {
			return (World) world;
		}
		return server.getEntityWorld();
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);

		DimensionManager.init();
	}

	@Override
	public void registerItemRenderer(Item item, int i, String name) {
//Do nothing here, as the server can't render things
	}
}