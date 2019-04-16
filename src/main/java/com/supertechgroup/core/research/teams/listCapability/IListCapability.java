package com.supertechgroup.core.research.teams.listCapability;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Used to track team lists and other team data. Attached to worlds, but only
 * read from the overworld (ideally)
 * 
 * @author oa10712
 *
 */
public interface IListCapability {

	public void addInvite(EntityPlayer player, String researchTeam);
	public boolean doesPlayerHaveInvite(EntityPlayer player);
		
}
