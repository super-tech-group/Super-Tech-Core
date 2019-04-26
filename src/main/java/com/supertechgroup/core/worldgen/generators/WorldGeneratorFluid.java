package com.supertechgroup.core.worldgen.generators;

import java.util.Random;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.worldgen.OreSavedData;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGeneratorFluid extends WorldGeneratorBase {

	private Block fluidBlock;

	public WorldGeneratorFluid(String name, int[] dims, int size, int chance, String[] stones, Fluid fluid) {
		super(null, name, dims, size, chance, stones);
		this.fluidBlock = fluid.getBlock();
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		position = position.add(rand.nextInt(16), rand.nextInt(48) + 10, rand.nextInt(16));
		if (fluidBlock != null && (chance == 1 || rand.nextInt(chance) == 0)) {
			if (this.validStoneTypes.contains(worldIn.getBlockState(position))) {
				generateDeposit(worldIn, rand, position);
			}

		}
		OreSavedData.get(worldIn).setChunkGenerated((position.getX() / 16), (position.getZ() / 16));
		return true;
	}

	private void generateDeposit(World worldIn, Random rand, BlockPos add) {
		IBlockState state = worldIn.getBlockState(add);
		double dist = size * size;
		for (int x = -size; x < size; x++) {
			for (int y = -size; y < size; y++) {
				for (int z = -size; z < size; z++) {
					if (add.distanceSq(x + add.getX(), y + add.getY(), z + add.getZ()) <= dist
							&& state.equals(worldIn.getBlockState(add.add(x, y, z)))) {
						worldIn.setBlockState(add.add(x, y, z), fluidBlock.getDefaultState());
					}
				}
			}
		}

	}

}
