package com.supertechgroup.core.machinery.basicsmelter;

import javax.annotation.Nullable;

import com.supertechgroup.core.machinery.slots.SlotItemHandlerFuel;
import com.supertechgroup.core.machinery.slots.SlotItemHandlerOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBasicSmelter extends Container {

	public TileEntityBasicSmelter te;
	private int cookTime;
	private int totalCookTime;
	private int furnaceBurnTime;
	private int currentItemBurnTime;

	public ContainerBasicSmelter(IInventory playerInventory, TileEntityBasicSmelter te) {
		this.te = te;
		// This container references items out of our own inventory (the 5 slots
		// we hold ourselves)
		// as well as the slots from the player inventory so that the user can
		// transfer items between
		// both inventories. The two calls below make sure that slots are
		// defined for both inventories.
		addOwnSlots(playerInventory);
		addPlayerSlots(playerInventory);
	}

	private void addOwnSlots(IInventory playerInventory) {
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		// Add our own slots
		this.addSlotToContainer(new SlotItemHandler(itemHandler, 0, 56, 17));// input
		this.addSlotToContainer(new SlotItemHandler(itemHandler, 3, 34, 35));// flux
		this.addSlotToContainer(new SlotItemHandlerFuel(itemHandler, 1, 56, 53));// fuel
		this.addSlotToContainer(new SlotItemHandlerOutput(itemHandler, 2, 116, 35));// primary out
		this.addSlotToContainer(new SlotItemHandlerOutput(itemHandler, 4, 142, 35));// slag out
	}

	private void addPlayerSlots(IInventory playerInventory) {
		// Slots for the main inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 70;
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = 58 + 70;
			addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icontainerlistener = this.listeners.get(i);

			if (this.cookTime != te.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, te.getField(2));
			}

			if (this.furnaceBurnTime != te.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, te.getField(0));
			}

			if (this.currentItemBurnTime != te.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, this.te.getField(1));
			}

			if (this.totalCookTime != te.getField(3)) {
				icontainerlistener.sendWindowProperty(this, 3, te.getField(3));
			}
		}

		this.cookTime = te.getField(2);
		this.furnaceBurnTime = te.getField(0);
		this.currentItemBurnTime = te.getField(1);
		this.totalCookTime = te.getField(3);
	}

	@Nullable

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TileEntityBasicSmelter.getSlotsNumber()) {
				if (!mergeItemStack(itemstack1, TileEntityBasicSmelter.getSlotsNumber(), inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(itemstack1, 0, TileEntityBasicSmelter.getSlotsNumber(), false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		te.setField(id, data);
	}
}
