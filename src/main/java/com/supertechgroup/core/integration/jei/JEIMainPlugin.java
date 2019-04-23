package com.supertechgroup.core.integration.jei;

import java.util.Collections;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.basicsmelter.CategoryBasicSmelting;
import com.supertechgroup.core.machinery.basicsmelter.WrapperBasicSmeltingRecipe;
import com.supertechgroup.core.recipies.BasicSmelterRecipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

@JEIPlugin
public class JEIMainPlugin implements IModPlugin {
	private static boolean jeiActive = Loader.isModLoaded("jei");
	public static IIngredientRegistry ingredientRegistry;

	public static IJeiHelpers jeiHelpers;

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
		jeiHelpers = registry.getJeiHelpers();

		registry.handleRecipes(BasicSmelterRecipe.class, WrapperBasicSmeltingRecipe.FACTORY,
				Reference.CATEGORY_BASIC_SMELTING);
		registry.addRecipes(BasicSmelterRecipe.getEntries(), Reference.CATEGORY_BASIC_SMELTING);
		//TODO add a item to represent the smelter here
		//registry.addRecipeCatalyst(new ItemStack(Items.APPLE), Reference.CATEGORY_BASIC_SMELTING);

		System.out.println("Added recipe handling for smelter");

	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new CategoryBasicSmelting(registry.getJeiHelpers().getGuiHelper()));
	}
}
