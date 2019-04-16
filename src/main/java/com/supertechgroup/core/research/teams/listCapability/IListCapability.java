package com.supertechgroup.core.research.teams.listCapability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.supertechgroup.core.research.Research;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Used to track team lists and other team data. Attached to worlds, but only
 * read from the overworld (ideally)
 *
 * @author oa10712
 *
 */
public interface IListCapability {

	public void addInvite(EntityPlayer otherPlayer, UUID team);

	public void completeResearchForTeam(UUID team, Research r);

	public void createTeam(EntityPlayer player);

	public ArrayList<Research> getCompletedForTeam(UUID team);

	public UUID[] getTeamIDs();

	public String getTeamName(UUID team);

	public boolean isCompletedForTeam(Research research, UUID rt);

	public boolean joinTeam(EntityPlayerMP player);

	public void setData(HashMap<UUID, String> teams, HashMap<UUID, ArrayList<Research>> unlockedResearch);
}
