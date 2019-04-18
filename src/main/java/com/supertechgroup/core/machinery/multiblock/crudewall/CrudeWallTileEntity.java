package com.supertechgroup.core.machinery.multiblock.crudewall;

import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

public class CrudeWallTileEntity extends TileMultiBlock {

	@Override
	protected boolean blockActivated(EntityPlayer player, EnumFacing side) {
//nothing to see here, this block doesn't really do anything but add structure
		return false;
	}

}
