package com.supertechgroup.core.research;

import net.minecraftforge.registries.IForgeRegistryEntry;

public class Research extends IForgeRegistryEntry.Impl<Research> implements ResearchRequirement {

	private double InspirationChance;
	private int researchProcceses;

	public double getInspirationChance() {
		return InspirationChance;
	}

	public int getResearchProcceses() {
		return researchProcceses;
	}

	public boolean getRequirementsFulfilled(ResearchTeam rt) {
		for (ResearchRequirement rr : requirements) {
			if (!rr.isFulfilled(rt)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isFulfilled(ResearchTeam rt) {
		return ResearchSavedData.get(rt.getWorld()).getTeamFinishedResearch(rt, this);
	}
}
