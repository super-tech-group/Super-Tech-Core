package com.supertechgroup.core.research;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityResearchStation extends TileEntity {

	private String team;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("team", team);
		System.out.println("Saving researchtable " + compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		System.err.println("Reading from NBT " + compound.toString());
		team = compound.getString("team");
	}

	public ResearchTeam getTeam() {
		//We've got to do it this way, the ResearchSavedData isn't ready to be read from when tile entities are created.
		return ResearchSavedData.get(world).getTeamByName(team);
	}

	public void setTeam(ResearchTeam nTeam) {
		team = nTeam.getTeamName();

	}
}
