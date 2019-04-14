package com.supertechgroup.core.metallurgy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.supertechgroup.core.items.MaterialItem;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialItemIngredient extends Ingredient {
	MaterialIngredientCriteria[] criteria;
	private NonNullList<ItemStack> matches;
	int type;
	private IntList itemIds = null;
	private ItemStack[] array = null;
	private int lastSizeA = -1, lastSizeL = -1;

	/**
	 * An ingredient made of a material
	 * 
	 * @param type     The type of Material Item. See the constants in #MaterialItem
	 * @param criteria
	 */
	public MaterialItemIngredient(int type, MaterialIngredientCriteria... criteria) {
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
				matches.add(new ItemStack(mat.getMaterialItem(), 1, type));
			}
		});
	}

	@Override
	@Nonnull
	public ItemStack[] getMatchingStacks() {
		if (array == null || this.lastSizeA != matches.size()) {
			NonNullList<ItemStack> lst = NonNullList.create();
			for (ItemStack itemstack : this.matches) {
				if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE)
					itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
				else
					lst.add(itemstack);
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
					for (ItemStack item : lst)
						this.itemIds.add(RecipeItemHelper.pack(item));
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
	public boolean apply(@Nullable ItemStack input) {
		if (input == null)
			return false;

		if (input.getItem() instanceof MaterialItem && input.getMetadata() == type) {
			MaterialItem matItem = (MaterialItem) input.getItem();
			for (MaterialIngredientCriteria c : this.criteria) {

				if (!c.meetsCriteria(matItem.getMaterial())) {
					return false;
				}
			}
		}
		return true;
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
