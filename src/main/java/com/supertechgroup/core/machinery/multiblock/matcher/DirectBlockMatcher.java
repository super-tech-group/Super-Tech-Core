package com.supertechgroup.core.machinery.multiblock.matcher;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DirectBlockMatcher extends BlockMatcher {

	private Block check;

	public DirectBlockMatcher(Block block) {
		this.check = block;
	}

	@Override
	public boolean apply(World world, BlockPos pos) {
		System.out.println(check.toString() + " " + world.getBlockState(pos).getBlock());
		System.out.println(toString() + " "
				+ check.getRegistryName().equals(world.getBlockState(pos).getBlock().getRegistryName()));
		return check.getRegistryName().equals(world.getBlockState(pos).getBlock().getRegistryName());
	}

	@Override
	public IBlockState getExample() {
		return check.getDefaultState();
	}

	@Override
	public String toString() {
		return check.toString();
	}
}
