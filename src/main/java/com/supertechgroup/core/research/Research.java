package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Research extends IForgeRegistryEntry.Impl<Research> implements ResearchRequirement {

	public static IForgeRegistry<Research> REGISTRY;
	private double InspirationChance;
	private int researchProcceses;
	public String researchName;
	private ArrayList<ItemStack> unlockedItems = new ArrayList<>();
	private HashMap<ResourceLocation, Integer> tasks = new HashMap<>();

	public void addTask(ResourceLocation taskType, int taskCount) {
		tasks.put(taskType, taskCount);
	}

	public Research(String name) {
		this.setRegistryName(name);
	}

	public void addUnlockedItem(ItemStack stack) {
		unlockedItems.add(stack);
	}

	public double getInspirationChance() {
		return InspirationChance;
	}

	public boolean getRequirementsFulfilled(ResearchTeam rt) {
		for (ResearchRequirement rr : requirements) {
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

	public ArrayList<ItemStack> getUnlockedItems() {
		return (ArrayList<ItemStack>) unlockedItems.clone();
	}

	@Override
	public boolean isFulfilled(ResearchTeam rt) {
		return ResearchSavedData.get(rt.getWorld()).getTeamFinishedResearch(rt, this);
	}

	public void registerResearch() {
		System.out.println("Registering resh " + this.getRegistryName().toString());
		Research.REGISTRY.register(this);
	}

	public boolean hasTask(ResourceLocation task) {
		return tasks.containsKey(task);
	}
}
