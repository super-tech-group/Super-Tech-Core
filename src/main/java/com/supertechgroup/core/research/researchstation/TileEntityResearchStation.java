package com.supertechgroup.core.research.researchstation;

import java.util.HashMap;
import java.util.UUID;

import com.supertechgroup.core.research.Research;
import com.supertechgroup.core.research.teams.listCapability.IListCapability;
import com.supertechgroup.core.research.teams.listCapability.ListCapabilityProvider;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;

public class TileEntityResearchStation extends TileEntity {

	private UUID team = TeamCapability.NULL_TEAM;

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

			IListCapability listCap = DimensionManager.getWorld(0).getCapability(ListCapabilityProvider.TEAM_LIST_CAP,
					null);
			listCap.completeResearchForTeam(team, r);
		}
	}

	public UUID getTeam() {
		return team;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		System.out.println("Trying read of " + compound.getString("team"));
		team = UUID.fromString(compound.getString("team"));
		NBTTagCompound tasks = compound.getCompoundTag("tasks");
		researchProgress.clear();
		tasks.getKeySet().forEach((k) -> {
			researchProgress.put(new ResourceLocation(k), tasks.getInteger(k));
		});
	}

	public void setTeam(UUID nTeam) {
		team = nTeam;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("team", team.toString());
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
