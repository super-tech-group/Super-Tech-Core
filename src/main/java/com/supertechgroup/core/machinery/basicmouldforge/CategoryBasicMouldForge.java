package com.supertechgroup.core.machinery.basicmouldforge;

import com.supertechgroup.core.Reference;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;

public class CategoryBasicMouldForge implements IRecipeCategory<WrapperBasicMouldForgeRecipe> {
	private final IDrawable background;
	private final String localizedName;

	public CategoryBasicMouldForge(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(Reference.GUI_JEI_1, 0, 0, 150, 54).addPadding(1, 0, 0, 1).build();
		localizedName = I18n.format("gui.supertechcore.basicmouldforge.name");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public String getModName() {
		return Reference.MODID;
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public String getUid() {
		return Reference.CATEGORY_BASIC_SMELTING;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, WrapperBasicMouldForgeRecipe recipeWrapper,
			IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		int slotID = 0;

		for (int i = 0; i < ingredients.getInputs(VanillaTypes.ITEM).size(); i++) {
			int y = (int) Math.floor(i / 3);
			int x = i - (y * 3);
			if (i < 9) {
				itemStacks.init(slotID++, true, x * 18, 1 + y * 18);
			}
		}

		for (int i = 0; i < ingredients.getOutputs(VanillaTypes.ITEM).size(); i++) {
			int y = (int) Math.floor(i / 3);
			int x = i - (y * 3);
			if (i < 9) {
				itemStacks.init(slotID++, false, 96 + x * 18, 1 + y * 18);
			}
		}

		itemStacks.set(ingredients);
	}

}
