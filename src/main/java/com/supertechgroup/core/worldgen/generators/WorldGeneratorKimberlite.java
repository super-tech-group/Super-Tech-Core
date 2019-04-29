package com.supertechgroup.core.worldgen.generators;

import java.util.Random;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.worldgen.ores.Ore;
import com.supertechgroup.core.worldgen.rocks.RockManager;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGeneratorKimberlite extends WorldGeneratorBase {

	public WorldGeneratorKimberlite(String name, int[] dims, int chance) {
		super(WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:diamond"))), name,
				dims, 0, chance, new String[] { "metamorphic", "ingeous", "sedimentary" });
	}

	@Override
	boolean generate(World world, Random random, BlockPos pos) {
		if (random.nextInt(chance) == 0) {
			int cx = random.nextInt(10) + 3;
			int cz = random.nextInt(10) + 3;
			double height = random.nextInt(6) + 12;
			IBlockState kimberlite = RockManager.stoneSpawns.get("kimberlite").iterator().next();
			for (double y = 0; y < height; y++) {
				int s = (int) (4.0d * ((height - y) / height)) + 1;
				for (int x = -s; x < s; x++) {
					for (int z = -s; z < s; z++) {
						pos = new BlockPos(cx + x + pos.getX(), y, cz + z + pos.getZ());
						if (!world.getBlockState(pos).equals(Blocks.BEDROCK.getDefaultState())) {
							if (random.nextDouble() < .1) {
								this.generateOreBlock(world, pos, kimberlite);
								world.setBlockState(pos, ModRegistry.superore.getDefaultState());
							} else {
								world.setBlockState(pos, kimberlite);
							}
						}
					}
				}
			}
		}
		return false;
	}

}
