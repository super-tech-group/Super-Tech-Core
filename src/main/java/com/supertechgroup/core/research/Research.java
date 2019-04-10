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
		// TODO Auto-generated method stub
		return false;
	}
}
