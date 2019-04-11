package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.List;

import com.supertechgroup.core.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class ResearchSavedData extends WorldSavedData {

	public static ResearchSavedData get(World world) {
		MapStorage storage = world.getMapStorage();
		ResearchSavedData instance = (ResearchSavedData) storage.getOrLoadData(ResearchSavedData.class,
				Reference.RESEARCH_DATA_NAME);

		if (instance == null) {
			instance = new ResearchSavedData();
			storage.setData(Reference.RESEARCH_DATA_NAME, instance);
		}
		instance.world = world;
		return instance;
	}

	private World world;

	private ResearchTeam[] teams;

	public ResearchSavedData() {
		super(Reference.RESEARCH_DATA_NAME);
	}

	public ResearchSavedData(String s) {
		super(s);
	}

	public boolean getTeamFinishedResearch(ResearchTeam rt, Research research) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

	}

	public boolean setTeamName(ResearchTeam team, String newName) {
		for (ResearchTeam t : teams) {
			if (t.getTeamName() == newName) {
				return false; // Name in use already
			}
		}
		team.setTeamName(newName);
		return true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound teamList = new NBTTagCompound();
		for (ResearchTeam team : teams) {
			NBTTagList tagList = new NBTTagList();
			List<String> compResearch = new ArrayList<>();
			for (Research r : team.getCompletedResearch()) {
				compResearch.add(r.getResearchName());
			}
			for (int i = 0; i < compResearch.size(); i++) {
				String s = compResearch.get(i);
				if (s != null) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("Research" + i, s);
					tagList.appendTag(tag);
				}
			}
			for (int i = 0; i < team.getMembers().size(); i++) {
				String s = team.getMembers().get(i).toString();
				if (s != null) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("Member" + i, s);
					tagList.appendTag(tag);
				}
			}
			teamList.setTag("ResearchTeam:" + team.getTeamName(), tagList);
		}
		compound.setTag("TeamData", teamList);

		System.out.println("Saving research");
		return compound;
	}
}
