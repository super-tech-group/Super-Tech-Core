package com.supertechgroup.core.research;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class ResearchTasks {
	public static final HashMap<Object, ResourceLocation> craftingTableResearch = new HashMap<>();

	public static void addTask(Object itemStack, ResourceLocation resourceLocation) {
		craftingTableResearch.put(itemStack, resourceLocation);
	}

	public static ResourceLocation getFromResultStack(ItemStack stack) {
		for (Object o : craftingTableResearch.keySet()) {
			if (o instanceof ItemStack && ItemStack.areItemStacksEqual(stack, (ItemStack) o)) {
				return craftingTableResearch.get(o);
			}
			if (o instanceof Ingredient && ((Ingredient) o).apply(stack)) {
				return craftingTableResearch.get(o);
			}
		}
		return new ResourceLocation("null:void");
	}
}
