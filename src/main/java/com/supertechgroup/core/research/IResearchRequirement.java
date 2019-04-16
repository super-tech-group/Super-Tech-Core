package com.supertechgroup.core.research;

import java.util.UUID;

public interface IResearchRequirement {

	void addRequirement(IResearchRequirement rr);

	/**
	 * The check for use on client side.
	 *
	 * @return
	 */
	boolean isFulfilled();

	/**
	 * Check if all requirements have been met on the server
	 *
	 * @param teamID the ResearchTeam to check for requirements
	 * @return
	 */
	boolean isFulfilled(UUID teamID);
}
