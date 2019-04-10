package com.supertechgroup.core.blocks;

import com.supertechgroup.core.metalurgy.Material;
import com.supertechgroup.core.util.BlockBase;

public class BlockMaterial extends BlockBase {

	private final Material material;

	public BlockMaterial(Material material) {
		super(net.minecraft.block.material.Material.IRON, "block" + material.getName());
		this.material = material;
	}

	public Material getSuperMaterial() {
		return material;
	}
}
