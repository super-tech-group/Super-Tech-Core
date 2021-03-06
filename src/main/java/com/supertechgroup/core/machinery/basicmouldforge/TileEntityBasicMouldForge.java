package com.supertechgroup.core.machinery.basicmouldforge;

import com.supertechgroup.core.capabilities.heat.HeatCapabilityProvider;
import com.supertechgroup.core.capabilities.heat.IHeatCapability;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;
import com.supertechgroup.core.recipies.BasicMouldForgeRecipe;
import com.supertechgroup.core.util.Helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileEntityBasicMouldForge extends TileMultiBlock implements ITickable {
	private double processTemp = IHeatCapability.AMBIENT_TEMP;
	private BasicMouldForgeRecipe inProgress;
	private EnumFacing facing = EnumFacing.NORTH;

	@Override
	protected boolean blockActivated(EntityPlayer player, EnumFacing side) {
		if (world.isRemote) {
			return true;
		}
		if (player.isSneaking()) {
			player.sendMessage(new TextComponentString(this.processTemp + " k"));

		}
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.processTemp = compound.getDouble("processTemp");
		this.facing = EnumFacing.byName(compound.getString("facing"));
		if (compound.hasKey("processName")) {
			this.inProgress = BasicMouldForgeRecipe.get(new ResourceLocation(compound.getString("processName")));
		}
	}

	public void setFacing(EnumFacing side) {
		this.facing = side;
	}

	private void smeltItem() {
		System.out.println("Putting items in " + getPos().offset(facing.getOpposite()));
		TileEntity behindBelow = world.getTileEntity(getPos().offset(facing.getOpposite()).down());
		if (behindBelow.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
			IItemHandler primaryHandler = behindBelow.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
					facing.getOpposite());

			if (Helpers.hasInventorySpace(inProgress.getPrimaryOutStack(), primaryHandler, 0,
					primaryHandler.getSlots())) {
				// if we have room for both the slag and the primary output add them
				Helpers.tryPlaceItemInInventory(inProgress.getPrimaryOutStack(),
						(IItemHandlerModifiable) primaryHandler, 0, primaryHandler.getSlots(), false);

				// and reset the crafting process to be ready for the next one
				inProgress = null;
				this.processTemp = IHeatCapability.AMBIENT_TEMP;
			}
		}
	}

	private void tryStartSmelt() {
		TileEntity te = world.getTileEntity(getPos().up());
		if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
			IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
			for (BasicMouldForgeRecipe bsr : BasicMouldForgeRecipe.getEntries()) {
				if (bsr.apply(cap)) {
					bsr.start(cap);
					this.inProgress = bsr;
					break;
				}
			}
		}
	}

	@Override
	public void update() {
		if (!world.isRemote) {
			if (world.getTileEntity(getPos().down()) != null) {
				TileEntity te = world.getTileEntity(getPos().down());
				if (te.hasCapability(HeatCapabilityProvider.HEAT_CAP, EnumFacing.DOWN)) {
					IHeatCapability cap = te.getCapability(HeatCapabilityProvider.HEAT_CAP, EnumFacing.DOWN);

					if (this.inProgress == null) {
						tryStartSmelt();
					} else {
						if (this.processTemp >= inProgress.getTemperature()) {
							smeltItem();
							this.inProgress = null;
							this.processTemp = IHeatCapability.AMBIENT_TEMP;
						} else {
							double jouleChange = (((this.inProgress.getCoefficient()
									* (cap.getTemp() - this.processTemp)) / 20));
							this.processTemp += (jouleChange / (this.inProgress.getSpecificHeatMass()));
						}
					}
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("processTemp", this.processTemp);
		compound.setString("facing", facing.toString());
		if (inProgress != null) {
			compound.setString("processName", inProgress.getRegistryName().toString());
		}
		return compound;
	}

}