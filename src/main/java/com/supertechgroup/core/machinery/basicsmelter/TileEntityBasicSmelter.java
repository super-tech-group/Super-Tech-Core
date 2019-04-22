package com.supertechgroup.core.machinery.basicsmelter;

import com.supertechgroup.core.capabilities.heat.HeatCapabilityProvider;
import com.supertechgroup.core.capabilities.heat.IHeatCapability;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;

public class TileEntityBasicSmelter extends TileMultiBlock implements ITickable {
	private double temperature = IHeatCapability.AMBIENT_TEMP;

	@Override
	protected boolean blockActivated(EntityPlayer player, EnumFacing side) {
		if (world.isRemote) {
			return true;
		}
		if (player.isSneaking()) {
			player.sendMessage(new TextComponentString(this.temperature + " k"));

		}
		return true;
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("temperature", this.temperature);

		return compound;
	}

	@Override
	public void update() {
		if (!world.isRemote && world.getTileEntity(getPos().down()) != null) {
			TileEntity te = world.getTileEntity(getPos().down());
			if (te.hasCapability(HeatCapabilityProvider.HEAT_CAP, EnumFacing.DOWN)) {
				IHeatCapability cap = te.getCapability(HeatCapabilityProvider.HEAT_CAP, EnumFacing.DOWN);
				this.temperature = cap.getTemp();
			}
		}

	}

}