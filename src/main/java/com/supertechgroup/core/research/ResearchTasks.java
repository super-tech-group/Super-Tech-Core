package com.supertechgroup.core.research;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class ResearchTasks {
	public static final HashMap<String, HashMap<Object, String>> researchTaskMap = new HashMap<>();

	public static void addTask(String type, String name, Object o) {
		if (researchTaskMap.containsKey(type)) {
			researchTaskMap.get(type).put(o, name);
		} else {
			HashMap<Object, String> typeMap = new HashMap<>();
			typeMap.put(o, name);
			researchTaskMap.put(type, typeMap);
		}
	}

	public static ResourceLocation getFromResultStack(String type, ItemStack stack) {
		HashMap<Object, String> taskMap = researchTaskMap.getOrDefault(type, new HashMap<>());
		for (Object o : taskMap.keySet()) {
			if (o instanceof ItemStack && ItemStack.areItemStacksEqual(stack, (ItemStack) o)) {
				return new ResourceLocation(type, taskMap.get(o));
			}
			if (o instanceof Ingredient && ((Ingredient) o).apply(stack)) {
				return new ResourceLocation(type, taskMap.get(o));
			}
		}
		return new ResourceLocation("null:void");
	}
}
