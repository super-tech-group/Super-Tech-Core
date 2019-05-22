package com.supertechgroup.core.items;

import com.supertechgroup.core.util.ItemBase;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class SuperTechItem extends ItemBase {
	public enum Types {
		BASIC_CIRCUIT, ADVANCED_CIRCUIT, ELITE_CIRCUIT, ULTIMATE_CIRCUIT, BASIC_CASING, SMALL_POWER_UNIT,
		HEATING_ELEMENT, SLAG, FLUX, FIRECLAY, FIREBRICK, UNKNOWN
	}

	public SuperTechItem() {
		super("itemTechComponent");
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	// add a subitem for each item we want to appear in the creative tab
	// in this case - each pre-defined component
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for (int i = 0; i < Types.values().length; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		Types metadata = stack.getMetadata() < Types.values().length ? Types.values()[stack.getMetadata()]
				: Types.UNKNOWN;
		return super.getUnlocalizedName() + "." + metadata.toString().toLowerCase();
	}

	public void registerModels() {
		for (Types type : Types.values()) {
			ModelLoader.setCustomModelResourceLocation(this, type.ordinal(),
					new ModelResourceLocation("supertechcore:item" + type.toString(), "inventory"));
		}
	}

	public void setupDictionary() {
		OreDictionary.registerOre("circuitBasic", new ItemStack(this, 1, Types.BASIC_CIRCUIT.ordinal()));
		OreDictionary.registerOre("circuitAdvanced", new ItemStack(this, 1, Types.ADVANCED_CIRCUIT.ordinal()));
		OreDictionary.registerOre("circuitElite", new ItemStack(this, 1, Types.ELITE_CIRCUIT.ordinal()));
		OreDictionary.registerOre("circuitUltimate", new ItemStack(this, 1, Types.ULTIMATE_CIRCUIT.ordinal()));
		OreDictionary.registerOre("casingBasic", new ItemStack(this, 1, Types.BASIC_CASING.ordinal()));
		OreDictionary.registerOre("powerUnitSmall", new ItemStack(this, 1, Types.SMALL_POWER_UNIT.ordinal()));
		OreDictionary.registerOre("heatingElement", new ItemStack(this, 1, Types.HEATING_ELEMENT.ordinal()));
		OreDictionary.registerOre("slag", new ItemStack(this, 1, Types.SLAG.ordinal()));
		OreDictionary.registerOre("flux", new ItemStack(this, 1, Types.FLUX.ordinal()));
	}
}
