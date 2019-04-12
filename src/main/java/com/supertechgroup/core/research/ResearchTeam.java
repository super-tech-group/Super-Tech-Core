package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.UUID;

import com.supertechgroup.core.network.CompleteResearchPacket;
import com.supertechgroup.core.network.PacketHandler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class ResearchTeam {

	private ArrayList<UUID> members;
	private ArrayList<Research> completedResearch = new ArrayList<>();
	private String TeamName;
	private World world;

	public ResearchTeam() {
		members = new ArrayList<>();
	}

	public ResearchTeam(String name) {
		TeamName = name;
		members = new ArrayList<>();
	}

	public void addCompletedResearch(Research r) {
		completedResearch.add(r);
		this.members.forEach((uuid) -> {
			CompleteResearchPacket packet = new CompleteResearchPacket(r);
			PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) this.world.getPlayerEntityByUUID(uuid));
		});
	}

	public boolean addMember(UUID newMember) {
		if (!members.contains(newMember)) {
			members.add(newMember);
			return true;
		}
		return false;
	}

	public ArrayList<Research> getCompletedResearch() {
		return completedResearch;
	}

	public ArrayList<UUID> getMembers() {
		return members;
	}

	public String getTeamName() {
		return TeamName;
	}

	public World getWorld() {
		return world;
	}

	public boolean hasMember(UUID player) {
		return members.contains(player);
	}

	public boolean removeMember(UUID toRemove) {
		if (members.contains(toRemove)) {
			members.remove(toRemove);
			return true;
		}
		return false;
	}

	public void setTeamName(String newName) {
		TeamName = newName;
	}

	public void setWorld(World world2) {
		world = world2;

	}
}
