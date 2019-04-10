package com.supertechgroup.core.research;

public class Research implements ResearchRequirement {

	private double InspirationChance;
	private int researchProcceses;

	public double getInspirationChance() {
		return InspirationChance;
	}

	public int getResearchProcceses() {
		return researchProcceses;
	}

	public boolean getRequirementsFulfilled() {
		for (ResearchRequirement rr : requirements) {
			if (!rr.isFulfilled()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isFulfilled() {
		// TODO Auto-generated method stub
		return false;
	}
}
