package com.supertechgroup.core.machinery.slots;

import javax.annotation.Nonnull;

import com.supertechgroup.core.util.Helpers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerFuel extends SlotItemHandler {

	public SlotItemHandlerFuel(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	/**
	 * Check if the stack is allowed to be placed in this slot, used for armor slots
	 * as well as furnace fuel.
	 */
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return Helpers.getItemBurnTime(stack) > 0;
	}

}
