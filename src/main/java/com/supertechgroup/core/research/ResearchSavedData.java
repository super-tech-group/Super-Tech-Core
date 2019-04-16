package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.research.teams.ResearchTeam;
import com.supertechgroup.core.research.teams.teamcapability.ITeamCapability;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapabilityProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class ResearchSavedData extends WorldSavedData {
	private static ResearchSavedData INSTANCE = null;

	public static ResearchSavedData get(World world) {
		if (INSTANCE == null) {
			INSTANCE = (ResearchSavedData) world.loadData(ResearchSavedData.class, Reference.RESEARCH_DATA_NAME);
			if (INSTANCE == null) {
				INSTANCE = new ResearchSavedData();
				world.setData(Reference.RESEARCH_DATA_NAME, INSTANCE);
			}
		}
		return INSTANCE;
	}

	public ArrayList<ResearchTeam> teams = new ArrayList<>();
	private HashMap<UUID, ResearchTeam> teamInvites = new HashMap<>();

	public ResearchSavedData() {
		super(Reference.RESEARCH_DATA_NAME);
	}

	public ResearchSavedData(String s) {
		super(s);
	}

	public void addInvite(UUID uuid, ResearchTeam researchTeam) {
		teamInvites.put(uuid, researchTeam);
		this.markDirty();
	}

	public ResearchTeam createNewTeam(String teamName, EntityPlayer newMember) {
		ResearchTeam r = new ResearchTeam(teamName);

		ITeamCapability cap = newMember.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
		cap.setTeam(r);

		teams.add(r);
		this.markDirty();
		return r;
	}

	public boolean doesPlayerHaveInvite(UUID uuid) {
		if (teamInvites.containsKey(uuid)) {
			return true;
		}
		return false;
	}

	public ResearchTeam getTeamByName(String name) {
		System.out.println("Looking for team " + name);
		if (teams.size() > 0) {
			for (ResearchTeam rt : teams) {
				System.out.println("checking against " + rt.getTeamName());
				if (rt.getTeamName().equals(name)) {
					return rt;
				}
			}
		}
		return null;
	}

	public ResearchTeam getTeamInvite(UUID uuid) {
		return teamInvites.get(uuid);
	}

	public boolean joinTeam(EntityPlayer player) {
		UUID uuid = player.getUniqueID();
		if (doesPlayerHaveInvite(uuid)) {
			ITeamCapability cap = player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
			ResearchTeam newTeam = getTeamInvite(uuid);
			cap.setTeam(newTeam);
			player.getServer().getPlayerList().getPlayerByUUID(uuid).sendMessage(
					new TextComponentString(TextFormatting.GREEN + "You have joined " + newTeam.getTeamName() + "."));
			this.markDirty();
			return true;

		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList teamList = nbt.getTagList("TeamList", Constants.NBT.TAG_COMPOUND);
		System.out.println("Loading " + teamList.tagCount() + " Team(s)");
		teamList.forEach((tag) -> {
			NBTTagCompound teamInfo = (NBTTagCompound) tag;

			ResearchTeam team = new ResearchTeam();

			team.setTeamName(teamInfo.getString("name"));

			teamInfo.getTagList("completedResearch", Constants.NBT.TAG_STRING).forEach((cr) -> {
				NBTTagString stringTag = (NBTTagString) cr;
				team.addCompletedResearch(Research.REGISTRY.getValue(new ResourceLocation(stringTag.getString())),
						false);
			});
			System.out.println("Loaded team " + team.getTeamName());
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
		this.markDirty();
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

			teamList.appendTag(teamInfo);
		}

		compound.setTag("TeamList", teamList);
		return compound;
	}
}
