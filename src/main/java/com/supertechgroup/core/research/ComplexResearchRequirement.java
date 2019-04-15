package com.supertechgroup.core.research;

import java.util.ArrayList;

import com.supertechgroup.core.research.teams.ResearchTeam;

public class ComplexResearchRequirement implements IResearchRequirement {
	private int numberRequired;

	ArrayList<IResearchRequirement> requirements = new ArrayList<>();

	public ComplexResearchRequirement(int numRequired, IResearchRequirement... rr) {
		this.numberRequired = numRequired;
		for (IResearchRequirement r : rr) {
			requirements.add(r);
		}

	}

	public int getRequiredCount() {
		return this.numberRequired;
	}

	@Override
	public boolean isFulfilled() {
		int count = 0;
		for (IResearchRequirement rr : requirements) {
			if (rr.isFulfilled()) {
				count++;
			}
		}
		return count >= numberRequired;
	}

	@Override
	public boolean isFulfilled(ResearchTeam rt) {
		int count = 0;
		for (IResearchRequirement rr : requirements) {
			if (rr.isFulfilled(rt)) {
				count++;
			}
		}
		return count >= numberRequired;
	}

	public void setRequiredCount(int num) {
		this.numberRequired = num;
	}

	@Override
	public void addRequirement(IResearchRequirement rr) {
		this.requirements.add(rr);
	}

}
