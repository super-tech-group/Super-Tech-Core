package com.supertechgroup.core.worldgen.generators;

import java.util.ArrayList;
import java.util.Random;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.worldgen.OreSavedData;
import com.supertechgroup.core.worldgen.ores.Ore;
import com.supertechgroup.core.worldgen.rocks.RockManager;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGeneratorKimberlite extends WorldGeneratorBase {

	IBlockState kimberlite;

	public WorldGeneratorKimberlite(String name, int[] dims, int chance) {
		super(WorldGeneratorBase.singleOre(Ore.REGISTRY.getValue(new ResourceLocation("supertechcore:diamond"))), name,
				dims, 0, chance, new String[] { "metamorphic", "ingeous", "sedimentary" });
		kimberlite = RockManager.stoneSpawns.get("kimberlite").iterator().next();
	}

	@Override
	boolean generate(World world, Random random, BlockPos pos) {
		if (random.nextInt(chance) == 0) {
			int cx = random.nextInt(10) + 4;
			int cz = random.nextInt(10) + 4;
			double height = random.nextInt(6) + 4;
			for (double y = 0; y <= height; y++) {
				int s = 4 - (int) (4 * y / height);
				for (int x = -s; x <= s; x++) {
					for (int z = -s; z <= s; z++) {
						BlockPos pos2 = new BlockPos(cx + x + pos.getX(), y + 1, cz + z + pos.getZ());
						if (random.nextDouble() < .2) {
							this.generateOreBlock(world, pos2, kimberlite);
							world.setBlockState(pos2, ModRegistry.superore.getDefaultState());
						} else {
							world.setBlockState(pos2, kimberlite);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean generateOreBlock(World world, BlockPos pos, IBlockState generatorStart) {
		ArrayList<ResourceLocation> oresAdded = new ArrayList<>();
		ores.forEach((Ore k, Double v) -> {
			if (world.rand.nextDouble() < v) {
				oresAdded.add(k.getRegistryName());
			}
		});
		if (!oresAdded.isEmpty()) {
			ResourceLocation[] newOres = new ResourceLocation[oresAdded.size()];
			for (int i = 0; i < newOres.length; i++) {
				newOres[i] = oresAdded.get(i);
			}
			OreSavedData.get(world).setData(pos.getX(), pos.getY(), pos.getZ(), RockManager.getTexture(kimberlite),
					newOres, kimberlite.getBlockHardness(world, pos));
			world.setBlockState(pos, ModRegistry.superore.getDefaultState());

		}

		return true;
	}

}
