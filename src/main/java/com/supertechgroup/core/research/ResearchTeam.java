package com.supertechgroup.core.research;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.world.World;

public class ResearchTeam {

	private ArrayList<UUID> members;
	private ArrayList<Research> completedResearch;
	private String TeamName;
	
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<UUID> getMembers(){
		return members;
	}
	
	public boolean addMember(UUID newMember) {
		if(!members.contains(newMember)){
			members.add(newMember);
			return true;
		}
		return false;
	}
	
	public boolean removeMember(UUID toRemove) {
		if(members.contains(toRemove)) {
			members.remove(toRemove);
			return true;
		}
		return false;
	}

	public ArrayList<Research> getCompletedResearch() {
		return completedResearch;
	}
	
	public void addCompletedResearch(Research r) {
		completedResearch.add(r);
	}
	
	public String getTeamName() {
		return TeamName;
	}
	
	public void setTeamName(String newName) {
		TeamName = newName;
	}
	
}
