package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import com.supertechgroup.core.network.CompleteResearchPacket;
import com.supertechgroup.core.research.teams.listCapability.IListCapability;
import com.supertechgroup.core.research.teams.listCapability.ListCapabilityProvider;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Research extends IForgeRegistryEntry.Impl<Research> implements IResearchRequirement {

	public static IForgeRegistry<Research> REGISTRY;

	public static Research getRandomMatch(UUID team, HashMap<ResourceLocation, Integer> taskMap) {
		IListCapability listCap = DimensionManager.getWorld(0).getCapability(ListCapabilityProvider.TEAM_LIST_CAP,
				null);
		ArrayList<Research> possible = new ArrayList<>();
		Research.REGISTRY.forEach((r) -> {
			if (!listCap.isCompletedForTeam(r, team) && r.getRequirementsFulfilled(team) && r.couldComplete(taskMap)) {
				possible.add(r);
			}
		});
		if (possible.isEmpty()) {
			return null;
		} else {
			return possible.get((new Random()).nextInt(possible.size()));
		}
	}

	private double InspirationChance;
	private int researchProcceses;
	public String researchName;

	private HashMap<ResourceLocation, Integer> tasks = new HashMap<>();

	ArrayList<IResearchRequirement> requirements = new ArrayList<>();

	public Research(String name) {
		this.setRegistryName(name);
	}

	@Override
	public void addRequirement(IResearchRequirement rr) {
		this.requirements.add(rr);
	}

	public void addTask(ResourceLocation taskType, int taskCount) {
		tasks.put(taskType, taskCount);
	}

	private boolean couldComplete(HashMap<ResourceLocation, Integer> taskMap) {
		Iterator<Entry<ResourceLocation, Integer>> iterator = tasks.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<ResourceLocation, Integer> pair = iterator.next();
			if (!taskMap.containsKey(pair.getKey()) || taskMap.get(pair.getKey()) < pair.getValue()) {
				System.out.println(this.getRegistryName() + " failed for " + pair.getKey() + ". " + pair.getValue()
						+ " needed, " + taskMap.getOrDefault(pair.getKey(), 0) + " found.");
				return false;
			}
		}
		return true;
	}

	public double getInspirationChance() {
		return InspirationChance;
	}

	public boolean getRequirementsFulfilled(UUID rt) {
		for (IResearchRequirement rr : requirements) {
			if (!rr.isFulfilled(rt)) {
				return false;
			}
		}
		return true;
	}

	public String getResearchName() {
		return researchName;
	}

	public int getResearchProcceses() {
		return researchProcceses;
	}

	public HashMap<ResourceLocation, Integer> getTasks() {
		return tasks;
	}

	public boolean hasTask(ResourceLocation task) {
		return tasks.containsKey(task);
	}

	@Override
	public boolean isFulfilled() {
		return CompleteResearchPacket.clientCompleted.contains(this);
	}

	@Override
	public boolean isFulfilled(UUID rt) {
		IListCapability listCap = DimensionManager.getWorld(0).getCapability(ListCapabilityProvider.TEAM_LIST_CAP,
				null);
		return listCap.isCompletedForTeam(this, rt);
	}

	public void registerResearch() {
		Research.REGISTRY.register(this);
	}

	@Override
	public String toString() {
		return this.getRegistryName().toString();
	}
}
