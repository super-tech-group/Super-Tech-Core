package com.supertechgroup.core.machinery.multiblock.matcher;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityBlockMatcher extends BlockMatcher {

	private Capability check;
	private EnumFacing facing;

	public CapabilityBlockMatcher(Capability cap, EnumFacing side) {
		this.check = cap;
		this.facing = side;
	}

	@Override
	protected boolean apply(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te == null || te.getCapability(check, facing) == null) {
			return false;
		}
		return true;
	}

}
