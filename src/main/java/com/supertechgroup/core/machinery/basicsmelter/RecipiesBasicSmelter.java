package com.supertechgroup.core.machinery.basicsmelter;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

public class RecipiesBasicSmelter {
	private static final RecipiesBasicSmelter RECIPE_BASE = new RecipiesBasicSmelter();
	/** The list of smelting results. */
	private final Map<ItemStack, ItemStack> smeltingList = Maps.<ItemStack, ItemStack>newHashMap();

	public static RecipiesBasicSmelter instance() {
		// TODO Auto-generated method stub
		return RECIPE_BASE;
	}

	/**
	 * Adds a smelting recipe using an ItemStack as the input for the recipe.
	 */
	public void addSmeltingRecipe(ItemStack input, ItemStack stack, float experience) {
		if (getSmeltingResult(input) != ItemStack.EMPTY) {
			net.minecraftforge.fml.common.FMLLog.log
					.info("Ignored basic smelting recipe with conflicting input: {} = {}", input, stack);
			return;
		}
		this.smeltingList.put(input, stack);
	}

	/**
	 * Returns the smelting result of an item.
	 */
	public ItemStack getSmeltingResult(ItemStack stack) {
		for (Entry<ItemStack, ItemStack> entry : this.smeltingList.entrySet()) {
			if (this.compareItemStacks(stack, entry.getKey())) {
				return entry.getValue();
			}
		}

		return ItemStack.EMPTY;
	}

	/**
	 * Compares two itemstacks to ensure that they are the same. This checks both
	 * the item and the metadata of the item.
	 */
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem() == stack1.getItem()
				&& (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}

}
