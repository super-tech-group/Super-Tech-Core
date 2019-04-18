package com.supertechgroup.core.capabilities.heat;

import net.minecraft.util.EnumFacing;

public class HeatCapability implements IHeatCapability {
	private double temperature = 0;
	private double heatToAbsorb = 0;

	@Override
	public double applyTemperatureChange() {
		temperature += heatToAbsorb;
		heatToAbsorb = 0;
		return temperature;
	}

	@Override
	public boolean canConnectHeat(EnumFacing side) {
		return true;
	}

	@Override
	public IHeatCapability getAdjacent(EnumFacing side) {
		return null;
	}

	@Override
	public double getConductionCoefficient() {
		return 1;
	}

	@Override
	public double getInsulationCoefficient(EnumFacing side) {
		return 1;
	}

	@Override
	public double getTemp() {
		return temperature;
	}

	@Override
	public void transferHeatTo(double heat) {
		heatToAbsorb += heat;
	}

}
