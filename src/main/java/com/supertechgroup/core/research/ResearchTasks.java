package com.supertechgroup.core.research;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ResearchTasks {
	public static final HashMap<ItemStack, ResourceLocation> craftingTableResearch = new HashMap<>();

	public static void addTask(ItemStack itemStack, ResourceLocation resourceLocation) {
		craftingTableResearch.put(itemStack, resourceLocation);
	}

	public static ResourceLocation getFromResultStack(ItemStack stack) {
		for (ItemStack is : craftingTableResearch.keySet()) {
			if (ItemStack.areItemsEqual(is, stack)) {
				return craftingTableResearch.get(is);
			}
		}
		return new ResourceLocation("null:void");
	}
}
