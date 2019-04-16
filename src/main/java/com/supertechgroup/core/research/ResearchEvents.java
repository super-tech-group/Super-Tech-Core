package com.supertechgroup.core.research;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.integration.jei.JEIMainPlugin;
import com.supertechgroup.core.items.ItemResearchBook;
import com.supertechgroup.core.items.MaterialItem;
import com.supertechgroup.core.items.MaterialTool;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.network.CompleteResearchPacket;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.research.teams.ResearchTeam;
import com.supertechgroup.core.research.teams.teamcapability.ITeamCapability;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapability;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapabilityProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;

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

		// fix some issues that would be caused
		JEIMainPlugin.handleItemBlacklisting(new ItemStack(ModRegistry.itemResearchBook), true);
		JEIMainPlugin.handleItemBlacklisting(ItemResearchBook.getEmptyBookStack(), false);
	}

	@SubscribeEvent
	public void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
		ItemStack hand = event.player.getHeldItemOffhand();
		if (hand.getItem().equals(ModRegistry.itemResearchBook) && hand.getTagCompound() != null
				&& hand.getTagCompound().getInteger("remaining") > 0) {
			NBTTagCompound tag = hand.getTagCompound();
			NBTTagList list = tag.getTagList("tasks", Constants.NBT.TAG_STRING);
			ResourceLocation craftingResearch = ResearchTasks.getFromResultStack(event.crafting);
			if (!craftingResearch.toString().equals("null:void")) {
				list.appendTag((new NBTTagString(craftingResearch.toString())));
				tag.setInteger("remaining", tag.getInteger("remaining") - 1);
				tag.setTag("tasks", list);
				hand.setTagCompound(tag);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
		System.out.println("Entity joined world " + e.player.getEntityWorld().getMapStorage());
		ResearchSavedData rsd = ResearchSavedData.get(e.player.getEntityWorld());
		System.out.println("created/loaded rsd " + rsd.mapName);
		ITeamCapability cap = e.player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
		if (cap.getTeam().equals(TeamCapability.NULL_TEAM)) {
			rsd.createNewTeam(e.player.getName() + "'s Team", e.player);
			e.player.sendMessage(new TextComponentString("Welcome, you've joined a new research team!"));
			rsd.teams.forEach((t) -> {
				e.player.sendMessage(new TextComponentString(t.getTeamName()));
			});
		}

		// send team's completed research
		ResearchTeam team = rsd.getTeamByName(e.player.getCapability(TeamCapabilityProvider.TEAM_CAP, null).getTeam());
		System.out.println(team);
		CompleteResearchPacket packet = new CompleteResearchPacket(team,
				team.getCompletedResearch().toArray(new Research[] {}));
		PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) e.player);
	}

	public static final ResourceLocation TEAM_CAP = new ResourceLocation(Reference.MODID, "researchTeam");

	/**
	 * Copy data from dead player to the new player
	 */
	@SubscribeEvent
	public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		ITeamCapability team = player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
		ITeamCapability oldTeam = event.getOriginal().getCapability(TeamCapabilityProvider.TEAM_CAP, null);
		team.setTeam(oldTeam.getTeam());
	}

	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityPlayer))
			return;
		event.addCapability(TEAM_CAP, new TeamCapabilityProvider());
	}

	@SubscribeEvent
	public void attachCapabilityWorld(AttachCapabilitiesEvent<World> event) {
		if (!(event.getObject() instanceof World))
			return;
		ResearchSavedData.get(event.getObject());
	}

}