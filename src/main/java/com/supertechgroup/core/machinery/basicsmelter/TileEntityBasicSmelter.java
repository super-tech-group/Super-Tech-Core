package com.supertechgroup.core.machinery.basicsmelter;

import javax.annotation.Nullable;

import com.supertechgroup.core.capabilities.heat.HeatCapabilityProvider;
import com.supertechgroup.core.capabilities.heat.IHeatCapability;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityBasicSmelter extends TileMultiBlock implements ITickable, IHeatCapability {
	private double temperature = 0;

	private double heatToAbsorb = 0;

	@Override
	public double applyTemperatureChange() {
		temperature += heatToAbsorb;
		heatToAbsorb = 0;
		return temperature;
	}

	@Override
	protected boolean blockActivated(EntityPlayer player, EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canConnectHeat(EnumFacing side) {
		return true;
	}

	@Override
	public IHeatCapability getAdjacent(EnumFacing side) {
		TileEntity adj = world.getTileEntity(getPos().offset(side));
		if (adj != null && adj.hasCapability(HeatCapabilityProvider.HEAT_CAP, side.getOpposite())) {
			return adj.getCapability(HeatCapabilityProvider.HEAT_CAP, side.getOpposite());
		}
		return null;
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == HeatCapabilityProvider.HEAT_CAP) {
			//return HeatCapabilityProvider.HEAT_CAP.cast(this);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public double getConductionCoefficient() {
		// TODO Auto-generated method stub
		return 1.7;
	}

	@Override
	public double getInsulationCoefficient(EnumFacing side) {
		// TODO Auto-generated method stub
		return 1.7;
	}

	@Override
	public double getTemp() {
		return temperature;
	}

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability,
			@Nullable net.minecraft.util.EnumFacing facing) {
		return capability == HeatCapabilityProvider.HEAT_CAP || super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.temperature = compound.getDouble("temperature");
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item
	 * in the furnace result stack
	 */
	public void smeltItem() {
//TODO finish recipe
	}

	@Override
	public void transferHeatTo(double heat) {
		heatToAbsorb += heat;
	}

	@Override
	public void update() {
//TODO do recipe updates
		this.updateHeatCapability(getWorld());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("temperature", this.temperature);

		return compound;
	}

}