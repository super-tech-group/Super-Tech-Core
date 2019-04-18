package com.supertechgroup.core.machinery.multiblock.crudeheater;

import javax.annotation.Nullable;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.capabilities.heat.HeatCapabilityProvider;
import com.supertechgroup.core.capabilities.heat.IHeatCapability;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;

import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CrudeHeaterTileEntity extends TileMultiBlock implements IHeatCapability, ITickable {
	static public int getSlotCount() {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(CrudeHeaterTileEntity tile) {
		return tile.getField(0) > 0;
	}

	/** The number of ticks that the furnace will keep burning */
	private int burnTime;

	/**
	 * The number of ticks that a fresh copy of the currently-burning item would
	 * keep the furnace burning for
	 */
	private int currentItemBurnTime;

	private ItemStackHandler itemStackHandler = new ItemStackHandler(getSlotCount()) {
		@Override
		protected void onContentsChanged(int slot) {
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			CrudeHeaterTileEntity.this.markDirty();
		}
	};

	private double temperature = 0;

	private double heatToAbsorb = 0;

	private double heatValue = 0;

	@Override
	public double applyTemperatureChange() {
		temperature += heatToAbsorb;
		heatToAbsorb = 0;
		return temperature;
	}

	@Override
	protected boolean blockActivated(EntityPlayer player, EnumFacing side) {
		if (world.isRemote) {
			return true;
		}

		player.openGui(SuperTechCoreMod.instance, Reference.GUI_CRUDE_HEATER, world, pos.getX(), pos.getY(),
				pos.getZ());
		return true;
	}

	private boolean canBurn() {
		// TODO make this read redstone info, e.g. active with a lever
		return true;
	}

	@Override
	public boolean canConnectHeat(EnumFacing side) {
		return true;
	}

	@Override
	public IHeatCapability getAdjacent(EnumFacing side) {
		TileEntity adj = world.getTileEntity(getPos().offset(side));
		if (adj.hasCapability(HeatCapabilityProvider.HEAT_CAP, side.getOpposite())) {
			return adj.getCapability(HeatCapabilityProvider.HEAT_CAP, side.getOpposite());
		}
		return null;
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		if (capability == HeatCapabilityProvider.HEAT_CAP) {
			return HeatCapabilityProvider.HEAT_CAP.cast(this);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public double getConductionCoefficient() {
		// TODO Auto-generated method stub
		return 1.7;
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username
	 * in chat
	 */
	@Override
	@Nullable
	public ITextComponent getDisplayName() {
		return new TextComponentString("Crude Heater");
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return this.burnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return (int) this.heatValue;
		default:
			return 0;
		}
	}

	public int getFieldCount() {
		return 3;
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
		return capability == HeatCapabilityProvider.HEAT_CAP
				|| capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	/**
	 * Furnace isBurning
	 */
	public boolean isBurning() {
		return this.burnTime > 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		this.burnTime = compound.getInteger("BurnTime");
		this.currentItemBurnTime = ForgeEventFactory.getItemBurnTime(this.itemStackHandler.getStackInSlot(0));
		this.heatValue = compound.getDouble("heatValue");
		this.temperature = compound.getDouble("temperature");
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.heatValue = value;
			break;
		}
	}

	@Override
	public void transferHeatTo(double heat) {
		heatToAbsorb += heat;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update() {
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {

			transferHeatTo((heatValue - this.getTemp()) / (this.getConductionCoefficient() * TRANSFER_RATE));
			--this.burnTime;
		}

		if (!this.world.isRemote) {

			ItemStack itemstack = this.itemStackHandler.getStackInSlot(0);

			if (this.isBurning() || !itemstack.isEmpty()) {
				if (!this.isBurning() && this.canBurn()) {
					this.burnTime = ForgeEventFactory.getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.burnTime;
					this.heatValue = HeatCapabilityProvider.getHeatValueForStack(itemstack);

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.itemStackHandler.setStackInSlot(1, item1);
							}
						}
					}
				}
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				BlockFurnace.setState(this.isBurning(), this.world, this.pos);
			}
		}

		this.updateHeatCapability(getWorld());
		if (flag1) {
			this.markDirty();
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		compound.setInteger("BurnTime", (short) this.burnTime);
		compound.setDouble("heatValue", this.heatValue);
		compound.setDouble("temperature", this.temperature);
		return compound;
	}

}
