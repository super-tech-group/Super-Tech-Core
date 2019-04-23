package com.supertechgroup.core.blocks;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.metallurgy.Material;

import net.minecraft.block.Block;

public class BlockMaterial extends Block {

	private final Material material;

	public BlockMaterial(Material material) {
		super(net.minecraft.block.material.Material.IRON);
		this.setUnlocalizedName(Reference.MODID + "block" + material.getName());
		this.setRegistryName(Reference.MODID, "block" + material.getName());
		this.material = material;
	}

	public Material getSuperMaterial() {
		return material;
	}
}
