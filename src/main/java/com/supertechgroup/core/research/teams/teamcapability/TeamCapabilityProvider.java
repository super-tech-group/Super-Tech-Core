package com.supertechgroup.core.research.teams.teamcapability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class TeamCapabilityProvider implements ICapabilitySerializable<NBTBase> {
	@CapabilityInject(ITeamCapability.class)
	public static final Capability<ITeamCapability> TEAM_CAP = null;

	private ITeamCapability instance = TEAM_CAP.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == TEAM_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == TEAM_CAP ? TEAM_CAP.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return TEAM_CAP.getStorage().writeNBT(TEAM_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		TEAM_CAP.getStorage().readNBT(TEAM_CAP, this.instance, null, nbt);
	}
}