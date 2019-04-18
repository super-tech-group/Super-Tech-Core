package com.supertechgroup.core.machinery.multiblock.matcher;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DirectBlockMatcher extends BlockMatcher {

	private Block check;

	public DirectBlockMatcher(Block block) {
		this.check = block;
	}

	@Override
	protected boolean apply(World world, BlockPos pos) {
		return check.equals(world.getBlockState(pos).getBlock());
	}

}
