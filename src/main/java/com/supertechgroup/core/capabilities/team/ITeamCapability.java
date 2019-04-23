package com.supertechgroup.core.capabilities.team;

import java.util.UUID;

/**
 * Used to track individual team membership. Mainly attached to players
 *
 * @author oa10712
 *
 */
public interface ITeamCapability {
	public UUID getTeam();

	public void setTeam(UUID teamID);
}