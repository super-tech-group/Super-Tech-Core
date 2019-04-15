package com.supertechgroup.core.research;

import java.util.ArrayList;

public interface IResearchRequirement {

	ArrayList<IResearchRequirement> requirements = new ArrayList<>();

	default void addRequirement(IResearchRequirement rr) {
		requirements.add(rr);
	}

	/**
	 * Check if all requirements have been met on the server
	 * 
	 * @param rt the ResearchTeam to check for requirements
	 * @return
	 */
	boolean isFulfilled(ResearchTeam rt);

	/**
	 * The check for use on client side.
	 * 
	 * @return
	 */
	boolean isFulfilled();
}
