package com.supertechgroup.core.machinery.multiblock.matcher;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class DictBlockMatcher extends BlockMatcher {

	private OreIngredient check;

	public DictBlockMatcher(String string) {
		this.check = new OreIngredient(string);
	}

	@Override
	protected boolean apply(World world, BlockPos pos) {
		return check.apply(new ItemStack(world.getBlockState(pos).getBlock()));
	}

}
