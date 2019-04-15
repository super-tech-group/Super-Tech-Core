package com.supertechgroup.core.research.teams;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class TeamCapabilityStorage implements IStorage<ITeamCapability> {

	@Override
	public NBTBase writeNBT(Capability<ITeamCapability> capability, ITeamCapability instance, EnumFacing side) {
		return new NBTTagString(instance.getTeam());
	}

	@Override
	public void readNBT(Capability<ITeamCapability> capability, ITeamCapability instance, EnumFacing side,
			NBTBase nbt) {
		instance.setTeam(((NBTTagString) nbt).getString());
	}

}
