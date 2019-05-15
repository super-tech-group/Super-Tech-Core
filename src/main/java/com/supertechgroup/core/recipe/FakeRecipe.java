package com.supertechgroup.core.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * This is a "blank" recipe used in parsing of special JSON recipes
 *
 * @author oa10712
 *
 */
public class FakeRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return false;
	}

}