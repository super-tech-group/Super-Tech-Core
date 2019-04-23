package com.supertechgroup.core.machinery.multiblock.matcher;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class DictBlockMatcher extends BlockMatcher {

	private OreIngredient check;
	String c;

	public DictBlockMatcher(String string) {
		this.check = new OreIngredient(string);
		c = string;
	}

	@Override
	public boolean apply(World world, BlockPos pos) {
		return check.apply(new ItemStack(world.getBlockState(pos).getBlock()));
	}

	@Override
	public IBlockState getExample() {
		return ((ItemBlock) check.getMatchingStacks()[0].getItem()).getBlock().getDefaultState();
	}

	@Override
	public String toString() {
		return c;
	}

}
