package com.supertechgroup.core.machinery.multiblock.matcher;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockMatcher {

	protected abstract boolean apply(World world, BlockPos pos);

}
