package com.supertechgroup.core.research;

import java.util.HashMap;

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
}
