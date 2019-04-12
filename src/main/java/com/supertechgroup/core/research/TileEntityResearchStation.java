package com.supertechgroup.core.research;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityResearchStation extends TileEntity {

	private String team;

	HashMap<Research, HashMap<ResourceLocation, Integer>> researchProgress = new HashMap<>();

	public ResearchTeam getTeam() {
		// We've got to do it this way, the ResearchSavedData isn't ready to be read
		// from when tile entities are created.
		return ResearchSavedData.get(world).getTeamByName(team);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		team = compound.getString("team");
	}

	public void setTeam(ResearchTeam nTeam) {
		team = nTeam.getTeamName();
	}

	public void addResearchProgress(ResourceLocation task) {
		Research.REGISTRY.forEach((r) -> {
			// if the research isn't already done, and all requirements to progress are met
			if (!getTeam().getCompletedResearch().contains(r) && r.getRequirementsFulfilled(getTeam())
					&& r.hasTask(task)) {
				if (researchProgress.containsKey(r)) {
					HashMap<ResourceLocation, Integer> progress = researchProgress.get(r);
					if (progress.containsKey(task)) {
						progress.put(task, progress.get(task) + 1);
					} else {
						progress.put(task, 1);
					}
				} else {
					HashMap<ResourceLocation, Integer> progress = new HashMap<>();
					progress.put(task, 1);
					researchProgress.put(r, progress);
				}
				//TODO check if task is done
			}
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("team", team);
		return compound;
	}
}
