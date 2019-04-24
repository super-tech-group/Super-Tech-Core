package com.supertechgroup.core.machinery.multiblock.crudewall;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.multiblock.BlockMultiWall;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class CrudeWallBlock extends BlockMultiWall {

	public CrudeWallBlock() {
		super(Material.ROCK);
		this.setUnlocalizedName(Reference.MODID + ".crude_wall");
		this.setRegistryName(Reference.MODID, "crude_wall");
		this.setHardness(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CrudeWallTileEntity();
	}

	public void registerModels() {
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(this.getRegistryName().toString(),
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), DEFAULT_ITEM_SUBTYPE,
				itemModelResourceLocation);
	}

}
