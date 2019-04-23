package com.supertechgroup.core.machinery.basicsmelter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.supertechgroup.core.recipies.BasicSmelterRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class WrapperBasicSmeltingRecipe implements IRecipeWrapper {
	public static final Factory FACTORY = new Factory();
	public final BasicSmelterRecipe recipe;
	private final List<ItemStack> inputs = new LinkedList<>();
	private int xSize = 150;

	public WrapperBasicSmeltingRecipe(BasicSmelterRecipe recipe) {
		this.recipe = recipe;
		inputs.addAll(recipe.getInputs());
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ArrayList<ItemStack> outputList = new ArrayList<>();
		outputList.add(recipe.getPrimaryOutStack());
		outputList.add(recipe.getSlagStack());
		ingredients.setInputs(VanillaTypes.ITEM, inputs);
		ingredients.setOutputs(VanillaTypes.ITEM, outputList);
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		if (minecraft.fontRenderer != null) {
			String text = ((int) recipe.getTemperature()) + "k";
			int color = 0x505050;
			minecraft.fontRenderer.drawString(text,
					(float) (xSize / 2 - minecraft.fontRenderer.getStringWidth(text) / 2), (float) 45, color, false);
		}
	}

	private static class Factory implements IRecipeWrapperFactory<BasicSmelterRecipe> {
		@Override
		public IRecipeWrapper getRecipeWrapper(BasicSmelterRecipe recipe) {
			return new WrapperBasicSmeltingRecipe(recipe);
		}
	}
}
