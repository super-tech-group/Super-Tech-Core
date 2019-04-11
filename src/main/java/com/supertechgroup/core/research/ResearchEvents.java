package com.supertechgroup.core.research;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.integration.jei.JEIMainPlugin;
import com.supertechgroup.core.items.MaterialTool;
import com.supertechgroup.core.metallurgy.Material;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ResearchEvents {

	@SubscribeEvent
	public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
//Hide material items by default, as most of them come from research
		Material.REGISTRY.forEach((Material m) -> {
			for (int i = 0; i < 15; i++) {
				JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getMaterialItem(), 1, i), true);
			}
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getBlock(), 1), true);
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getItemHammer(), 1, MaterialTool.HAMMER), true);
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getItemPliers(), 1, MaterialTool.PLIERS), true);
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getItemDrawplate(), 1, MaterialTool.DRAW_PLATE), true);
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getItemPickaxe(), 1, MaterialTool.PICKAXE), true);
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getItemShovel(), 1, MaterialTool.SHOVEL), true);
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(m.getItemAxe(), 1, MaterialTool.AXE), true);
		});

		// hide disable vanilla stuff
		for (Item i : ModRegistry.disabledVanillaItems) {
			JEIMainPlugin.handleItemBlacklisting(new ItemStack(i), true);
		}

//Unhide some basic stuff

		Material wood = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":wood"));
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(wood.getItemHammer(), 1, MaterialTool.HAMMER), false);

	}
}
