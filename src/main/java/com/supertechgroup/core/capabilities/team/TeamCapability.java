package com.supertechgroup.core.capabilities.team;

import java.util.UUID;

public class TeamCapability implements ITeamCapability {
	public static final UUID NULL_TEAM = new UUID(0L, 0L);
	private UUID team = NULL_TEAM;

	@Override
	public UUID getTeam() {
		return team;
	}

	@Override
	public void setTeam(UUID teamID) {
		team = teamID;
	}

}
