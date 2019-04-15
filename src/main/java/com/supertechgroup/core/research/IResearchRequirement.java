package com.supertechgroup.core.research;

import java.util.ArrayList;

public interface IResearchRequirement {

	ArrayList<IResearchRequirement> requirements = new ArrayList<>();

	default void addRequirement(IResearchRequirement rr) {
		requirements.add(rr);
	}

	/**
	 * The check for use on client side.
	 *
	 * @return
	 */
	boolean isFulfilled();

	/**
	 * Check if all requirements have been met on the server
	 *
	 * @param rt the ResearchTeam to check for requirements
	 * @return
	 */
	boolean isFulfilled(ResearchTeam rt);
}
