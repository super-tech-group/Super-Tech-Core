package com.supertechgroup.core.research;

import java.util.UUID;

public interface IUnlockable {

	/**
	 * Add another researchRequirement for this recipe. Note, each call of this adds
	 * an optional way to get this recipe. The player only needs to meet one of
	 * these requirements, but you can use PartialREsearch to make a more complex
	 * unlock
	 *
	 * @param rr
	 */
	void addResearchUnlock(IResearchRequirement rr);

	/**
	 * Check for unlocks on the client
	 *
	 * @return
	 */
	boolean isUnlocked();

	/**
	 * check if the requirements have been met on the server
	 *
	 * @param team
	 * @return
	 */
	public boolean isUnlocked(UUID team);

	void setRequirementsNeeded(int num);
}
