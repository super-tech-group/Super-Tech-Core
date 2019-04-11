package com.supertechgroup.core.worldgen.ores;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.util.ItemBase;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This represents an ore item. This can be unprocessed versions, or partially
 * processed before it becomes a resource item.
 *
 * @author oa10712
 *
 */
public class OreItem extends ItemBase {
	public static final int ORE = 0;
	public static final int NETHER_ORE = 1;
	public static final int END_ORE = 2;

	public static final int CRUSHED = 10;

	private Ore ore;

	public OreItem(Ore ore) {
		super("itemOre" + ore.getName());
		this.ore = ore;
		setMaxDamage(0);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.MISC); // items will appear on the
	}

	/**
	 * This is what allows us to use 1 entry per ore type, rather than have 4
	 * entries per ore type in the localization files.
	 */
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (I18n.canTranslate(getUnlocalizedNameInefficiently(stack) + '.' + ore.getName())) {
			return I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + '.' + ore.getName());
		}
		return String.format(super.getItemStackDisplayName(stack),
				I18n.canTranslate(Reference.MODID + ".entry." + ore.getName())
						? I18n.translateToLocal(Reference.MODID + ".entry." + ore.getName())
						: ore.getName());
	}

	public Ore getOre() {
		return ore;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		ItemStack subItemStack = new ItemStack(this, 1, ORE);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, NETHER_ORE);
		subItems.add(subItemStack);
		subItemStack = new ItemStack(this, 1, END_ORE);
		subItems.add(subItemStack);

		subItemStack = new ItemStack(this, 1, CRUSHED);
		subItems.add(subItemStack);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int metadata = stack.getMetadata();
		if (metadata == ORE) {
			return "item." + Reference.MODID + ".ore";
		}
		if (metadata == NETHER_ORE) {
			return "item." + Reference.MODID + ".nether";
		}
		if (metadata == END_ORE) {
			return "item." + Reference.MODID + ".end";
		}
		if (metadata == CRUSHED) {
			return "item." + Reference.MODID + ".crushed";
		}
		return "item.itemOreObject.ERROR_" + metadata;
	}

}