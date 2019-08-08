package com.supertechgroup.core.worldgen;

import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.supertechgroup.core.util.SimplexNoise;
import com.supertechgroup.core.worldgen.rocks.BlockRock;
import com.supertechgroup.core.worldgen.rocks.RockManager;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

public class ChunkProviderSuperTech implements IChunkGenerator {

	private IBlockState seaBlockState;
	private static final double genScale = 0.004;
	private final Random rand;
	private NoiseGeneratorOctaves minLimitPerlinNoise;
	private NoiseGeneratorOctaves maxLimitPerlinNoise;
	private NoiseGeneratorOctaves mainPerlinNoise;
	private NoiseGeneratorPerlin surfaceNoise;
	public NoiseGeneratorOctaves scaleNoise;
	public NoiseGeneratorOctaves depthNoise;
	public NoiseGeneratorOctaves forestNoise;
	private final World world;
	private final boolean mapFeaturesEnabled;
	private final WorldType terrainType;
	private final double[] heightMap;
	private final float[] biomeWeights;
	private ChunkGeneratorSettings settings;
	private double[] depthBuffer = new double[256];
	private MapGenBase caveGenerator = new MapGenCaves() {
		protected boolean canReplaceBlock(IBlockState p_175793_1_, IBlockState p_175793_2_) {
			return super.canReplaceBlock(p_175793_1_, p_175793_2_) || p_175793_1_.getBlock() instanceof BlockRock;
		}
	};
	private MapGenStronghold strongholdGenerator = new MapGenStronghold();
	private MapGenVillage villageGenerator = new MapGenVillage();
	private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
	private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
	private MapGenBase ravineGenerator = new MapGenRavine() {
		private boolean isExceptionBiome(net.minecraft.world.biome.Biome biome) {
			if (biome == net.minecraft.init.Biomes.BEACH)
				return true;
			if (biome == net.minecraft.init.Biomes.DESERT)
				return true;
			if (biome == net.minecraft.init.Biomes.MUSHROOM_ISLAND)
				return true;
			if (biome == net.minecraft.init.Biomes.MUSHROOM_ISLAND_SHORE)
				return true;
			return false;
		}

		protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop) {
			net.minecraft.world.biome.Biome biome = world.getBiome(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
			IBlockState state = data.getBlockState(x, y, z);
			IBlockState top = isExceptionBiome(biome) ? Blocks.GRASS.getDefaultState() : biome.topBlock;
			IBlockState filler = isExceptionBiome(biome) ? Blocks.DIRT.getDefaultState() : biome.fillerBlock;

			if (state.getBlock() instanceof BlockRock || state.getBlock() == top.getBlock()
					|| state.getBlock() == filler.getBlock()) {
				if (y - 1 < 10) {
					data.setBlockState(x, y, z, FLOWING_LAVA);
				} else {
					data.setBlockState(x, y, z, AIR);

					if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock()) {
						data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
					}
				}
			}
		}
	};
	private StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();
	private Biome[] biomesForGeneration;
	double[] mainNoiseRegion;
	double[] minLimitRegion;
	double[] maxLimitRegion;
	double[] depthRegion;

	SimplexNoise noise = new SimplexNoise();
	Vec3d layerOffset;
	Vec3d geomeOffset;

	public ChunkProviderSuperTech(World worldIn, long seed, boolean mapFeaturesEnabledIn, String generatorOptions) {
		{
			caveGenerator = net.minecraftforge.event.terraingen.TerrainGen.getModdedMapGen(caveGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE);
			strongholdGenerator = (MapGenStronghold) net.minecraftforge.event.terraingen.TerrainGen.getModdedMapGen(
					strongholdGenerator, net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.STRONGHOLD);
			villageGenerator = (MapGenVillage) net.minecraftforge.event.terraingen.TerrainGen.getModdedMapGen(
					villageGenerator, net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE);
			mineshaftGenerator = (MapGenMineshaft) net.minecraftforge.event.terraingen.TerrainGen.getModdedMapGen(
					mineshaftGenerator, net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT);
			scatteredFeatureGenerator = (MapGenScatteredFeature) net.minecraftforge.event.terraingen.TerrainGen
					.getModdedMapGen(scatteredFeatureGenerator,
							net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE);
			ravineGenerator = net.minecraftforge.event.terraingen.TerrainGen.getModdedMapGen(ravineGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE);
			oceanMonumentGenerator = (StructureOceanMonument) net.minecraftforge.event.terraingen.TerrainGen
					.getModdedMapGen(oceanMonumentGenerator,
							net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.OCEAN_MONUMENT);
		}
		this.world = worldIn;
		this.mapFeaturesEnabled = mapFeaturesEnabledIn;
		this.terrainType = worldIn.getWorldInfo().getTerrainType();
		this.rand = new Random(seed);
		this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
		this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
		this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
		this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.heightMap = new double[825];
		this.biomeWeights = new float[25];
		layerOffset = new Vec3d(10000.0F * rand.nextFloat(), 10000.0F * rand.nextFloat(), 10000.0F * rand.nextFloat());
		geomeOffset = new Vec3d(10000.0F * rand.nextFloat(), 10000.0F * rand.nextFloat(), 10000.0F * rand.nextFloat());
		this.seaBlockState = Blocks.WATER.getDefaultState();
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
				this.biomeWeights[i + 2 + (j + 2) * 5] = f;
			}
		}

		if (generatorOptions != null) {
			this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(generatorOptions).build();
			worldIn.setSeaLevel(this.settings.seaLevel);
		}

		net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld ctx = new net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld(
				minLimitPerlinNoise, maxLimitPerlinNoise, mainPerlinNoise, surfaceNoise, scaleNoise, depthNoise,
				forestNoise);
		ctx = net.minecraftforge.event.terraingen.TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, ctx);
		this.minLimitPerlinNoise = ctx.getLPerlin1();
		this.maxLimitPerlinNoise = ctx.getLPerlin2();
		this.mainPerlinNoise = ctx.getPerlin();
		this.surfaceNoise = ctx.getHeight();
		this.scaleNoise = ctx.getScale();
		this.depthNoise = ctx.getDepth();
		this.forestNoise = ctx.getForest();
	}

	public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
		this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration,
				x * 4 - 2, z * 4 - 2, 10, 10);
		this.generateHeightmap(x * 4, 0, z * 4);

		for (int ix = 0; ix < 4; ++ix) {
			int xL = ix * 5;
			int xH = (ix + 1) * 5;

			for (int iz = 0; iz < 4; ++iz) {
				int xLzL = (xL + iz) * 33;
				int xLzH = (xL + iz + 1) * 33;
				int xHzL = (xH + iz) * 33;
				int xHzH = (xH + iz + 1) * 33;

				for (int iy = 0; iy < 32; ++iy) {
					double xLyLzL = this.heightMap[xLzL + iy];
					double xLyLzH = this.heightMap[xLzH + iy];
					double xHyLzL = this.heightMap[xHzL + iy];
					double xHyLzH = this.heightMap[xHzH + iy];
					double xLyHzL = (this.heightMap[xLzL + iy + 1] - xLyLzL) * 0.125D;
					double xLyHzH = (this.heightMap[xLzH + iy + 1] - xLyLzH) * 0.125D;
					double xHyHzL = (this.heightMap[xHzL + iy + 1] - xHyLzL) * 0.125D;
					double xHyHzH = (this.heightMap[xHzH + iy + 1] - xHyLzH) * 0.125D;

					double noiseStepY00 = (xLyHzL - xLyLzL) * 0.125D;
					double noiseStepY01 = (xLyHzH - xLyLzH) * 0.125D;
					double noiseStepY10 = (xHyHzL - xHyLzL) * 0.125D;
					double noiseStepY11 = (xHyHzH - xHyLzH) * 0.125D;

					double noisexLS = xLyLzL;
					double noisexHS = xLyLzH;
					double noisexLE = xHyLzL;
					double noisexHE = xHyLzH;

					for (int jy = 0; jy < 8; ++jy) {

						double noisezS = noisexLS;
						double noisezE = noisexHS;

						double stepLX = (noisexLE - noisexLS) * 0.25d;
						double stepHX = (noisexHE - noisexHS) * 0.25d;

						for (int jx = 0; jx < 4; ++jx) {
							double noiseStepZ = (noisezE - noisezS) * 0.25d;
							double noiseVal = noisezS;

							// subchunk is 4 blocks long in x direction, index as jz
							for (int jz = 0; jz < 4; ++jz) {
								int tx = ix * 4 + jx;
								int ty = iy * 8 + jy;
								int tz = iz * 4 + jz;

								int igneous = (int) (noise.get2dNoiseValue(x * 16 + tx, z * 16 + tz, layerOffset,
										genScale) * 15) + 20;
								int sedimentary = (int) (noise.get2dNoiseValue(x * 16 + ix * 4 + jx,
										z * 16 + iz * 4 + jz, layerOffset, genScale) * 15) + 20;
								double val = noise.get3dNoiseValue(x * 16 + tx, ty, z * 16 + tz, geomeOffset, genScale);
								// If the noise value is above zero, this block starts as stone
								// Otherwise it's 'empty' - air above sealevel and water below it
								if (noiseVal > 0.0D) {
									if (ty < igneous) {
										// RockType.IGNEOUS;
										primer.setBlockState(tx, ty, tz,
												pickBlockFromSet(val, RockManager.stoneSpawns.get("igneous")));
									} else if (ty < igneous + sedimentary) {
										// RockType.SEDIMENTARY;
										primer.setBlockState(tx, ty, tz,
												pickBlockFromSet(val, RockManager.stoneSpawns.get("sedimentary")));
									} else {
										// RockType.METAMORPHIC;
										primer.setBlockState(tx, ty, tz,
												pickBlockFromSet(val, RockManager.stoneSpawns.get("metamorphic")));
									}
								} else if (ty < this.settings.seaLevel) {
									primer.setBlockState(tx, ty, tz, this.seaBlockState);
								}
								noiseVal += noiseStepZ;
							}

							noisezS += stepLX;
							noisezE += stepHX;
						}

						noisexLS += noiseStepY00;
						noisexHS += noiseStepY01;
						noisexLE += noiseStepY10;
						noisexHE += noiseStepY11;
					}
				}
			}
		}
	}

	private IBlockState pickBlockFromSet(double value, Set<IBlockState> list) {
		value = ((value + 1) / 2) * list.size();
		return list.stream().skip((int) value).findFirst().get();
	}

	public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn) {
		if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world))
			return;
		this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double) (x * 16), (double) (z * 16), 16, 16,
				0.0625D, 0.0625D, 1.0D);

		for (int i = 0; i < 16; ++i) {
			for (int j = 0; j < 16; ++j) {
				Biome biome = biomesIn[j + i * 16];
				int c = 0;
				for (int k = 255; k >= 0; k--) {
					if (k <= rand.nextInt(5)) {
						primer.setBlockState(j, k, i, Blocks.BEDROCK.getDefaultState());
					}
					if (primer.getBlockState(j, k, i).getBlock().isOpaqueCube(primer.getBlockState(j, k, i))) {
						if (c == 0) {
							c++;
							primer.setBlockState(j, k, i, biome.topBlock);
						} else if (c < 3) {
							c++;
							primer.setBlockState(j, k, i, biome.fillerBlock);
						}
					}

				}
				/*
				 * biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j,
				 * this.depthBuffer[j + i * 16]);
				 */
			}
		}
	}

	/**
	 * Generates the chunk at the specified position, from scratch
	 */
	public Chunk generateChunk(int x, int z) {
		this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.setBlocksInChunk(x, z, chunkprimer);
		this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16,
				16);
		this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration);

		if (this.settings.useCaves) {
			this.caveGenerator.generate(this.world, x, z, chunkprimer);
		}

		if (this.settings.useRavines) {
			this.ravineGenerator.generate(this.world, x, z, chunkprimer);
		}

		if (this.mapFeaturesEnabled) {
			if (this.settings.useMineShafts) {
				this.mineshaftGenerator.generate(this.world, x, z, chunkprimer);
			}

			if (this.settings.useVillages) {
				this.villageGenerator.generate(this.world, x, z, chunkprimer);
			}

			if (this.settings.useStrongholds) {
				this.strongholdGenerator.generate(this.world, x, z, chunkprimer);
			}

			if (this.settings.useTemples) {
				this.scatteredFeatureGenerator.generate(this.world, x, z, chunkprimer);
			}

			if (this.settings.useMonuments) {
				this.oceanMonumentGenerator.generate(this.world, x, z, chunkprimer);
			}
		}

		Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
		byte[] abyte = chunk.getBiomeArray();

		for (int i = 0; i < abyte.length; ++i) {
			abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
		}

		chunk.generateSkylightMap();
		return chunk;
	}

	private void generateHeightmap(int p_185978_1_, int p_185978_2_, int p_185978_3_) {
		this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, p_185978_1_, p_185978_3_, 5, 5,
				(double) this.settings.depthNoiseScaleX, (double) this.settings.depthNoiseScaleZ,
				(double) this.settings.depthNoiseScaleExponent);
		float f = this.settings.coordinateScale;
		float f1 = this.settings.heightScale;
		this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, p_185978_1_, p_185978_2_,
				p_185978_3_, 5, 33, 5, (double) (f / this.settings.mainNoiseScaleX),
				(double) (f1 / this.settings.mainNoiseScaleY), (double) (f / this.settings.mainNoiseScaleZ));
		this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, p_185978_1_,
				p_185978_2_, p_185978_3_, 5, 33, 5, (double) f, (double) f1, (double) f);
		this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, p_185978_1_,
				p_185978_2_, p_185978_3_, 5, 33, 5, (double) f, (double) f1, (double) f);
		int i = 0;
		int j = 0;

		for (int k = 0; k < 5; ++k) {
			for (int l = 0; l < 5; ++l) {
				float f2 = 0.0F;
				float f3 = 0.0F;
				float f4 = 0.0F;
				Biome biome = this.biomesForGeneration[k + 2 + (l + 2) * 10];

				for (int j1 = -2; j1 <= 2; ++j1) {
					for (int k1 = -2; k1 <= 2; ++k1) {
						Biome biome1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
						float f5 = this.settings.biomeDepthOffSet
								+ biome1.getBaseHeight() * this.settings.biomeDepthWeight;
						float f6 = this.settings.biomeScaleOffset
								+ biome1.getHeightVariation() * this.settings.biomeScaleWeight;

						if (this.terrainType == WorldType.AMPLIFIED && f5 > 0.0F) {
							f5 = 1.0F + f5 * 2.0F;
							f6 = 1.0F + f6 * 4.0F;
						}

						float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

						if (biome1.getBaseHeight() > biome.getBaseHeight()) {
							f7 /= 2.0F;
						}

						f2 += f6 * f7;
						f3 += f5 * f7;
						f4 += f7;
					}
				}

				f2 = f2 / f4;
				f3 = f3 / f4;
				f2 = f2 * 0.9F + 0.1F;
				f3 = (f3 * 4.0F - 1.0F) / 8.0F;
				double d7 = this.depthRegion[j] / 8000.0D;

				if (d7 < 0.0D) {
					d7 = -d7 * 0.3D;
				}

				d7 = d7 * 3.0D - 2.0D;

				if (d7 < 0.0D) {
					d7 = d7 / 2.0D;

					if (d7 < -1.0D) {
						d7 = -1.0D;
					}

					d7 = d7 / 1.4D;
					d7 = d7 / 2.0D;
				} else {
					if (d7 > 1.0D) {
						d7 = 1.0D;
					}

					d7 = d7 / 8.0D;
				}

				++j;
				double d8 = (double) f3;
				double d9 = (double) f2;
				d8 = d8 + d7 * 0.2D;
				d8 = d8 * (double) this.settings.baseSize / 8.0D;
				double d0 = (double) this.settings.baseSize + d8 * 4.0D;

				for (int l1 = 0; l1 < 33; ++l1) {
					double d1 = ((double) l1 - d0) * (double) this.settings.stretchY * 128.0D / 256.0D / d9;

					if (d1 < 0.0D) {
						d1 *= 4.0D;
					}

					double d2 = this.minLimitRegion[i] / (double) this.settings.lowerLimitScale;
					double d3 = this.maxLimitRegion[i] / (double) this.settings.upperLimitScale;
					double d4 = (this.mainNoiseRegion[i] / 10.0D + 1.0D) / 2.0D;
					double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;

					if (l1 > 29) {
						double d6 = (double) ((float) (l1 - 29) / 3.0F);
						d5 = d5 * (1.0D - d6) + -10.0D * d6;
					}

					this.heightMap[i] = d5;
					++i;
				}
			}
		}
	}

	/**
	 * Generate initial structures in this chunk, e.g. mineshafts, temples, lakes,
	 * and dungeons
	 */
	public void populate(int x, int z) {
		BlockFalling.fallInstantly = true;
		int i = x * 16;
		int j = z * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
		this.rand.setSeed(this.world.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
		boolean flag = false;
		ChunkPos chunkpos = new ChunkPos(x, z);

		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

		if (this.mapFeaturesEnabled) {
			if (this.settings.useMineShafts) {
				this.mineshaftGenerator.generateStructure(this.world, this.rand, chunkpos);
			}

			if (this.settings.useVillages) {
				flag = this.villageGenerator.generateStructure(this.world, this.rand, chunkpos);
			}

			if (this.settings.useStrongholds) {
				this.strongholdGenerator.generateStructure(this.world, this.rand, chunkpos);
			}

			if (this.settings.useTemples) {
				this.scatteredFeatureGenerator.generateStructure(this.world, this.rand, chunkpos);
			}

			if (this.settings.useMonuments) {
				this.oceanMonumentGenerator.generateStructure(this.world, this.rand, chunkpos);
			}

		}

		if (biome != Biomes.DESERT && biome != Biomes.DESERT_HILLS && this.settings.useWaterLakes && !flag
				&& this.rand.nextInt(this.settings.waterLakeChance) == 0)
			if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag,
					net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
				int i1 = this.rand.nextInt(16) + 8;
				int j1 = this.rand.nextInt(256);
				int k1 = this.rand.nextInt(16) + 8;
				(new WorldGenLakes(Blocks.WATER)).generate(this.world, this.rand, blockpos.add(i1, j1, k1));
			}

		if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
			if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag,
					net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA)) {
				int i2 = this.rand.nextInt(16) + 8;
				int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
				int k3 = this.rand.nextInt(16) + 8;

				if (l2 < this.world.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
					(new WorldGenLakes(Blocks.LAVA)).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
				}
			}

		if (this.settings.useDungeons)
			if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag,
					net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON)) {
				for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2) {
					int i3 = this.rand.nextInt(16) + 8;
					int l3 = this.rand.nextInt(256);
					int l1 = this.rand.nextInt(16) + 8;
					(new WorldGenDungeons()).generate(this.world, this.rand, blockpos.add(i3, l3, l1));
				}
			}

		biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag,
				net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
			WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
		blockpos = blockpos.add(8, 0, 8);

		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag,
				net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE)) {
			for (int k2 = 0; k2 < 16; ++k2) {
				for (int j3 = 0; j3 < 16; ++j3) {
					BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
					BlockPos blockpos2 = blockpos1.down();

					if (this.world.canBlockFreezeWater(blockpos2)) {
						this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
					}

					if (this.world.canSnowAt(blockpos1, true)) {
						this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
					}
				}
			}
		} // Forge: End ICE

		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

		BlockFalling.fallInstantly = false;
	}

	/**
	 * Called to generate additional structures after initial worldgen, used by
	 * ocean monuments
	 */
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		boolean flag = false;

		if (this.settings.useMonuments && this.mapFeaturesEnabled && chunkIn.getInhabitedTime() < 3600L) {
			flag |= this.oceanMonumentGenerator.generateStructure(this.world, this.rand, new ChunkPos(x, z));
		}

		return flag;
	}

	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		Biome biome = this.world.getBiome(pos);

		if (this.mapFeaturesEnabled) {
			if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.isSwampHut(pos)) {
				return this.scatteredFeatureGenerator.getMonsters();
			}

			if (creatureType == EnumCreatureType.MONSTER && this.settings.useMonuments
					&& this.oceanMonumentGenerator.isPositionInStructure(this.world, pos)) {
				return this.oceanMonumentGenerator.getMonsters();
			}
		}

		return biome.getSpawnableList(creatureType);
	}

	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		if (!this.mapFeaturesEnabled) {
			return false;
		} else if ("Stronghold".equals(structureName) && this.strongholdGenerator != null) {
			return this.strongholdGenerator.isInsideStructure(pos);
		} else if ("Monument".equals(structureName) && this.oceanMonumentGenerator != null) {
			return this.oceanMonumentGenerator.isInsideStructure(pos);
		} else if ("Village".equals(structureName) && this.villageGenerator != null) {
			return this.villageGenerator.isInsideStructure(pos);
		} else if ("Mineshaft".equals(structureName) && this.mineshaftGenerator != null) {
			return this.mineshaftGenerator.isInsideStructure(pos);
		} else {
			return "Temple".equals(structureName) && this.scatteredFeatureGenerator != null
					? this.scatteredFeatureGenerator.isInsideStructure(pos)
					: false;
		}
	}

	@Nullable
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
			boolean findUnexplored) {
		if (!this.mapFeaturesEnabled) {
			return null;
		} else if ("Stronghold".equals(structureName) && this.strongholdGenerator != null) {
			return this.strongholdGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
		} else if ("Monument".equals(structureName) && this.oceanMonumentGenerator != null) {
			return this.oceanMonumentGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
		} else if ("Village".equals(structureName) && this.villageGenerator != null) {
			return this.villageGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
		} else if ("Mineshaft".equals(structureName) && this.mineshaftGenerator != null) {
			return this.mineshaftGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
		} else {
			return "Temple".equals(structureName) && this.scatteredFeatureGenerator != null
					? this.scatteredFeatureGenerator.getNearestStructurePos(worldIn, position, findUnexplored)
					: null;
		}
	}

	/**
	 * Recreates data about structures intersecting given chunk (used for example by
	 * getPossibleCreatures), without placing any blocks. When called for the first
	 * time before any chunk is generated - also initializes the internal state
	 * needed by getPossibleCreatures.
	 */
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		if (this.mapFeaturesEnabled) {
			if (this.settings.useMineShafts) {
				this.mineshaftGenerator.generate(this.world, x, z, (ChunkPrimer) null);
			}

			if (this.settings.useVillages) {
				this.villageGenerator.generate(this.world, x, z, (ChunkPrimer) null);
			}

			if (this.settings.useStrongholds) {
				this.strongholdGenerator.generate(this.world, x, z, (ChunkPrimer) null);
			}

			if (this.settings.useTemples) {
				this.scatteredFeatureGenerator.generate(this.world, x, z, (ChunkPrimer) null);
			}

			if (this.settings.useMonuments) {
				this.oceanMonumentGenerator.generate(this.world, x, z, (ChunkPrimer) null);
			}
		}
	}
}
