package com.supertechgroup.core.research;

import java.util.ArrayList;

public interface ResearchRequirement {

	ArrayList<ResearchRequirement> requirements = new ArrayList<>();

	default void addRequirement(ResearchRequirement rr) {
		requirements.add(rr);
	}

	boolean isFulfilled(ResearchTeam rt);
}
