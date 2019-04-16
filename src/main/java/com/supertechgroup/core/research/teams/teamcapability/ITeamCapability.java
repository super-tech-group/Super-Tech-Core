package com.supertechgroup.core.research.teams.teamcapability;

import com.supertechgroup.core.research.teams.ResearchTeam;

/**
 * Used to track individual team membership. Mainly attached to players
 * 
 * @author oa10712
 *
 */
public interface ITeamCapability {
	public void setTeam(ResearchTeam rt);

	public String getTeam();

	public void setTeam(String string);
}
