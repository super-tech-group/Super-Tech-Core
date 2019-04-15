package com.supertechgroup.core.research;

import com.supertechgroup.core.research.teams.ResearchTeam;

public interface IUnlockable {
	ComplexResearchRequirement required = new ComplexResearchRequirement(1);

	/**
	 * Add another researchRequirement for this recipe. Note, each call of this adds
	 * an optional way to get this recipe. The player only needs to meet one of
	 * these requirements, but you can use PartialREsearch to make a more complex
	 * unlock
	 *
	 * @param rr
	 */
	default void addResearchUnlock(IResearchRequirement rr) {
		required.addRequirement(rr);
	}

	/**
	 * Check for unlocks on the client
	 *
	 * @return
	 */
	default boolean isUnlocked() {
		return required.isFulfilled();
	}

	/**
	 * check if the requirements have been met on the server
	 *
	 * @param team
	 * @return
	 */
	public default boolean isUnlocked(ResearchTeam team) {
		return required.isFulfilled(team);
	}

	default void setRequirementsNeeded(int num) {
		required.setRequiredCount(num);
	}
}
