package com.supertechgroup.core.recipies;

import java.util.Collection;
import java.util.HashMap;

import com.supertechgroup.core.util.Helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class BasicMouldForgeRecipe {
	private static final HashMap<ResourceLocation, BasicMouldForgeRecipe> RECIPIES = new HashMap<>();

	public static BasicMouldForgeRecipe get(ResourceLocation resourceLocation) {
		return RECIPIES.get(resourceLocation);
	}

	public static Collection<BasicMouldForgeRecipe> getEntries() {
		return RECIPIES.values();
	}

	public static void registerRecipe(BasicMouldForgeRecipe recipe, ResourceLocation name) {
		if (RECIPIES.containsKey(name)) {
			System.out.println("Attempted to overwrite a Mould Forge Recipe with name " + name.toString()
					+ ". This is not allowed.");
		} else {
			recipe.name = name;
			RECIPIES.put(name, recipe);
		}
	}

	protected NonNullList<ItemStack> input = null;
	private ResourceLocation name;
	ItemStack primaryOut;
	ItemStack mould;

	double temp;

	double coefficient;

	double specHeatMass;

	public BasicMouldForgeRecipe(ItemStack[] in, ItemStack mould, ItemStack primary, double requiredTemp,
			double thermalCoefficient, double specHeatMass) {
		this.input = NonNullList.from(ItemStack.EMPTY, in);
		this.primaryOut = primary;
		this.mould = mould;
		this.temp = requiredTemp;
		// useful approx calc if J/K/kg not available: (molarHeat)/density
		this.coefficient = thermalCoefficient;
		this.specHeatMass = specHeatMass;
	}

	public boolean apply(IItemHandler cap) {
		for (ItemStack is : this.input) {
			if (!Helpers.consumeFromInventory((IItemHandlerModifiable) cap, is, true)) {
				return false;
			}
		}
		return true;
	}

	public double getCoefficient() {
		return this.coefficient;
	}

	public NonNullList<ItemStack> getInputs() {
		return input;
	}

	public ItemStack getPrimaryOutStack() {
		return primaryOut.copy();
	}

	public ResourceLocation getRegistryName() {
		return name;
	}

	public double getSpecificHeatMass() {
		return this.specHeatMass;
	}

	public double getTemperature() {
		return temp;
	}

	public void start(IItemHandler cap) {
		for (ItemStack is : this.input) {
			Helpers.consumeFromInventory((IItemHandlerModifiable) cap, is, false);
		}
	}

}
