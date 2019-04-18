package com.supertechgroup.core.machinery.multiblock.crudeio;

import javax.annotation.Nullable;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CrudeIOTileEntity extends TileMultiBlock {
	static public int getSlotCount() {
		return 9;
	}

	private ItemStackHandler itemStackHandler = new ItemStackHandler(getSlotCount()) {
		@Override
		protected void onContentsChanged(int slot) {
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			CrudeIOTileEntity.this.markDirty();
		}
	};

	@Override
	protected void blockActivated(EntityPlayer player, EnumFacing side) {
		if (world.isRemote) {
			return;
		}

		player.openGui(SuperTechCoreMod.instance, Reference.GUI_CRUDE_IO, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		return super.getCapability(capability, facing);
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username
	 * in chat
	 */
	@Override
	@Nullable
	public ITextComponent getDisplayName() {
		return new TextComponentString("Crude I/O");
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		return compound;
	}
}
