package com.supertechgroup.core.worldgen;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.proxy.ClientProxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

/**
 *
 * @author oa10712
 */
public class OreSavedData extends WorldSavedData {

	public static OreSavedData get(World world) {
		MapStorage storage = world.getPerWorldStorage();// get the ore data per dimension, we don't want it shared here
		OreSavedData instance = (OreSavedData) storage.getOrLoadData(OreSavedData.class, Reference.ORE_DATA_NAME);

		if (instance == null) {
			instance = new OreSavedData();
			storage.setData(Reference.ORE_DATA_NAME, instance);
		}
		return instance;
	}

	public static void set(World world, OreSavedData newData) {
		MapStorage storage = world.getPerWorldStorage();
		storage.setData(Reference.ORE_DATA_NAME, newData);
	}

	/**
	 * <X,<Y,<Z,Data>>> where Data[0] is the rock type of the ore and the remaining
	 * elements are the ore data
	 */
	HashMap<Integer, HashMap<Integer, HashMap<Integer, ResourceLocation[]>>> data = new HashMap<>();

	HashMap<Integer, ArrayList<Integer>> generated = new HashMap<>();

	HashMap<Integer, HashMap<Integer, HashMap<Integer, Float>>> hardnessData = new HashMap<>();
	// Required constructors

	public OreSavedData() {
		super(Reference.ORE_DATA_NAME);
	}

	public OreSavedData(String s) {
		super(s);
	}

	public void clearData() {
		data = new HashMap<>();
		generated = new HashMap<>();
		hardnessData = new HashMap<>();
		markDirty();
	}

