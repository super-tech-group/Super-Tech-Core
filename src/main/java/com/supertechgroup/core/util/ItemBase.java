package com.supertechgroup.core.util;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/**
 * Helper class for mod items.
 *
 * @author oa10712
 *
 */
public class ItemBase extends Item {

	protected String name;

	public ItemBase(String name) {
		this.name = name;
		setUnlocalizedName(Reference.MODID + "." + name);
		setRegistryName(name);
	}

	public void registerItemModel() {
		SuperTechCoreMod.proxy.registerItemRenderer(this, 0, name);
		ModelLoader.setCustomModelResourceLocation(this, 0,
				new ModelResourceLocation(Reference.MODID + ":" + name, "inventory"));
	}

}