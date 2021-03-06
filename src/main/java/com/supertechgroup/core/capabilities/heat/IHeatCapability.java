package com.supertechgroup.core.capabilities.heat;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IHeatCapability {
	/**
	 * The value of the zero point of our temperature scale in kelvin
	 */
	double AMBIENT_TEMP = 300;

	default void applyTemperatureChange() {
		double newTemp = this.getTemp() + (this.getJouleChange() / (this.getSpecHeatMass()));

		this.setTemp(newTemp);
		this.setJouleChange(0);
	}

	default double calcJoules(double otherTemp, EnumFacing facing) {
		return (((this.getConductionCoefficient(facing) * (otherTemp - this.getTemp())) / 20));
	}

	boolean canConnectHeat(EnumFacing side);

	IHeatCapability getAdjacent(EnumFacing side);

	double getConductionCoefficient(EnumFacing side);

	double getJouleChange();

	/**
	 *
	 * @return the specific heat multiplied by the mass for the onbject this
	 *         represents
	 */
	double getSpecHeatMass();

	double getTemp();

	void setJouleChange(double d);

	void setTemp(Double newTemp);

	default void updateHeatCapability(World world) {
		if (world.getTotalWorldTime() % 2 == 0) {
			double jouleChange = 0;
			for (EnumFacing facing : EnumFacing.VALUES) {
				IHeatCapability adj = getAdjacent(facing);
				if (adj != null) {
					jouleChange += calcJoules(adj.getTemp(), facing);
				} else {
					jouleChange += calcJoules(AMBIENT_TEMP, facing);
				}
			}
			this.setJouleChange(this.getJouleChange() + jouleChange);
		} else {
			applyTemperatureChange();
		}
	}
}
