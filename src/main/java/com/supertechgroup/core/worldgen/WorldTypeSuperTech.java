package com.supertechgroup.core.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeSuperTech extends WorldType {

	public WorldTypeSuperTech() {
		super("SuperTechWorld");
	}

	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return new BiomeProviderSuperTech(world.getWorldInfo());
	}

	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkProviderSuperTech(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(),
				generatorOptions);
	}
}
