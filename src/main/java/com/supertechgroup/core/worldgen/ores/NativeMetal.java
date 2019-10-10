package com.supertechgroup.core.worldgen.ores;

import com.supertechgroup.core.items.MaterialItem;
import com.supertechgroup.core.metallurgy.Material;

import net.minecraft.item.ItemStack;

public class NativeMetal extends Ore {
	Material mat;

	public NativeMetal(Material metal) {
		super("native" + metal.getName(), metal.getColor());
		mat = metal;
	}

	@Override
	public ItemStack getDrops(byte base) {
		return new ItemStack(mat.getMaterialItem(), 1, MaterialItem.NUGGET);
	}

}
