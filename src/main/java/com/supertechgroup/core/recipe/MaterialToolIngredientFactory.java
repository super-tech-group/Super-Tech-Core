package com.supertechgroup.core.recipe;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.supertechgroup.core.items.MaterialItem;
import com.supertechgroup.core.items.MaterialTool;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.recipe.MaterialIngredientCriteria.Type;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialToolIngredientFactory implements IIngredientFactory {
	public static class MaterialToolIngredient extends Ingredient {
		MaterialIngredientCriteria[] criteria;
		private NonNullList<ItemStack> matches;
		int type;
		private IntList itemIds = null;
		private ItemStack[] array = null;
		private int lastSizeA = -1, lastSizeL = -1;

		/**
		 * An ingredient made of a material
		 *
		 * @param type     The type of Material Item. See the constants in #MaterialTool
		 * @param criteria
		 */
		public MaterialToolIngredient(int type, MaterialIngredientCriteria... criteria) {
			this.type = type;
			this.criteria = criteria;
			this.matches = NonNullList.create();
			Material.REGISTRY.forEach((mat) -> {
				boolean pass = true;
				for (MaterialIngredientCriteria c : criteria) {
					if (!c.meetsCriteria(mat)) {
						pass = false;
						break;
					}
				}
				if (pass) {
					matches.add(new ItemStack(new MaterialTool(mat, type), 1, type));
				}
			});
		}

		@Override
		public boolean apply(@Nullable ItemStack input) {
			if (input == null) {
				return false;
			}

			if (input.getItem() instanceof MaterialTool && input.getMetadata() == type) {
				MaterialTool matItem = (MaterialTool) input.getItem();
				for (MaterialIngredientCriteria c : this.criteria) {

					if (!c.meetsCriteria(matItem.getMaterial())) {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		@Nonnull
		public ItemStack[] getMatchingStacks() {
			if (array == null || this.lastSizeA != matches.size()) {
				NonNullList<ItemStack> lst = NonNullList.create();
				for (ItemStack itemstack : this.matches) {
					if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
						itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
					} else {
						lst.add(itemstack);
					}
				}
				this.array = lst.toArray(new ItemStack[lst.size()]);
				this.lastSizeA = matches.size();
			}
			return this.array;
		}

		@Override
		@Nonnull
		public IntList getValidItemStacksPacked() {
			if (this.itemIds == null || this.lastSizeL != matches.size()) {
				this.itemIds = new IntArrayList(this.matches.size());

				for (ItemStack itemstack : this.matches) {
					if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
						NonNullList<ItemStack> lst = NonNullList.create();
						itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
						for (ItemStack item : lst) {
							this.itemIds.add(RecipeItemHelper.pack(item));
						}
					} else {
						this.itemIds.add(RecipeItemHelper.pack(itemstack));
					}
				}

				this.itemIds.sort(IntComparators.NATURAL_COMPARATOR);
				this.lastSizeL = matches.size();
			}

			return this.itemIds;
		}

		@Override
		protected void invalidate() {
			this.itemIds = null;
			this.array = null;
		}

		@Override
		public boolean isSimple() {
			return true;
		}
	}

	@Override
	public Ingredient parse(JsonContext context, JsonObject json) {
		ArrayList<MaterialIngredientCriteria> reqs = new ArrayList<>();
		json.entrySet().forEach((element) -> {
			if (element.getKey().equalsIgnoreCase("type") || element.getKey().equalsIgnoreCase("item")) {
				// ignore this case, as we handle it later
			} else {
				reqs.add(new MaterialIngredientCriteria(
						Material.Property
								.valueOf(element.getKey().substring(0, element.getKey().length() - 1).toUpperCase()),
						element.getKey().substring(element.getKey().length() - 1) == ">" ? Type.ABOVE : Type.BELOW,
						element.getValue().getAsDouble()));
			}
		});
		return new MaterialToolIngredient(MaterialItem.getTypeFromString(JsonUtils.getString(json, "item")),
				reqs.toArray(new MaterialIngredientCriteria[0]));
	}
}
