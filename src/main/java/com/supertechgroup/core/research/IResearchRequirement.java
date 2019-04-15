package com.supertechgroup.core.research;

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
	 * @param rt the ResearchTeam to check for requirements
	 * @return
	 */
	boolean isFulfilled(ResearchTeam rt);
}
