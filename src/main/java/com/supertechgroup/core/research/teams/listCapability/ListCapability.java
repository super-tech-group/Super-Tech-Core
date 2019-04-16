package com.supertechgroup.core.research.teams.listCapability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.supertechgroup.core.network.CompleteResearchPacket;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.research.Research;
import com.supertechgroup.core.research.teams.teamcapability.ITeamCapability;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapabilityProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ListCapability implements IListCapability {
	/**
	 * Team UUID, then user friendly team name
	 */
	private HashMap<UUID, String> teams = new HashMap<>();
	/**
	 * Team UUID, then arraylist of completed research for said team
	 */
	private HashMap<UUID, ArrayList<Research>> unlockedResearch = new HashMap<>();
	/**
	 * player UUID, then team UUID
	 */
	private HashMap<UUID, UUID> teamInvites = new HashMap<>();

	@Override
	public void addInvite(EntityPlayer otherPlayer, UUID team) {
		teamInvites.put(otherPlayer.getUniqueID(), team);
	}

	@Override
	public void completeResearchForTeam(UUID team, Research r) {
		System.out.println("Completing " + r + " for " + this.getTeamName(team));
		ArrayList<Research> completed = getCompletedForTeam(team);
		completed.add(r);
		unlockedResearch.put(team, completed);
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			server.getPlayerList().getPlayers().forEach((player) -> {
				ITeamCapability cap = player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
				if (cap.getTeam().equals(team)) {
					CompleteResearchPacket packet = new CompleteResearchPacket(team, r);
					PacketHandler.INSTANCE.sendTo(packet, player);
					System.out.println("Packet sent");
				}
			});
		}
	}

	@Override
	public void createTeam(EntityPlayer player) {
		UUID teamID = UUID.randomUUID();
		teams.put(teamID, player.getDisplayNameString() + "'s Team");
		ITeamCapability cap = player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
		cap.setTeam(teamID);
		System.out.println("Creating new team for " + player + ", id: " + teamID);
	}

	@Override
	public ArrayList<Research> getCompletedForTeam(UUID team) {

		return unlockedResearch.getOrDefault(team, new ArrayList<>());
	}

	@Override
	public UUID[] getTeamIDs() {
		return this.teams.keySet().toArray(new UUID[] {});
	}

	@Override
	public String getTeamName(UUID team) {
		return teams.getOrDefault(team, "NO TEAM");
	}

	@Override
	public boolean isCompletedForTeam(Research research, UUID team) {
		return unlockedResearch.getOrDefault(team, new ArrayList<>()).contains(research);
	}

	@Override
	public boolean joinTeam(EntityPlayerMP player) {
		UUID playerID = player.getUniqueID();
		if (teamInvites.containsKey(playerID)) {
			ITeamCapability cap = player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
			UUID teamID = teamInvites.get(playerID);
			cap.setTeam(teamID);
			player.getServer().getPlayerList().getPlayerByUUID(playerID).sendMessage(new TextComponentString(
					TextFormatting.GREEN + "You have joined " + this.getTeamName(teamID) + "."));
			return true;

		}
		return false;
	}

	@Override
	public void setData(HashMap<UUID, String> teams, HashMap<UUID, ArrayList<Research>> unlockedResearch) {
		this.teams = teams;
		this.unlockedResearch = unlockedResearch;
		this.teamInvites.clear();
	}
}
