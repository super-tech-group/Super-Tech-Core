package com.supertechgroup.core.worldgen.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.supertechgroup.core.Config;
import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.capabilities.ore.IOreCapability;
import com.supertechgroup.core.capabilities.ore.OreCapabilityProvider;
import com.supertechgroup.core.worldgen.ores.Ore;
import com.supertechgroup.core.worldgen.rocks.RockManager;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * The base world generator. Any ore generator in this mod should extend this.
 *
 * @author oa10712
 *
 */
public abstract class WorldGeneratorBase {

	public static Map<Ore, Double> singleOre(Ore ore) {
		HashMap<Ore, Double> ores = new HashMap<>();
		ores.put(ore, 1d);
		return ores;
	}

	public Map<Ore, Double> ores;// List of metals in this generator along
	// with
	// their chance to generate per block
	public int size;// Size of the generator. This means different things
	// depending on the implementation
	public List<IBlockState> validStoneTypes = new ArrayList<>();
	public int chance;// Chance per chunk to generate an instance
	private String name;// The identifying name for this generator

	public ArrayList<Integer> dims = new ArrayList<>();

	public WorldGeneratorBase(Map<Ore, Double> ores, String name, int[] dims, int size, int chance, String... stones) {
		this.ores = ores;
		this.size = size;
		this.name = name;
		this.chance = chance >= 1 ? chance : 1;
		for (int i : dims) {
			addDim(i);
		}
		for (String s : stones) {
			validStoneTypes.addAll(RockManager.getStones(s));
		}
	}

	public void addDim(int i) {
		dims.add(i);
	}

	public BlockPos[] facing(BlockPos center) {
		BlockPos[] ret = new BlockPos[7];
		ret[0] = center;
		ret[1] = center.up();
		ret[2] = center.down();
		ret[3] = center.north();
		ret[4] = center.south();
		ret[5] = center.east();
		ret[6] = center.west();
		return ret;
	}

	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (dims.contains(world.provider.getDimension())) {
			BlockPos pos = new BlockPos(chunkX * 16 + world.rand.nextInt(8) + 4, 0,
					chunkZ * 16 + world.rand.nextInt(8) + 4);
			generate(world, random, pos);
		}
	}

	abstract boolean generate(World world, Random random, BlockPos pos);

	/**
	 * Create/update a single ore block in the world. This reads the available ores
	 * from the given generator and sets up a new ore block, ore adds ore data to an
	 * existing one if present
	 *
	 * @param world
	 * @param pos
	 * @return
	 */
	public boolean generateOreBlock(World world, BlockPos pos, IBlockState generatorStart) {
		IBlockState state = world.getBlockState(pos);
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		IOreCapability cap = chunk.getCapability(OreCapabilityProvider.ORE_CAP, null);
		if (validStoneTypes.contains(state) && state.equals(generatorStart)) {
			ArrayList<ResourceLocation> oresAdded = new ArrayList<>();
			ores.forEach((Ore k, Double v) -> {
				if (world.rand.nextDouble() < v) {
					oresAdded.add(k.getRegistryName());
				}
			});
			if (!oresAdded.isEmpty()) {
				Object[] data = new Object[oresAdded.size() + 2];
				data[0] = state.getBlockHardness(world, pos);
				data[1] = RockManager.getTexture(state);
				for (int i = 0; i < oresAdded.size(); i++) {
					data[i + 2] = oresAdded.get(i);
				}
				cap.setData(pos.getX(), pos.getY(), pos.getZ(), data);
				chunk.markDirty();
				world.setBlockState(pos, ModRegistry.superore.getDefaultState());

			}
		} else if (Config.nether.containsKey(state) && state.equals(generatorStart)) {
			ArrayList<ResourceLocation> oresAdded = new ArrayList<>();
			ores.forEach((Ore k, Double v) -> {
				if (world.rand.nextDouble() < v) {
					oresAdded.add(k.getRegistryName());
				}
			});
			if (!oresAdded.isEmpty()) {
				Object[] data = new Object[oresAdded.size() + 2];
				data[0] = state.getBlockHardness(world, pos);
				data[1] = Config.nether.get(state);
				for (int i = 0; i < oresAdded.size(); i++) {
					data[i + 2] = oresAdded.get(i);
				}
				cap.setData(pos.getX(), pos.getY(), pos.getZ(), data);
				chunk.markDirty();
				world.setBlockState(pos, ModRegistry.superore.getDefaultState());
			}
		} else if (Config.end.containsKey(state) && state.equals(generatorStart)) {
			ArrayList<ResourceLocation> oresAdded = new ArrayList<>();
			ores.forEach((Ore k, Double v) -> {
				if (world.rand.nextDouble() < v) {
					oresAdded.add(k.getRegistryName());
				}
			});
			if (!oresAdded.isEmpty()) {
				Object[] data = new Object[oresAdded.size() + 2];
				data[0] = state.getBlockHardness(world, pos);
				data[1] = Config.end.get(state);
				for (int i = 0; i < oresAdded.size(); i++) {
					data[i + 2] = oresAdded.get(i);
				}
				cap.setData(pos.getX(), pos.getY(), pos.getZ(), data);
				chunk.markDirty();
				world.setBlockState(pos, ModRegistry.superore.getDefaultState());
			}
		} else if (state.getBlock() == ModRegistry.superore) {
			if (cap.getBase(pos).equals(RockManager.getTexture(generatorStart))) {
				ArrayList<ResourceLocation> oresAdded = new ArrayList<>();
				ores.forEach((Ore k, Double v) -> {
					if (world.rand.nextDouble() < v) {
						oresAdded.add(k.getRegistryName());
					}
				});
				Object[] oldData = cap.getData(pos.getX(), pos.getY(), pos.getZ());
				Object[] data = new Object[oldData.length + oresAdded.size()];
				for (int i = 0; i < oldData.length; i++) {
					data[i] = oldData[i];
				}
				for (int i = 0; i < oresAdded.size(); i++) {
					data[i + oldData.length] = oresAdded.get(i);
				}
				cap.setData(pos.getX(), pos.getY(), pos.getZ(), data);
				chunk.markDirty();
				world.setBlockState(pos, ModRegistry.superore.getDefaultState());
			}
		}
		return true;
	}

	public List<Integer> getDims() {
		return dims;
	}

	public String getName() {
		return name;
	}

	public Map<Ore, Double> getOres() {
		return ores;
	}

	public int getSize() {
		return size;
	}

	public void setName(String n) {
		name = n;
	}
}