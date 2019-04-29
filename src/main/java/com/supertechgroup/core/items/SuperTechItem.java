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

	public final static int BASIC_CIRCUIT = 0;

	public final static int ADVANCED_CIRCUIT = 1;

	public final static int ELITE_CIRCUIT = 2;

	public final static int ULTIMATE_CIRCUIT = 3;
	public final static int BASIC_CASING = 4;
	public final static int SMALL_POWER_UNIT = 5;
	public final static int HEATING_UNIT = 6;
	public final static int SLAG = 7;
	public final static int FLUX = 8;
	public static final int FIRE_CLAY = 9;
	public static final int FIRE_BRICK = 10;
	public static final int WOOD_PULP = 11;

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
		for (int i = 0; i < 12; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int metadata = stack.getMetadata();
		switch (metadata) {
		case BASIC_CIRCUIT:
			return super.getUnlocalizedName() + ".circuitBasic";
		case ADVANCED_CIRCUIT:
			return super.getUnlocalizedName() + ".circuitAdvanced";
		case ELITE_CIRCUIT:
			return super.getUnlocalizedName() + ".circuitElite";
		case ULTIMATE_CIRCUIT:
			return super.getUnlocalizedName() + ".circuitUltimate";
		case BASIC_CASING:
			return super.getUnlocalizedName() + ".casingBasic";
		case SMALL_POWER_UNIT:
			return super.getUnlocalizedName() + ".powerUnitSmall";
		case HEATING_UNIT:
			return super.getUnlocalizedName() + ".heatingElement";
		case SLAG:
			return super.getUnlocalizedName() + ".slag";
		case FLUX:
			return super.getUnlocalizedName() + ".flux";
		case FIRE_CLAY:
			return super.getUnlocalizedName() + ".fireClay";
		case FIRE_BRICK:
			return super.getUnlocalizedName() + ".fireBrick";
		case WOOD_PULP:
			return super.getUnlocalizedName() + ".woodPulp";
		default:
			return super.getUnlocalizedName();
		}
	}

	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, BASIC_CIRCUIT,
				new ModelResourceLocation("supertechcore:itemBasicCircuit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, ADVANCED_CIRCUIT,
				new ModelResourceLocation("supertechcore:itemAdvancedCircuit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, ELITE_CIRCUIT,
				new ModelResourceLocation("supertechcore:itemEliteCircuit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, ULTIMATE_CIRCUIT,
				new ModelResourceLocation("supertechcore:itemUltimateCircuit", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, BASIC_CASING,
				new ModelResourceLocation("supertechcore:itemCasingBasic", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, SMALL_POWER_UNIT,
				new ModelResourceLocation("supertechcore:itemPowerUnitSmall", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, HEATING_UNIT,
				new ModelResourceLocation("supertechcore:itemHeatingElement", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, SLAG,
				new ModelResourceLocation("supertechcore:itemSlag", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, FLUX,
				new ModelResourceLocation("supertechcore:itemFlux", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, FIRE_CLAY,
				new ModelResourceLocation("supertechcore:itemFireClay", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, FIRE_BRICK,
				new ModelResourceLocation("supertechcore:itemFireBrick", "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, WOOD_PULP,
				new ModelResourceLocation("supertechcore:itemwoodPulp", "inventory"));
	}

	public void setupDictionary() {
		OreDictionary.registerOre("circuitBasic", new ItemStack(this, 1, BASIC_CIRCUIT));
		OreDictionary.registerOre("circuitAdvanced", new ItemStack(this, 1, ADVANCED_CIRCUIT));
		OreDictionary.registerOre("circuitElite", new ItemStack(this, 1, ELITE_CIRCUIT));
		OreDictionary.registerOre("circuitUltimate", new ItemStack(this, 1, ULTIMATE_CIRCUIT));
		OreDictionary.registerOre("casingBasic", new ItemStack(this, 1, BASIC_CASING));
		OreDictionary.registerOre("powerUnitSmall", new ItemStack(this, 1, SMALL_POWER_UNIT));
		OreDictionary.registerOre("heatingElement", new ItemStack(this, 1, HEATING_UNIT));
		OreDictionary.registerOre("slag", new ItemStack(this, 1, SLAG));
		OreDictionary.registerOre("flux", new ItemStack(this, 1, FLUX));
		OreDictionary.registerOre("woodPulp", new ItemStack(this, 1, WOOD_PULP));
	}
}
