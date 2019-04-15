package com.supertechgroup.core.worldgen.ores;

import java.util.ArrayList;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 *
 * @author oa10712
 *
 */
public class Ore extends IForgeRegistryEntry.Impl<Ore> {
	public static IForgeRegistry<Ore> REGISTRY;
	private String name;
	private OreItem itemOre;
	private ArrayList<String> altNames = new ArrayList<>();

	/**
	 * The RGB code for the color of this ore
	 */
	private int color;

	public Ore(String string, int color) {
		name = string;
		this.color = color;
		itemOre = new OreItem(this);
		altNames.add(string);
	}

	public Ore addDictSuffix(String name2) {
		altNames.add(name2);
		return this;
	}

	public int getColor() {
		return color;
	}

	public ItemStack getDrops(byte base) {
		switch (base) {// Switch based on base block
		case -1:// NetherRack and similar
			return new ItemStack(itemOre, 1, OreItem.NETHER_ORE);
		case 1:// Endstone and similar
			return new ItemStack(itemOre, 1, OreItem.END_ORE);
		default:// Stone and unspecified
			return new ItemStack(itemOre, 1, OreItem.ORE);
		}
	}

	public OreItem getItemOre() {
		return itemOre;
	}

	public String getName() {
		return name;
	}

	public Ore registerOre() {
		this.setRegistryName(getName());
		GameRegistry.findRegistry(Item.class).register(itemOre);

		registerOreDict();
		SuperTechCoreMod.proxy.registerModels(this);

		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation("crushingOres"),
						new ItemStack(itemOre, 1, OreItem.CRUSHED),
						new Object[] { new OreIngredient("toolHammer"), new ItemStack(itemOre, 1, OreItem.ORE) })
								.setRegistryName(Reference.MODID, "crushed" + name));

		Ore.REGISTRY.register(this);
		return this;
	}

	private void registerOreDict() {
		for (String s : altNames) {
			ItemStack subItemStack = new ItemStack(itemOre, 1, OreItem.ORE);
			OreDictionary.registerOre("ore" + s, subItemStack);
			subItemStack = new ItemStack(itemOre, 1, OreItem.NETHER_ORE);
			OreDictionary.registerOre("oreNether" + s, subItemStack);
			subItemStack = new ItemStack(itemOre, 1, OreItem.END_ORE);
			OreDictionary.registerOre("oreEnd" + s, subItemStack);
			subItemStack = new ItemStack(itemOre, 1, OreItem.CRUSHED);
			OreDictionary.registerOre("crushed" + s, subItemStack);
		}
	}

}