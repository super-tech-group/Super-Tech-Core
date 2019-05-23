package com.supertechgroup.core.research;

import java.util.ArrayList;
import java.util.UUID;

public class ComplexResearchRequirement implements IResearchRequirement {
	public static final int ALL = -1;

	private int numberRequired;

	ArrayList<IResearchRequirement> requirements = new ArrayList<>();

	public ComplexResearchRequirement(int numRequired, IResearchRequirement... rr) {
		this.numberRequired = numRequired;
		for (IResearchRequirement r : rr) {
			requirements.add(r);
		}

	}

	@Override
	public void addRequirement(IResearchRequirement rr) {
		this.requirements.add(rr);
	}

	public int getRequiredCount() {
		return this.numberRequired;
	}

	@Override
	public boolean isFulfilled() {
		if (numberRequired == ALL) {
			for (IResearchRequirement rr : requirements) {
				if (!rr.isFulfilled()) {
					return false;
				}
			}
			return true;
		} else {
			int count = 0;
			for (IResearchRequirement rr : requirements) {
				if (rr.isFulfilled()) {
					count++;
				}
			}
			return count >= numberRequired;
		}
	}

	@Override
	public boolean isFulfilled(UUID rt) {
		if (numberRequired == ComplexResearchRequirement.ALL) {
			for (IResearchRequirement rr : requirements) {
				if (!rr.isFulfilled(rt)) {
					return false;
				}
			}
			return true;
		} else {
			int count = 0;
			for (IResearchRequirement rr : requirements) {
				if (rr.isFulfilled(rt)) {
					count++;
				}
			}
			return count >= numberRequired;
		}
	}

	public void setRequiredCount(int num) {
		this.numberRequired = num;
	}

}
