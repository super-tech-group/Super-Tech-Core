package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.supertechgroup.core.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
			instance.setWorld(world);
		}
		return instance;
	}

	World world;

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
	}

	public ResearchTeam createNewTeam(String teamName, UUID newMember) {
		ResearchTeam r = new ResearchTeam(teamName);
		r.setWorld(this.world);
		r.addMember(newMember);
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

	public boolean doesPlayerHaveTeam(UUID player) {
		if (teams.size() > 0) {
			for (ResearchTeam rt : teams) {
				if (rt.hasMember(player)) {
					return true;
				}
			}
		}
		return false;
	}

	public ResearchTeam findPlayersResearchTeam(UUID player) {
		if (teams.size() > 0) {
			for (ResearchTeam rt : teams) {
				if (rt.hasMember(player)) {
					return rt;
				}
			}
		}
		return null;
	}

	public ResearchTeam getTeamByName(String name) {
		if (teams.size() > 0) {
			for (ResearchTeam rt : teams) {
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
			ResearchTeam newTeam = getTeamInvite(uuid);
			ResearchTeam oldTeam = findPlayersResearchTeam(uuid);
			if (oldTeam != null) {
				newTeam.addMember(uuid);
				player.getServer().getPlayerList().getPlayerByUUID(uuid).sendMessage(new TextComponentString(
						TextFormatting.GREEN + "You have joined " + newTeam.getTeamName() + "."));
				oldTeam.removeMember(uuid);
				if (oldTeam.getMembers().size() == 0) {
					teams.remove(oldTeam);
				}
				this.markDirty();
				return true;
			} else {
				newTeam.addMember(uuid);
				player.getServer().getPlayerList().getPlayerByUUID(uuid).sendMessage(new TextComponentString(
						TextFormatting.GREEN + "You have joined " + newTeam.getTeamName() + "."));
				this.markDirty();
				return true;
			}
		}
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
				team.addMember(UUID.fromString(stringTag.getString()));
			});
			team.setWorld(this.world);
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

	private void setWorld(World world2) {
		world = world2;
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
		return compound;
	}
}
