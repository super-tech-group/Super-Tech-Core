package com.supertechgroup.core.integration.jei;

import java.util.Collections;

import com.supertechgroup.core.items.MaterialItem;
import com.supertechgroup.core.metallurgy.Material;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class JEIMainPlugin implements IModPlugin {
	private static boolean jeiActive = Loader.isModLoaded("jei");
	public static IIngredientRegistry ingredientRegistry;

	@Override
	public void register(IModRegistry registry) {
		ingredientRegistry = registry.getIngredientRegistry();
	}

	private static void blacklistItem(ItemStack stack) {
		if (jeiActive) {
			System.out.println("Blacklisting item: " + stack);
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ingredientRegistry.removeIngredientsAtRuntime(VanillaTypes.ITEM, Collections.singletonList(stack));
			});
		}
	}

	private static void whitelistItem(ItemStack stack) {
		if (jeiActive) {
			System.out.println("Whitelisting item: " + stack);
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ingredientRegistry.addIngredientsAtRuntime(VanillaTypes.ITEM, Collections.singletonList(stack));
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
}
