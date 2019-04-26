package com.supertechgroup.core.recipies;

import java.util.Collection;
import java.util.HashMap;

import com.supertechgroup.core.util.Helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class BasicSmelterRecipe {
	private static final HashMap<ResourceLocation, BasicSmelterRecipe> RECIPIES = new HashMap<>();

	public static BasicSmelterRecipe get(ResourceLocation resourceLocation) {
		return RECIPIES.get(resourceLocation);
	}

	public static Collection<BasicSmelterRecipe> getEntries() {
		return RECIPIES.values();
	}

	public static void registerRecipe(BasicSmelterRecipe recipe, ResourceLocation name) {
		if (RECIPIES.containsKey(name)) {
			System.out.println(
					"Attempted to overwrite a Smelting Recipe with name " + name.toString() + ". This is not allowed.");
		} else {
			recipe.name = name;
			RECIPIES.put(name, recipe);
		}
	}

	protected NonNullList<ItemStack> input = null;
	private ResourceLocation name;
	ItemStack slagOut;
	ItemStack primaryOut;

	double temp;

	double coefficient;

	double specHeatMass;

	public BasicSmelterRecipe(ItemStack[] in, ItemStack slag, ItemStack primary, double requiredTemp,
			double thermalCoefficient, double specHeatMass) {
		this.input = NonNullList.from(ItemStack.EMPTY, in);
		this.slagOut = slag;
		this.primaryOut = primary;
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

	public ItemStack getSlagStack() {
		return slagOut.copy();
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
