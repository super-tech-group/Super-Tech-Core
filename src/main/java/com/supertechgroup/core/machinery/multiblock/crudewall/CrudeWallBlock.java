package com.supertechgroup.core.machinery.multiblock.crudewall;

import com.supertechgroup.core.machinery.multiblock.BlockMultiWall;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CrudeWallBlock extends BlockMultiWall {

	public CrudeWallBlock() {
		super("crude_wall");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CrudeWallTileEntity();
	}

}
