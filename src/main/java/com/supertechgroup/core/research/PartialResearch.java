package com.supertechgroup.core.research;

public class PartialResearch implements ResearchRequirement {
	private int numberRequired;

	public PartialResearch(int numRequired, ResearchRequirement... rr) {
		this.numberRequired = numRequired;
		for (ResearchRequirement r : rr) {
			requirements.add(r);
		}

	}

	@Override
	public boolean isFulfilled(ResearchTeam rt) {
		int count = 0;
		for (ResearchRequirement rr : requirements) {
			if (rr.isFulfilled(rt)) {
				count++;
			}
		}
		return count >= numberRequired;
	}

}
