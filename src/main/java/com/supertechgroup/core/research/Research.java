package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import com.supertechgroup.core.network.CompleteResearchPacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Research extends IForgeRegistryEntry.Impl<Research> implements IResearchRequirement {

	public static IForgeRegistry<Research> REGISTRY;
	private double InspirationChance;
	private int researchProcceses;
	public String researchName;
	private HashMap<ResourceLocation, Integer> tasks = new HashMap<>();

	ArrayList<IResearchRequirement> requirements = new ArrayList<>();

	public Research(String name) {
		this.setRegistryName(name);
	}

	public void addTask(ResourceLocation taskType, int taskCount) {
		tasks.put(taskType, taskCount);
	}

	public double getInspirationChance() {
		return InspirationChance;
	}

	public boolean getRequirementsFulfilled(ResearchTeam rt) {
		for (IResearchRequirement rr : requirements) {
			if (!rr.isFulfilled(rt)) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		return this.getRegistryName().toString();
	}

	public String getResearchName() {
		return researchName;
	}

	public int getResearchProcceses() {
		return researchProcceses;
	}

	public boolean hasTask(ResourceLocation task) {
		return tasks.containsKey(task);
	}

	@Override
	public boolean isFulfilled() {
		return CompleteResearchPacket.clientCompleted.contains(this);
	}

	@Override
	public boolean isFulfilled(ResearchTeam rt) {
		return rt.isResearchCompleted(this);
	}

	public void registerResearch() {
		Research.REGISTRY.register(this);
	}

	public static Research getRandomMatch(ResearchTeam team, HashMap<ResourceLocation, Integer> taskMap) {
		ArrayList<Research> possible = new ArrayList<>();
		Research.REGISTRY.forEach((r) -> {
			if (!team.isResearchCompleted(r) && r.getRequirementsFulfilled(team) && r.couldComplete(taskMap)) {
				possible.add(r);
			}
		});
		if (possible.isEmpty()) {
			return null;
		} else {
			return possible.get((new Random()).nextInt(possible.size()));
		}
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

	public HashMap<ResourceLocation, Integer> getTasks() {
		return tasks;
	}

	@Override
	public void addRequirement(IResearchRequirement rr) {
		this.requirements.add(rr);
	}
}
