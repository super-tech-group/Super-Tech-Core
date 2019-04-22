package com.supertechgroup.core.capabilities.heat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class HeatCapabilityProvider implements ICapabilitySerializable<NBTBase> {
	@CapabilityInject(IHeatCapability.class)
	public static final Capability<IHeatCapability> HEAT_CAP = null;

	public static double getHeatValueForStack(ItemStack itemstack) {
		// TODO Auto-generated method stub
		return 2222;
	}

	private IHeatCapability instance = HEAT_CAP.getDefaultInstance();

	@Override
	public void deserializeNBT(NBTBase nbt) {
		HEAT_CAP.getStorage().readNBT(HEAT_CAP, this.instance, null, nbt);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == HEAT_CAP ? HEAT_CAP.<T>cast(this.instance) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == HEAT_CAP;
	}

	@Override
	public NBTBase serializeNBT() {
		return HEAT_CAP.getStorage().writeNBT(HEAT_CAP, this.instance, null);
	}
}
