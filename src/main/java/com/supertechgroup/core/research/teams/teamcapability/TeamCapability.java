package com.supertechgroup.core.research.teams.teamcapability;

import com.supertechgroup.core.research.teams.ResearchTeam;

public class TeamCapability implements ITeamCapability {
	public static final String NULL_TEAM = "NULL";
	private String team = NULL_TEAM;

	@Override
	public void setTeam(ResearchTeam rt) {
		this.team = rt.getTeamName();
	}

	@Override
	public String getTeam() {
		return team;
	}

	@Override
	public void setTeam(String string) {
		this.team = string;
	}

}
