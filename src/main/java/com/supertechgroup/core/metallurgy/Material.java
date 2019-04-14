package com.supertechgroup.core.metallurgy;

import java.util.HashMap;
import java.util.Map;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.blocks.BlockMaterial;
import com.supertechgroup.core.items.MaterialItem;
import com.supertechgroup.core.items.MaterialItemBlock;
import com.supertechgroup.core.items.MaterialTool;
import com.supertechgroup.core.proxy.ClientProxy;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Material extends IForgeRegistryEntry.Impl<Material> {
	public enum Property {
		/**
		 * density measured in g/cm3 at room temperature
		 */
		DENSITY,
		/**
		 * Electrical resistance measured in nΩ·m (at 20 °C)
		 */
		ELECTRICAL_RESISTANCE,
		/**
		 * thermal expansion measured in µm/(m·K) (at 25 °C)
		 */
		THERMAL_EXPANSION,
		/**
		 * Shear modulus measured in GPa
		 */
		SHEAR_MODULUS,
		/**
		 * thermal conductivity measured in W/(m·K)
		 */
		THERMAL_CONDUCTIVITY,
		/**
		 * Specific heat of the material measured in joule per kelvin
		 */
		SPECIFIC_HEAT,
		/**
		 * Young's modulus measured in GPa
		 *
		 * This is how much pressure applied outward this material can withstand.
		 */
		YOUNGS_MODULUS,
		/**
		 * Bulk Modulus measured in GPa.
		 *
		 * This is how much pressure applied inward this material can withstand.
		 */
		BULK_MODULUS
	}

	private HashMap<Property, Double> properties = new HashMap<>();

	public static class MaterialBuilder {
		Material building;

		public MaterialBuilder(String name) {
			building = new Material(name, 0x000000, -1, -1, 5.0, 999999, 10, 30, 15, 200, 150);
		}

		public Material build() {
			building.block = new BlockMaterial(building);

			building.itemBlock = new MaterialItemBlock(building.block, building);
			building.itemBlock.setRegistryName(building.block.getRegistryName());

			building.itemMaterial = new MaterialItem(building);
			building.itemAxe = new MaterialTool(building, MaterialTool.AXE);
			building.itemDrawplate = new MaterialTool(building, MaterialTool.DRAW_PLATE);
			building.itemHammer = new MaterialTool(building, MaterialTool.HAMMER);
			building.itemPickaxe = new MaterialTool(building, MaterialTool.PICKAXE);
			building.itemPliers = new MaterialTool(building, MaterialTool.PLIERS);
			building.itemShovel = new MaterialTool(building, MaterialTool.SHOVEL);
			return building;
		}

		/**
		 * Bulk Modulus measured in GPa.
		 *
		 * This is how much pressure applied inward this material can withstand.
		 */
		public MaterialBuilder setBulkModulus(double mod) {
			building.properties.put(Property.BULK_MODULUS, mod);
			return this;
		}

		/**
		 * The RGB code for the color of this
		 */
		public MaterialBuilder setColor(int color) {
			building.color = color;
			return this;
		}

		public MaterialBuilder setCustomDrops(ItemStack itemStack) {
			building.customDrops = itemStack;
			return this;
		}

		/**
		 * density measured in g/cm3 at room temperature
		 */
		public MaterialBuilder setDensity(double density) {
			building.properties.put(Property.DENSITY, density);
			return this;
		}

		/**
		 * Electrical resistance measured in nΩ·m (at 20 °C)
		 */
		public MaterialBuilder setElectricalResistance(double resistance) {
			building.properties.put(Property.ELECTRICAL_RESISTANCE, resistance);
			return this;
		}

		/**
		 * The harvest level of this
		 */
		public MaterialBuilder setHarvestLevel(int level) {
			building.harvest = level;
			return this;
		}

		/**
		 * Shear modulus measured in GPa
		 *
		 * This is how much pressure applied sideways this material can withstand.
		 */
		public MaterialBuilder setShearModulus(double shear) {
			building.properties.put(Property.SHEAR_MODULUS, shear);
			return this;
		}

		public MaterialBuilder setSpecificHeat(double heat) {
			building.properties.put(Property.SPECIFIC_HEAT, heat);
			return this;
		}

		/**
		 * thermal conductivity measured in W/(m·K)
		 */
		public MaterialBuilder setThermalConductivity(double conductivity) {
			building.properties.put(Property.THERMAL_CONDUCTIVITY, conductivity);
			return this;
		}

		/**
		 * thermal expansion measured in µm/(m·K) (at 25 °C)
		 */
		public MaterialBuilder setThermalExpansion(double expansion) {
			building.properties.put(Property.THERMAL_EXPANSION, expansion);
			return this;
		}

		/**
		 * Level that a tool made of this can mine
		 */
		public MaterialBuilder setToolLevel(int level) {
			building.tool = level;
			return this;
		}

		/**
		 * Young's modulus measured in GPa
		 *
		 * This is how much pressure applied outward this material can withstand.
		 */
		public MaterialBuilder setYoungsModulus(double mod) {
			building.properties.put(Property.YOUNGS_MODULUS, mod);
			return this;
		}
	}

	public static IForgeRegistry<Material> REGISTRY;

	public ItemStack customDrops = null;

	/**
	 * The oreDict name of the metal
	 */
	private final String name;
	/**
	 * The RGB code for the color of this
	 */
	private int color;
	/**
	 * The harvest level of this
	 */
	private int harvest;

	/**
	 * Level that a tool made of this can mine
	 */
	private int tool;

	private BlockMaterial block;

	private ItemBlock itemBlock;

	private MaterialItem itemMaterial;
	// private MaterialTool itemTool;

	private MaterialTool itemAxe;
	private MaterialTool itemHammer;
	private MaterialTool itemPliers;
	private MaterialTool itemShovel;
	private MaterialTool itemDrawplate;
	private MaterialTool itemPickaxe;

	private Material(String name, int color, int harvest, int mine, double density, double resistance, double expansion,
			double shear, double conductivity, double young, double bulk) {
		this.name = name;
		this.color = color;
		this.harvest = harvest;
		tool = mine;

		properties.put(Property.BULK_MODULUS, bulk);
		properties.put(Property.DENSITY, density);
		properties.put(Property.ELECTRICAL_RESISTANCE, resistance);
		properties.put(Property.THERMAL_EXPANSION, expansion);
		properties.put(Property.SHEAR_MODULUS, shear);
		properties.put(Property.THERMAL_CONDUCTIVITY, conductivity);
		properties.put(Property.YOUNGS_MODULUS, young);
	}

	public void addBasicProcessing() {
		GameRegistry.findRegistry(IRecipe.class)
				.register(new ShapelessOreRecipe(new ResourceLocation("dusts"),
						new ItemStack(itemMaterial, 1, MaterialItem.DUST),
						new Object[] { new OreIngredient("ore" + name), new OreIngredient("toolHammer") })
								.setRegistryName(Reference.MODID, "hammer_ore_" + name));
	}

	@SideOnly(Side.CLIENT)
	public void clientPrep() {
		ModelLoader.setCustomModelResourceLocation(itemBlock, 0, ClientProxy.itemLocation);
		ModelLoader.setCustomModelResourceLocation(itemBlock, 0, ClientProxy.blockLocation);
		ModelLoader.setCustomStateMapper(block, blockIn -> {
			final Map<IBlockState, ModelResourceLocation> loc = new HashMap<>();
			loc.put(blockIn.getDefaultState(), ClientProxy.blockLocation);
			return loc;
		});
	}

	public BlockMaterial getBlock() {
		return block;
	}

	public int getFluidCapacity() {
		return (int) (properties.get(Property.YOUNGS_MODULUS) * 30);
	}

	public int getFluidTransferRate() {
		return (int) (properties.get(Property.YOUNGS_MODULUS) * 3);
	}

	public int getHarvest() {
		return harvest;
	}

	public Item getItemAxe() {
		return itemAxe;
	}

	public Item getItemBlock() {
		return itemBlock;
	}

	public Item getItemDrawplate() {
		return itemDrawplate;
	}

	public Item getItemHammer() {
		return itemHammer;
	}

	public Item getItemPickaxe() {
		return itemPickaxe;
	}

	public Item getItemPliers() {
		return itemPliers;
	}

	public Item getItemShovel() {
		return itemShovel;
	}

	public MaterialItem getMaterialItem() {
		return itemMaterial;
	}

	public double getProperty(Property prop) {
		return this.properties.getOrDefault(prop, Double.NaN);
	}

	public int getMaxToolDamage(int type) {
		switch (type) {
		case MaterialTool.PLIERS:
			return (int) (properties.get(Property.SHEAR_MODULUS) * 2);
		case MaterialTool.DRAW_PLATE:
			return (int) (properties.get(Property.SHEAR_MODULUS) + properties.get(Property.BULK_MODULUS));
		case MaterialTool.HAMMER:
			return (int) (properties.get(Property.BULK_MODULUS) * 3);
		case MaterialTool.PICKAXE:
			return (int) (properties.get(Property.BULK_MODULUS) * 2);
		}
		return (int) (properties.get(Property.BULK_MODULUS) * 1);
	}

	public String getName() {
		return name;
	}

	public int getToolLevel() {
		return tool;
	}

	public int getTransferRate() {
		return (int) Math.floor((1 / properties.get(Property.ELECTRICAL_RESISTANCE))
				* properties.get(Property.THERMAL_CONDUCTIVITY) * 32);
	}

	public void registerMaterial() {
		this.setRegistryName(getName());

		GameRegistry.findRegistry(Block.class).register(block);
		GameRegistry.findRegistry(Item.class).register(itemBlock);
		OreDictionary.registerOre("block" + getName(), new ItemStack(block));

		GameRegistry.findRegistry(Item.class).register(itemMaterial);
		GameRegistry.findRegistry(Item.class).register(itemPickaxe);
		GameRegistry.findRegistry(Item.class).register(itemAxe);
		GameRegistry.findRegistry(Item.class).register(itemHammer);
		GameRegistry.findRegistry(Item.class).register(itemDrawplate);
		GameRegistry.findRegistry(Item.class).register(itemShovel);
		GameRegistry.findRegistry(Item.class).register(itemPliers);

		registerOreDict();
		SuperTechCoreMod.proxy.registerModels(this);

		Material.REGISTRY.register(this);

		if (SuperTechCoreMod.proxy.getSide() == Side.CLIENT) {
			clientPrep();
		}
	}

	public void registerOreDict() {
		ItemStack subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.INGOT);
		OreDictionary.registerOre("ingot" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.DUST);
		OreDictionary.registerOre("dust" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.GEAR);
		OreDictionary.registerOre("gear" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.NUGGET);
		OreDictionary.registerOre("nugget" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.PLATE);
		OreDictionary.registerOre("plate" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.ROD);
		OreDictionary.registerOre("rod" + getName(), subItemStack);
		OreDictionary.registerOre("stick" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.CLUMP);
		OreDictionary.registerOre("clump" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.CRYSTAL);
		OreDictionary.registerOre("crystal" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.SHARD);
		OreDictionary.registerOre("shard" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.WIRE);
		OreDictionary.registerOre("wire" + getName(), subItemStack);
		OreDictionary.registerOre("cable" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.DIRTY);
		OreDictionary.registerOre("dustDirty" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.FOIL);
		OreDictionary.registerOre("foil" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.TINY);
		OreDictionary.registerOre("dustTiny" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.COIN);
		OreDictionary.registerOre("coin" + getName(), subItemStack);
		subItemStack = new ItemStack(itemMaterial, 1, MaterialItem.BLADE);
		OreDictionary.registerOre("blade" + getName(), subItemStack);

		subItemStack = new ItemStack(itemHammer, 1, MaterialTool.HAMMER);
		OreDictionary.registerOre("hammer" + getName(), subItemStack);
		OreDictionary.registerOre("toolHammer", subItemStack);
		subItemStack = new ItemStack(itemPickaxe, 1, MaterialTool.PICKAXE);
		OreDictionary.registerOre("pickaxe" + getName(), subItemStack);
		OreDictionary.registerOre("toolPickaxe", subItemStack);
		subItemStack = new ItemStack(itemPliers, 1, MaterialTool.PLIERS);
		OreDictionary.registerOre("pliers" + getName(), subItemStack);
		OreDictionary.registerOre("toolPliers", subItemStack);
		subItemStack = new ItemStack(itemDrawplate, 1, MaterialTool.DRAW_PLATE);
		OreDictionary.registerOre("drawplate" + getName(), subItemStack);
		OreDictionary.registerOre("toolDrawPlate", subItemStack);

	}

	public int getColor() {
		return this.color;
	}
}