	/**
	 * Get the base rock texture location
	 *
	 * @param pos
	 * @return
	 */
	public ResourceLocation getBase(BlockPos pos) {
		return getBase(pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Get the base rock texture location, falling back to vanilla stone
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public ResourceLocation getBase(int x, int y, int z) {
		try {
			return data.get(x).get(y).get(z)[0];
		} catch (Exception ex) {
			return new ResourceLocation("minecraft:stone");
		}
	}

	/**
	 * Creates a tag of ore data in a chunk. Intended for use with #updateFromTag.
	 *
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public NBTTagCompound getForChunk(int chunkX, int chunkZ) {
		NBTTagCompound ret = new NBTTagCompound();
		int xStart = chunkX * 16;
		int zStart = chunkZ * 16;

		for (int i = 0; i < 16; i++) {// cycle the x range
			if (data.containsKey(xStart + i)) {
				NBTTagCompound xTag = new NBTTagCompound();
				HashMap<Integer, HashMap<Integer, ResourceLocation[]>> xData = data.get(xStart + i);
				xData.forEach((Integer y, HashMap<Integer, ResourceLocation[]> yData) -> {
					NBTTagCompound yTag = new NBTTagCompound();
					yData.forEach((Integer z, ResourceLocation[] ores) -> {
						if (z >= zStart && z < zStart + 16) {// if its within
																// the z range
							NBTTagList list = new NBTTagList();
							for (ResourceLocation ore : ores) {
								list.appendTag(new NBTTagString(ore.toString()));
							}
							yTag.setTag(z.toString(), list);
						}
					});
					xTag.setTag(y.toString(), yTag);
				});
				ret.setTag((i + xStart) + "", xTag);
			}
			if (hardnessData.containsKey(xStart + i)) {
				NBTTagCompound xTag = new NBTTagCompound();
				HashMap<Integer, HashMap<Integer, Float>> xData = hardnessData.get(xStart + i);

				xData.forEach((Integer y, HashMap<Integer, Float> yData) -> {
					NBTTagCompound yTag = new NBTTagCompound();
					yData.forEach((Integer z, Float hardness) -> {
						if (z >= zStart && z < zStart + 16) {// if its within
																// the z range
							yTag.setFloat(z.toString(), hardness);
						}
					});
					xTag.setTag(y.toString(), yTag);
				});
				ret.setTag((i + xStart) + "h", xTag);
			}
		}
		return ret;
	}

	/**
	 * Creates a tag of ore data in a single position. Intended for use with block
	 * break updates.
	 *
	 * @return
	 */
	public NBTTagCompound getForPos(BlockPos pos) {
		NBTTagCompound ret = new NBTTagCompound();
		if (data.containsKey(pos.getX())) {
			NBTTagCompound xTag = new NBTTagCompound();
			if (data.get(pos.getX()).containsKey(pos.getY())) {
				NBTTagCompound yTag = new NBTTagCompound();
				if (data.get(pos.getX()).get(pos.getY()).containsKey(pos.getZ())) {
					ResourceLocation[] get = data.get(pos.getX()).get(pos.getY()).get(pos.getZ());
					NBTTagList list = new NBTTagList();
					for (ResourceLocation element : get) {
						list.appendTag(new NBTTagString(element.toString()));
					}
					yTag.setTag(pos.getZ() + "", list);
				} else {
					NBTTagList list = new NBTTagList();
					yTag.setTag(pos.getZ() + "", list);
				}
				xTag.setTag(pos.getY() + "", yTag);
			}
			ret.setTag(pos.getX() + "", xTag);
		}
		if (hardnessData.containsKey(pos.getX())) {
			NBTTagCompound xTag = new NBTTagCompound();
			if (hardnessData.get(pos.getX()).containsKey(pos.getY())) {
				NBTTagCompound yTag = new NBTTagCompound();
				if (hardnessData.get(pos.getX()).get(pos.getY()).containsKey(pos.getZ())) {
					Float hardness = hardnessData.get(pos.getX()).get(pos.getY()).get(pos.getZ());
					yTag.setFloat(pos.getZ() + "", hardness);
				} else {
					yTag.setFloat(pos.getZ() + "", 1f);
				}
				xTag.setTag(pos.getY() + "", yTag);
			}
			ret.setTag(pos.getX() + "h", xTag);
		}
		return ret;
	}

	/**
	 * Attempts to get the hardness of a block at Block position. If it is unable to
	 * find a given hardness will return 1f.
	 *
	 * @param pos
	 * @return
	 */
	public float getHardness(BlockPos pos) {
		return getHardness(pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Attempts to get the hardness of a block at Coords X, Y, Z. If it is unable to
	 * find a given hardness will return 1f.
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public float getHardness(int x, int y, int z) {
		try {
			return hardnessData.get(x).get(y).get(z);
		} catch (Exception ex) {
			System.out.println("Failed to find a block hardness at location: " + x + ", " + y + ", " + z);
			return 1f;
		}
	}

	/**
	 * returns an array of all Ores within a single block at position 'pos'
	 *
	 * @param pos
	 * @return
	 */
	public ResourceLocation[] getOres(BlockPos pos) {
		return getOres(pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * returns an array of all Ores within a single block at position 'x,y,z'
	 *
	 * @param pos
	 * @return
	 */
	public ResourceLocation[] getOres(int x, int y, int z) {
		try {
			ResourceLocation[] get = data.get(x).get(y).get(z);
			ResourceLocation[] ret = new ResourceLocation[get.length - 1];
			for (int i = 1; i < get.length; i++) {
				ret[i - 1] = get[i];
			}
			return ret;
		} catch (Exception ex) {
			return new ResourceLocation[0];
		}
	}

	/**
	 * Check if we have attempted ore generation in a specified chunk
	 *
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public boolean isChunkGenerated(int chunkX, int chunkZ) {
		return generated.containsKey(chunkX) && generated.get(chunkX).contains(chunkZ);
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
		parentNBTTagCompound.getKeySet().forEach((x) -> {
			NBTTagCompound xTag = parentNBTTagCompound.getCompoundTag(x);
			if (x.contains("h")) {
				xTag.getKeySet().forEach((y) -> {
					NBTTagCompound yTag = xTag.getCompoundTag(y);
					yTag.getKeySet().forEach((z) -> {
						Float hardness = yTag.getFloat(z);
						setHardness(Integer.parseInt(StringUtils.chop(x)), Integer.parseInt(y), Integer.parseInt(z),
								hardness);
					});
				});
			} else {
				xTag.getKeySet().forEach((y) -> {
					NBTTagCompound yTag = xTag.getCompoundTag(y);
					yTag.getKeySet().forEach((z) -> {

						NBTTagList tag = yTag.getTagList(z, Constants.NBT.TAG_STRING);
						ResourceLocation[] dataList = new ResourceLocation[tag.tagCount()];

						for (int i = 0; i < tag.tagCount(); i++) {
							dataList[i] = new ResourceLocation(tag.getStringTagAt(i));
						}
						setData(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z), dataList);
						BlockPos pos = new BlockPos(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
						if (SuperTechCoreMod.proxy instanceof ClientProxy) {
							try {
								SuperTechCoreMod.proxy.getWorld().markBlockRangeForRenderUpdate(pos, pos);
							} catch (Exception ex) {
							}
						}
						setChunkGenerated((Integer.parseInt(x) / 16), (Integer.parseInt(z) / 16));
					});
				});
			}

		});
		markDirty();
	}

	/**
	 * used to set the texture for the background rock for a ore tile
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param base The texture location for the rock
	 */
	public void setBase(int x, int y, int z, ResourceLocation base) {
		if (!data.containsKey(x)) {
			data.put(x, new HashMap<>());
		}
		if (!data.get(x).containsKey(y)) {
			data.get(x).put(y, new HashMap<>());
		}
		if (data.get(x).get(y).containsKey(z)) {
			data.get(x).get(y).get(z)[0] = base;
			return;
		}
		data.get(x).get(y).put(z, new ResourceLocation[] { base });
		markDirty();
	}

	public void setChunkGenerated(int chunkX, int chunkZ) {
		if (!generated.containsKey(chunkX)) {
			generated.put(chunkX, new ArrayList<>());
		}
		if (!generated.get(chunkX).contains(chunkZ)) {
			generated.get(chunkX).add(chunkZ);
		}
	}

	/**
	 * Create the oreblock data
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param base The texture location for the background rock
	 * @param ores The ore location for the ore data.
	 */
	public void setData(int x, int y, int z, ResourceLocation base, ResourceLocation[] ores, float hardness) {
		setData(x, y, z, ArrayUtils.add(ores.clone(), 0, base));
		setHardness(x, y, z, hardness);
	}

	/**
	 * Create the oreblock data
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @param dataList An array of ResourceLocations, the first item is the texture
	 *                 location for the background rock, all others are ore
	 *                 locations for contained ores
	 */
	public void setData(int x, int y, int z, ResourceLocation[] dataList) {
		if (!data.containsKey(x)) {
			data.put(x, new HashMap<>());
		}
		if (!data.get(x).containsKey(y)) {
			data.get(x).put(y, new HashMap<>());
		}
		ResourceLocation[] newData = new ResourceLocation[dataList.length];
		for (int i = 0; i < dataList.length; i++) {
			newData[i] = dataList[i];
		}
		data.get(x).get(y).put(z, newData);
		markDirty();
	}

	public void setHardness(BlockPos pos, Float hardness) {
		setHardness(pos.getX(), pos.getY(), pos.getZ(), hardness);
	}

	public void setHardness(int x, int y, int z, Float hardness) {
		if (!hardnessData.containsKey(x)) {
			hardnessData.put(x, new HashMap());
		}
		if (!hardnessData.get(x).containsKey(y)) {
			hardnessData.get(x).put(y, new HashMap());
		}
		hardnessData.get(x).get(y).put(z, hardness);
		markDirty();
	}

	public void setOres(BlockPos pos, ResourceLocation[] ores) {
		setOres(pos.getX(), pos.getY(), pos.getZ(), ores);
	}

	public void setOres(int x, int y, int z, ResourceLocation[] ores) {
		if (!data.containsKey(x)) {
			data.put(x, new HashMap<>());
		}
		if (!data.get(x).containsKey(y)) {
			data.get(x).put(y, new HashMap<>());
		}
		ResourceLocation[] newData = new ResourceLocation[ores.length + 1];

		if (data.get(x).get(y).containsKey(z)) {
			newData[0] = getBase(x, y, z);
		} else {
			newData[0] = new ResourceLocation("minecraft:stone");
		}
		for (int i = 0; i < ores.length; i++) {
			newData[i + 1] = ores[i];
		}
		data.get(x).get(y).put(z, newData);
		markDirty();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound) {
		data.forEach((Integer x, HashMap<Integer, HashMap<Integer, ResourceLocation[]>> xData) -> {
			NBTTagCompound xTag = new NBTTagCompound();
			xData.forEach((Integer y, HashMap<Integer, ResourceLocation[]> yData) -> {
				NBTTagCompound yTag = new NBTTagCompound();
				yData.forEach((Integer z, ResourceLocation[] ores) -> {
					NBTTagList list = new NBTTagList();
					for (ResourceLocation ore : ores) {
						list.appendTag(new NBTTagString(ore.toString()));
					}
					yTag.setTag(z.toString(), list);
				});
				xTag.setTag(y.toString(), yTag);
			});
			parentNBTTagCompound.setTag(x.toString(), xTag);
		});
		hardnessData.forEach((Integer x, HashMap<Integer, HashMap<Integer, Float>> xData) -> {
			NBTTagCompound xTag = new NBTTagCompound();
			xData.forEach((Integer y, HashMap<Integer, Float> yData) -> {
				NBTTagCompound yTag = new NBTTagCompound();
				yData.forEach((Integer z, Float hardness) -> {
					yTag.setFloat(z.toString(), hardness);
				});
				xTag.setTag(y.toString(), yTag);
			});
			parentNBTTagCompound.setTag(x.toString() + "h", xTag);
		});
		return parentNBTTagCompound;
	}
}