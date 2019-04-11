package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.world.World;

public class ResearchTeam {

	private ArrayList<UUID> members;
	private ArrayList<Research> completedResearch;
	private String TeamName;

	public ResearchTeam() {
		members = new ArrayList();
	}

	public ResearchTeam(String name) {
		TeamName = name;
		members = new ArrayList();
	}

	public void addCompletedResearch(Research r) {
		completedResearch.add(r);
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
		// TODO Auto-generated method stub
		return null;
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
}
