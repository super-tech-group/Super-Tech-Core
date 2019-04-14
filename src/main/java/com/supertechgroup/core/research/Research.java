package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Research extends IForgeRegistryEntry.Impl<Research> implements IResearchRequirement {

	public static IForgeRegistry<Research> REGISTRY;
	private double InspirationChance;
	private int researchProcceses;
	public String researchName;
	private ArrayList<ItemStack> unlockedItems = new ArrayList<>();
	private HashMap<ResourceLocation, Integer> tasks = new HashMap<>();

	public Research(String name) {
		this.setRegistryName(name);
	}

	public void addTask(ResourceLocation taskType, int taskCount) {
		tasks.put(taskType, taskCount);
	}

	public void addUnlockedItem(ItemStack stack) {
		unlockedItems.add(stack);
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

	public ArrayList<ItemStack> getUnlockedItems() {
		return (ArrayList<ItemStack>) unlockedItems.clone();
	}

	public boolean hasTask(ResourceLocation task) {
		return tasks.containsKey(task);
	}

	@Override
	public boolean isFulfilled(ResearchTeam rt) {
		return rt.isResearchCompleted(this);
	}

	public void registerResearch() {
		System.out.println("Registering resh " + this.getRegistryName().toString());
		Research.REGISTRY.register(this);
	}
}
