package com.supertechgroup.core.research.teams;

import java.util.ArrayList;

import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.network.CompleteResearchPacket;
import com.supertechgroup.core.network.PacketHandler;
import com.supertechgroup.core.research.Research;
import com.supertechgroup.core.research.ResearchSavedData;
import com.supertechgroup.core.research.teams.teamcapability.ITeamCapability;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapabilityProvider;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ResearchTeam {
	private ArrayList<Research> completedResearch = new ArrayList<>();
	private String TeamName;

	public ResearchTeam() {
	}

	public ResearchTeam(String name) {
		TeamName = name;
	}

	public void addCompletedResearch(Research r) {
		addCompletedResearch(r, true);
	}

	public ArrayList<Research> getCompletedResearch() {
		return completedResearch;
	}

	public String getTeamName() {
		return TeamName;
	}

	public boolean isResearchCompleted(Research research) {
		return completedResearch.contains(research);
	}

	public void setTeamName(String newName) {
		TeamName = newName;
	}

	public void addCompletedResearch(Research r, boolean sendPacket) {
		System.out.println("Completing " + r + " for " + this.getTeamName());
		completedResearch.add(r);
		ResearchSavedData.get(SuperTechCoreMod.proxy.getWorld()).markDirty();
		if (sendPacket && SuperTechCoreMod.proxy.getSide() == Side.SERVER) {
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			server.getPlayerList().getPlayers().forEach((player) -> {
				ITeamCapability cap = player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
				if (cap.getTeam().equals(this.getTeamName())) {
					CompleteResearchPacket packet = new CompleteResearchPacket(this, r);
					PacketHandler.INSTANCE.sendTo(packet, player);
					System.out.println("Packet sent");
				}
			});
		}
	}
}
