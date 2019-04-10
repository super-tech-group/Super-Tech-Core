package com.supertechgroup.core.util;

import com.supertechgroup.core.SuperTechCoreMod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Helper class for mod blocks
 *
 * @author oa10712
 *
 */
public class BlockBase extends Block implements ItemModelProvider {

	protected String name;

	public BlockBase(Material material, String name) {
		super(material);

		this.name = name;

		setUnlocalizedName(name);
		setRegistryName(name);

		// setCreativeTab(SuperTechTweaksMod.creativeTab);
	}

	@Override
	public void registerItemModel(Item item) {
		SuperTechCoreMod.proxy.registerItemRenderer(item, 0, name);
	}

	@Override
	public BlockBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
}