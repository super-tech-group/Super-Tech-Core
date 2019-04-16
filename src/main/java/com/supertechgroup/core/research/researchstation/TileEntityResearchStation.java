package com.supertechgroup.core.research.researchstation;

import java.util.HashMap;

import com.supertechgroup.core.research.Research;
import com.supertechgroup.core.research.ResearchSavedData;
import com.supertechgroup.core.research.teams.ResearchTeam;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityResearchStation extends TileEntity {

	private String team;

	HashMap<ResourceLocation, Integer> researchProgress = new HashMap<>();

	public void addResearchProgress(ResourceLocation task) {
		System.out.println("Adding task: " + task);
		if (researchProgress.containsKey(task)) {
			researchProgress.put(task, researchProgress.get(task) + 1);
		} else {
			researchProgress.put(task, 1);
		}
		Research r = Research.getRandomMatch(getTeam(), researchProgress);
		if (r != null) {
			HashMap<ResourceLocation, Integer> rTasks = r.getTasks();
			rTasks.forEach((k, v) -> {
				researchProgress.put(k, researchProgress.get(k) - v);
				if (researchProgress.get(k) == 0) {
					researchProgress.remove(k);
				}
			});
			System.out.println("Complete " + r.toString());
			this.getTeam().addCompletedResearch(r);
		}
	}

	public ResearchTeam getTeam() {
		// We've got to do it this way, the ResearchSavedData isn't ready to be read
		// from when tile entities are created.
		ResearchTeam t = ResearchSavedData.get(world).getTeamByName(team);
		return t;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		team = compound.getString("team");
		NBTTagCompound tasks = compound.getCompoundTag("tasks");
		researchProgress.clear();
		tasks.getKeySet().forEach((k) -> {
			researchProgress.put(new ResourceLocation(k), tasks.getInteger(k));
		});
	}

	public void setTeam(ResearchTeam nTeam) {
		team = nTeam.getTeamName();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("team", team);
		NBTTagCompound tasks = new NBTTagCompound();
		researchProgress.forEach((k, v) -> {
			tasks.setInteger(k.toString(), v);
		});
		compound.setTag("tasks", tasks);
		return compound;
	}

	public HashMap<ResourceLocation, Integer> getTasks() {
		return researchProgress;
	}
}
