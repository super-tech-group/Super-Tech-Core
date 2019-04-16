package com.supertechgroup.core.research.teams.listCapability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.supertechgroup.core.research.Research;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;

public class ListCapabilityStorage implements IStorage<IListCapability> {

	@Override
	public NBTBase writeNBT(Capability<IListCapability> capability, IListCapability instance, EnumFacing side) {
		NBTTagList teamList = new NBTTagList();
		for (UUID teamID : instance.getTeamIDs()) {
			NBTTagCompound teamInfo = new NBTTagCompound();

			teamInfo.setString("id", teamID.toString());
			teamInfo.setString("name", instance.getTeamName(teamID));

			NBTTagList compResearch = new NBTTagList();
			for (Research r : instance.getCompletedForTeam(teamID)) {
				compResearch.appendTag(new NBTTagString(r.getRegistryName().toString()));
			}
			teamInfo.setTag("completedResearch", compResearch);

			teamList.appendTag(teamInfo);
		}
		return teamList;
	}

	@Override
	public void readNBT(Capability<IListCapability> capability, IListCapability instance, EnumFacing side,
			NBTBase nbt) {
		NBTTagList teamList = (NBTTagList) nbt;

		HashMap<UUID, String> teams = new HashMap<>();
		HashMap<UUID, ArrayList<Research>> unlockedResearch = new HashMap<>();

		teamList.forEach((tag) -> {
			NBTTagCompound teamInfo = (NBTTagCompound) tag;

			teams.put(UUID.fromString(teamInfo.getString("id")), teamInfo.getString("name"));

			ArrayList<Research> researchList = new ArrayList<>();

			NBTTagList compResearch = teamInfo.getTagList("completedResearch", Constants.NBT.TAG_STRING);
			compResearch.forEach((resc) -> {
				NBTTagString loc = (NBTTagString) resc;
				researchList.add(Research.REGISTRY.getValue(new ResourceLocation(loc.getString())));
			});

			unlockedResearch.put(UUID.fromString(teamInfo.getString("id")), researchList);

		});

		instance.setData(teams, unlockedResearch);

	}

}
