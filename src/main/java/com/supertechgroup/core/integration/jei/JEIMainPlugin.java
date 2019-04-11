package com.supertechgroup.core.integration.jei;

import java.util.Collections;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class JEIMainPlugin implements IModPlugin {
	private static boolean jeiActive = Loader.isModLoaded("jei");
	public static IIngredientRegistry ingredientRegistry;

	private static void blacklistItem(ItemStack stack) {
		if (jeiActive) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ingredientRegistry.removeIngredientsAtRuntime(VanillaTypes.ITEM, Collections.singletonList(stack));
			});
		}
	}

	public static void handleItemBlacklisting(ItemStack stack, boolean shouldBlacklist) {
		if (shouldBlacklist) {
			blacklistItem(stack);

			return;
		}
		whitelistItem(stack);

	}

	private static void whitelistItem(ItemStack stack) {
		if (jeiActive) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ingredientRegistry.addIngredientsAtRuntime(VanillaTypes.ITEM, Collections.singletonList(stack));
			});
		}
	}

	@Override
	public void register(IModRegistry registry) {
		ingredientRegistry = registry.getIngredientRegistry();
	}
}
