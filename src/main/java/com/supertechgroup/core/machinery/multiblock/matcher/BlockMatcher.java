package com.supertechgroup.core.machinery.multiblock.matcher;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockMatcher {

	public abstract boolean apply(World world, BlockPos pos);

	public abstract IBlockState getExample();

}
