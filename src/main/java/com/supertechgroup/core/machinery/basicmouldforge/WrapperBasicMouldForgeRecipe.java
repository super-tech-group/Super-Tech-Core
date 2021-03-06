package com.supertechgroup.core.machinery.basicmouldforge;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.supertechgroup.core.recipies.BasicMouldForgeRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class WrapperBasicMouldForgeRecipe implements IRecipeWrapper {
	private static class Factory implements IRecipeWrapperFactory<BasicMouldForgeRecipe> {
		@Override
		public IRecipeWrapper getRecipeWrapper(BasicMouldForgeRecipe recipe) {
			return new WrapperBasicMouldForgeRecipe(recipe);
		}
	}

	public static final Factory FACTORY = new Factory();
	public final BasicMouldForgeRecipe recipe;
	private final List<ItemStack> inputs = new LinkedList<>();

	private int xSize = 150;

	public WrapperBasicMouldForgeRecipe(BasicMouldForgeRecipe recipe) {
		this.recipe = recipe;
		inputs.addAll(recipe.getInputs());
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		if (minecraft.fontRenderer != null) {
			String text = ((int) recipe.getTemperature()) + "k";
			int color = 0x505050;
			minecraft.fontRenderer.drawString(text, xSize / 2 - minecraft.fontRenderer.getStringWidth(text) / 2, 45,
					color, false);
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<ItemStack> outputList = new ArrayList<>();
		outputList.add(recipe.getPrimaryOutStack());
		ingredients.setInputs(VanillaTypes.ITEM, inputs);
		ingredients.setOutputs(VanillaTypes.ITEM, outputList);
	}
}
