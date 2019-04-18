package com.supertechgroup.core.capabilities.heat;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IHeatCapability {
	/**
	 * The inverse transfer rate of heat. Higher values mean slower transfer.
	 */
	double TRANSFER_RATE = 6;
	/**
	 * The value of the zero point of our temperature scale in kelvin
	 */
	double AMBIENT_TEMP = 300;

	/**
	 * The heat transfer coefficient for air
	 */
	double AIR_COEFFICIENT = 38;

	double applyTemperatureChange();

	boolean canConnectHeat(EnumFacing side);

	public IHeatCapability getAdjacent(EnumFacing side);

	double getConductionCoefficient();

	double getInsulationCoefficient(EnumFacing side);

	double getTemp();

	void transferHeatTo(double heat);

	public default void updateHeatCapability(World world) {
		if (world.getTotalWorldTime() % 2 == 0) {
			for (EnumFacing facing : EnumFacing.VALUES) {
				IHeatCapability adj = getAdjacent(facing);
				if (adj != null) {
					transferHeatTo(
							(adj.getTemp() - this.getTemp()) / (this.getConductionCoefficient() * TRANSFER_RATE));
				} else {
					transferHeatTo(
							(AMBIENT_TEMP - this.getTemp()) / (this.getInsulationCoefficient(facing) * TRANSFER_RATE));
				}
			}
		} else {
			applyTemperatureChange();
		}
	}
}
