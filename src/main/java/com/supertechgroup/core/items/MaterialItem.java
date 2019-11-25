package com.supertechgroup.core.items;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.metallurgy.Material;
import com.supertechgroup.core.util.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MaterialItem extends ItemBase {
	public static final int INGOT = 0;
	public static final int DUST = 1;
	public static final int GEAR = 2;
	public static final int NUGGET = 3;
	public static final int PLATE = 4;
	public static final int ROD = 5;
	public static final int CLUMP = 6;
	public static final int CRYSTAL = 7;
	public static final int SHARD = 8;
	public static final int WIRE = 9;
	public static final int DIRTY = 10;
	public static final int FOIL = 11;
	public static final int TINY = 12;
	public static final int COIN = 13;
	public static final int BLADE = 14;

	Material material;

	public MaterialItem(Material material) {
		super("item" + material.getName());
		this.material = material;
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.MISC); // items will appear on the
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (I18n.canTranslate(getUnlocalizedNameInefficiently(stack) + '.' + material.getName())) {
			return I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + '.' + material.getName());
		}
		return String.format(super.getItemStackDisplayName(stack),
				I18n.canTranslate(Reference.MODID + ".entry." + material.getName())
						? I18n.translateToLocal(Reference.MODID + ".entry." + material.getName())
						: material.getName());

		// return "TODO: getItemStackDisplayName() in MaterialItem.java";
	}

	public Material getMaterial() {
		return material;
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {

		ItemStack subItemStack = new ItemStack(this, 1, INGOT);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, DUST);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, GEAR);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, NUGGET);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, PLATE);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, ROD);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, CLUMP);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, CRYSTAL);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, SHARD);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, WIRE);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, DIRTY);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, FOIL);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, TINY);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, COIN);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, BLADE);
		subItems.add(subItemStack);

	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int metadata = stack.getMetadata();
		if (metadata == TINY) {
			return "item." + Reference.MODID + ".dustTiny";
		}
		if (metadata == FOIL) {
			return "item." + Reference.MODID + ".foil";
		}
		if (metadata == DIRTY) {
			return "item." + Reference.MODID + ".dustDirty";
		}
		if (metadata == WIRE) {
			return "item." + Reference.MODID + ".wire";
		}
		if (metadata == SHARD) {
			return "item." + Reference.MODID + ".shard";
		}
		if (metadata == CRYSTAL) {
			return "item." + Reference.MODID + ".crystal";
		}
		if (metadata == CLUMP) {
			return "item." + Reference.MODID + ".clump";
		}
		if (metadata == ROD) {
			return "item." + Reference.MODID + ".rod";
		}
		if (metadata == PLATE) {
			return "item." + Reference.MODID + ".plate";
		}
		if (metadata == NUGGET) {
			return "item." + Reference.MODID + ".nugget";
		}
		if (metadata == GEAR) {
			return "item." + Reference.MODID + ".gear";
		}
		if (metadata == DUST) {
			return "item." + Reference.MODID + ".dust";
		}
		if (metadata == INGOT) {
			return "item." + Reference.MODID + ".ingot";
		}
		if (metadata == COIN) {
			return "item." + Reference.MODID + ".coin";
		}
		if (metadata == BLADE) {
			return "item." + Reference.MODID + ".blade";
		}
		return "item.itemMaterialObject.ERROR_" + metadata;
	}

	public static int getTypeFromString(String string) {
		switch (string.toLowerCase()) {
		case "dust":
			return DUST;
		case "gear":
			return GEAR;
		case "nugget":
			return NUGGET;
		case "plate":
			return PLATE;
		case "rod":
			return ROD;
		case "clump":
			return CLUMP;
		case "crystal":
			return CRYSTAL;
		case "shard":
			return SHARD;
		case "wire":
			return WIRE;
		case "dirty":
			return DIRTY;
		case "foil":
			return FOIL;
		case "tiny":
			return TINY;
		case "coin":
			return COIN;
		case "blade":
			return BLADE;
		}
		return 0;
	}

}