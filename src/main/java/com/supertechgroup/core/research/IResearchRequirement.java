package com.supertechgroup.core.research;

import java.util.ArrayList;

public interface IResearchRequirement {

	ArrayList<IResearchRequirement> requirements = new ArrayList<>();

	default void addRequirement(IResearchRequirement rr) {
		requirements.add(rr);
	}

	boolean isFulfilled(ResearchTeam rt);
}
