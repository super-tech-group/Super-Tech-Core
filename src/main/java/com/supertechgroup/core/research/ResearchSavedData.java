package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.UUID;

import com.supertechgroup.core.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

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

	private ArrayList<ResearchTeam> teams;

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
		NBTTagList teamList = nbt.getTagList("TeamList", Constants.NBT.TAG_COMPOUND);
		teamList.forEach((tag) -> {
			NBTTagCompound teamInfo = (NBTTagCompound) tag;

			ResearchTeam team = new ResearchTeam();

			team.setTeamName(teamInfo.getString("name"));

			teamInfo.getTagList("completedResearch", Constants.NBT.TAG_STRING).forEach((cr) -> {
				NBTTagString stringTag = (NBTTagString) cr;
				team.addCompletedResearch(Research.REGISTRY.getValue(new ResourceLocation(stringTag.toString())));
			});

			teamInfo.getTagList("members", Constants.NBT.TAG_STRING).forEach((m) -> {
				NBTTagString stringTag = (NBTTagString) m;
				team.addMember(UUID.fromString(stringTag.toString()));
			});
			teams.add(team);
		});

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
		NBTTagList teamList = new NBTTagList();
		for (ResearchTeam team : teams) {
			NBTTagCompound teamInfo = new NBTTagCompound();

			teamInfo.setString("name", team.getTeamName());

			NBTTagList compResearch = new NBTTagList();
			for (Research r : team.getCompletedResearch()) {
				compResearch.appendTag(new NBTTagString(r.getRegistryName().toString()));
			}
			teamInfo.setTag("completedResearch", compResearch);

			NBTTagList members = new NBTTagList();
			team.getMembers().forEach((uuid) -> {
				members.appendTag(new NBTTagString(uuid.toString()));
			});
			teamInfo.setTag("members", members);

			teamList.appendTag(teamInfo);
		}

		compound.setTag("TeamList", teamList);

		System.out.println("Saving research");
		return compound;
	}
}
