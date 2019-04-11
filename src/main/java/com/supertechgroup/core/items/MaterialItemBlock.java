package com.supertechgroup.core.items;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.metallurgy.Material;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class MaterialItemBlock extends ItemBlock {
	Material material;

	public MaterialItemBlock(Block block, Material material) {
		super(block);
		this.material = material;
		setUnlocalizedName("block" + material.getName());
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

	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {

		return "tile." + Reference.MODID + ".block";
	}
}