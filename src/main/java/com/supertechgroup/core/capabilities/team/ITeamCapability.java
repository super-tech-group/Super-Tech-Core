package com.supertechgroup.core.capabilities.team;

import java.util.UUID;

/**
 * Used to track individual team membership. Mainly attached to players
 *
 * @author oa10712
 *
 */
public interface ITeamCapability {
	UUID getTeam();

	void setTeam(UUID teamID);
}
