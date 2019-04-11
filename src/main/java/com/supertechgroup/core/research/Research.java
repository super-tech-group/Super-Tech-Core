package com.supertechgroup.core.research;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Research extends IForgeRegistryEntry.Impl<Research> implements ResearchRequirement {

	public static IForgeRegistry<Research> REGISTRY;
	private double InspirationChance;
	private int researchProcceses;
	public String researchName;
	private ArrayList<ItemStack> unlockedItems = new ArrayList<>();

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
}
