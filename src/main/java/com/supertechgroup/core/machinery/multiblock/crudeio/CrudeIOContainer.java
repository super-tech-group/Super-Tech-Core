package com.supertechgroup.core.machinery.multiblock.crudeio;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrudeIOContainer extends Container {
	private CrudeIOTileEntity tile;

	public CrudeIOContainer(IInventory playerInventory, CrudeIOTileEntity te) {
		this.tile = te;
		// This container references items out of our own inventory as well as the slots
		// from the player inventory so that the user can transfer items between both
		// inventories. The two calls below make sure that slots are defined for both
		// inventories.
		addOwnSlots(playerInventory);
		addPlayerSlots(playerInventory);
	}

	private void addOwnSlots(IInventory playerInventory) {
		final int SLOT_X_SPACING = 18;
		final int TILE_INVENTORY_XPOS = 8;
		final int TILE_INVENTORY_YPOS = 20;
		IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		// Add the tile inventory container to the gui
		for (int x = 0; x < CrudeIOTileEntity.getSlotCount(); x++) {
			addSlotToContainer(
					new SlotItemHandler(itemHandler, x, TILE_INVENTORY_XPOS + SLOT_X_SPACING * x, TILE_INVENTORY_YPOS));
		}
	}

	private void addPlayerSlots(IInventory playerInventory) {
		// Slots for the main inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 51;
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = 58 + 51;
			addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tile.canInteractWith(playerIn);
	}

	// This is where you specify what happens when a player shift clicks a slot in
	// the gui
	// (when you shift click a slot in the TileEntity Inventory, it moves it to the
	// first available position in the hotbar and/or
	// player inventory. When you you shift-click a hotbar or player inventory item,
	// it moves it to the first available
	// position in the TileEntity inventory)
	// At the very least you must override this and return EMPTY_ITEM or the game
	// will crash when the player shift clicks a slot
	// returns EMPTY_ITEM if the source slot is empty, or if none of the the source
	// slot items could be moved
	// otherwise, returns a copy of the source stack
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex) {
		Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) {
			return ItemStack.EMPTY; // EMPTY_ITEM
		}
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (sourceSlotIndex >= 0 && sourceSlotIndex < 36) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!mergeItemStack(sourceStack, 37, 37 + CrudeIOTileEntity.getSlotCount(), false)) {
				return ItemStack.EMPTY; // EMPTY_ITEM
			}
		} else if (sourceSlotIndex >= 37 && sourceSlotIndex < 37 + CrudeIOTileEntity.getSlotCount()) {
			// This is a TE slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, 0, 36, false)) {
				return ItemStack.EMPTY; // EMPTY_ITEM
			}
		} else {
			System.err.print("Invalid slotIndex:" + sourceSlotIndex);
			return ItemStack.EMPTY; // EMPTY_ITEM
		}

		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.getCount() == 0) { // getStackSize
			sourceSlot.putStack(ItemStack.EMPTY); // EMPTY_ITEM
		} else {
			sourceSlot.onSlotChanged();
		}

		sourceSlot.onTake(player, sourceStack); // onPickupFromSlot()
		return copyOfSourceStack;
	}
}
