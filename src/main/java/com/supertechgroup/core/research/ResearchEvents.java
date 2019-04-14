package com.supertechgroup.core.research;

import java.util.UUID;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.integration.jei.JEIMainPlugin;
import com.supertechgroup.core.items.MaterialItem;
import com.supertechgroup.core.items.MaterialTool;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.network.CompleteResearchPacket;
import com.supertechgroup.core.network.PacketHandler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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

		// Unhide some basic stuff
		Material wood = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":wood"));
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(wood.getItemShovel(), 1, MaterialTool.SHOVEL), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(wood.getItemPickaxe(), 1, MaterialTool.PICKAXE), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(wood.getItemAxe(), 1, MaterialTool.AXE), false);

		Material stone = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":stone"));
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(stone.getItemShovel(), 1, MaterialTool.SHOVEL), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(stone.getItemPickaxe(), 1, MaterialTool.PICKAXE), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(stone.getItemAxe(), 1, MaterialTool.AXE), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(stone.getItemHammer(), 1, MaterialTool.HAMMER), false);

		Material iron = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":iron"));
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(iron.getMaterialItem(), 1, MaterialItem.NUGGET), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(iron.getMaterialItem(), 1, MaterialItem.INGOT), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(iron.getMaterialItem(), 1, MaterialItem.PLATE), false);

		Material tin = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":tin"));
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(tin.getMaterialItem(), 1, MaterialItem.NUGGET), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(tin.getMaterialItem(), 1, MaterialItem.INGOT), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(tin.getMaterialItem(), 1, MaterialItem.PLATE), false);

		Material copper = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":copper"));
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(copper.getMaterialItem(), 1, MaterialItem.NUGGET), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(copper.getMaterialItem(), 1, MaterialItem.INGOT), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(copper.getMaterialItem(), 1, MaterialItem.PLATE), false);

		Material zinc = Material.REGISTRY.getValue(new ResourceLocation(Reference.MODID + ":zinc"));
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(zinc.getMaterialItem(), 1, MaterialItem.NUGGET), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(zinc.getMaterialItem(), 1, MaterialItem.INGOT), false);
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(zinc.getMaterialItem(), 1, MaterialItem.PLATE), false);
	}

	@SubscribeEvent
	public void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
		event.player.sendMessage(new TextComponentString(event.crafting.getDisplayName()));
	}

	// Research Team stuff
	@SubscribeEvent
	public void onPlayerLogin(EntityJoinWorldEvent e) {
		if (e.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.getEntity();
			UUID uuid = player.getUniqueID();
			ResearchSavedData rsd = ResearchSavedData.get(e.getWorld());
			if (!rsd.doesPlayerHaveTeam(uuid)) {
				rsd.createNewTeam(player.getName() + "'s Team", uuid);
				player.sendMessage(new TextComponentString("Welcome, you've joined a new research team!"));
				rsd.teams.forEach((t) -> {
					player.sendMessage(new TextComponentString(t.getTeamName()));
				});
			}

			// send team's completed research
			CompleteResearchPacket packet = new CompleteResearchPacket(rsd.findPlayersResearchTeam(player.getUniqueID())
					.getCompletedResearch().toArray(new Research[] {}));
			PacketHandler.INSTANCE.sendTo(packet, player);
		}
	}
}
