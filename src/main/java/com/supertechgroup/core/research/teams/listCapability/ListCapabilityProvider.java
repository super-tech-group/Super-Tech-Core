package com.supertechgroup.core.research.teams.listCapability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ListCapabilityProvider implements ICapabilitySerializable<NBTBase> {
	@CapabilityInject(IListCapability.class)
	public static final Capability<IListCapability> TEAM_LIST_CAP = null;

	private IListCapability instance = TEAM_LIST_CAP.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == TEAM_LIST_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == TEAM_LIST_CAP ? TEAM_LIST_CAP.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return TEAM_LIST_CAP.getStorage().writeNBT(TEAM_LIST_CAP, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		TEAM_LIST_CAP.getStorage().readNBT(TEAM_LIST_CAP, this.instance, null, nbt);
	}
}