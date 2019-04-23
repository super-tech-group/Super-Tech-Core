package com.supertechgroup.core.capabilities.team;

import java.util.UUID;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class TeamCapabilityStorage implements IStorage<ITeamCapability> {

	@Override
	public void readNBT(Capability<ITeamCapability> capability, ITeamCapability instance, EnumFacing side,
			NBTBase nbt) {
		instance.setTeam(UUID.fromString(((NBTTagString) nbt).getString()));
	}

	@Override
	public NBTBase writeNBT(Capability<ITeamCapability> capability, ITeamCapability instance, EnumFacing side) {
		return new NBTTagString(instance.getTeam().toString());
	}

}
