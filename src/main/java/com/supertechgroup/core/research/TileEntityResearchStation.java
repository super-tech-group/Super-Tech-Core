package com.supertechgroup.core.research;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityResearchStation extends TileEntity {

	private String team;

	public ResearchTeam getTeam() {
		// We've got to do it this way, the ResearchSavedData isn't ready to be read
		// from when tile entities are created.
		return ResearchSavedData.get(world).getTeamByName(team);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		team = compound.getString("team");
	}

	public void setTeam(ResearchTeam nTeam) {
		team = nTeam.getTeamName();

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("team", team);
		return compound;
	}
}
