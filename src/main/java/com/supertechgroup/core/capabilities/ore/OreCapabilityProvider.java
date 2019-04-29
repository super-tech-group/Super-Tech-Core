package com.supertechgroup.core.capabilities.ore;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class OreCapabilityProvider implements ICapabilitySerializable<NBTBase> {
	@CapabilityInject(IOreCapability.class)
	public static final Capability<IOreCapability> ORE_CAP = null;

	private IOreCapability instance = ORE_CAP.getDefaultInstance();

	@Override
	public void deserializeNBT(NBTBase nbt) {
		ORE_CAP.getStorage().readNBT(ORE_CAP, this.instance, null, nbt);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == ORE_CAP ? ORE_CAP.<T>cast(this.instance) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == ORE_CAP;
	}

	@Override
	public NBTBase serializeNBT() {
		return ORE_CAP.getStorage().writeNBT(ORE_CAP, this.instance, null);
	}
}