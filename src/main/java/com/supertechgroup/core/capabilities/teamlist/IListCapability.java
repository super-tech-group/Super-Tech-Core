package com.supertechgroup.core.capabilities.teamlist;

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

	void addInvite(EntityPlayer otherPlayer, UUID team);

	void completeResearchForTeam(UUID team, Research r);

	void createTeam(EntityPlayer player);

	ArrayList<Research> getCompletedForTeam(UUID team);

	UUID[] getTeamIDs();

	String getTeamName(UUID team);

	boolean isCompletedForTeam(Research research, UUID rt);

	boolean joinTeam(EntityPlayerMP player);

	void setData(HashMap<UUID, String> teams, HashMap<UUID, ArrayList<Research>> unlockedResearch);
}
